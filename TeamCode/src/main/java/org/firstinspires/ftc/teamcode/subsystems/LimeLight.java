package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.OpModeData.AllianceColor;

import java.util.List;

public class LimeLight extends SubsystemBase {

    // Hardware
    private final Limelight3A limelight;

    // Latest vision results
    private LLResult llResult;
    private List<LLResultTypes.FiducialResult> frs;

    // Vision calculations
    private double Dyaw;
    public Pattern obeliskPattern;

    // Pipelines
    private int currentPipeline;
    private static final int STANDARD_PIPELINE = 0;
    private static final int BLUE_PIPELINE = 1;
    private static final int RED_PIPELINE = 2;
    private static final int OBELISK_PIPELINE = 3;

    // Staleness tolerances
    public static final long STANDARD_STALENESS_TOLERANCE = 100;
    public static final long LOCALIZATION_STALENESS_TOLERANCE = 100;

    // Obelisk patterns
    public enum Pattern {
        PPG,
        PGP,
        GPP
    }

    // Constructor
    public LimeLight() {
        limelight = BarnRobot.getInstance().farminatorHardware.limelight;
        switchPipeline(STANDARD_PIPELINE);
        Dyaw = 0;
    }

    // --- Pipeline Switching ---
    public void switchPipeline(int pipeline) {
        limelight.pipelineSwitch(pipeline);
        currentPipeline = pipeline;
        llResult = null;
        frs = null;
    }

    public void switchToLocalizationPipeline() {
        if (BarnRobot.getInstance().opmodeData.allianceColor == AllianceColor.BLUE) {
            switchPipeline(BLUE_PIPELINE);
        } else {
            switchPipeline(RED_PIPELINE);
        }
    }

    public void switchToObeliskPipeline() {
        switchPipeline(OBELISK_PIPELINE);
    }

    // --- Initialization ---
    public void start() {
        limelight.start();
    }

    // --- Validity Checks ---
    public boolean isValid() {
        return llResult.isValid();
    }

    private boolean isDataValid() {
        if (currentPipeline == OBELISK_PIPELINE) return llResult != null;
        if (currentPipeline == STANDARD_PIPELINE) return llResult != null && llResult.getStaleness() < STANDARD_STALENESS_TOLERANCE;
        if (currentPipeline == BLUE_PIPELINE || currentPipeline == RED_PIPELINE)
            return llResult != null && llResult.getStaleness() < LOCALIZATION_STALENESS_TOLERANCE;
        return false;
    }

    public boolean isPatternFound() {
        return obeliskPattern != null;
    }

    // --- Vision Processing ---
    public void findPattern() {
        if (isDataValid() && !isPatternFound()) {
            LLResultTypes.FiducialResult obeliskFr = findLargestAreaFr(frs);
            obeliskPattern = getObeliskPattern(obeliskFr.getFiducialId());
        }
    }

    private LLResultTypes.FiducialResult findLargestAreaFr(List<LLResultTypes.FiducialResult> frs) {
        LLResultTypes.FiducialResult largestAreaFr = frs.get(0);
        for (LLResultTypes.FiducialResult fr : frs) {
            if (fr.getTargetArea() > largestAreaFr.getTargetArea()) {
                largestAreaFr = fr;
            }
        }
        return largestAreaFr;
    }

    private Pattern getObeliskPattern(int id) {
        switch (id) {
            case 21: return Pattern.GPP;
            case 22: return Pattern.PGP;
            case 23: return Pattern.PPG;
            default: return null;
        }
    }

    public void calculateD(LLResultTypes.FiducialResult fr) {
        Dyaw = fr.getTargetXDegrees();
    }

    public double getDyaw() {
        return Dyaw;
    }

    // --- Periodic Updates ---
    @Override
    public void periodic() {
        llResult = limelight.getLatestResult();
        if (llResult.isValid() && !llResult.getFiducialResults().isEmpty()) {
            frs = llResult.getFiducialResults();
        }
    }

    // --- Telemetry ---
    public void displayTelemetry() {
        boolean valid = llResult != null && llResult.isValid();
        BarnRobot.getInstance().telemetry.addData("Detected", valid);
        BarnRobot.getInstance().telemetry.addData("Pattern", obeliskPattern);
    }
}
