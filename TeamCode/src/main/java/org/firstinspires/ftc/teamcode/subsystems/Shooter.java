package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.ShooterPIDFController;

@Config
public class Shooter  extends SubsystemBase {

    public static double p = 1, f = 0.0007;
    private ShooterPIDFController pidfController;
    private DcMotorEx shooterRight;
    private DcMotorEx shooterLeft;

    public static double SHOOTER_VELOCITY_RANGE_4 = 1550; // only for far zone
    public static double SHOOTER_VELOCITY_RANGE_3 = 1250;
    public static double SHOOTER_VELOCITY_RANGE_2 = 1200;
    public static double SHOOTER_VELOCITY_RANGE_1 = 1050;


    public static double SHOOTING_RANGE_1 = 1.1;
    public static double SHOOTING_RANGE_2 = 2.05;
    public static double SHOOTING_RANGE_3 = 2.8;



    public Shooter() {
        shooterRight = BarnRobot.getInstance().robotHardware.shooterRight;
        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

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
            power = pidfController.calculate(SHOOTER_VELOCITY_RANGE_1, getVelocity());
        }
        else if (distance > SHOOTING_RANGE_1 && distance < SHOOTING_RANGE_2){
            power = pidfController.calculate(SHOOTER_VELOCITY_RANGE_2, getVelocity());
        }
        else if (distance > SHOOTING_RANGE_2 && distance < SHOOTING_RANGE_3){
            power = pidfController.calculate(SHOOTER_VELOCITY_RANGE_3, getVelocity());
        }
        else if (distance > SHOOTING_RANGE_3) {
            power = pidfController.calculate(SHOOTER_VELOCITY_RANGE_4, getVelocity());
        }
        else {
            power = 0;
        }
        setPower(power
        );
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


    public RunCommand runShooter(double velocity){
        return new RunCommand(() -> operateShooter(velocity), this);
    }

    public RunCommand runShooterBasedOnDistance(){
        return new RunCommand(() -> operateShooterDistanceBased(
                BarnRobot.getInstance().drive.getDistanceFromGoal()
        ), this);
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
        return (getVelocity() > SHOOTER_VELOCITY_RANGE_1 - 40 && getVelocity() < SHOOTER_VELOCITY_RANGE_1 + 40) ||
                (getVelocity() > SHOOTER_VELOCITY_RANGE_2 - 40 && getVelocity() < SHOOTER_VELOCITY_RANGE_2 + 40) ||
                (getVelocity() > SHOOTER_VELOCITY_RANGE_4 - 40 && getVelocity() < SHOOTER_VELOCITY_RANGE_4 + 40);
    }

    public boolean isShotDetected(double tgtRpm) {
        return getVelocity() < tgtRpm - 60;
    }

}