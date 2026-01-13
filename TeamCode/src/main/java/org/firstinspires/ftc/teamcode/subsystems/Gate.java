package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.BarnRobot;

@Config
public class Gate extends SubsystemBase {
    public static double tmpPos = 0;
    private Servo rightServo;
    private Servo leftServo;

    private final double MIN = 0;
    private final double MAX = 1;

    public Gate(){
        rightServo = BarnRobot.getInstance().robotHardware.rightGate;
        leftServo = BarnRobot.getInstance().robotHardware.leftGate;
        rightServo.setDirection(Servo.Direction.FORWARD);
        leftServo.setDirection(Servo.Direction.REVERSE);
        rightServo.scaleRange(MIN,MAX);
        leftServo.scaleRange(MIN,MAX);
        close();
    }

    private void setPosition(double pos){
        rightServo.setPosition(pos);
        leftServo.setPosition(pos);
    }

    private void open() {
        setPosition(MAX);
    }

    public Command openCommand(){
        return new InstantCommand(() -> open(), this);
    }

    private void close() {
        rightServo.setPosition(MIN);
        leftServo.setPosition(MIN);
    }

    public Command closeCommand(){
        return new InstantCommand(() -> close(), this);
    }

    public RunCommand goToTmpPos(){
        return new RunCommand(() -> setPosition(tmpPos), this);
    }

    public void displayTelemetry(){
        BarnRobot.getInstance().telemetry.addData("leftGatePos", leftServo.getPosition());
        BarnRobot.getInstance().telemetry.addData("rightGatePos", rightServo.getPosition());
    }

}
