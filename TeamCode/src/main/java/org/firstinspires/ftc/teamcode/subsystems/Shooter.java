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
    private DcMotorEx shooter;
    private static final double MOTOR_RPS = 27;
    private static final double WHEEL_RADIUS = 0.048;
    public static double DEFAULT_SPEED = 4 * Math.PI * WHEEL_RADIUS * MOTOR_RPS / WHEEL_RADIUS;
    private final double g = 9.87;
    private final double SHOOTING_HEIGHT = 0.32;
    private final double SHOOTING_ANGLE = Math.toRadians(53);
    private final double GOAL_HEIGHT = 0.98;
    public final double RPM_TOLERANCE = 10;
    public static double SHOOTING_CONSTANT = 4;  //was 4.4


    public Shooter() {
        shooter = BarnRobot.getInstance().farminatorHardware.shooter;
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooter.setDirection(DcMotorSimple.Direction.FORWARD);
        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }


    private void setSpeed(double speed) {
        shooter.setVelocity(speed);
    }
    public boolean isMotorReady() {
        return (shooter.getVelocity() > rangeDependentVelocity(BarnRobot.getInstance().limelight.getGoalRange()) - RPM_TOLERANCE &&
                shooter.getVelocity() < rangeDependentVelocity(BarnRobot.getInstance().limelight.getGoalRange()) + RPM_TOLERANCE);
    }

    public double rangeDependentVelocity(double range) {
        double artifactSpeed = g / (Math.cos(SHOOTING_ANGLE) * Math.sqrt(2)) * range / Math.sqrt(SHOOTING_HEIGHT + range * Math.tan(SHOOTING_ANGLE) - GOAL_HEIGHT);
        return artifactSpeed * SHOOTING_CONSTANT / WHEEL_RADIUS;
    }

    public Command customShooterCommand(double speed) {
        return new InstantCommand(() -> setSpeed(speed), this);
    }

    public Command calcAndShootCommand() {
        return new InstantCommand(() -> setSpeed(rangeDependentVelocity(BarnRobot.getInstance().limelight.getGoalRange())));
    }

    public Command activateShooterCommand() {
        return new InstantCommand(() -> setSpeed(DEFAULT_SPEED), this);
    }

    public Command deactivateShooterCommand() {
        return new InstantCommand(() -> setSpeed(0), this);
    }

    public Command shootAtRangeCommand() {
        return new RunCommand(() -> setSpeed(rangeDependentVelocity(BarnRobot.getInstance().limelight.getGoalRange())));
//        double range = BarnRobot.getInstance().limelight.getGoalRange();
//        return new InstantCommand(() -> setSpeed(rangeDependentVelocity(range)), this);
    }

    public void displayTelemetry(){
        Telemetry telemetry = BarnRobot.getInstance().telemetry;

        telemetry.addData("Shooter speed formula", rangeDependentVelocity(BarnRobot.getInstance().limelight.getGoalRange()));
        telemetry.addData("shooter actual speed", shooter.getVelocity());
        telemetry.addData("is motor ready", isMotorReady());
    }
}