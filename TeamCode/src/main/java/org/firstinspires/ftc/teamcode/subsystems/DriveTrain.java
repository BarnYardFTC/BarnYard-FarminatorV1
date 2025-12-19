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
import org.firstinspires.ftc.teamcode.util.OpModeData;

@Config
public class DriveTrain extends SubsystemBase {

    // ============================================================
    //                       CONSTANTS
    // ============================================================

    public static double pYaw = 0.03, dYaw = 0.0008;
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


    /** Field coordinates for the target goal. */
    public static final double GOAL_X_1 = -1.72;
    public static final double GOAL_X_2 = -1.65;
    public static final double BLUE_GOAL_Y = -1.6;
    public static final double RED_GOAL_Y = 1.55;

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

    public void turnOnly(double turn){
        mecanumDriveComponent.turnOnly(turn);
    }

    // ============================================================
    //                      AUTO ALIGNMENT LOGIC
    // ============================================================

    /** Main entry: aligns robot to the AprilTag or approximate direction */
//    private void alignToGoal(double x, double y) {
////        boolean valid = BarnRobot.getInstance().limelight.isGoalTagDetected();
//        tagJustVanished = false;
//
//        double turnSpeed;
//
//        if (!valid) {
//            // If tag just lost sight, turn in opposite direction
//            if (lastLimelightValid) tagJustVanished = true;
//            turnSpeed = determineFinalTurnSpeed();
//        } else {
////            double yawDiff = BarnRobot.getInstance().limelight.getDyaw();
////            turnSpeed = diffToSpeed(yawDiff);
//        }
//
//        drive(x, y, turnSpeed);
//        lastLimelightValid = valid;
//    }

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
        double output = pidControllerYaw.calculate(yawDiff, 0);
        if (Math.abs(output) < MIN_TURNING_SPEED && Math.abs(yawDiff) > 1)
            output = Math.copySign(MIN_TURNING_SPEED, output);
        return output;
    }

    private void localizationBasedGoalAlignment(double spdX, double spdY){

        Pose2d currentPose = BarnRobot.getInstance().pinpointLocalizer.getPose();

        double currentX = currentPose.position.x * 0.0254;
        double currentY = currentPose.position.y * 0.0254;
        double currentHeading = getBotAbsoluteHeading();

        double desiredHeading, tangentAngle;

        if (BarnRobot.getInstance().opmodeData.allianceColor == OpModeData.AllianceColor.RED){
            if (getDistanceFromGoal() < Shooter.SHOOTING_RANGE_3) {
                tangentAngle = Math.toDegrees(Math.atan((currentX - GOAL_X_2)/(RED_GOAL_Y - currentY)));
            }
            else {
                tangentAngle = Math.toDegrees(Math.atan((currentX - GOAL_X_1)/(RED_GOAL_Y - currentY)));
            }
            desiredHeading = 90 + tangentAngle;
        }
        else {
            if (getDistanceFromGoal() < Shooter.SHOOTING_RANGE_3) {
                tangentAngle = Math.toDegrees(Math.atan((currentX - GOAL_X_2)/(currentY-BLUE_GOAL_Y)));
            }
            else {
                tangentAngle = Math.toDegrees(Math.atan((currentX - GOAL_X_1)/(currentY-BLUE_GOAL_Y)));
            }
            desiredHeading = 270 - tangentAngle;
        }

        double diffYaw = desiredHeading - currentHeading;

        BarnRobot.getInstance().telemetry.addData("dyaw", diffYaw);
        BarnRobot.getInstance().telemetry.addData("desired heading", desiredHeading);
        double turnSpd = diffToSpeed(diffYaw);

        if (BarnRobot.getInstance().opmodeData.opModeType == OpModeData.OpModeType.TELEOP){
            drive(spdX, spdY, turnSpd);
        }
        else turnOnly(turnSpd);



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
        return new RunCommand(() -> localizationBasedGoalAlignment(
                BarnRobot.getInstance().gamepadEx2.getLeftX(),
                BarnRobot.getInstance().gamepadEx2.getLeftY())
                , this);
    }

    public RunCommand alignToTagCommandAuto() {
        return new RunCommand(() -> localizationBasedGoalAlignment(
                0,0)
                , this);
    }

    public Command stop(){
        return new InstantCommand(() -> turnOnly(0));
    }


    public Command resetPinpointTracking(){
        return new InstantCommand(() -> BarnRobot.getInstance().pinpointLocalizer.driver.resetPosAndIMU(), this);
    }

    public Command updatePinpointPose(Pose2d pose2d){
        return new InstantCommand(() -> BarnRobot.getInstance().pinpointLocalizer.setPose(pose2d));
    }

    public double getDistanceFromGoal(){
        double currentPoseX = BarnRobot.getInstance().pinpointLocalizer.getPose().position.x * 0.0254; // conversion from inch to meter
        double currentPoseY = BarnRobot.getInstance().pinpointLocalizer.getPose().position.y * 0.0254; // conversion from inch to meter
        

        if (BarnRobot.getInstance().opmodeData.allianceColor == OpModeData.AllianceColor.BLUE)
            return calcDistance(currentPoseX, currentPoseY, GOAL_X_1, BLUE_GOAL_Y);
        else return calcDistance(currentPoseX, currentPoseY, GOAL_X_1, RED_GOAL_Y);
    }

    public double calcDistance(double xG, double yG, double xR, double yR) {
        return Math.sqrt((xG - xR) * (xG - xR) + (yG - yR) * (yG - yR));
    }

    public double getBotAbsoluteHeading(){
        double heading;
        heading = Math.toDegrees(BarnRobot.getInstance().pinpointLocalizer.getPose().heading.toDouble());
        if (heading < 0) return heading + 360;
        return heading;
    }


    // ============================================================
    //                           PERIODIC
    // ============================================================

    @Override
    public void periodic() {
        pidControllerYaw.setPID(pYaw, 0, dYaw);
    }
}
