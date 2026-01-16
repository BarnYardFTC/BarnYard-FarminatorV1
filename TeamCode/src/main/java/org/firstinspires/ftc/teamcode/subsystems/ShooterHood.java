package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.subsystems.Shooter.SHOOTING_RANGE_1;
import static org.firstinspires.ftc.teamcode.subsystems.Shooter.SHOOTING_RANGE_2;
import static org.firstinspires.ftc.teamcode.subsystems.Shooter.SHOOTING_RANGE_3;

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

    InterpLUT range1Lut;
    InterpLUT range2Lut;
    InterpLUT range3Lut;
    InterpLUT range4Lut;

    public static double SERVO_POSITION = 1;


    public ShooterHood(){
        servo = BarnRobot.getInstance().robotHardware.shooterHood;
        servo.setDirection(Servo.Direction.REVERSE);    // Change if needed
        servo.scaleRange(MIN,MAX);
        servo.setPosition(MAX);
        initInterpLUT();
    }

    private void initInterpLUT(){
        range1Lut = new InterpLUT();
        range2Lut = new InterpLUT();
        range3Lut = new InterpLUT();
        range4Lut = new InterpLUT();

        //Adding each val with a key
        range1Lut.add(0.5, 0.15);
        range1Lut.add(0.6, 0.2);
        range1Lut.add(0.7, 0.3);
        range1Lut.add(0.81, 0.4);
        range1Lut.add(0.9, 0.45);
        range1Lut.add(1, 0.55);
        range1Lut.add(1.08, 0.7);

        range2Lut.add(1.12,0.3);
        range2Lut.add(1.24,0.35);
        range2Lut.add(1.29,0.45);
        range2Lut.add(1.4, 0.55);
        range2Lut.add(1.5, 0.75);
        range2Lut.add(1.6, 0.85);
        range2Lut.add(1.7, 0.9);
        range2Lut.add(1.79, 0.95);

        range3Lut.add(1.8,0.2);
        range3Lut.add(2,0.5);
        range3Lut.add(3,0.7);

        range4Lut.add(1,0.2);
        range4Lut.add(2,0.5);
        range4Lut.add(3,0.7);

        //generating final equation
        range1Lut.createLUT();
        range2Lut.createLUT();
        range3Lut.createLUT();
        range4Lut.createLUT();
    }

    public void distanceDependentAngleRange1(double distance) {
        distance = capDistanceRange1(distance);
        double position = range1Lut.get(distance);

        BarnRobot.getInstance().telemetry.addData("range dependent position close", position);
        servo.setPosition(position);
    }

    public void distanceDependentAngleRange2(double distance) {
        distance = capDistanceRange2(distance);
        double position = range2Lut.get(distance);

        servo.setPosition(position);
    }

    public void distanceDependentAngleRange3() {
        servo.setPosition(1);
    }

    public void distanceDependentAngleRange4() {
        servo.setPosition(1);
    }

    public void autoHoodAlignmentFunc(){
        double distance = BarnRobot.getInstance().drive.getDistanceFromGoal();

        autoHoodAlignmentConstantDistance(distance);

    }

    public void autoHoodAlignmentConstantDistance(double distance){
        if (distance < SHOOTING_RANGE_1) {
            distanceDependentAngleRange1(distance);
        }
        else if (distance > SHOOTING_RANGE_1 && distance < SHOOTING_RANGE_2){
            distanceDependentAngleRange2(distance);
        }
        else if (distance > SHOOTING_RANGE_2 && distance < SHOOTING_RANGE_3){
            distanceDependentAngleRange3();
        }
        else if (distance > SHOOTING_RANGE_3) {
            distanceDependentAngleRange4();
        }
    }

    public Command setHoodCloseToGoalPos(){
        return new RunCommand(() -> servo.setPosition(0.1), this);
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
        return new RunCommand(() ->
                goToPosition(), this);
    }

    public void goToPosition(){
        if (SERVO_POSITION > MAX) SERVO_POSITION = MAX;
        if (SERVO_POSITION < MIN) SERVO_POSITION = MIN;
        servo.setPosition(SERVO_POSITION);
    }

    public Command DefaultCommand(){
        return new RunCommand(() -> rest(), this);
    }

    public void rest(){
        BarnRobot.getInstance().telemetry.addLine("hood resting");
    }



    public void displayTelemetry(){
        BarnRobot robot = BarnRobot.getInstance();
        robot.telemetry.addData("Position", servo.getPosition());
    }

    private double capDistanceRange1(double distance){
        if (distance <= 0.5) distance = 0.51;
        else if (distance >= 1.08) distance = 1.079;
        return distance;
    }

    private double capDistanceRange2(double distance){
        if (distance <= 1.12) distance = 1.13;
        else if (distance >= 1.79) distance = 1.78;
        return distance;
    }



}
