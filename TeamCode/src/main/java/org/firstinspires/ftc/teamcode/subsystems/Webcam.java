package org.firstinspires.ftc.teamcode.subsystems;

import android.util.Size;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.eosvsim.ArtifactDetection;
import org.firstinspires.ftc.teamcode.subsystems.components.ArtifactPipeline;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;

import java.util.List;

@Config
public class Webcam extends SubsystemBase {

//    private VisionPortal visionPortal;
//    private AprilTagProcessor aprilTag;
//    private ArtifactDetection detector;
    private OpenCvCamera camera;
    private ArtifactDetection pipeline;


    private double shooterLeftPixel;
    private double middleLeftPixel;

    private busyType shooterPos = busyType.FREE;
    private busyType middlePos = busyType.FREE;

    private artifactReadiness currentMiddleArtifactReadiness = artifactReadiness.NOTHING;
    private artifactReadiness currentShooterArtifactReadiness = artifactReadiness.NOTHING;

    public enum artifactReadiness {READY, UNREADY, NOTHING}
    private enum busyType {BUSY, FREE}

    public enum artifactPositions {
        SHOOTPOSMIN(800), MIDDLEPOS(500);
        private int numVal;

        artifactPositions(int numVal) {
            this.numVal = numVal;
        }

        public int getNumVal() {
            return numVal;
        }
    }




    public Webcam(HardwareMap hw) {
        initArtFinder(hw);
    }

    private void initArtFinder(HardwareMap hardwareMap) {

        int cameraMonitorViewId = hardwareMap.appContext.getResources()
                .getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance()
                .createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        pipeline = new ArtifactDetection();

        camera.setPipeline(pipeline);


    }


    public void ArtifactLeftPixel(){
        if (pipeline.isArtifactFound()){
            /** This logic for 2 artifacts in the robot(For the future) */
//            for (int i=0; i<corners.size(); i++){
//                if (i%3 == 0){
//                    if (corners.get(i).get(0) > artifactPositions.MIDDLEPOS.getNumVal() && corners.get(i).get(0) <= artifactPositions.SHOOTPOSMIN.getNumVal()){
//                        shooterLeftPixel = corners.get(i).get(0);
//                    }
//                    if (corners.get(i).get(0) > artifactPositions.INTAKEPOS.getNumVal() && corners.get(i).get(0) <= artifactPositions.MIDDLEPOS.getNumVal()){
//                        middleLeftPixel = corners.get(i).get(0);
//                    }
//                    if (corners.get(i).get(0) <= artifactPositions.INTAKEPOS.getNumVal()){
//                        intakeLeftPixel = corners.get(i).get(0);
//                    }
//                }
//            }
            shooterLeftPixel = pipeline.getLeftBorderX();
        }
    }


    /** Updates the current artifact readiness */
    public void ArtifactReadinessFunc(){
        ArtifactLeftPixel();
        if (pipeline.isArtifactFound()){
            currentShooterArtifactReadiness = artifactReadiness.READY;
//            if (shooterLeftPixel > artifactPositions.SHOOTPOSMIN.getNumVal() && shooterPos == busyType.FREE){
//                shooterPos = busyType.BUSY;
//                telemetry.addLine("Artifact state is READY");
//                ArtifactReadiness.remove(artifactReadiness.UNREADY);
//                ArtifactReadiness.add(artifactReadiness.READY);
//            }
//            else{
//                shooterPos = busyType.FREE;
//                this.telemetry.addLine("Artifact state is UNREADY");
//                ArtifactReadiness.remove(artifactReadiness.READY);
//                ArtifactReadiness.add(artifactReadiness.UNREADY);
//            }
        }
        else {
            currentShooterArtifactReadiness = artifactReadiness.NOTHING;
        }

    }




    /** Display webcam + pinpoint telemetry. */
    public void displayTelemetry() {
        System.out.println("Artifact X: " + pipeline.getLeftBorderX());

    }

    public Enum<artifactReadiness> getArtifactReadiness(){
        return currentShooterArtifactReadiness;
    }
}
//---------------------------------------------------------------------------------------------
//    public static double MAX_UPDATE_DISTANCE = 1.5;

//    public Position lastDetection;
//    private final ElapsedTime poseUpdateTimer = new ElapsedTime();
//
//    public static final int BLUE_LOCALIZATION_PIPELINE = 1;
//    public static final int RED_LOCALIZATION_PIPELINE = 2;
//    private static final double POSE_UPDATE_INTERVAL_SEC = 5.0;
//
//    public enum Pattern {
//        PPG,
//        PGP,
//        GPP
//    }

//    private Pattern gamePattern;

// CHANGE THESE TO MATCH YOUR CAMERA MOUNT!!!

//    public static double WEBCAM_X = 12;
//    public static double WEBCAM_Y = 12;
//    public static double WEBCAM_Z = 27.5;
//
//    private Position cameraPosition = new Position(
//            DistanceUnit.CM,
//            WEBCAM_X, WEBCAM_Y, WEBCAM_Z, 0
//    );

//    private final YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(
//            AngleUnit.DEGREES,
//            0, -70, 0, 0
//    );
// Telemetry
//        Position pos = getRobotPosition();

//        BarnRobot.getInstance().telemetry.addData("Webcam pose x:", pos.x);
//        BarnRobot.getInstance().telemetry.addData("Webcam pose y:", pos.y);

