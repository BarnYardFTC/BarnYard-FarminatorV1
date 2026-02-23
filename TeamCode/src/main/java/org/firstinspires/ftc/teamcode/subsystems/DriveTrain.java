package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDController;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.components.MecanumDriveComponent;
import org.firstinspires.ftc.teamcode.util.OpModeData;

@Config
public class DriveTrain extends SubsystemBase {

    // ============================================================
    //                       CONSTANTS / TUNING
    // ============================================================

    // Yaw PID (deg -> output turn)
    public static double closeP = 0.9, closeD = 0.05;
    public static double farP = 0.6, farD = 0.2;
    public final static double FAR_PID_DISTANCE = 2;
    public static double farLimelightGoal = -0.1;

    public double getFarLimelightGoal(){
        return farLimelightGoal;
    }

    //Big zone:
    public static double ax = -72, ay = 72;
    public static double bx = 10, by = 0;
    public static double cx = -72, cy = -72;
    //small zone
    public static double dx = 72, dy = 48;
    public static double ex = 24, ey = 0;
    public static double fx = 56, fy = -48;


    public static double TURN_START_POWER = 0.085; // tune 0.07–0.11
    public static double TURN_START_ERROR = 2.0;   // deg — only help when farther than this


    // Search / fallback turning speeds (when tag not visible)
    public static double ALIGNMENT_TURNING_SPEED_OUTZONE = 0.8;
    public static double ALIGNMENT_TURNING_SPEED_INZONE = 0.3;


    private boolean searchingForTag = true;


    // Minimum turning speed clamp (helps overcome friction / deadband)
    public static double MIN_TURNING_SPEED = 0.1;

    // Lookahead time (sec) for predicting future position during "shoot while driving"
    public static double VELOCITY_LOOKAHEAD = 1.5;

    private static final double PID_THRESHOLD = 0.03;

    /** Field coordinates for the target goal. (meters) */
    public static final double GOAL_X_1 = -1.72;
    public static final double GOAL_X_2 = -1.65;
    public static final double BLUE_GOAL_Y = -1.45;
    public static final double RED_GOAL_Y = 1.45;

    public static double GOAL_ROBOT_MIN_DISTANCE = 0.7;

    // thresholds (tune if needed)
    private static final double POSITION_EPSILON = 0.5; // mm (or inches, match your units)
    private static final double HEADING_EPSILON = Math.toRadians(1.0); // 1 degree
    private static final double VELOCITY_EPSILON = 0.01; // units/sec
    private static final double ANGULAR_VELOCITY_EPSILON = Math.toRadians(1.0); // rad/sec

    // NOTE: not used in this class currently, kept for future use
    private static final double SHOOTER_DIAMETER = 96;

    // ============================================================
    //                       HARDWARE / COMPONENTS
    // ============================================================

    public final MecanumDriveComponent mecanumDriveComponent;

    // ============================================================
    //                       STATE / CONTROL VARIABLES
    // ============================================================

    private double lastTurnSpeed = ALIGNMENT_TURNING_SPEED_INZONE;

    // Limelight visibility state
    private boolean lastLimelightValid;
    private boolean tagJustVanished;

    // PID controller for yaw correction
    private final PIDController pidControllerClose;
    private final PIDController pidControllerFar;

    // NOTE: currently unused, kept because you may want to reference initial alignment
    private final double initialBotHeading;

    // Maintain-position mode storage
    private Pose2d stopPose;

    // Static detection state
    private double lastX = Double.NaN;
    private double lastY = Double.NaN;
    private double lastHeading = Double.NaN;
    private long lastTimeNs = 0;

    // For func that moves robot to coordinates
    private static final double p = 0.01, d = 0.01; // Tune these
    private static final PIDController pid = new PIDController(p, 0, d);


    // ============================================================
    //                       CONSTRUCTOR
    // ============================================================

