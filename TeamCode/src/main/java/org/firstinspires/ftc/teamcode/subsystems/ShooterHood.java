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
    private final double MIN = 0.35;
    private final double MAX = 0.95;

    private double servoPos = 0.95;

    InterpLUT range1Lut;
    InterpLUT range2Lut;

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

        //Adding each val with a key
        range1Lut.add(0.45, 0.35);
        range1Lut.add(0.75, 0.4);
        range1Lut.add(0.86, 0.45);
        range1Lut.add(1.16, 0.5);
        range1Lut.add(1.28, 0.6);

        range2Lut.add(1.35,0.5);
        range2Lut.add(1.47,0.6);
        range2Lut.add(1.6,0.7);
        range2Lut.add(1.75, 0.8);
        range2Lut.add(1.86, 1);
        range2Lut.add(1.95, 1);
        range2Lut.add(2.04, 1);

        //generating final equation
        range1Lut.createLUT();
        range2Lut.createLUT();
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

    public Command setHoodPosNoLimit(double position){
        return new InstantCommand(() -> servo.setPosition(position), this);
    }

    public Command autoHoodAlignment(){
        return new RunCommand(() -> autoHoodAlignmentFunc(), this);
    }

//    public Command lower() {
//        return new InstantCommand(() -> {
//            double newPos = servo.getPosition() - 0.15;
//            if (newPos < MIN) newPos = MIN;
//            servo.setPosition(newPos);
//        }, this);
//    }
//
//    public Command raise() {
//        return new InstantCommand(() -> {
//            double newPos = servo.getPosition() + 0.15;
//            if (newPos > 1) newPos = 1;
//            servo.setPosition(newPos);
//        }, this);
//    }

    private void setPosition(double position){
        if (position < MIN) position = MIN;
        if (position > MAX) position = MAX;
        servo.setPosition(position);
    }

    public void lower() {
        servoPos -= 0.15;
        if (servoPos < MIN) servoPos = MIN;
    }

    public void raise() {
        servoPos += 0.15;
        if (servoPos > MAX) servoPos = MAX;
    }



    public Command setHoodPosition(double position){
        if (position > MAX) position = MAX;
        if (position < MIN) position = MIN;
        double finalPosition = position;
        return new InstantCommand(() ->
                servo.setPosition(finalPosition), this);

    }

    public Command defaultHoodCommand(){
        return new RunCommand(() -> {
            setPosition(servoPos);
        }, this);
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
        robot.telemetry.addData("Position2", servoPos);
    }

    private double capDistanceRange1(double distance){
        if (distance <= 0.45) distance = 0.46;
        else if (distance >= 1.28) distance = 1.279;
        return distance;
    }

    private double capDistanceRange2(double distance){
        if (distance <= 1.35) distance = 1.36;
        else if (distance >= 2.04) distance = 2.03;
        return distance;
    }


    public void setCustomPosition(double position){
        servoPos = position;
    }


}
