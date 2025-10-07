package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.BarnRobot;

public class Intake extends SubsystemBase {

    private DcMotorEx intake;

    public Intake(){
        this.intake = BarnRobot.getInstance().farminatorHardware.intake;
    }

    public void setPower(double power){
        intake.setPower(power);
    }

    public Command intake(double power){
        return new InstantCommand(() -> setPower(power), this);
    }

}