//        BarnRobot.getInstance().telemetry.addData("Pattern:", getGamePattern());
//-------------

//init
//      cameraPosition = new Position(
//                DistanceUnit.CM,
//                WEBCAM_X, WEBCAM_Y, WEBCAM_Z, 0
//        );
//
//        aprilTag = new AprilTagProcessor.Builder()
//                .setCameraPose(cameraPosition, cameraOrientation)
//                .build();
//        VisionPortal.Builder builder = new VisionPortal.Builder();
//        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
//        builder.addProcessor(aprilTag);
//        visionPortal = builder.build();
//        lastDetection = new Position();



/** Returns the full list of AprilTag detections */
//    public List<AprilTagDetection> getDetections() {
//        return aprilTag.getDetections();
//    }

/** Returns the first valid detection with field pose (ignores Obelisk tags) */
//    public AprilTagDetection getBestDetection() {
//        for (AprilTagDetection d : aprilTag.getDetections()) {
//            if (d.metadata != null && !d.metadata.name.contains("Obelisk")) {
//                if (d.id == 24 && BarnRobot.getInstance().opmodeData.allianceColor == OpModeData.AllianceColor.RED ||
//                d.id == 20 && BarnRobot.getInstance().opmodeData.allianceColor == OpModeData.AllianceColor.BLUE){
//                    return d;
//                }
//            }
//        }
//        return null;
//    }


/** Updates MOTIF pattern based on the obelisk detections (slightly changed code from a func above) */
//    public void updateGamePattern() {
//        for (AprilTagDetection d : aprilTag.getDetections()) {
//            if (d.metadata != null && d.metadata.name.contains("Obelisk")) {
//                switch (d.metadata.id) {
//                    case 21:
//                        gamePattern = Pattern.GPP;
//                        break;
//                    case 22:
//                        gamePattern = Pattern.PGP;
//                        break;
//                    case 23:
//                        gamePattern = Pattern.PPG;
//                        break;
//                    default:
//                        gamePattern = null;
//                }
//            }
//        }
//    }

//    public Pattern getGamePattern() {
//        return gamePattern;
//    }

/** Returns robot position in INCHES relative to the field, or null if no valid tag */
//    public Position getRobotPosition() {
//        AprilTagDetection d = getBestDetection();
//        if (d == null) return new Position(DistanceUnit.INCH, 0, 0, 0, 0);
//        if (
//                BarnRobot.getInstance().opmodeData.webcamPipeline == Webcam.BLUE_LOCALIZATION_PIPELINE && d.id == 20 ||
//                        BarnRobot.getInstance().opmodeData.webcamPipeline == Webcam.BLUE_LOCALIZATION_PIPELINE && d.id == 24
//        ){
//            return d.robotPose.getPosition();
//        }
//        else
//            return new Position(DistanceUnit.INCH, 0, 0, 0, 0);
//    }

/** Returns robot orientation in DEGREES relative to the field, or null if no valid tag */
//    public YawPitchRollAngles getRobotOrientation() {
//        AprilTagDetection d = getBestDetection();
//        if (d == null) return new YawPitchRollAngles(AngleUnit.DEGREES, 0, 0, 0, 0);
//        return d.robotPose.getOrientation();
//    }
//
//    /** Updates the localizer with the current robot pose */
//    public void updatePose(){
//        if (getRobotPosition() != null){
//            Pose2d currenrPose = new Pose2d(getRobotPosition().x, getRobotPosition().y, BarnRobot.getInstance().pinpointLocalizer.getPose().heading.toDouble());
//            BarnRobot.getInstance().pinpointLocalizer.setPose(currenrPose);
//        }
//    }
//
//    public boolean isLocalizationTagDetected(){
//        AprilTagDetection d = getBestDetection();
//        return isTagDetected() &&
//                (BarnRobot.getInstance().opmodeData.webcamPipeline == Webcam.BLUE_LOCALIZATION_PIPELINE && d.id == 20 ||
//                BarnRobot.getInstance().opmodeData.webcamPipeline == Webcam.BLUE_LOCALIZATION_PIPELINE && d.id == 24);
//    }
//
//    public boolean isTagDetected(){
//        return getBestDetection() != null;
//    }
//
//    /** Stop camera stream */
//    public void stopStreaming() {
//        if (visionPortal != null) visionPortal.stopStreaming();
//    }
//
//    /** Resume camera stream */
//    public void resumeStreaming() {
//        if (visionPortal != null) visionPortal.resumeStreaming();
//    }
//
//    /** Close the camera to save power */
//    public void close() {
//        if (visionPortal != null) visionPortal.close();
//    }
//
//    /** Operate webcam. */
//    @Override
//    public void periodic() {
//        if (isLocalizationTagDetected()
//                && BarnRobot.getInstance().drive.getDistanceFromGoal() < MAX_UPDATE_DISTANCE
//                && poseUpdateTimer.seconds() >= POSE_UPDATE_INTERVAL_SEC &&
//                BarnRobot.getInstance().drive.isRobotStatic()
//                ) {
//
//            updatePose();
//            poseUpdateTimer.reset();
//        }
//
//        if (gamePattern == null) {
//            updateGamePattern();
//        }
//
//        displayTelemetry();
//    }

//------------------------------------------------------------------------------------------------------

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
