package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.subsystems.DriveTrain.BLUE_GOAL_Y;
import static org.firstinspires.ftc.teamcode.subsystems.DriveTrain.GOAL_X_1;
import static org.firstinspires.ftc.teamcode.subsystems.DriveTrain.RED_GOAL_Y;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.ShooterPIDFController;

@Config
public class Shooter  extends SubsystemBase {

    public static double p = 1, f = 0.0007;
    private ShooterPIDFController pidfController;
    private DcMotorEx shooterRight;
    private DcMotorEx shooterLeft;
    private ColorSensor shooterSensor;
    private ColorSensor midSensor;
    private ColorSensor intakeSensor;


    public static double SHOOTER_VELOCITY_RANGE_4 = 1500; // only for far zone
    public static double SHOOTER_VELOCITY_RANGE_3 = 1250;
    public static double SHOOTER_VELOCITY_RANGE_2 = 1200;
    public static double SHOOTER_VELOCITY_RANGE_1 = 1050;


    public static double SHOOTING_RANGE_1 = 1.3;
    public static double SHOOTING_RANGE_2 = 2.05;
    public static double SHOOTING_RANGE_3 = 2.8;

    private double targetVelocity;


    public Shooter() {
        shooterRight = BarnRobot.getInstance().robotHardware.shooterRight;
        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.shooterSensor = BarnRobot.getInstance().shooterColorSensor;
        this.midSensor = BarnRobot.getInstance().midColorSensor;
        this.intakeSensor = BarnRobot.getInstance().intakeColoseSensor;


        shooterLeft = BarnRobot.getInstance().robotHardware.shooterLeft;
        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        pidfController = new ShooterPIDFController(p, 0, 0, f);
    }

    private void setPower(double power) {
        shooterRight.setPower(power);
        shooterLeft.setPower(power);
    }

    private void operateShooter(double velocity){
        pidfController.setPIDF(p, 0, 0, f);
        double power = pidfController.calculate(velocity, getVelocity());
        setPower(power);
    }

    public void operateShooterDistanceBased(double distance) {
        pidfController.setPIDF(p, 0, 0, f);
        double power;
        if (distance < SHOOTING_RANGE_1) {
            targetVelocity = SHOOTER_VELOCITY_RANGE_1;
        }
        else if (distance > SHOOTING_RANGE_1 && distance < SHOOTING_RANGE_2){
            targetVelocity = SHOOTER_VELOCITY_RANGE_2;
        }
        else if (distance > SHOOTING_RANGE_2 && distance < SHOOTING_RANGE_3){
            targetVelocity = SHOOTER_VELOCITY_RANGE_3;
        }
        else if (distance > SHOOTING_RANGE_3) {
            targetVelocity = SHOOTER_VELOCITY_RANGE_4;
        }
        else {
            targetVelocity = getVelocity();
        }
        power = pidfController.calculate(targetVelocity, getVelocity());
        setPower(power);
    }

    private void operateShooterReverse(){
        setPower(-1);
    }


    public RunCommand runShooterFar(){
        return new RunCommand(() -> operateShooter(SHOOTER_VELOCITY_RANGE_4), this);
    }

    public RunCommand runShooterClose(){
        return new RunCommand(() -> operateShooter(SHOOTER_VELOCITY_RANGE_1), this);
    }

    public RunCommand runShooterReversed(){
        return new RunCommand(() -> operateShooterReverse(), this);
    }

    public RunCommand turnOff(){
        return new RunCommand(() -> setPower(0), this);
    }

    public InstantCommand turnOffInstant(){
        return new InstantCommand(() -> setPower(0), this);
    }

    public RunCommand runShooter(double velocity){
        return new RunCommand(() -> operateShooter(velocity), this);
    }

    public RunCommand runShooterBasedOnDistance(){
        return new RunCommand(() -> operateShooterDistanceBased(
                BarnRobot.getInstance().drive.getDistanceFromGoal()
        ), this);
    }

    public RunCommand runShooterBasedOnConstantDistance(double distance){
        return new RunCommand(() -> operateShooterDistanceBased(
                distance
        ), this);
    }

    public Command smartIntakeCommand(){    //Disable intake when there is enough artifacts in the robot
        return new ConditionalCommand(
                new InstantCommand(() -> setPower(0), this), // on true
                new RunCommand(() -> operateShooterDistanceBased(
                        BarnRobot.getInstance().drive.getDistanceFromGoal()
                )),          // on false
                () -> this.shooterSensor.isShootPosBusy() && this.midSensor.isMidPosBusy() && this.intakeSensor.isIntakePosBusy()
        );
    }




    public void displayTelemetry(){
        Telemetry telemetry = BarnRobot.getInstance().telemetry;
        telemetry.addData("shooter velocity", shooterRight.getVelocity());
        telemetry.addData("left shooter power", shooterLeft.getPower());
    }

    public double getVelocity() {
        return (shooterRight.getVelocity());
    }
    public double getPower(){
        return shooterRight.getPower();
    }



    public void testMotors(){
        shooterRight.setPower(1);
        shooterLeft.setPower(1);
    }

    public boolean isReady() {
        if (targetVelocity == 0) return false;
        return (getVelocity() > targetVelocity - 40 && getVelocity() < targetVelocity + 40);
    }

    public boolean isShotDetected(double tgtRpm) {
        return getVelocity() < tgtRpm - 60;
    }

}