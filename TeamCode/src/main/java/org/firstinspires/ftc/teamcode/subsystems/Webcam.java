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
 * Webcam Vision Subsystem
 *
 * Handles:
 * - AprilTag field localization
 * - MOTIF pattern detection
 * - Goal alignment yaw calculation (field-based)
 * - Safe pose correction when robot is static
 */
@Config
public class Webcam extends SubsystemBase {

    /* ---------------- FIELD CONSTANTS ---------------- */

    /** Goal X position on field (inches) */
    private static final double GOAL_X = -64.96;

    /** Goal Y positions depending on alliance (inches) */
    private static final double BLUE_GOAL_Y = -1.45;
    private static final double RED_GOAL_Y  =  1.45;

    /* ---------------- VISION CONSTANTS ---------------- */

    private static final double POSE_UPDATE_INTERVAL_SEC = 3;
    public static double MAX_UPDATE_DISTANCE = 1.5;

    private static final Size CAMERA_RESOLUTION = new Size(640, 480);

    public static double WEBCAM_X = 0;
    public static double WEBCAM_Y = 7.3;
    public static double WEBCAM_Z = 36;

    private static final YawPitchRollAngles CAMERA_ORIENTATION =
            new YawPitchRollAngles(AngleUnit.DEGREES, 0, -64, 0, 0);

    private static final double FX = 500, FY = 500, CX = 320, CY = 240;

    /* ---------------- STATE ---------------- */

    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTag;
    private final ElapsedTime poseUpdateTimer = new ElapsedTime();

    /** Current detected MOTIF pattern */
    private Pattern gamePattern;

    /** Field-based yaw offset to goal (degrees) */
    private double dYaw = 0;

    /* ---------------- GAME PATTERN ENUM ---------------- */

    public enum Pattern { PPG, PGP, GPP }

    /* ---------------- INIT ---------------- */

    public Webcam(HardwareMap hardwareMap) {
        initVision(hardwareMap);
    }

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

    /* ---------------- APRILTAG ACCESS ---------------- */

    public List<AprilTagDetection> getDetections() {
        return aprilTag.getDetections();
    }

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

    public boolean isLocalizationTagDetected() {
        return getBestDetection() != null;
    }

    /* ---------------- GOAL ALIGNMENT ---------------- */

    /**
     * Updates the yaw offset required for the robot to face the goal.
     * Positive = turn left, Negative = turn right
     */
    private void updateGoalYaw() {
        AprilTagDetection d = getBestDetection();
        if (d == null) return;

        double robotX = d.robotPose.getPosition().x;
        double robotY = d.robotPose.getPosition().y;

        double goalY = BarnRobot.getInstance().opmodeData.allianceColor == OpModeData.AllianceColor.BLUE
                ? BLUE_GOAL_Y
                : RED_GOAL_Y;

        double angleToGoal = Math.toDegrees(Math.atan2(goalY - robotY, GOAL_X - robotX));

        double robotHeading = Math.toDegrees(
                BarnRobot.getInstance().pinpointLocalizer.getPose().heading.toDouble()
        );

        dYaw = angleWrap(angleToGoal - robotHeading);
    }

    private double angleWrap(double degrees) {
        while (degrees > 180) degrees -= 360;
        while (degrees < -180) degrees += 360;
        return degrees;
    }

    public double getGoalYawOffset() {
        return dYaw;
    }

    /* ---------------- POSE UPDATE ---------------- */

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

    /* ---------------- GAME PATTERN ---------------- */

    public double getBotHeading(){
        double heading = 0;
        try {
            if (!aprilTag.getDetections().isEmpty()) {
                heading = aprilTag.getDetections().get(0).robotPose.getOrientation().getYaw(AngleUnit.DEGREES);
                heading += 90;
            }
            return heading;
        }
        catch (IndexOutOfBoundsException out){
            return 0;
        }
        catch (NullPointerException e){
            return 0;
        }
    }
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

    public Pattern getGamePattern() {
        return gamePattern;
    }

    /* ---------------- DISTANCE TO GOAL ---------------- */

    public double getDistanceToGoal() {
        AprilTagDetection d = getBestDetection();
        if (d == null) return -1;

        double goalY = BarnRobot.getInstance().opmodeData.allianceColor == OpModeData.AllianceColor.BLUE
                ? BLUE_GOAL_Y
                : RED_GOAL_Y;

        double dx = GOAL_X - d.robotPose.getPosition().x;
        double dy = goalY - d.robotPose.getPosition().y;

        return Math.hypot(dx, dy);
    }

    /* ---------------- PERIODIC ---------------- */

    @Override
    public void periodic() {
        if (isLocalizationTagDetected()
                && BarnRobot.getInstance().drive.isRobotStatic()
                && poseUpdateTimer.seconds() >= POSE_UPDATE_INTERVAL_SEC) {

            updatePose();
            poseUpdateTimer.reset();
        }

        if (getBotHeading() != 0){
            updateHeading(getBotHeading());
        }
    }

    private static double wrapTo180(double angle) {
        angle = angle % 360;          // keep within 0–360 range
        if (angle > 180) {
            angle -= 360;
        }
        return angle;
    }

    private void updateHeading(double heading){
        Pose2d newPose = new Pose2d(
                BarnRobot.getInstance().pinpointLocalizer.getPose().position.x,
                BarnRobot.getInstance().pinpointLocalizer.getPose().position.y,
                Math.toRadians(wrapTo180(heading))
        );

        BarnRobot.getInstance().pinpointLocalizer.setPose(newPose);
    }
    /* ---------------- CAMERA CONTROL ---------------- */

    public void stopStreaming() {
        if (visionPortal != null) visionPortal.stopStreaming();
    }

    public void resumeStreaming() {
        if (visionPortal != null) visionPortal.resumeStreaming();
    }

    public void close() {
        if (visionPortal != null) visionPortal.close();
    }

    /* ---------------- TELEMETRY ---------------- */

    public void displayTelemetry() {
        BarnRobot robot = BarnRobot.getInstance();
        AprilTagDetection d = getBestDetection();
        robot.telemetry.addData("isRobotStatic", BarnRobot.getInstance().drive.isRobotStatic());
        robot.telemetry.addData("poseUpdateTimer.secpnds()", poseUpdateTimer.seconds());
        robot.telemetry.addData("isLocalizationTagDetected", isLocalizationTagDetected());
        robot.telemetry.addData("Webcam Robot X", d != null ? d.robotPose.getPosition().x : "N/A");
        robot.telemetry.addData("Webcam Robot Y", d != null ? d.robotPose.getPosition().y : "N/A");
        robot.telemetry.addData("webcam heading", getBotHeading());
        if (!aprilTag.getDetections().isEmpty()){
            robot.telemetry.addData("webcam real heading", aprilTag.getDetections().get(0).robotPose.getOrientation().getYaw(AngleUnit.DEGREES));
        }
        robot.telemetry.addData("Webcam Goal dYaw", dYaw);
        robot.telemetry.addData("Webcam Goal Distance", getDistanceToGoal());
    }
}
