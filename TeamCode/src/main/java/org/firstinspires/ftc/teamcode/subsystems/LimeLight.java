
package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.OpModeData;

import java.util.List;

/**
 * Subsystem for controlling and reading data from the Limelight3A vision sensor.

 * Handles pipeline switching, fiducial detection, range calculation, and telemetry output.
 */
public class LimeLight extends SubsystemBase {

    /*
    Limelight 3d Pos on bot:
        - Forward: 0.2
        - Up: 0.34
        - Right: 0
     */
    public static boolean isAlignmentReady;

    /** Maximum allowed staleness for vision data. */

    public static final int STANDARD_STALENESS_TOLERANCE = 100;

    private static final double POSE_UPDATE_INTERVAL_SEC = 5.0;

    public Position lastDetection;
    private final ElapsedTime poseUpdateTimer = new ElapsedTime();
    private final ElapsedTime validShootTimer = new ElapsedTime();

    /** Polling frequency of the Limelight in Hz. */
    public static final int POLL_RATE_HZ = 100;

    /** Field coordinates for the target goal. */
    private static final double GOAL_X = -1.65;
    private static final double BLUE_GOAL_Y = -1.62;
    private static final double RED_GOAL_Y = 1.62;

    /** Limelight hardware object. */
    private final Limelight3A limelight;

    /** Latest result from the Limelight. */
    private LLResult llResult;

    /** List of fiducial results from the Limelight. */
    private List<LLResultTypes.FiducialResult> frs;


    /** Yaw offset to goal. */
    private double Dyaw;

    /** Distance to goal in meters. */
    private double goalRange;

    /**
     * Constructs the LimeLight subsystem and initializes default settings.
     */
    public LimeLight() {
        limelight = BarnRobot.getInstance().robotHardware.limelight;
        poseUpdateTimer.reset();
        validShootTimer.reset();
        limelight.pipelineSwitch(7);
        limelight.setPollRateHz(POLL_RATE_HZ);
        resetData();
        start();
        isAlignmentReady = false;
    }

    public void resetData(){
        llResult = null;
        frs = null;
        Dyaw = 0;
        goalRange = 0;
    }



    public boolean isAlignedToGoal(){
        if(!(isGoalTagDetected() && Math.abs(getGoalDistance()) < 0.5)){
            validShootTimer.reset();
        }
        if(validShootTimer.seconds() > 0.5) return true;
        return false;

    }

    /** Starts the Limelight processing loop. */
    public void start() {
        limelight.start();
    }


    /**
     * Checks if the Limelight data is valid and up-to-date.
     *
     * @return true if data is valid
     */
    public boolean isDataValid() {
        return llResult != null && frs != null && !frs.isEmpty() && llResult.isValid()
                && llResult.getStaleness() < STANDARD_STALENESS_TOLERANCE;
    }

    /**
     * Finds the fiducial result with the largest target area.
     *
     * @param frs list of fiducial results
     * @return fiducial result with largest area
     */
    private LLResultTypes.FiducialResult findLargestAreaFr(List<LLResultTypes.FiducialResult> frs) {
        LLResultTypes.FiducialResult largest = frs.get(0);
        for (LLResultTypes.FiducialResult fr : frs) {
            if (fr.getTargetArea() > largest.getTargetArea()) {
                largest = fr;
            }
        }
        return largest;
    }

    public double getGoalDistance(){
        if(isGoalTagDetected()){
            LLResultTypes.FiducialResult largest = frs.get(0);
            for (LLResultTypes.FiducialResult fr : frs) {
                if (fr.getTargetArea() > largest.getTargetArea()) {
                    largest = fr;
                }
            }
            return largest.getTargetPoseCameraSpace().getPosition().z;
        }
        return BarnRobot.getInstance().drive.getDistanceFromGoal();

    }

    public double cashedYaw = -1;
    public double getGoalYaw(){
        if (frs != null){
            LLResultTypes.FiducialResult largest = frs.get(0);
            for (LLResultTypes.FiducialResult fr : frs) {
                if (fr.getTargetArea() > largest.getTargetArea()) {
                    largest = fr;
                }
            }
            cashedYaw = largest.getTargetPoseCameraSpace().getPosition().x;
        }
        return cashedYaw;
    }

    /**
     * Maps a fiducial ID to an obelisk pattern.
     *
     * @param id fiducial ID
     * @return corresponding obelisk pattern
     */