    public DriveTrain() {
        mecanumDriveComponent = new MecanumDriveComponent();

        initialBotHeading = 270;
        pidControllerClose = new PIDController(closeP, 0, closeD);
        pidControllerFar = new PIDController(farP, 0, farD);
    }

    // ============================================================
    //                          LOW-LEVEL DRIVE API
    // ============================================================

    /** Field-centric drive wrapper */
    public void drive(double x, double y, double turn) {
        lastLimelightValid = false;
        mecanumDriveComponent.driveFieldCentric(x, y, turn);
    }

    /** Rotate in place wrapper */
    public void turnOnly(double turn) {
        mecanumDriveComponent.turnOnly(turn);
    }

    // ============================================================
    //                          TURN CONTROL HELPERS
    // ============================================================

    /**
     * Converts yaw error into turn speed using PID.
     * NOTE: yawDiff is expected in "degrees-like" space in your codebase:
     * - diffToSpeed uses PIDController directly, and you compare yawDiff > 1
     * Keep consistent with Limelight.getDyaw() and desiredHeading math.
     */

    private double diffToSpeed(double yawDiff) {
        // PID tries to drive yawDiff → 0
        double output;
        // PID tries to drive yawDiff → 0
        if(BarnRobot.getInstance().limelight.getGoalDistance() > FAR_PID_DISTANCE){
            pidControllerFar.setPID(farP, 0, farD);
            output = pidControllerFar.calculate(0, yawDiff);
        }
        else {
            pidControllerClose.setPID(closeP, 0, closeD);
            output = pidControllerClose.calculate(0, yawDiff);
        }
        // Deadband so we don't twitch near zero
        if (Math.abs(yawDiff) < PID_THRESHOLD) {
            return 0;
        }

        // Ensure minimum turning power to overcome friction
        if (Math.abs(output) < MIN_TURNING_SPEED) {
            output = Math.copySign(MIN_TURNING_SPEED, output);
        }

        return output;
    }



    /**
     * When tag is NOT visible: decide which direction/speed to spin to re-acquire it.
     * Uses heading + alliance-based "zone" logic.
     */
    private double determineFinalTurnSpeed() {
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

        lastTurnSpeed = getTurnSpeed(lowerBound, upperBound);
        return lastTurnSpeed;
    }

    /**
     * Returns "search turn speed" based on whether we're inside the alliance zone or outside it.
     * - Inside zone: bounce between edges (and optionally reverse when tag just vanished)
     * - Outside zone: rotate shortest path toward nearest boundary
     */
    private double getTurnSpeed(double lower, double upper) {
        double heading = getBotAbsoluteHeading();
        double edgeMargin = 0.1;   // how close to edge before flipping
        double speedTurn;
        boolean insideZone = heading >= lower && heading <= upper;
        if (!insideZone) {
            // --- OUTSIDE ZONE: go to nearest boundary (fast) ---
            double distToLower = angularDistanceDeg(heading, lower);
            double distToUpper = angularDistanceDeg(heading, upper);
            speedTurn = (distToLower < distToUpper)
                    ? -ALIGNMENT_TURNING_SPEED_OUTZONE
                    : ALIGNMENT_TURNING_SPEED_OUTZONE;
            // Set search direction memory for when we enter zone
            lastTurnSpeed = speedTurn;
            return speedTurn;

        }
        // --- INSIDE ZONE: sweep back and forth slowly ---

        // If we somehow had no direction yet, pick one
        if (lastTurnSpeed == 0) {
            lastTurnSpeed = ALIGNMENT_TURNING_SPEED_INZONE;
        }
        speedTurn = Math.signum(lastTurnSpeed) * ALIGNMENT_TURNING_SPEED_INZONE;
        // Flip if we just lost the tag (forces re-scan opposite side)
        if (tagJustVanished) {
            speedTurn *= -1;
        }
        // Flip ONLY when actually reaching the zone edges
        boolean nearUpperEdge = heading >= upper - edgeMargin;
        boolean nearLowerEdge = heading <= lower + edgeMargin;
        if ((speedTurn > 0 && nearUpperEdge) || (speedTurn < 0 && nearLowerEdge)) {
            speedTurn *= -1;
        }
        lastTurnSpeed = speedTurn;
        return speedTurn;
    }


