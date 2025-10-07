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
    private double Dx, Dy, Dyaw, Dz;
    private final int PIPELINE = 7;


    public LimeLight(){
        init();
        Dx = 0;
        Dy = 0;
        Dyaw = 0;
        limelight.pipelineSwitch(PIPELINE);
    }

    public void init() {
        limelight = BarnRobot.getInstance().farminatorHardware.limelight;
        limelight.pipelineSwitch(PIPELINE);
    }

    // This function is required to execute right when the OpMode starts (after init)
    public void start(){
        limelight.start();
    }

    public double getDx() {
        return Dx;
    }

    public double getDy(){
        return Dy;
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
        Dy = fr.getTargetPoseCameraSpace().getPosition().y;
        Dz = fr.getTargetPoseCameraSpace().getPosition().z;

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
            BarnRobot.getInstance().telemetry.addData("Dx", Dx);
            BarnRobot.getInstance().telemetry.addData("Dy", Dy);
            BarnRobot.getInstance().telemetry.addData("Dz", Dz);
            BarnRobot.getInstance().telemetry.addData("Dyaw", Dyaw);
        }
    }
}


