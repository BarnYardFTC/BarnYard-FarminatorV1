package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BarnRobot;

@Config
public class Shooter  extends SubsystemBase {
    private DcMotorEx shooterRight;
    private DcMotorEx shooterLeft;
    private static final double MOTOR_RPS = 27;
    private static final double WHEEL_RADIUS = 0.048;

    public static double SHOOTER_DEFAULT_VELOCITY = 1000; // TODO: Find value based on pidf controller

    public static double TEMP_POWER_FUNCTION_CONSTANT = 9; //TODO Remove when we have a pidf controller


    public Shooter() {
        shooterRight = BarnRobot.getInstance().robotHardware.shooterRight;
        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        shooterLeft = BarnRobot.getInstance().robotHardware.shooterLeft;
        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    private void setPower(double power) {
        shooterRight.setPower(power);
        shooterLeft.setPower(power);
    }

    private void operateShooter(){
        setPower(TEMP_POWER_FUNCTION_CONSTANT);
    }



    //TODO: REMOVE FUNCTION when we have a pidf controller
    public double rangeDependentVelocity(double range) {
        double SHOOTING_CONSTANT = 4.4;
        double g = 9.87;
        double SHOOTING_HEIGHT = 0.32;
        double SHOOTING_ANGLE = Math.toRadians(53);
        double GOAL_HEIGHT = 0.98;
        double artifactSpeed = g / (Math.cos(SHOOTING_ANGLE) * Math.sqrt(2)) * range / Math.sqrt(SHOOTING_HEIGHT + range * Math.tan(SHOOTING_ANGLE) - GOAL_HEIGHT);
        return artifactSpeed * SHOOTING_CONSTANT / WHEEL_RADIUS;
    }


    public RunCommand runShooter(){
        return new RunCommand(() -> operateShooter());
    }

    public RunCommand turnOff(){
        return new RunCommand(() -> setPower(0), this);
    }

    public Command shootAtRangeCommand() {
        return new RunCommand(() -> setPower(rangeDependentVelocity(BarnRobot.getInstance().limelight.getGoalRange())));
//        double range = BarnRobot.getInstance().limelight.getGoalRange();
//        return new InstantCommand(() -> setSpeed(rangeDependentVelocity(range)), this);
    }

    public void displayTelemetry(){
        Telemetry telemetry = BarnRobot.getInstance().telemetry;
        telemetry.addData("shooter actual speed", shooterRight.getVelocity());
        telemetry.addData("left shooter power", shooterLeft.getPower());
        telemetry.addData("right shooter power", shooterRight.getPower());
    }

    public double getVelocity() {
        return (shooterRight.getVelocity() + shooterLeft.getVelocity()) / 2;
    }

    public void testMotors(){
        shooterRight.setPower(1);
        shooterLeft.setPower(1);
    }

    //TODO: TEMP
    public Command runPowerBasedOnVoltageCompFunction(){
        return new RunCommand(() -> setPower(TEMP_POWER_FUNCTION_CONSTANT/
                Math.max(BarnRobot.getInstance().robotHardware.voltageSensor.getVoltage(), 1e-6)), this);
    }
}