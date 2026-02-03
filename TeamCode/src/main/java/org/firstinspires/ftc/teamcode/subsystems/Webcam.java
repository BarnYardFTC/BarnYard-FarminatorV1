package org.firstinspires.ftc.teamcode.subsystems;

import android.util.Size;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
//import org.firstinspires.ftc.teamcode.subsystems.components.pipelines.ArtifactDetection;
//import org.firstinspires.ftc.teamcode.subsystems.components.pipelines.ArtifactPipeline;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.HorizontalLineOverlay;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;

import java.util.List;

@Config
public class Webcam extends SubsystemBase {

    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTag;
//    private ArtifactDetection detector;
    private OpenCvCamera camera;
//    private ArtifactPipeline pipeline;
    public static double MAX_UPDATE_DISTANCE = 1.5;

    public Position lastDetection;
    private final ElapsedTime poseUpdateTimer = new ElapsedTime();

    public static final int BLUE_LOCALIZATION_PIPELINE = 1;
    public static final int RED_LOCALIZATION_PIPELINE = 2;
    private static final double POSE_UPDATE_INTERVAL_SEC = 5.0;

    public enum Pattern {
        PPG,
        PGP,
        GPP
    }

    private Pattern gamePattern;

// CHANGE THESE TO MATCH YOUR CAMERA MOUNT!!!

    public static double WEBCAM_X = 11.5;
    public static double WEBCAM_Y = 17.75;
    public static double WEBCAM_Z = 31.5;

    private Position cameraPosition = new Position(
            DistanceUnit.CM,
            WEBCAM_X, WEBCAM_Y, WEBCAM_Z, 0
    );

    private final YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(
            AngleUnit.DEGREES,
            0, -64 , 0, 0
    );

    double fx = 500;
    double fy = 500;
    double cx = 320;
    double cy = 240;

    public Webcam(HardwareMap hw) {
        initArtFinder(hw);
    }

    private void initArtFinder(HardwareMap hardwareMap) {

        cameraPosition = new Position(DistanceUnit.CM, WEBCAM_X, WEBCAM_Y, WEBCAM_Z, 0);

        aprilTag = new AprilTagProcessor.Builder()
                .setLensIntrinsics(fx, fy, cx, cy)
                .setCameraPose(cameraPosition, cameraOrientation)
                .build();

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setCameraResolution(new Size(640, 480))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .addProcessor(aprilTag)
                .addProcessor(new HorizontalLineOverlay())
                .build();


        lastDetection = new Position();
    }

    /** Returns the full list of AprilTag detections */
    public List<AprilTagDetection> getDetections() {
        return aprilTag.getDetections();
    }

/** Returns the first valid detection with field pose (ignores Obelisk tags) */
    public AprilTagDetection getBestDetection() {
        for (AprilTagDetection d : aprilTag.getDetections()) {
            if (d.metadata != null && !d.metadata.name.contains("Obelisk")) {
                if ((d.id == 24 && BarnRobot.getInstance().opmodeData.allianceColor == OpModeData.AllianceColor.RED) ||
                        (d.id == 20 && BarnRobot.getInstance().opmodeData.allianceColor == OpModeData.AllianceColor.BLUE)){
                    return d;
                }
            }
        }
        return null;
    }


/** Updates MOTIF pattern based on the obelisk detections (slightly changed code from a func above) */
    public void updateGamePattern() {
        for (AprilTagDetection d : aprilTag.getDetections()) {
            if (d.metadata != null && d.metadata.name.contains("Obelisk")) {
                switch (d.metadata.id) {
                    case 21:
                        gamePattern = Pattern.GPP;
                        break;
                    case 22:
                        gamePattern = Pattern.PGP;
                        break;
                    case 23:
                        gamePattern = Pattern.PPG;
                        break;
                    default:
                        gamePattern = null;
                }
            }
        }
    }

    public Pattern getGamePattern() {
        return gamePattern;
    }

/** Returns robot position in INCHES relative to the field, or null if no valid tag */
    public Position getRobotPosition() {
        AprilTagDetection d = getBestDetection();
        if (d == null) return new Position(DistanceUnit.INCH, 0, 0, 0, 0);
        if (
                BarnRobot.getInstance().opmodeData.allianceColor == OpModeData.AllianceColor.BLUE && d.id == 20 ||
                        BarnRobot.getInstance().opmodeData.allianceColor == OpModeData.AllianceColor.RED && d.id == 24
        ){
            return d.robotPose.getPosition();
        }
        else
            return new Position(DistanceUnit.INCH, 0, 0, 0, 0);
    }

/** Returns robot orientation in DEGREES relative to the field, or null if no valid tag */
    public YawPitchRollAngles getRobotOrientation() {
        AprilTagDetection d = getBestDetection();
        if (d == null) return new YawPitchRollAngles(AngleUnit.DEGREES, 0, 0, 0, 0);
        return d.robotPose.getOrientation();
    }

    /** Updates the localizer with the current robot pose */
    public void updatePose(){
        if (getRobotPosition() != null){
            Pose2d currenrPose = new Pose2d(getRobotPosition().x, getRobotPosition().y, BarnRobot.getInstance().pinpointLocalizer.getPose().heading.toDouble());
            BarnRobot.getInstance().pinpointLocalizer.setPose(currenrPose);
        }
    }

    public boolean isLocalizationTagDetected(){
        AprilTagDetection d = getBestDetection();
        return isTagDetected() &&
                (BarnRobot.getInstance().opmodeData.allianceColor == OpModeData.AllianceColor.BLUE&& d.id == 20 ||
                BarnRobot.getInstance().opmodeData.allianceColor == OpModeData.AllianceColor.RED && d.id == 24);
    }

    public boolean isTagDetected(){
        return getBestDetection() != null;
    }

    /** Stop camera stream */
    public void stopStreaming() {
        if (visionPortal != null) visionPortal.stopStreaming();
    }

    /** Resume camera stream */
    public void resumeStreaming() {
        if (visionPortal != null) visionPortal.resumeStreaming();
    }

    /** Close the camera to save power */
    public void close() {
        if (visionPortal != null) visionPortal.close();
    }

    /** Operate webcam. */
    @Override
    public void periodic() {

        if (isLocalizationTagDetected()
//                && BarnRobot.getInstance().drive.getDistanceFromGoal() < MAX_UPDATE_DISTANCE
                && poseUpdateTimer.seconds() >= POSE_UPDATE_INTERVAL_SEC &&
                BarnRobot.getInstance().drive.isRobotStatic()
                ) {

            AprilTagDetection d = getBestDetection();
            BarnRobot.getInstance().telemetry.addData("Robot x: ", d.robotPose.getPosition().x);
            BarnRobot.getInstance().telemetry.addData("Robot y: ", d.robotPose.getPosition().y);

            updatePose();
            poseUpdateTimer.reset();
        }
    }


    /** Display webcam + pinpoint telemetry. */
    public void displayTelemetry() {
        BarnRobot.getInstance().telemetry.addData("Robot orintation: ", getRobotOrientation().getYaw());
        BarnRobot.getInstance().telemetry.addData("Robot x: ", getRobotPosition().x);
        BarnRobot.getInstance().telemetry.addData("Robot y: ", getRobotPosition().y);
    }

}
//---------------------------------------------------------------------------------------------
//
//-------------