    /** Small helper: smallest circular distance between two headings (deg) */
    private double angularDistanceDeg(double a, double b) {
        double d = Math.abs((b - a) % 360.0);
        return (d > 180) ? 360 - d : d;
    }

    // ============================================================
    //                          ALIGNMENT LOGIC
    // ============================================================

    public  boolean isInsideLaunchZone() {
        double px = BarnRobot.getInstance().pinpointLocalizer.getPose().position.x;
        double py = BarnRobot.getInstance().pinpointLocalizer.getPose().position.y;
        //coordinates of triangle vertexes

        //big zone
        double d1 = cross(px, py, ax, ay, bx, by);
        double d2 = cross(px, py, bx, by, cx, cy);
        double d3 = cross(px, py, cx, cy, ax, ay);
        //small zone
        double d4 = cross(px, py, dx, dy, ex, ey);
        double d5 = cross(px, py, ex, ey, fx, fy);
        double d6 = cross(px, py, fx, fy, dx, dy);


        boolean hasNegBig = (d1 < 0) || (d2 < 0) || (d3 < 0);
        boolean hasPosBig = (d1 > 0) || (d2 > 0) || (d3 > 0);

        boolean hasNegSmall = (d4 < 0) || (d5 < 0) || (d6 < 0);
        boolean hasPosSmall = (d4 > 0) || (d5 > 0) || (d6 > 0);

        return (!(hasNegBig && hasPosBig) || !(hasNegSmall && hasPosSmall)); // inside if all same sign
    }

    private static double cross(double px, double py,
                                double ax, double ay,
                                double bx, double by) {
        return (px - bx) * (ay - by) - (ax - bx) * (py - by);
    }

    /** Main entry: aligns robot to the AprilTag or approximate direction */
    private void alignToGoal(double x, double y) {
        boolean valid = BarnRobot.getInstance().limelight.isGoalTagDetected();

        double turnSpeed;

        if (valid) {
            searchingForTag = false;
            double yawDiff = BarnRobot.getInstance().limelight.getGoalYaw();
            turnSpeed = diffToSpeed(yawDiff);
            BarnRobot.getInstance().telemetry.addData("pid speed", turnSpeed);
        } else {
            if (!searchingForTag) {
                searchingForTag = true;
                tagJustVanished = true;
            } else {
                tagJustVanished = false;
            }

            turnSpeed = determineFinalTurnSpeed();
        }

        drive(x, y, turnSpeed);
        lastLimelightValid = valid;
    }


