package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import org.firstinspires.ftc.teamcode.BarnRobot;

public class Shooter  extends SubsystemBase {
    private DcMotorEx shooter;

    public Shooter() {
        shooter = BarnRobot.getInstance().farminatorHardware.shooter;
    }

    public void setPower(double power) {
        shooter.setPower(power);
    }

    public Command shootCommand(double power) {
        return new InstantCommand(() -> setPower(power), this);
    }

}
