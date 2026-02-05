package org.firstinspires.ftc.teamcode.subsystems;

import android.util.Size;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.SquareOverlay;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

/**
 * <h2>Webcam Vision Subsystem</h2>
 *
 * <p>
 * This subsystem manages all robot vision processing using the FTC VisionPortal API.
 * It is responsible for:
 * </p>
 *
 * <ul>
 *   <li>Detecting AprilTags for field localization</li>
 *   <li>Determining MOTIF game pattern using Obelisk tags</li>
 *   <li>Providing robot pose updates to the Pinpoint localizer</li>
 *   <li>Managing camera streaming lifecycle</li>
 * </ul>
 *
 * <p>
 * The subsystem is designed to update localization only when the robot is static
 * to avoid injecting motion blur or bad pose estimates.
 * </p>
 */
@Config
public class Webcam extends SubsystemBase {

    /** Minimum time between consecutive pose corrections (seconds). */
    private static final double POSE_UPDATE_INTERVAL_SEC = 1.0;

    /** Maximum detection distance (meters) allowed for pose updates. */
    public static double MAX_UPDATE_DISTANCE = 1.5;

    /** Camera streaming resolution. */
    private static final Size CAMERA_RESOLUTION = new Size(640, 480);

    /** Camera X offset from robot center (cm). */
    public static double WEBCAM_X = -5.1;

    /** Camera Y offset from robot center (cm). */
    public static double WEBCAM_Y = 7.3;

    /** Camera Z height from robot base (cm). */
    public static double WEBCAM_Z = 31.1;

    /** Camera orientation relative to robot frame. */
    private static final YawPitchRollAngles CAMERA_ORIENTATION =
            new YawPitchRollAngles(AngleUnit.DEGREES, 0, -64, 0, 0);

    /** Camera intrinsic focal length (x). */
    private static final double FX = 500;

    /** Camera intrinsic focal length (y). */
    private static final double FY = 500;

    /** Camera optical center X. */
    private static final double CX = 320;

    /** Camera optical center Y. */
    private static final double CY = 240;

    /** VisionPortal instance handling camera and processors. */
    private VisionPortal visionPortal;

    /** AprilTag processor for detection and pose estimation. */
    private AprilTagProcessor aprilTag;

    /** Timer controlling how frequently localization updates occur. */
    private final ElapsedTime poseUpdateTimer = new ElapsedTime();

    /** Current detected MOTIF pattern from Obelisk tags. */
    private Pattern gamePattern;

    /**
     * Represents the possible MOTIF game patterns detected via Obelisk AprilTags.
     */
    public enum Pattern {
        /** Purple–Purple–Green */
        PPG,
        /** Purple–Green–Purple */
        PGP,
        /** Green–Purple–Purple */
        GPP
    }

    /**
     * Constructs the Webcam subsystem and initializes vision processing.
     *
     * @param hardwareMap FTC hardware map used to access the webcam device
     */
    public Webcam(HardwareMap hardwareMap) {
        initVision(hardwareMap);
    }

    /**
     * Initializes the VisionPortal and attaches the AprilTag processor.
     *
     * @param hardwareMap FTC hardware map
     */
    private void initVision(HardwareMap hardwareMap) {
        Position cameraPosition = new Position(
                DistanceUnit.CM, WEBCAM_X, WEBCAM_Y, WEBCAM_Z, 0
        );

        aprilTag = new AprilTagProcessor.Builder()
                .setLensIntrinsics(FX, FY, CX, CY)
                .setCameraPose(cameraPosition, CAMERA_ORIENTATION)
                .build();

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setCameraResolution(CAMERA_RESOLUTION)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .addProcessor(aprilTag)
                .addProcessor(new SquareOverlay())
                .build();
    }

    /**
     * Returns all currently visible AprilTag detections.
     *
     * @return list of detected AprilTags
     */
    public List<AprilTagDetection> getDetections() {
        return aprilTag.getDetections();
    }

