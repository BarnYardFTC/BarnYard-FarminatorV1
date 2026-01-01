package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.teamcode.BarnRobot;

import java.util.List;

//Sasha: edited in order to continue on LED class, and according to my view on this class output
@Config
public class LimeLightColorRecognition {
    private Limelight3A limelight;

    /** Latest result from the Limelight. */
    private LLResult llResult;

    /** List of fiducial results from the Limelight. */
    private List<LLResultTypes.FiducialResult> frs;

    /** List of color results from the Limelight */
    public List<LLResultTypes.ColorResult> colorData;

    private double ta;
    private double tx;
    private double ty;
    private boolean isFound = false;

    public enum Artifact {
        NONE,
        GREEN,
        PURPLE
    }

//    private Artifact frontTransferArtifact = Artifact.NONE;
    private static Artifact backTransferArtifact = Artifact.NONE;


    public LimeLightColorRecognition() {
        limelight = BarnRobot.getInstance().robotHardware.limelight;
        limelight.setPollRateHz(60);
        limelight.start(); // This tells Limelight to start looking!
        limelight.pipelineSwitch(0); // Switch to pipeline number 0
    }

    public void llResult(){
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            double tx = result.getTx(); // How far left or right the target is (degrees)
            double ty = result.getTy(); // How far up or down the target is (degrees)
            double ta = result.getTa(); // How big the target looks (0%-100% of the image)
        }
    }

    public void updateResults(LLResult result){
        if(result == null  || result.isValid() || result.getColorResults() == null){
            isFound = false;
            colorData = null;
            return;
        }
        isFound = true;
        colorData = result.getColorResults();
    }

    public double getTa(){
        return ta;
    }
    public double getTx(){
        return tx;
    }
    public double getTy(){
        return ty;
    }

    public boolean getIsFound(){
        return isFound;
    }

//    public Artifact getFrontTransferArtifact() {
//        return frontTransferArtifact;
//    }

    public Artifact getBackTransferArtifact() {
        return backTransferArtifact;
    }

}