    /**
     * Alignment that uses localization + a predicted future position to aim at goal.
     * Works for:
     * - TELEOP: drive(spdX, spdY, turnSpd)
     * - AUTO:   turnOnly(turnSpd)
     */
//    private void localizationBasedGoalAlignment(double spdX, double spdY) {
//
//        BarnRobot robot = BarnRobot.getInstance();
//        PinpointLocalizer localizer = robot.pinpointLocalizer;
//
//        Pose2d pose = localizer.getPose();
//        Pose2d vel  = localizer.getPoseVelocity();
//
//        // --- Predict future position (meters) ---
//        double predictedX = (pose.position.x + vel.position.x * VELOCITY_LOOKAHEAD) * 0.0254;
//        double predictedY = (pose.position.y + vel.position.y * VELOCITY_LOOKAHEAD) * 0.0254;
//
//        double currentHeading = getBotAbsoluteHeading();
//
//        // --- Alliance-dependent constants ---
//        boolean isRed = robot.opmodeData.allianceColor == OpModeData.AllianceColor.RED;
//
//        double goalY = isRed ? RED_GOAL_Y : BLUE_GOAL_Y;
//        double baseHeading = isRed ? 90.0 : 270.0;
//
//        double goalX = (getDistanceFromGoal() < Shooter.SHOOTING_RANGE_2)
//                ? GOAL_X_2
//                : GOAL_X_1;
//
//        // --- Geometry ---
//        double dx = goalX - predictedX;
//        double dy = goalY - predictedY;
//
//        double tangentAngle = Math.toDegrees(Math.atan2(dx, dy));
//
//        // --- Shoot-while-driving lead ---
//        double shootWhileDriveCoef = 1.0; // tune
//        double velocityLead =
//                (vel.position.x * dx + vel.position.y * dy) * shootWhileDriveCoef;
//
//        // --- Final desired heading ---
//        double desiredHeading = baseHeading - tangentAngle + velocityLead;
//
//        // --- Control ---
//        double diffYaw = desiredHeading - currentHeading;
//        double turnSpd = diffToSpeed(diffYaw);
//
//        // --- Telemetry ---
////        robot.telemetry.addData("predictedX", predictedX);
////        robot.telemetry.addData("predictedY", predictedY);
////        robot.telemetry.addData("dx", dx);
////        robot.telemetry.addData("dy", dy);
////        robot.telemetry.addData("tangentAngle", tangentAngle);
////        robot.telemetry.addData("velocityLead", velocityLead);
////        robot.telemetry.addData("desiredHeading", desiredHeading);
////        robot.telemetry.addData("diffYaw", diffYaw);
////        robot.telemetry.addData("vel", vel);
//
//        // --- Drive ---
//        if (robot.opmodeData.opModeType == OpModeData.OpModeType.TELEOP) {
//            drive(spdX, spdY, turnSpd);
//        } else {
//            turnOnly(turnSpd);
//        }
//    }

    // ============================================================
    //                           COMMANDS (in a clean order)
    // ============================================================

    /** Default manual field-centric drive (1 driver) */
    public Command driveOneDriverCommand() {
        return new RunCommand(
                () -> drive(
                        BarnRobot.getInstance().gamepadEx1.getLeftX() + BarnRobot.getInstance().gamepadEx2.getLeftX(),
                        BarnRobot.getInstance().gamepadEx1.getLeftY()+ BarnRobot.getInstance().gamepadEx2.getLeftY(),
                        BarnRobot.getInstance().gamepadEx1.getRightX() + BarnRobot.getInstance().gamepadEx2.getRightX()
                ),
                this
        );
    }

    /** Field-centric drive (2 drivers summed) */
    public Command driveTwoDriversCommand() {
        return new RunCommand(
                () -> drive(
                        BarnRobot.getInstance().gamepadEx2.getLeftX() + BarnRobot.getInstance().gamepadEx1.getLeftX(),
                        BarnRobot.getInstance().gamepadEx2.getLeftY() + BarnRobot.getInstance().gamepadEx1.getLeftY(),
                        BarnRobot.getInstance().gamepadEx2.getRightX() + BarnRobot.getInstance().gamepadEx1.getRightX()
                ),
                this
        );
    }

    /** Robot-centric drive (no field orientation) */
    public Command driveNonFieldOrientedCommand() {
        return new RunCommand(
                () -> mecanumDriveComponent.driveNonFieldCentric(
                        BarnRobot.getInstance().gamepadEx1.getLeftX() + BarnRobot.getInstance().gamepadEx2.getLeftX(),
                        BarnRobot.getInstance().gamepadEx1.getLeftY() + BarnRobot.getInstance().gamepadEx2.getLeftY(),
                        BarnRobot.getInstance().gamepadEx1.getRightX() + BarnRobot.getInstance().gamepadEx2.getRightX()
                ),
                this
        );
    }

    /** Continuous alignment using localization-based aiming */
//    public Command alignToTagCommand() {
//        return new RunCommand(
//                () -> localizationBasedGoalAlignment(
//                        BarnRobot.getInstance().gamepadEx1.getLeftX(),
//                        BarnRobot.getInstance().gamepadEx1.getLeftY()
//                ),
//                this
//        );
//    }

