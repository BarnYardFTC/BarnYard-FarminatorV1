package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robocol.Command;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.PinpointLocalizer;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

@Config
public class Webcam extends SubsystemBase {

    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTag;

    public static double MAX_UPDATE_DISTANCE = 1.5;

    public enum Pattern {
        PPG,
        PGP,
        GPP
    }

    private Pattern gamePattern;

    // CHANGE THESE TO MATCH YOUR CAMERA MOUNT!!!
    private final Position cameraPosition = new Position(
            DistanceUnit.CM,
            12, 10, 30, 0
    );

    private final YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(
            AngleUnit.DEGREES,
            0, -90, 0, 0
    );

    public Webcam(HardwareMap hw) {
        initAprilTag(hw);
    }

    private void initAprilTag(HardwareMap hardwareMap) {
        aprilTag = new AprilTagProcessor.Builder()
                .setCameraPose(cameraPosition, cameraOrientation)
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        builder.addProcessor(aprilTag);
        visionPortal = builder.build();
    }

    /** Returns the full list of AprilTag detections */
    public List<AprilTagDetection> getDetections() {
        return aprilTag.getDetections();
    }

    /** Returns the first valid detection with field pose (ignores Obelisk tags) */
    public AprilTagDetection getBestDetection() {
        for (AprilTagDetection d : aprilTag.getDetections()) {
            if (d.metadata != null && !d.metadata.name.contains("Obelisk")) {
                return d;
            }
        }
        return null;
    }

