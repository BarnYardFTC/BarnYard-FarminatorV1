package org.firstinspires.ftc.teamcode.SubSystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class ClawSubSystem extends SubsystemBase {
    private final Servo clawServo;
    public static double open = 0.5;
    public static double close = 0;

    public ClawSubSystem() {
        clawServo = hardwareMap.get(Servo.class, "claw");
    }

    private void setPosition(double pos) {
        clawServo.setPosition(pos);
    }

    public Command grab() {
        return new InstantCommand(() -> setPosition(close),this);
    }

    public Command release() {
        return new InstantCommand(() -> setPosition(open),this);
    }
}