    /** Continuous alignment using Limelight tag yaw (fallback search when tag lost) */
    public Command alignToTagLamLamCommand() {
        return new RunCommand(
                () -> alignToGoal(
                        BarnRobot.getInstance().gamepadEx1.getLeftX() + BarnRobot.getInstance().gamepadEx2.getLeftX(),
                        BarnRobot.getInstance().gamepadEx1.getLeftY() + BarnRobot.getInstance().gamepadEx2.getLeftY()
                ),
                this
        );
    }

    public Command alignToTagAutoCommand() {
        return new RunCommand(
                () -> alignToGoal(
                        BarnRobot.getInstance().gamepadEx1.getLeftX() + BarnRobot.getInstance().gamepadEx2.getLeftX(),
                        BarnRobot.getInstance().gamepadEx1.getLeftY() + BarnRobot.getInstance().gamepadEx2.getLeftY()
                ),
                this
        );
    }

    public void driveToPose(double targetX, double targetY, double targetHeading) {
        Pose2d currentPose = BarnRobot.getInstance().pinpointLocalizer.getPose();

        //calculates translation speeds
        double xSpeed = pid.calculate(currentPose.position.x, targetX);
        double ySpeed = pid.calculate(currentPose.position.y, targetY);

        //calculates rotation speed using existing yaw logic
        double headingError = angleWrap(Math.toRadians(targetHeading) - currentPose.heading.toDouble());
        double turnSpeed = diffToSpeed(Math.toDegrees(headingError));

        drive(xSpeed, ySpeed, turnSpeed);
    }

    /** Auto version: alignment with zero translation (turn only) */
//    public RunCommand alignToTagCommandAuto() {
//        return new RunCommand(
//                () -> localizationBasedGoalAlignment(0, 0),
//                this
//        );
//    }

    /** Stops rotation (turn = 0). NOTE: does not explicitly stop translation if something else drives it. */
    public Command stop() {
        return new InstantCommand(() -> turnOnly(0));
    }

    /** Saves current pose and then runs a hold/maintain loop (uses maintainPos in component) */
    public Command maintainPosCommand() {
        return new SequentialCommandGroup(
                new InstantCommand(() ->
                        stopPose = BarnRobot.getInstance().pinpointLocalizer.getPose()
                ),
                new RunCommand(() -> mecanumDriveComponent.maintainPos(
                        BarnRobot.getInstance().gamepadEx1.getLeftX() + BarnRobot.getInstance().gamepadEx2.getLeftX(),
                        stopPose
                ), this)
        );
    }

    // ----------------------------
    // Localization / Pinpoint / Limelight helper commands
    // ----------------------------

    public Command updatePinpointCommand() {
        return new InstantCommand(() -> BarnRobot.getInstance().limelight.updatePose(), this);
    }

//    public Command resetPinpointCommand() {
//        return new InstantCommand(() -> BarnRobot.getInstance().limelight.getRobotFieldPose(), this);
//    }

    public Command resetPinpointTracking() {
        return new InstantCommand(() -> BarnRobot.getInstance().pinpointLocalizer.driver.resetPosAndIMU(), this);
    }

    public Command reset() {
        return new InstantCommand(() ->
                BarnRobot.getInstance().pinpointLocalizer.setPose(
                        new Pose2d(
                                BarnRobot.getInstance().pinpointLocalizer.getPose().position.x,
                                BarnRobot.getInstance().pinpointLocalizer.getPose().position.y,
                                0
                        )
                )
        );
    }

    public Command updatePinpointPose(Pose2d pose2d) {
        return new InstantCommand(() -> BarnRobot.getInstance().pinpointLocalizer.setPose(pose2d));
    }

    // ============================================================
    //                           GEOMETRY / MEASUREMENTS
    // ============================================================

