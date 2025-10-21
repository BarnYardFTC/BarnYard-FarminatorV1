package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.OpModeData.AllianceColor;

import java.util.List;

public class LimeLight extends SubsystemBase {

    // ------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------

    // Pipelines
    public static final int STANDARD_PIPELINE = 0;
    public static final int BLUE_PIPELINE = 1;
    public static final int RED_PIPELINE = 2;
    public static final int OBELISK_PIPELINE = 3;

    // Polling and staleness
    public static final int STANDARD_STALENESS_TOLERANCE = 100;
    public static final int POLL_RATE_HZ = 100;

    // Field coordinates
    private static final double GOAL_X = -1.57;
    private static final double BLUE_GOAL_Y = -1.62;
    private static final double RED_GOAL_Y = 1.62;

    // ------------------------------------------------------------
    // Enums
    // ------------------------------------------------------------

    public enum Pattern {
        PPG,
        PGP,
        GPP
    }

    // ------------------------------------------------------------
    // Hardware & State
    // ------------------------------------------------------------

    private final Limelight3A limelight;
    private LLResult llResult;
    private List<LLResultTypes.FiducialResult> frs;

    // Vision data
    private Pattern obeliskPattern;
    private double Dyaw;
    private double goalRange;

    // Current pipeline
    private int currentPipeline;

    // ------------------------------------------------------------
    // Constructor & Initialization
    // ------------------------------------------------------------

    public LimeLight() {
        limelight = BarnRobot.getInstance().farminatorHardware.limelight;
        limelight.setPollRateHz(POLL_RATE_HZ);
        switchPipeline(STANDARD_PIPELINE);
        Dyaw = 0;
        start();
    }

    public void start() {
        limelight.start();
    }

    // ------------------------------------------------------------
    // Pipeline Management
    // ------------------------------------------------------------

    public void switchPipeline(int pipeline) {
        limelight.pipelineSwitch(pipeline);
        currentPipeline = pipeline;
        llResult = null;
        frs = null;
    }

    public void switchToLocalizationPipeline() {
        AllianceColor alliance = BarnRobot.getInstance().opmodeData.allianceColor;
        if (alliance == AllianceColor.BLUE) {
            switchPipeline(BLUE_PIPELINE);
        } else {
            switchPipeline(RED_PIPELINE);
        }
    }

    // ------------------------------------------------------------
    // Data Validation
    // ------------------------------------------------------------

    private boolean isDataValid() {
        if (currentPipeline == OBELISK_PIPELINE) {
            return llResult != null;
        } else {
            return llResult != null && llResult.isValid() &&
                    llResult.getStaleness() < STANDARD_STALENESS_TOLERANCE;
        }
    }

    // ------------------------------------------------------------
    // Vision Detection Helpers
    // ------------------------------------------------------------

    private LLResultTypes.FiducialResult findLargestAreaFr(List<LLResultTypes.FiducialResult> frs) {
        LLResultTypes.FiducialResult largest = frs.get(0);
        for (LLResultTypes.FiducialResult fr : frs) {
            if (fr.getTargetArea() > largest.getTargetArea()) {
                largest = fr;
            }
        }
        return largest;
    }

    private Pattern getObeliskPattern(int id) {
        switch (id) {
            case 21: return Pattern.GPP;
            case 22: return Pattern.PGP;
            case 23: return Pattern.PPG;
            default: return null;
        }
    }

    private double calcRange(double xG, double yG, double xR, double yR) {
        return Math.sqrt((xG - xR) * (xG - xR) + (yG - yR) * (yG - yR));
    }

    // ------------------------------------------------------------
    // Vision Processing
    // ------------------------------------------------------------

    public void findPattern() {
        if (isDataValid() && !isPatternFound()) {
            LLResultTypes.FiducialResult obeliskFr = findLargestAreaFr(frs);
            obeliskPattern = getObeliskPattern(obeliskFr.getFiducialId());
        }
    }

    public void findDyaw() {
        if (isDataValid() && isGoalTagDetected()){
            Dyaw = frs.get(0).getTargetXDegrees();
        }
    }

    public void findRange() {
        if (isDataValid()) {
            limelight.updateRobotOrientation(BarnRobot.getInstance().drive.getHeading());
            Pose3D botpose_mt2 = llResult.getBotpose_MT2();
            if (botpose_mt2 != null) {
                double x = botpose_mt2.getPosition().x;
                double y = botpose_mt2.getPosition().y;
                goalRange = calcRange(GOAL_X, BLUE_GOAL_Y, x, y);
            }
        }
    }

    // ------------------------------------------------------------
    // Status Checks
    // ------------------------------------------------------------

    public boolean isPatternFound() {
        return obeliskPattern != null;
    }

    public boolean isGoalTagDetected() {
        return (currentPipeline == BLUE_PIPELINE || currentPipeline == RED_PIPELINE)
                && isDataValid();
    }

    // ------------------------------------------------------------
    // Periodic Update & Telemetry
    // ------------------------------------------------------------

    @Override
    public void periodic() {
        llResult = limelight.getLatestResult();
        if (llResult.isValid() && !llResult.getFiducialResults().isEmpty()) {
            frs = llResult.getFiducialResults();
        }
    }

    public void displayTelemetry() {
        BarnRobot robot = BarnRobot.getInstance();
        robot.telemetry.addData("Detected", llResult != null && llResult.isValid());
        robot.telemetry.addData("Data Valid", isDataValid());
        robot.telemetry.addData("Dyaw", Dyaw);

        Pose3D pose = llResult != null ? llResult.getBotpose_MT2() : null;
        if (pose != null) {
            robot.telemetry.addData("Location", "(" + pose.getPosition().x + ", " + pose.getPosition().y + ")");
        }

        robot.telemetry.addData("Range", getGoalRange());
    }

    // ------------------------------------------------------------
    // Getters
    // ------------------------------------------------------------

    public double getDyaw() {
        return Dyaw;
    }

    public Pattern getObeliskPattern() {
        return obeliskPattern;
    }

    public double getGoalRange() {
        return goalRange;
    }
}