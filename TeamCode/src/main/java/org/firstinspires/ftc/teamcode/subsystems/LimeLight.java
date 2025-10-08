package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.OpModeData.AllianceColor;

import java.util.List;

public class LimeLight extends SubsystemBase {

    private Limelight3A limelight;
    private LLResult llResult;
    private double Dyaw;
    private final int STANDART_PIPELINE = 0;
    private final int BLUE_PIPELINE = 1;
    private final int RED_PIPELINE = 2;


    public LimeLight(){
        init();
        Dyaw = 0;
        limelight.pipelineSwitch();
    }

    public void init() {
        limelight = BarnRobot.getInstance().farminatorHardware.limelight;
        limelight.pipelineSwitch();
    }

    // This function is required to execute right when the OpMode starts (after init)
    public void start(){
        limelight.start();
    }

    public boolean isValid(){
        return llResult.isValid();
    }

    public double getDyaw() {
        return Dyaw;
    }

    private void calculateD(LLResultTypes.FiducialResult fr){

        /*
         There is an issue that in order to get pitch
          you need to use getRoll and in order to get yaw
           you need to use getPitch. Don't change it
         */
        Dyaw = fr.getTargetXDegrees();
    }
    @Override
    public void periodic() {
        AllianceColor allianceColor = BarnRobot.getInstance().opmodeData.allianceColor;
        llResult = limelight.getLatestResult();
        if (llResult.isValid()){
            List<LLResultTypes.FiducialResult> fiducialResults = llResult.getFiducialResults();
            for (LLResultTypes.FiducialResult fr : fiducialResults) {
                calculateD(fr);
//                if((allianceColor ==   AllianceColor.BLUE && fr.getFiducialId() == 20) ||
//                        (allianceColor == AllianceColor.RED && fr.getFiducialId() == 24)){
//                    calculateD(fr);
//                }
            }
        }
    }

    public void displayTelemetry() {
        boolean valid = llResult != null && llResult.isValid();
        BarnRobot.getInstance().telemetry.addData("Detected", valid);
        if (valid) {
            BarnRobot.getInstance().telemetry.addData("Dyaw", Dyaw);
        }
    }
}


