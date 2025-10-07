package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.GlobalData;

import java.util.List;

public class LimeLight extends SubsystemBase {

    private Limelight3A limelight;
    private LLResult llResult;
    private double Dx, Dy, Dyaw, Dz;
    private final int PIPELINE = 7;
    private GlobalData.Alliance teamColor;


    public LimeLight(){
        init();
        Dx = 0;
        Dy = 0;
        Dyaw = 0;
        limelight.pipelineSwitch(PIPELINE);
    }

    public void init() {
        teamColor = BarnRobot.getInstance().teamColor;
        limelight = BarnRobot.getInstance().hardwareMap.get(Limelight3A.class, "limelight");
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



    public double getDyaw() {
        return Dyaw;
    }

    private void calculateD(LLResultTypes.FiducialResult fr){

        /*
         There is an issue that in order to get pitch
          you need to use getRoll and in order to get yaw
           you need to use getPitch. Don't change it
         */
        Dyaw = fr.getTargetPoseCameraSpace().getPosition().x;

    }
    @Override
    public void periodic() {
        llResult = limelight.getLatestResult();
        if (llResult.isValid()){
            List<LLResultTypes.FiducialResult> fiducialResults = llResult.getFiducialResults();
            for (LLResultTypes.FiducialResult fr : fiducialResults) {
                if((teamColor == GlobalData.Alliance.BLUE && fr.getFiducialId() == 20) ||
                        (teamColor == GlobalData.Alliance.RED && fr.getFiducialId() == 24)){
                    calculateD(fr);
                }
            }
        }
    }

    public void displayTelemetry(){
        BarnRobot.getInstance().telemetry.addData("Dx", Dx);
        BarnRobot.getInstance().telemetry.addData("Dz", Dz);
        BarnRobot.getInstance().telemetry.addData("Dyaw", Dyaw);
    }
}


