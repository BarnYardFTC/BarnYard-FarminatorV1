package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;

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
        rightBackTrans.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFrontTrans.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void setPower(double power){
        leftFrontTrans.setPower(power);
        leftBackTrans.setPower(power);
        rightFrontTrans.setPower(power);
        rightBackTrans.setPower(power);
    }

    public Command setPowerCommand(double power){
        return new InstantCommand(() -> setPower(power), this);
    }

    public Command transferCommand(){
        return new SequentialCommandGroup(
                setPowerCommand(1),
                new WaitCommand(2000),
                setPowerCommand(0));
    }
}
