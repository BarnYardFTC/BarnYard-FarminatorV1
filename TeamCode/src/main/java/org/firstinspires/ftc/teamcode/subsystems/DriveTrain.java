package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDController;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.components.MecanumDriveComponent;

@Config
public class DriveTrain extends SubsystemBase {

    // ============================================================
    //                       CONSTANTS
    // ============================================================

    public static double pYaw = 0.005, dYaw = 0.0008;
    public static double ALIGNMENT_TURNING_SPEED_OUTZONE = 0.6;
    public static double ALIGNMENT_TURNING_SPEED_INZONE = 0.35;

    // ============================================================
    //                       HARDWARE
    // ============================================================

    public final MecanumDriveComponent mecanumDriveComponent;

    // ============================================================
    //                       CONTROL VARIABLES
    // ============================================================

    private final PIDController pidControllerYaw;
    private final double initialBotHeading;

    private double lastTurnSpeed = 0;
    private boolean lastLimelightValid;
    private boolean tagJustVanished;


    public static double MIN_TURNING_SPEED = 0.06;

    // ============================================================
    //                       CONSTRUCTOR
    // ============================================================

    public DriveTrain() {
        mecanumDriveComponent = new MecanumDriveComponent();

        initialBotHeading = 270;
        pidControllerYaw = new PIDController(pYaw, 0, dYaw);

        lastLimelightValid = false;
        tagJustVanished = false;
    }

    // ============================================================
    //                          DRIVING
    // ============================================================

    public void drive(double x, double y, double turn) {
        mecanumDriveComponent.driveFieldCentric(x, y, turn);
    }

    public void displayTelemetry() {
        BarnRobot.getInstance().telemetry.addData("spdX", mecanumDriveComponent.getSpdX());
        BarnRobot.getInstance().telemetry.addData("spdY", mecanumDriveComponent.getSpdY());
        BarnRobot.getInstance().telemetry.addData("spdTurn", mecanumDriveComponent.getSpdTurn());
    }

    // ============================================================
    //                      AUTO ALIGNMENT LOGIC
    // ============================================================

    /** Main entry: aligns robot to the AprilTag or approximate direction */
    private void alignToGoal(double x, double y) {
        boolean valid = BarnRobot.getInstance().limelight.isGoalTagDetected();
        tagJustVanished = false;

        double turnSpeed;

        if (!valid) {
            // If tag just lost sight, turn in opposite direction
            if (lastLimelightValid) tagJustVanished = true;
            turnSpeed = determineFinalTurnSpeed();
        } else {
            double yawDiff = BarnRobot.getInstance().limelight.getDyaw();
            turnSpeed = diffToSpeed(yawDiff);
        }

        drive(x, y, turnSpeed);
        lastLimelightValid = valid;
    }

    /** Determines turn direction when Limelight is invalid */
    private double determineFinalTurnSpeed() {
        double heading = BarnRobot.getInstance().pinpointLocalizer.getPose().heading.toDouble();

        double lowerBound, upperBound;

        // Set alliance-specific angle zone
        switch (BarnRobot.getInstance().opmodeData.allianceColor) {
            case BLUE:
                lowerBound = 180;
                upperBound = 270;
                break;
            case RED:
                lowerBound = 90;
                upperBound = 180;
                break;
            default:
                return 0;
        }
        lastTurnSpeed = getTurnSpeed(heading, lowerBound, upperBound);
        return lastTurnSpeed;


    }

    /** Decides how to rotate based on heading and target zone */
    private double getTurnSpeed(double heading, double lower, double upper) {
        double margin = 3.0; // degrees tolerance
        double speedTurn;

        boolean insideZone = heading >= lower && heading <= upper;

        if (insideZone) {
            if (lastTurnSpeed > 0) speedTurn = ALIGNMENT_TURNING_SPEED_INZONE;
            else speedTurn = -ALIGNMENT_TURNING_SPEED_INZONE;

            if (tagJustVanished){
                // Reverse when you just missed the tag
                speedTurn *= -1;
            }
            else {
                // Reverse when hitting far edge
                boolean hitUpper = speedTurn < 0 && heading >= upper - margin;
                boolean hitLower = speedTurn > 0 && heading <= lower + margin;
                if (hitUpper || hitLower) speedTurn *= -1;
            }

        } else {
            // Outside zone → rotate shortest path toward nearest boundary
            double distToLower = angularDistanceDeg(heading, lower);
            double distToUpper = angularDistanceDeg(heading, upper);
            speedTurn = (distToLower <= distToUpper) ? -ALIGNMENT_TURNING_SPEED_OUTZONE : ALIGNMENT_TURNING_SPEED_OUTZONE;

        }

        return speedTurn;
    }

    /** Returns smallest absolute angular distance (0..180) */
    private double angularDistanceDeg(double a, double b) {
        double d = Math.abs((b - a) % 360.0);
        return (d > 180) ? 360 - d : d;
    }

    /** Converts yaw difference (Limelight) into turning speed via PID */
    private double diffToSpeed(double yawDiff) {
        double output = pidControllerYaw.calculate(-yawDiff, 0);
        BarnRobot.getInstance().telemetry.addData("output: ", output);
        if (Math.abs(output) < MIN_TURNING_SPEED && Math.abs(yawDiff) > 1)
            output = Math.copySign(MIN_TURNING_SPEED, output);
        return output;
    }

    // ============================================================
    //                           COMMANDS
    // ============================================================

    /** Default manual field-centric drive */
    public Command driveCommand() {
        return new RunCommand(
                () -> drive(
                        BarnRobot.getInstance().gamepadEx2.getLeftX(),
                        BarnRobot.getInstance().gamepadEx2.getLeftY(),
                        BarnRobot.getInstance().gamepadEx2.getRightX()
                ),
                this
        );
    }

    /** Continuous alignment command (runs alignToGoal loop) */
    public Command alignToTagCommand() {
        return new RunCommand(() -> alignToGoal(
                BarnRobot.getInstance().gamepadEx1.getLeftX(),
                BarnRobot.getInstance().gamepadEx1.getLeftY())
                , this);
    }


    public Command resetPinpointImuTracking(){
        return new InstantCommand(() -> BarnRobot.getInstance().pinpointLocalizer.driver.resetPosAndIMU(), this);
    }


    // ============================================================
    //                           PERIODIC
    // ============================================================

    @Override
    public void periodic() {
        pidControllerYaw.setPID(pYaw, 0, dYaw);
    }
}