    /** Distance from current robot pose to goal (meters). Uses alliance to choose goalY. */
    public double getDistanceFromGoal() {
        double currentPoseX, currentPoseY;
        if (BarnRobot.getInstance().opmodeData.opModeType == OpModeData.OpModeType.AUTONOMOUS){
            currentPoseX = BarnRobot.getInstance().roadRunnerMecanumDrive.localizer.getPose().position.x * 0.0254; // inch -> meter
            currentPoseY = BarnRobot.getInstance().roadRunnerMecanumDrive.localizer.getPose().position.y * 0.0254; // inch -> meter
        }
        else {
            currentPoseY = BarnRobot.getInstance().pinpointLocalizer.getPose().position.y * 0.0254; // inch -> meter
            currentPoseX = BarnRobot.getInstance().pinpointLocalizer.getPose().position.x * 0.0254; // inch -> meter
        }

        if (BarnRobot.getInstance().opmodeData.allianceColor == OpModeData.AllianceColor.BLUE)
            return calcDistance(currentPoseX, currentPoseY, GOAL_X_1, BLUE_GOAL_Y);
        else
            return calcDistance(currentPoseX, currentPoseY, GOAL_X_1, RED_GOAL_Y);
    }

    /** Generic distance helper */
    public double calcDistance(double xG, double yG, double xR, double yR) {
        return Math.sqrt((xG - xR) * (xG - xR) + (yG - yR) * (yG - yR));
    }

    /** Absolute heading in degrees [0, 360) */
    public double getBotAbsoluteHeading() {
        double heading = Math.toDegrees(BarnRobot.getInstance().pinpointLocalizer.getPose().heading.toDouble());
        if (heading < 0) return heading + 360;
        return heading;
    }

    // ============================================================
    //                           STATIC DETECTION
    // ============================================================

    /** Returns true if robot is basically not moving (based on delta pose over time). */
    public boolean isRobotStatic() {
        double y = BarnRobot.getInstance().pinpointLocalizer.getPose().position.y;
        double x = BarnRobot.getInstance().pinpointLocalizer.getPose().position.x;
        double heading = getBotAbsoluteHeading();
        long now = System.nanoTime();

        // first call initialization
        if (Double.isNaN(lastX)) {
            lastX = x;
            lastY = y;
            lastHeading = heading;
            lastTimeNs = now;
            return true;
        }

        double dt = (now - lastTimeNs) * 1e-9; // seconds
        if (dt <= 0) return true;

        double dx = x - lastX;
        double dy = y - lastY;
        double dHeading = angleWrap(heading - lastHeading);

        double linearVelocity = Math.hypot(dx, dy) / dt;
        double angularVelocity = Math.abs(dHeading) / dt;

        // update history
        lastX = x;
        lastY = y;
        lastHeading = heading;
        lastTimeNs = now;

        return linearVelocity < VELOCITY_EPSILON
                && angularVelocity < ANGULAR_VELOCITY_EPSILON;
    }

    /** Wrap radians to [-pi, pi] */
    private double angleWrap(double radians) {
        while (radians > Math.PI) radians -= 2 * Math.PI;
        while (radians < -Math.PI) radians += 2 * Math.PI;
        return radians;
    }

    // ============================================================
    //                           TELEMETRY
    // ============================================================

    public void displayPinpointDataTelemetry() {
        BarnRobot.getInstance().telemetry.addData("pinpoint x", BarnRobot.getInstance().pinpointLocalizer.getPose().position.x);
        BarnRobot.getInstance().telemetry.addData("pinpoint y", BarnRobot.getInstance().pinpointLocalizer.getPose().position.y);
        BarnRobot.getInstance().telemetry.addData("pinpoint heading", getBotAbsoluteHeading());
        BarnRobot.getInstance().telemetry.addData("is in zone", isInsideLaunchZone());
        BarnRobot.getInstance().telemetry.addData("distance from goal", getDistanceFromGoal());
    }

    // ============================================================
    //                           PERIODIC
    // ============================================================

    @Override
    public void periodic() {
        // Keep PID values hot-reloadable from Dashboard
        pidControllerClose.setPID(closeP, 0, closeD);
    }
}