    /** Updates MOTIF pattern based on the obelisk detections (slightly changed code from a func above) */
    public void updateGamePattern() {
        for (AprilTagDetection d : aprilTag.getDetections()) {
            if (d.metadata != null && d.metadata.name.contains("Obelisk")) {
                switch (d.id) {
                    case 21:
                        gamePattern = Pattern.GPP;
                    case 22:
                        gamePattern = Pattern.PGP;
                    case 23:
                        gamePattern = Pattern.PPG;
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
        if (d == null) return null;
        return d.robotPose.getPosition();
    }

    /** Returns robot orientation in DEGREES relative to the field, or null if no valid tag */
    public YawPitchRollAngles getRobotOrientation() {
        AprilTagDetection d = getBestDetection();
        if (d == null) return null;
        return d.robotPose.getOrientation();
    }

    /** Updates the localizer with the current robot pose */
    public void updatePose(){
        Pose2d currenrPose = new Pose2d(getRobotPosition().x, getRobotPosition().y,  getRobotOrientation().getYaw());
        BarnRobot.getInstance().pinpointLocalizer.setPose(currenrPose);
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
    public void operate() {
        if (getBestDetection() != null && BarnRobot.getInstance().drive.getDistanceFromGoal() < MAX_UPDATE_DISTANCE) {
            updatePose();
        }
        if (gamePattern == null) {
            updateGamePattern();
        }
        displayTelemetry();
    }

    public RunCommand operateCommand() {
        return new RunCommand(() -> operate(), this);
    }

    /** Display webcam + pinpoint telemetry. */
    public void displayTelemetry() {
        if (getRobotPosition() != null) {
            BarnRobot.getInstance().telemetry.addData("Webcam pose x:", getRobotPosition().x);
            BarnRobot.getInstance().telemetry.addData("Webcam pose y:", getRobotPosition().y);

        }
        BarnRobot.getInstance().telemetry.addData("Pinpoint pose x:", BarnRobot.getInstance().pinpointLocalizer.getPose().position.x);
        BarnRobot.getInstance().telemetry.addData("Pinpoint pose y:", BarnRobot.getInstance().pinpointLocalizer.getPose().position.y);

        BarnRobot.getInstance().telemetry.addData("Pattern:", getGamePattern());

    }
}


//
//
//import com.qualcomm.robotcore.hardware.HardwareMap;
//
//import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
//import org.firstinspires.ftc.teamcode.subsystems.Webcam.AprilTagDetectionPipeline;
//import org.openftc.apriltag.AprilTagDetection;
//import org.openftc.apriltag.AprilTagPose;
//import org.openftc.easyopencv.OpenCvCamera;
//import org.openftc.easyopencv.OpenCvCameraFactory;
//import org.openftc.easyopencv.OpenCvCameraRotation;
//import org.openftc.easyopencv.OpenCvWebcam;
//
//
//public class WebCam {
//    private OpenCvWebcam internalWebcam;
//    private AprilTagDetectionPipeline aprilTagPipeline;
//    protected double yaw,roll,yawInDegrees;
//
//
//
//    public double robotYaw;
//    public boolean whereIsTag = false;
//    public boolean isTagDetectedABoolean = false;
//    private final int cameraCenterX = 320;
//
//
//    public WebCam(HardwareMap hardwareMap) {
//
//        int cameraMonitorViewId = hardwareMap.appContext.getResources()
//                .getIdentifier("cameraMonitorViewId", "id",
//                        hardwareMap.appContext.getPackageName());
//
//
//        internalWebcam = OpenCvCameraFactory.getInstance()
//                .createWebcam(
//                        hardwareMap.get(WebcamName.class, "Webcam 1"),
//                        cameraMonitorViewId
//                );
//
//        aprilTagPipeline = new AprilTagDetectionPipeline();
//
//        internalWebcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
//            @Override
//            public void onOpened() {
//
//                internalWebcam.setPipeline(aprilTagPipeline);
//
//                internalWebcam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
//            }
//
//            @Override
//            public void onError(int errorCode) {
//            }
//        });
//
//    }
//
//    public AprilTagDetection getLatestDetection() {
//        if (aprilTagPipeline.getDetectedTag() != null) {
//            isTagDetectedABoolean = true;
//            return aprilTagPipeline.getDetectedTag();
//        }
//        else {
//            return null;
//        }
//    }
//
//    public Pattern getPattern() {
//        switch (getLatestDetection().id) {
//            case 21: return Pattern.GPP;
//            case 22: return Pattern.PGP;
//            case 23: return Pattern.PPG;
//            default: return null;
//        }
//    }
//
//    public double isTagDetected(){
//        if (getLatestDetection() != null){
//            return 1;
//        }
//        return 0;
//    }
//
//    public AprilTagPose getPose() {
//        return aprilTagPipeline.getPose();
//    }
//
//
////    public AprilTagPose getPose(){
////        AprilTagDetection detection = getLatestDetection();
////        if (detection != null && detection.pose != null && detection.pose.R != null) {
////            return getLatestDetection().pose;
////        }
////
////        return null;
////    }
//
//    public double getYaw(){
//        AprilTagPose pose = getPose();
//        if (pose != null) {
//            roll = 0.0;
//            yaw = Math.atan2(-(pose.R).get(0,1), (pose.R).get(1,1));
//            double yawInDegrees = Math.toDegrees(yaw);
//            return yawInDegrees;
//        }
//
//        return 0;
//
//    }
//
//    public double getDistance() {
//        if (getLatestDetection() != null) {
//            if (cameraCenterX > getLatestDetection().center.x) {
//                whereIsTag = true;
//                return cameraCenterX - getLatestDetection().center.x;
//            } else {
//                return getLatestDetection().center.x - cameraCenterX;
//            }
//
//        }
//        return 0;
//
//    }
//
//
//
//    public double[] calculateRobotPos(double tag_field_x, double tag_field_y, double tag_camera_x_m, double tag_camera_z_m, double robot_heading){
//        double tag_camera_x = tag_camera_x_m * 100.0;
//        double tag_camera_z = tag_camera_z_m * 100.0;
//
//        double rotated_tag_X = tag_camera_x * Math.cos(robot_heading) - tag_camera_z * Math.sin(robot_heading);
//        double rotated_tag_Y = tag_camera_x* Math.sin(robot_heading) + tag_camera_z * Math.cos(robot_heading);
//
//        double robot_X = tag_field_x - rotated_tag_X;
//        double robot_Y = tag_field_y - rotated_tag_Y;
//
//        return new double[]{robot_X, robot_Y};
//    }
//
//    public double getRobotYaw(){
//        if (getLatestDetection() != null) {
//            if (whereIsTag) {
//                robotYaw = getYaw() + getDistance();
//            }
//            else{
//                robotYaw = getYaw() - getDistance();
//            }
//
////            double[] robotPos = calculateRobotPos(tagPoseX, tagPoseY, detectedTag.pose.x, detectedTag.pose.z, robotYaw);  this is how to use calculateRobotPos()
//
//        return robotYaw;
//        }
//        return 0;
//    }
//
//
//
//
//
//    public OpenCvWebcam getInternalWebcam() {
//        return internalWebcam;
//    }
//}
//
//
//
//
//
