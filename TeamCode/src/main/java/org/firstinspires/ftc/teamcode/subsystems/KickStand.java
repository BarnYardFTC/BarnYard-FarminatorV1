package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.BarnRobot;

public class KickStand extends SubsystemBase {

    public Servo leftKickStand;
    public Servo rightKickStand;

    public final double MIN = 0.15;
    public final double MAX = 0.7;
    public double tmpPos = 0.7;

    public KickStand(){
        leftKickStand = BarnRobot.getInstance().robotHardware.leftKickStand;
        rightKickStand = BarnRobot.getInstance().robotHardware.rightKickStand;
        rightKickStand.setDirection(Servo.Direction.REVERSE);
        leftKickStand.setDirection(Servo.Direction.FORWARD);

        lower();
    }

    private void setPosition(double pos){
        rightKickStand.setPosition(pos);
        leftKickStand.setPosition(pos);

    }

    private void setLeftPosition(double pos){
        leftKickStand.setPosition(pos);
    }

    private void setRightPosition(double pos){
        rightKickStand.setPosition(pos);
    }

    private void lower(){
        setPosition(MIN);
    }

    public Command deactivateCommand(){
        return new InstantCommand(() -> lower());
    }

    private void raise(){
        setPosition(MAX);
    }

    public Command activateCommand(){
        return new InstantCommand(() -> raise());
    }


    public void displayTelemetry(){
        BarnRobot.getInstance().telemetry.addData("stand position: ", rightKickStand.getPosition());
    }
}
