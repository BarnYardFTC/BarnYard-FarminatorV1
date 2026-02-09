package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.BarnRobot;

@Config // Allows tuning constants via dashboard
public class Transfer extends SubsystemBase {

    private DcMotorEx transferMotor;
    private ColorSensor colorSensor;

    // ------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------
    public Transfer() {
        BarnRobot robot = BarnRobot.getInstance();
        transferMotor = robot.robotHardware.transfer;
        this.colorSensor = new ColorSensor();
        transferMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public Command activateTransfer(){
        return new InstantCommand(() -> transferMotor.setPower(1));
    }

    public Command deactivateTransfer(){
        return new InstantCommand(() -> transferMotor.setPower(0));
    }

    public Command customTransferCommand(double power){
        return new InstantCommand(() -> transferMotor.setPower(power));
    }

    public Command smartTransferCommand(){
        return new ConditionalCommand(
                new InstantCommand(() -> transferMotor.setPower(0)),
                new InstantCommand(() -> transferMotor.setPower(1)),
                this.colorSensor::isShootAndMidIn
        );
    }

}
