package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.BarnRobot;

@Config
public class Arms extends SubsystemBase {
    private Servo rightServo;
    private Servo leftServo;

    private final double MIN = 0;
    private final double MAX = 1;

    public Arms(){
        rightServo = BarnRobot.getInstance().robotHardware.rightArm;
        leftServo = BarnRobot.getInstance().robotHardware.leftArm;
        rightServo.setDirection(Servo.Direction.FORWARD);
        leftServo.setDirection(Servo.Direction.REVERSE);
        rightServo.scaleRange(MIN,MAX);
        leftServo.scaleRange(MIN,MAX);
        stop();
    }

    private void retract() {
        rightServo.setPosition(MIN);
        leftServo.setPosition(MAX);
    }

    public Command retractCommand(){
        return new InstantCommand(() -> retract(), this);
    }

    private void stop() {
        rightServo.setPosition(MIN);
        leftServo.setPosition(MIN);
    }

    public Command stopCommand(){
        return new InstantCommand(() -> stop(), this);
    }
}
