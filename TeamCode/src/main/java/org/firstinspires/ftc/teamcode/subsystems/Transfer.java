package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;

@Config //Needed if we want to tune out values in dashboard
public class Transfer extends SubsystemBase {

    private CRServo leftFrontTrans;
    private CRServo leftBackTrans;
    private CRServo rightFrontTrans;
    private CRServo rightBackTrans;
    private static  double DEFAULT_POWER = 1;
    public static int TRANSFER_ONE_DURATION = 800;
    public static int TRANSFER_ALL_DURATION = TRANSFER_ONE_DURATION * 3;

    public Transfer(){
        leftFrontTrans = BarnRobot.getInstance().farminatorHardware.leftFrontTransfer;
        leftBackTrans = BarnRobot.getInstance().farminatorHardware.leftBackTransfer;
        rightFrontTrans = BarnRobot.getInstance().farminatorHardware.rightFrontTransfer;
        rightBackTrans = BarnRobot.getInstance().farminatorHardware.rightBackTransfer;

        // Needed so that positive power = transfer
        leftFrontTrans.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBackTrans.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void setPower(double power){
        leftFrontTrans.setPower(power);
        leftBackTrans.setPower(power);
        rightFrontTrans.setPower(power);
        rightBackTrans.setPower(power);
    }

    public void setBackPower(double power){
        leftBackTrans.setPower(power);
        rightBackTrans.setPower(power);
    }

    public void setFrontPower(double power){
        leftFrontTrans.setPower(power);
        rightFrontTrans.setPower(power);
    }

    public void deactivateTransfer(){
        setPower(0);
    }

    public void activateTransfer(){
        setPower(DEFAULT_POWER);
    }

    public void activateBackTransfer(){
        setBackPower(DEFAULT_POWER);
    }
    public void deactivateBackTransfer(){
        setBackPower(0);
    }

    public void activateFrontTransfer(){
        setFrontPower(DEFAULT_POWER);
    }
    public void deactivateFrontTransfer(){
        setFrontPower(0);
    }


    public Command activateTransferCommand(){
        return new InstantCommand(() -> activateTransfer(), this);
    }
    public Command deactivateTransferCommand(){
        return new InstantCommand(() -> deactivateTransfer(), this);
    }

    public Command activateBackTransferCommand(){
        return new InstantCommand(() -> activateBackTransfer(), this);
    }

    public Command activateFrontTransferCommand(){
        return new InstantCommand(() -> activateFrontTransfer(), this);
    }

    public Command deactivateBackTransferCommand(){
        return new InstantCommand(() -> deactivateBackTransfer(), this);
    }

    public Command deactivateFrontTransferCommand(){
        return new InstantCommand(() -> deactivateFrontTransfer(), this);
    }



}
