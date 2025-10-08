package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
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
        leftFrontTrans = BarnRobot.getInstance().farminatorHardware.leftFrontTransfer;
        leftBackTrans = BarnRobot.getInstance().farminatorHardware.leftBackTransfer;
        rightFrontTrans = BarnRobot.getInstance().farminatorHardware.rightFrontTransfer;
        rightBackTrans = BarnRobot.getInstance().farminatorHardware.rightBackTransfer;
    }

    public void setPower(double power){
        leftFrontTrans.setPower(power);
        leftBackTrans.setPower(power);
        rightFrontTrans.setPower(power);
        rightBackTrans.setPower(power);
    }

    public void stopTransfer(){
        setPower(0);
    }

    public void startTransfer(){
        setPower(1);
    }

    public Command startTransferCommand(){
        return new InstantCommand(() -> startTransfer(), this);
    }
    public Command stopTransferCommand(){
        return new InstantCommand(() -> stopTransfer(), this);
    }

    public Command transferCommand(){
        return new SequentialCommandGroup(
                startTransferCommand(),
                new WaitCommand(2000),
                stopTransferCommand());
    }
}
