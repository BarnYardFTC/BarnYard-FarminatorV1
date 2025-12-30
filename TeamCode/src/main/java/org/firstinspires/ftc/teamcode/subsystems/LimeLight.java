package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.BarnRobot;

import java.util.ArrayList;
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

    /** Pipeline optimized for blue alliance localization. */
    public static final int BLUE_LOCALIZATION_PIPELINE = 1;

    /** Pipeline optimized for red alliance localization. */
    public static final int RED_LOCALIZATION_PIPELINE = 2;

    /** Pipeline used for detecting obelisk patterns. */
    public static final int OBELISK_PIPELINE = 3;

    /** Maximum allowed staleness for vision data. */
    public static final int STANDARD_STALENESS_TOLERANCE = 100;

    /** Polling frequency of the Limelight in Hz. */
    public static final int POLL_RATE_HZ = 100;

    /** Field coordinates for the target goal. */
    private static final double GOAL_X = -1.57;
    private static final double BLUE_GOAL_Y = -1.62;
    private static final double RED_GOAL_Y = 1.62;

    /** Fiducial patterns for the obelisk. */
    public enum Pattern {
        PPG,
        PGP,
        GPP
    }

    /** Limelight hardware object. */
    private final Limelight3A limelight;

    /** Latest result from the Limelight. */
    private LLResult llResult;
    public LimeLightColorRecognition llColor;



    /** List of fiducial results from the Limelight. */
    private List<LLResultTypes.FiducialResult> frs;

    /** Current detected obelisk pattern. */
    private Pattern obeliskPattern;

    /** Yaw offset to goal. */
    private double Dyaw;

    /** Distance to goal in meters. */
    private double goalRange;

    /** Currently active pipeline. */
    public int currentPipeline;

    private double shooterLeftPixel;
    private double middleLeftPixel;
    private double intakeLeftPixel;

    private busyType shooterPos = busyType.FREE;
    private busyType middlePos = busyType.FREE;
    private busyType intakePos = busyType.FREE;

    private artifactReadiness currentIntakeArtifactReadiness = artifactReadiness.NOTHING;
    private artifactReadiness currentMiddleArtifactReadiness = artifactReadiness.NOTHING;
    private artifactReadiness currentShooterArtifactReadiness = artifactReadiness.NOTHING;

    private List<Double> allLeftPixels;

    public enum artifactReadiness {READY, UNREADY, NOTHING}
    private enum busyType {BUSY, FREE}

    // temp telemetry
    public Telemetry telemetry;

    public enum artifactPositions {
        SHOOTPOSMIN(800), MIDDLEPOS(500), INTAKEPOS(100);
        private int numVal;

        artifactPositions(int numVal) {
            this.numVal = numVal;
        }

        public int getNumVal() {
            return numVal;
        }
    }



    /**
     * Constructs the LimeLight subsystem and initializes default settings.
     */
    public LimeLight(int pipeline, Telemetry telemetry) {
        llColor = new LimeLightColorRecognition();
        limelight = BarnRobot.getInstance().robotHardware.limelight;
        limelight.setPollRateHz(POLL_RATE_HZ);
        allLeftPixels = new ArrayList<>();
        switchPipeline(pipeline);
        Dyaw = 0;
        this.telemetry = telemetry;
        start();
    }

    public void resetData(){
        llResult = null;
        frs = null;
        obeliskPattern = null;
        Dyaw = 0;
        goalRange = 0;
        currentPipeline = BLUE_LOCALIZATION_PIPELINE;
    }

    /** Starts the Limelight processing loop. */
    public void start() {
        limelight.start();
    }

    /**
     * Switches the Limelight to a specific pipeline.
     *
     * @param pipeline pipeline index to activate
     */
    public void switchPipeline(int pipeline) {
        limelight.pipelineSwitch(pipeline);
        currentPipeline = pipeline;
        llResult = null;
        frs = null;
    }


    /**
     * Checks if the Limelight data is valid and up-to-date.
     *
     * @return true if data is valid
     */
    public boolean isDataValid() {
        if (currentPipeline == OBELISK_PIPELINE) {
            return llResult != null && llResult.isValid();
        } else {
            return llResult != null && frs != null && !frs.isEmpty() && llResult.isValid()
                    && llResult.getStaleness() < STANDARD_STALENESS_TOLERANCE;
        }
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

    /**
     * Maps a fiducial ID to an obelisk pattern.
     *
     * @param id fiducial ID
     * @return corresponding obelisk pattern
     */
    private Pattern getObeliskPattern(int id) {
        switch (id) {
            case 21: return Pattern.GPP;
            case 22: return Pattern.PGP;
            case 23: return Pattern.PPG;
            default: return null;
        }
    }

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
//     */
//    public void findPattern() {
//        if (isDataValid() && !isPatternFound()) {
//            LLResultTypes.FiducialResult obeliskFr = findLargestAreaFr(frs);
//            obeliskPattern = getObeliskPattern(obeliskFr.getFiducialId());
//        }
//    }

    /** Finds the left pixel of the artifact */
    public void ArtifactLeftPixel(){
        if (llColor.getIsFound()){

            List<LLResultTypes.ColorResult> colorData = llColor.colorData;

            if(colorData == null || colorData.isEmpty()) return;


            List<List<Double>> corners = colorData.get(0).getTargetCorners();

            if(corners.size() != 4) return;

            /** This logic for 3 artifacts in the robot(For the future) */
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

            List<Double> leftCorner = corners.get(3);
            shooterLeftPixel = leftCorner.get(0);
            this.telemetry.addData("Left Pixel", shooterLeftPixel);
        }
    }


    /** Updates the current artifact readiness */
    public void ArtifactReadinessFunc(){
        ArtifactLeftPixel();
        if (llColor.getIsFound()){
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
     * Checks if an obelisk pattern has been detected.
     *
     * @return true if a pattern is detected
     */
    public boolean isPatternFound() {
        return obeliskPattern != null;
    }

    /**
     * Checks if the Limelight currently sees a goal tag.
     *
     * @return true if a goal tag is detected
     */
    public boolean isGoalTagDetected() {
        return (currentPipeline == BLUE_LOCALIZATION_PIPELINE || currentPipeline == RED_LOCALIZATION_PIPELINE) && isDataValid();
    }

    /** Updates Limelight results; should be called periodically. */
    @Override
    public void periodic() {
        llResult = limelight.getLatestResult();


        if (llColor != null){
            llColor.updateResults(llResult);
        }

        if (llResult.isValid() && !llResult.getFiducialResults().isEmpty()) {
            frs = llResult.getFiducialResults();
        }

        ArtifactReadinessFunc();
    }

    /** Outputs all relevant telemetry for the Limelight subsystem. */
    public void displayTelemetry() {
        BarnRobot robot = BarnRobot.getInstance();
        robot.telemetry.addData("Detected", llResult != null && llResult.isValid());
        robot.telemetry.addData("Data Valid", isDataValid());
        robot.telemetry.addData("Dyaw", Dyaw);

        Pose3D pose = llResult != null ? llResult.getBotpose_MT2() : null;
        if (pose != null) {
            robot.telemetry.addData("Location", "(" + pose.getPosition().x + ", " + pose.getPosition().y + ")");
        }

        robot.telemetry.addData("Pattern", obeliskPattern);
        robot.telemetry.addData("Range", getGoalRange());
    }

    /** @return current artifact readiness */
    public Enum<artifactReadiness> getArtifactReadiness(){
        return currentShooterArtifactReadiness;
    }

    public void farminatorPos(){
        double posX = llResult.getBotpose().getPosition().x;
        double posY = llResult.getBotpose().getPosition().y;

    }

    /** @return current yaw offset to goal */
    public double getDyaw() {
        return Dyaw;
    }

    /** @return currently detected obelisk pattern */
    public Pattern getObeliskPattern() {
        return obeliskPattern;
    }

    /** @return calculated distance to goal in meters */
    public double getGoalRange() {
        return goalRange;
    }
}
