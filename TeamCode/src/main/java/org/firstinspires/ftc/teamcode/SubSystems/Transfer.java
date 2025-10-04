package org.firstinspires.ftc.teamcode.SubSystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.Subsystem;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.BarnRobot;

import java.util.Set;

public class Transfer extends SubsystemBase {

    private CRServo leftFrontTrans;
    private CRServo leftBackTrans;
    private CRServo rightFrontTrans;
    private CRServo rightBackTrans;

    public Transfer(){
        leftFrontTrans = BarnRobot.getInstance().hardwareMap.get(CRServo.class, "leftFrontTrans");
        leftBackTrans = BarnRobot.getInstance().hardwareMap.get(CRServo.class, "leftBackTrans");
        rightFrontTrans = BarnRobot.getInstance().hardwareMap.get(CRServo.class, "rightFrontTrans");
        rightBackTrans = BarnRobot.getInstance().hardwareMap.get(CRServo.class, "rightBackTrans");
    }

    public void setPower(double power){
        leftFrontTrans.setPower(power);
        leftBackTrans.setPower(power);
        rightFrontTrans.setPower(power);
        rightBackTrans.setPower(power);
    }

    public Command transfer(double power){
        return new InstantCommand(() -> setPower(power), this);
    }
}
