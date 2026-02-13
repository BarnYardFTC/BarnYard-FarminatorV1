package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.util.InterpLUT;

import org.firstinspires.ftc.teamcode.BarnRobot;

@Config
public class ShooterHood extends SubsystemBase {
    private Servo servo;
    private final double MIN = 0.35;
    private final double MAX = 0.95;

    private double servoPos = 0.95;

    InterpLUT rangeLut;

    public static double SERVO_POSITION = 1;


    public ShooterHood(){
        servo = BarnRobot.getInstance().robotHardware.shooterHood;
        servo.setDirection(Servo.Direction.REVERSE);    // Change if needed
        servo.setPosition(1);
        initInterpLUT();
    }

    private void initInterpLUT(){
        rangeLut = new InterpLUT();

        //Adding each val with a key
        rangeLut.add(0.05, 0.45);
        rangeLut.add(0.22, 0.55);
        rangeLut.add(0.51,0.65);
        rangeLut.add(0.83,0.7);
        rangeLut.add(1.05,0.95);
        rangeLut.add(1.25,1);
        rangeLut.add(1.65,0.85);
        rangeLut.add(1.91,0.95);
        rangeLut.add(2,1);
        rangeLut.add(2.12,1);
        //generating final equation

        rangeLut.createLUT();
    }

    public void distanceDependentAngleRange(double distance) {
        distance = capDistanceRange1(distance);
        double position = rangeLut.get(distance);

        BarnRobot.getInstance().telemetry.addData("range dependent position close", position);
//        setPosition(position);
        servoPos = position;

    }


    public void autoHoodAlignmentConstantDistance(){

        double distance = BarnRobot.getInstance().limelight.getGoalDistance();

        if (distance == -1) {
            setPosition(MIN);
        }
        else {
            distanceDependentAngleRange(distance);
        }
    }

    public void shooterHoodBasedOnDistance(double distance){
        if (distance == -1) {
            setPosition(MIN);
        }
        else {
            distanceDependentAngleRange(distance);
        }
    }

    public void shooterHoodOnDistance(){
        shooterHoodBasedOnDistance(
                BarnRobot.getInstance().limelight.getGoalDistance()
        );
        setPosition(servoPos);
    }

    public Command setHoodCloseToGoalPos(){
        return new RunCommand(() -> setPosition(MIN), this);
    }

    public Command setHoodPosNoLimit(double position){
        return new InstantCommand(() -> servo.setPosition(position), this);
    }

    public RunCommand autoHoodAlignment(){
        return new RunCommand(() -> shooterHoodOnDistance(), this);
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

    public Command defaultAndAutoHoodCommand(){
        return new SequentialCommandGroup(
                defaultHoodCommand(),
                autoHoodAlignment()
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
        if (distance <= 0.05) distance = 0.06;
        else if (distance >= 2.12) distance = 2.11;
        return distance;
    }


    public static double DASHBOARD_POS = 0.5;
    public void setPositionDashboard(){
        servo.setPosition(DASHBOARD_POS);
    }

    public RunCommand setCustomDashboardPos(){
        return new RunCommand(() -> setPositionDashboard(), this);
    }


    public void setCustomPosition(double position){
        servoPos = position;
    }


}
