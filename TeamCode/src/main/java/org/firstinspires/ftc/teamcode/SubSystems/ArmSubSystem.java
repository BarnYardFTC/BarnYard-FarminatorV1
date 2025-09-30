package org.firstinspires.ftc.teamcode.SubSystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;


public class ArmSubSystem extends SubsystemBase {
    private final Servo rightServo;
    private final Servo leftServo;
    public static double up = 0.5;
    public static double middle = 0.25;
    public static double down = 0;

    public ArmSubSystem() {
        rightServo = hardwareMap.get(Servo.class, "armRightServo");
        leftServo = hardwareMap.get(Servo.class, "armLeftServo");
    }

    private void setPosition(double pos) {
        rightServo.setPosition(pos);
        leftServo.setPosition(1-pos);
    }

    public Command setPositionCommand(double pos) {
        return new InstantCommand(() -> setPosition(pos),this);
    }
}
