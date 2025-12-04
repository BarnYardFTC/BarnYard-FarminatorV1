package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.subsystems.Shooter.CLOSE_SHOOTING_RANGE;
import static org.firstinspires.ftc.teamcode.subsystems.Shooter.FAR_SHOOTING_RANGE;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.util.InterpLUT;

import org.firstinspires.ftc.teamcode.BarnRobot;

@Config
public class ShooterHood extends SubsystemBase {
    private Servo servo;
    private final double MIN = 0.3;
    private final double MAX = 1;

    InterpLUT close_lut;
    InterpLUT mid_lut;
    InterpLUT far_lut;

    public static double SERVO_POSITION = 0;


    public ShooterHood(){
        servo = BarnRobot.getInstance().robotHardware.shooterHood;
        servo.setDirection(Servo.Direction.REVERSE);    // Change if needed
        servo.scaleRange(MIN,MAX);
        servo.setPosition(MAX);
        initInterpLUT();
    }

    private void initInterpLUT(){
        close_lut = new InterpLUT();
        mid_lut = new InterpLUT();
        far_lut = new InterpLUT();

        //Adding each val with a key
        close_lut.add(1.1, 0.2);
        close_lut.add(2.7, .5);
        close_lut.add(3.6, 0.75);

        mid_lut.add(1,0.2);
        mid_lut.add(2,0.5);
        mid_lut.add(3,0.7);

        far_lut.add(1,0.2);
        far_lut.add(2,0.5);
        far_lut.add(3,0.7);

        //generating final equation
        close_lut.createLUT();
        mid_lut.createLUT();
        far_lut.createLUT();
    }

    public void distanceDependentAngleClose(double distance) {
        double position = close_lut.get(distance);

        BarnRobot.getInstance().telemetry.addData("range dependent position close", position);
        servo.setPosition(position);
    }

    public void distanceDependentAngleMid(double distance) {
        double position = mid_lut.get(distance);

        BarnRobot.getInstance().telemetry.addData("range dependent position mid", position);
        servo.setPosition(position);
    }

    public void distanceDependentAngleFar(double distance) {
        double position = far_lut.get(distance);

        BarnRobot.getInstance().telemetry.addData("range dependent position far", position);
        servo.setPosition(position);
    }

    public void autoHoodAlignmentFunc(){
        double distance = BarnRobot.getInstance().drive.getDistanceFromGoal();
        BarnRobot.getInstance().telemetry.addData("distance test", distance);

        if (distance < CLOSE_SHOOTING_RANGE) {
            distanceDependentAngleClose(distance);
        }
        else if (distance > CLOSE_SHOOTING_RANGE && distance < FAR_SHOOTING_RANGE){
            distanceDependentAngleMid(distance);
        }
        else {
            distanceDependentAngleClose(distance);
        }
    }

    public Command autoHoodAlignment(){
        return new RunCommand(() -> autoHoodAlignmentFunc(), this);
    }

    public Command lower() {
        return new InstantCommand(() -> {
            double newPos = servo.getPosition() - 0.1;
            if (newPos < MIN) newPos = MIN;
            servo.setPosition(newPos);
        }, this);
    }

    public Command raise() {
        return new InstantCommand(() -> {
            double newPos = servo.getPosition() + 0.1;
            if (newPos > 1) newPos = 1;
            servo.setPosition(newPos);
        }, this);
    }

    public Command setHoodPosition(double position){
        if (position > MAX) position = MAX;
        if (position < MIN) position = MIN;
        double finalPosition = position;
        return new InstantCommand(() ->
            servo.setPosition(finalPosition), this);
    }

    public Command returnToBase(){
        return new InstantCommand(() -> {
            servo.setPosition(MIN);
            },this
        );
    }

    public Command goToPositionCommand(){
        return setHoodPosition(SERVO_POSITION);
    }

    public void displayTelemetry(){
        BarnRobot robot = BarnRobot.getInstance();
        robot.telemetry.addData("Position", servo.getPosition());
    }
}