    /**
     * Calculates distance between robot and goal coordinates.
     *
     * @param xG goal X
     * @param yG goal Y
     * @param xR robot X
     * @param yR robot Y
     * @return Euclidean distance
     */
    private double calcRange(double xG, double yG, double xR, double yR) {
        return Math.sqrt((xG - xR) * (xG - xR) + (yG - yR) * (yG - yR));
    }

    /**
     * Detects the obelisk pattern if it hasn't been found yet.
     */

    /** Updates the yaw offset (Dyaw) if a goal tag is detected. */
    public void findDyaw() {
        if (isDataValid() && isGoalTagDetected()) {
            Dyaw = frs.get(0).getTargetXDegrees();
        }
    }

    /**
     * Calculates the range to the goal using the robot's heading.
     *
     * @param heading current robot heading
     */
    public void findRange(double heading) {
        if (isDataValid()) {
            limelight.updateRobotOrientation(heading);
            Pose3D botpose_mt2 = llResult.getBotpose_MT2();
            if (botpose_mt2 != null) {
                double x = botpose_mt2.getPosition().x;
                double y = botpose_mt2.getPosition().y;
                goalRange = calcRange(GOAL_X, BLUE_GOAL_Y, x, y);
            }
        }
    }

    /**
     * Checks if the Limelight currently sees a goal tag.
     *
     * @return true if a goal tag is detected
     */
    public boolean isGoalTagDetected() {
        boolean frsContainsGoalTag = false;
        if (frs != null){
            for (LLResultTypes.FiducialResult fr: frs){
                if (
                        (fr.getFiducialId() == 20 && BarnRobot.getInstance().opmodeData.allianceColor == OpModeData.AllianceColor.BLUE )||
                                (fr.getFiducialId() == 24 && BarnRobot.getInstance().opmodeData.allianceColor == OpModeData.AllianceColor.RED)
                )
                    frsContainsGoalTag = true;
            }
        }
        return frsContainsGoalTag;
    }

    /** Updates Limelight results; should be called periodically. */
    @Override
    public void periodic() {
        llResult = limelight.getLatestResult();
        frs = llResult.getFiducialResults();
        isAlignmentReady = isAlignedToGoal();
        limelight.updateRobotOrientation(Math.toDegrees(BarnRobot.getInstance().pinpointLocalizer.getPose().heading.toDouble()));

        if (isGoalTagDetected()
                && poseUpdateTimer.seconds() >= POSE_UPDATE_INTERVAL_SEC &&
                BarnRobot.getInstance().drive.isRobotStatic()
        ) {
            updatePose();
            poseUpdateTimer.reset();
        }
    }

    public void updatePose(){
        if (llResult.getBotpose_MT2() != null){
            Pose2d currenrPose = new Pose2d(llResult.getBotpose_MT2().getPosition().x / 0.0254 ,llResult.getBotpose_MT2().getPosition().y / 0.0254, BarnRobot.getInstance().pinpointLocalizer.getPose().heading.toDouble());
            BarnRobot.getInstance().pinpointLocalizer.setPose(currenrPose);
        }
    }

//    public Pose3D getRobotFieldPose(){
//        if (llResult.getBotpose_MT2() != null)
//            return llResult.getBotpose_MT2();
//        return null;
//    }

    /** Outputs all relevant telemetry for the Limelight subsystem. */
    public void displayTelemetry() {
        BarnRobot robot = BarnRobot.getInstance();
        robot.telemetry.addData("Limelight Data Valid", isDataValid());
        robot.telemetry.addData("Limelight llResult != null", limelight.getLatestResult() != null);
        robot.telemetry.addData("Limelight goal detected", isGoalTagDetected());
        robot.telemetry.addData("Aligned", isAlignedToGoal());
// && poseUpdateTimer.seconds() >= POSE_UPDATE_INTERVAL_SEC
        if (isGoalTagDetected()) {
            robot.telemetry.addData("Limelight llAngle", llResult.getBotpose().getOrientation());
            robot.telemetry.addData("Limelight distance", getGoalDistance());
            robot.telemetry.addData("Limelight yaw", getGoalYaw());
        }

        poseUpdateTimer.reset();
    }

    /** @return current yaw offset to goal */
    public double getDyaw() {
        return Dyaw;
    }


    /** @return calculated distance to goal in meters */
    public double getGoalRange() {
        return goalRange;
    }


}
