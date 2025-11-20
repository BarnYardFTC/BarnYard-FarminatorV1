package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BarnRobot;

/**
 * Shooter subsystem controls the shooter motor for launching game elements.
 *
 * Provides velocity-based control, range-dependent speed calculations, and
 * integration with the command-based framework.
 */
@Config // Allows tuning constants from dashboard
public class Shooter extends SubsystemBase {

    // ------------------------------------------------------------
    // Hardware
    // ------------------------------------------------------------
    private final DcMotorEx shooter;
    private final Servo shooterAlignment;

    // ------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------
    private static final double WHEEL_RADIUS = 0.048;  // meters
    public static double DEFAULT_SPEED = 1000;         // RPM
    public final double RPM_TOLERANCE = 10;            // allowable error for motor readiness

    public static double TARGET_SPEED = 1850;
    private double servoPos;

    public static double SPEED = 0.01;

    private final double UPPER_BORDER = 1;
    private final double LOWER_BORDER = 0;


    // Shooting parameters
    private final double g = 9.87;                     // gravity (m/s^2)
    private final double SHOOTING_HEIGHT = 0.32;       // meters
    private final double SHOOTING_ANGLE = Math.toRadians(53);
    private final double GOAL_HEIGHT = 0.98;          // meters
    public static double SHOOTING_CONSTANT = 4;        // empirically tuned multiplier

    private static double SHOOTER_SPEED = 1000;

    // PIDF coefficients for velocity control
    private static final PIDFCoefficients pidf = new PIDFCoefficients(10, 0, 0, 12);

    // ------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------
    public Shooter() {
        shooter = BarnRobot.getInstance().robotHardware.shooter;
        shooterAlignment = BarnRobot.getInstance().robotHardware.shooterHood;

        double servoPos = 0.5;
        // Motor configuration
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooter.setDirection(DcMotorSimple.Direction.FORWARD);

        // Velocity control
        shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
    }

    // ------------------------------------------------------------
    // Low-level control
    // ------------------------------------------------------------

    /** Sets the shooter motor velocity in encoder ticks/sec. */
    private void setSpeed(double speed) {
        shooter.setVelocity(speed);
    }

    //ToDo:fix func(servo rotates 0.01 each click of bumper in ShooterAlignmentServoTest, needs to move til button is pressed)
    private void setShooterAlignmentPower(double power) {
//        if(servoPos > LOWER_BORDER && servoPos < UPPER_BORDER)
        servoPos += power * SPEED;

        shooterAlignment.setPosition(servoPos);
    }

    /**
     * Checks if the shooter motor is ready based on the current range from the Limelight.
     */
    public boolean isMotorReady() {
        double target = rangeDependentVelocity(BarnRobot.getInstance().limelight.getGoalRange());
        double velocity = shooter.getVelocity();
        return velocity > target - RPM_TOLERANCE && velocity < target + RPM_TOLERANCE;
    }

    /** Checks if the shooter motor is ready for a custom range. */
    public boolean isMotorReady(double range) {
        double target = rangeDependentVelocity(range);
        double velocity = shooter.getVelocity();
        return velocity > target - RPM_TOLERANCE && velocity < target + RPM_TOLERANCE;
    }

    /**
     * Calculates the required shooter velocity based on the target range.
     *
     * @param range distance to target in meters
     * @return required motor velocity
     */
    public double rangeDependentVelocity(double range) {
        double artifactSpeed = g / (Math.cos(SHOOTING_ANGLE) * Math.sqrt(2))
                * range / Math.sqrt(SHOOTING_HEIGHT + range * Math.tan(SHOOTING_ANGLE) - GOAL_HEIGHT);
        return artifactSpeed * SHOOTING_CONSTANT / WHEEL_RADIUS;
    }

    // ------------------------------------------------------------
    // Command-based wrappers
    // ------------------------------------------------------------

    /** Sets a custom speed for the shooter motor. */
    public Command customShooterCommand(double speed) {
        return new InstantCommand(() -> setSpeed(speed), this);
    }

    public Command runShooter(double power) {
        return new RunCommand(() -> setPower(power), this);

    }

    /** Calculates the speed based on Limelight range and sets the shooter velocity. */
//    public Command calcAndShootCommand() {
//        return new InstantCommand(() -> setSpeed(rangeDependentVelocity(BarnRobot.getInstance().limelight.getGoalRange())), this);
//    }

    /** Activates the shooter at default power. */
    public Command activateShooterCommand() {
        return new InstantCommand(() -> shooter.setPower(0.6), this);
    }

    public Command setPower(double power){
        return new InstantCommand(() -> shooter.setPower(power), this);
    }

    /** Deactivates the shooter motor. */
    public Command deactivateShooterCommand() {
        return new InstantCommand(() -> shooter.setPower(0), this);
    }

    /** Continuously runs the shooter at a velocity based on current Limelight range. */
//    public Command shootAtRangeCommand() {
//        return new RunCommand(() -> setSpeed(BarnRobot.getInstance().limelight.getGoalRange() == 0
//                ? DEFAULT_SPEED
//                : rangeDependentVelocity(BarnRobot.getInstance().limelight.getGoalRange())), this);
//    }

    public Command setShooterAlignment(double power) {
        return new InstantCommand(() -> setShooterAlignmentPower(power), this);
    }

    // ------------------------------------------------------------
    // Telemetry
    // ------------------------------------------------------------

    /** Displays current shooter info on the driver station telemetry. */
    public void displayTelemetry() {
        Telemetry telemetry = BarnRobot.getInstance().telemetry;
        telemetry.addData("Shooter alignment pos:", shooterAlignment.getPosition());
//        telemetry.addData("Shooter target speed", rangeDependentVelocity(BarnRobot.getInstance().limelight.getGoalRange()));
        telemetry.addData("Shooter actual speed", shooter.getVelocity());
//        telemetry.addData("Is motor ready", isMotorReady());
    }


//    public Command runShooterCommand(){
//
//    }

    public double getVelocity(){
        return shooter.getVelocity();
    }
}