    /**
     * Returns the best AprilTag detection usable for localization based on alliance.
     *
     * @return valid localization AprilTag or {@code null} if none detected
     */
    public AprilTagDetection getBestDetection() {
        for (AprilTagDetection d : aprilTag.getDetections()) {
            if (d.metadata == null || d.metadata.name.contains("Obelisk")) continue;

            boolean isBlueTag = d.id == 20;
            boolean isRedTag  = d.id == 24;

            if (BarnRobot.getInstance().opmodeData.allianceColor == OpModeData.AllianceColor.BLUE && isBlueTag)
                return d;

            if (BarnRobot.getInstance().opmodeData.allianceColor == OpModeData.AllianceColor.RED && isRedTag)
                return d;
        }
        return null;
    }

    /**
     * Updates the detected MOTIF pattern using Obelisk AprilTag IDs.
     */
    public void updateGamePattern() {
        for (AprilTagDetection d : aprilTag.getDetections()) {
            if (d.metadata == null || !d.metadata.name.contains("Obelisk")) continue;

            switch (d.metadata.id) {
                case 21: gamePattern = Pattern.GPP; break;
                case 22: gamePattern = Pattern.PGP; break;
                case 23: gamePattern = Pattern.PPG; break;
                default: gamePattern = null;
            }
        }
    }

    /**
     * @return the currently detected MOTIF pattern, or {@code null} if none found
     */
    public Pattern getGamePattern() {
        return gamePattern;
    }

    /**
     * Returns the robot's estimated field position using AprilTag localization.
     *
     * @return robot field position in inches
     */
    public Position getRobotPosition() {
        AprilTagDetection d = getBestDetection();
        return d != null ? d.robotPose.getPosition()
                : new Position(DistanceUnit.INCH, 0, 0, 0, 0);
    }

    /**
     * Returns the robot's field orientation using AprilTag localization.
     *
     * @return yaw/pitch/roll angles in degrees
     */
    public YawPitchRollAngles getRobotOrientation() {
        AprilTagDetection d = getBestDetection();
        return d != null ? d.robotPose.getOrientation()
                : new YawPitchRollAngles(AngleUnit.DEGREES, 0, 0, 0, 0);
    }

    /**
     * Pushes the AprilTag-derived pose into the Pinpoint localizer.
     * Only heading from odometry is preserved to avoid abrupt rotation jumps.
     */
    public void updatePose() {
        AprilTagDetection d = getBestDetection();
        if (d == null) return;

        Pose2d newPose = new Pose2d(
                d.robotPose.getPosition().x,
                d.robotPose.getPosition().y,
                BarnRobot.getInstance().pinpointLocalizer.getPose().heading.toDouble()
        );

        BarnRobot.getInstance().pinpointLocalizer.setPose(newPose);
    }

    /**
     * Checks if a valid localization AprilTag is currently visible.
     *
     * @return true if a localization tag is detected
     */
    public boolean isLocalizationTagDetected() {
        return getBestDetection() != null;
    }

    /**
     * Called automatically by the command framework scheduler.
     * Performs periodic pose updates when safe.
     */
    @Override
    public void periodic() {
        if (isLocalizationTagDetected()
                && BarnRobot.getInstance().drive.isRobotStatic()
                && poseUpdateTimer.seconds() >= POSE_UPDATE_INTERVAL_SEC) {

            updatePose();
            poseUpdateTimer.reset();
        }
    }

    /** Stops the camera stream. */
    public void stopStreaming() {
        if (visionPortal != null) visionPortal.stopStreaming();
    }

    /** Resumes the camera stream. */
    public void resumeStreaming() {
        if (visionPortal != null) visionPortal.resumeStreaming();
    }

    /** Fully closes the camera and releases resources. */
    public void close() {
        if (visionPortal != null) visionPortal.close();
    }

    /**
     * Adds AprilTag-based localization telemetry to the driver station.
     */
    public void displayTelemetry() {
        BarnRobot.getInstance().telemetry.addData("Robot Yaw", getRobotOrientation().getYaw());
        BarnRobot.getInstance().telemetry.addData("Robot X", getRobotPosition().x);
        BarnRobot.getInstance().telemetry.addData("Robot Y", getRobotPosition().y);
    }
}
