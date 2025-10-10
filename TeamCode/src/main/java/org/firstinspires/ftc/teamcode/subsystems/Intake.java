package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.BarnRobot;

public class Intake extends SubsystemBase {

    private DcMotorEx intake;
    private final double DEFAULT_POWER = 1;

    public Intake(){
        this.intake = BarnRobot.getInstance().farminatorHardware.intake;
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void setPower(double power){
        intake.setPower(power);
    }

    public Command customIntakeCommand(double power){
        return new RunCommand(() -> setPower(power), this);
    }

    public Command activateIntake(){
        return new RunCommand(()-> setPower(DEFAULT_POWER), this);
    }

    public Command deactivateIntake(){
        return new InstantCommand(()-> setPower(0), this);
    }



}
