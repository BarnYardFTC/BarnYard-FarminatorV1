package org.firstinspires.ftc.teamcode.subsystems;

import android.app.VoiceInteractor;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDController;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.components.MecanumDriveComponent;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.PinpointLocalizer;

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

    public static double MIN_TURNING_SPEED = 0.06;

    private Pose2d stopPose;


    /** Field coordinates for the target goal. */
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
    private static final double SHOOTER_DIAMETER = 96;

    private double lastX = Double.NaN;
    private double lastY = Double.NaN;
    private double lastHeading = Double.NaN;
    private long lastTimeNs = 0;


    // ============================================================
    //                       CONSTRUCTOR
    // ============================================================

    public DriveTrain() {
        mecanumDriveComponent = new MecanumDriveComponent();

        initialBotHeading = 270;
        pidControllerYaw = new PIDController(pYaw, 0, dYaw);
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

    private double diffToSpeed(double yawDiff) {
        double output = pidControllerYaw.calculate(yawDiff, 0);
        if (Math.abs(output) < MIN_TURNING_SPEED && Math.abs(yawDiff) > 1)
            output = Math.copySign(MIN_TURNING_SPEED, output);
        return output;
    }

    public static double VELOCITY_LOOKAHEAD = 1.5;


    private void localizationBasedGoalAlignment(double spdX, double spdY) {

        BarnRobot robot = BarnRobot.getInstance();
        PinpointLocalizer localizer = robot.pinpointLocalizer;

        Pose2d pose = localizer.getPose();
        Pose2d vel  = localizer.getPoseVelocity();

        // --- Predict future position (meters) ---
        double predictedX = (pose.position.x + vel.position.x * VELOCITY_LOOKAHEAD) * 0.0254;
        double predictedY = (pose.position.y + vel.position.y * VELOCITY_LOOKAHEAD) * 0.0254;

        double currentHeading = getBotAbsoluteHeading();

        // --- Alliance-dependent constants ---
        boolean isRed = robot.opmodeData.allianceColor == OpModeData.AllianceColor.RED;

        double goalY = isRed ? RED_GOAL_Y : BLUE_GOAL_Y;
        double baseHeading = isRed ? 90.0 : 270.0;

        double goalX = (getDistanceFromGoal() < Shooter.SHOOTING_RANGE_2)
                ? GOAL_X_2
                : GOAL_X_1;

        // --- Geometry ---
        double dx = goalX - predictedX;
        double dy = goalY - predictedY;

        double tangentAngle = Math.toDegrees(Math.atan2(dx, dy));

        // --- Shoot-while-driving lead ---
        double shootWhileDriveCoef = 1.0; // tune
        double velocityLead =
                (vel.position.x * dx + vel.position.y * dy) * shootWhileDriveCoef;

        // --- Final desired heading ---
        double desiredHeading = baseHeading - tangentAngle + velocityLead;

        // --- Control ---
        double diffYaw = desiredHeading - currentHeading;
        double turnSpd = diffToSpeed(diffYaw);

        // --- Telemetry ---
        robot.telemetry.addData("predictedX", predictedX);
        robot.telemetry.addData("predictedY", predictedY);
        robot.telemetry.addData("dx", dx);
        robot.telemetry.addData("dy", dy);
        robot.telemetry.addData("tangentAngle", tangentAngle);
        robot.telemetry.addData("velocityLead", velocityLead);
        robot.telemetry.addData("desiredHeading", desiredHeading);
        robot.telemetry.addData("diffYaw", diffYaw);
        robot.telemetry.addData("vel", vel);

        // --- Drive ---
        if (robot.opmodeData.opModeType == OpModeData.OpModeType.TELEOP) {
            drive(spdX, spdY, turnSpd);
        } else {
            turnOnly(turnSpd);
        }
    }


    // ============================================================
    //                           COMMANDS
    // ============================================================
    /** Default manual field-centric drive */
    public Command driveOneDriverCommand() {
        return new RunCommand(
                () -> drive(
                        BarnRobot.getInstance().gamepadEx1.getLeftX(),
                        BarnRobot.getInstance().gamepadEx1.getLeftY(),
                        BarnRobot.getInstance().gamepadEx1.getRightX()
                ),
                this
        );
    }

    public Command driveTwoDriversCommand(){
        return new RunCommand(
                () -> drive(
                        BarnRobot.getInstance().gamepadEx2.getLeftX() + BarnRobot.getInstance().gamepadEx1.getLeftX(),
                        BarnRobot.getInstance().gamepadEx2.getLeftY() + BarnRobot.getInstance().gamepadEx1.getLeftY(),
                        BarnRobot.getInstance().gamepadEx2.getRightX() + BarnRobot.getInstance().gamepadEx1.getRightX()
                ), this
        );
    }

    public Command driveNonFieldOrientedCommand(){
        return new RunCommand(
                () -> mecanumDriveComponent.driveNonFieldCentric(
                                BarnRobot.getInstance().gamepadEx1.getLeftX(),
                                BarnRobot.getInstance().gamepadEx1.getLeftY(),
                                BarnRobot.getInstance().gamepadEx1.getRightX()
                        ), this
        );

    }

    /** Continuous alignment command (runs alignToGoal loop) */
    public Command alignToTagCommand() {
        return new RunCommand(() -> localizationBasedGoalAlignment(
                BarnRobot.getInstance().gamepadEx1.getLeftX(),
                BarnRobot.getInstance().gamepadEx1.getLeftY())
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

    public Command maintainPosCommand() {
        return new SequentialCommandGroup(
                new InstantCommand(() ->
                    stopPose = BarnRobot.getInstance().pinpointLocalizer.getPose()
                ),
                new RunCommand(() -> mecanumDriveComponent.maintainPos(
                        BarnRobot.getInstance().gamepadEx1.getLeftX(), stopPose), this)
        );
    }



    public Command resetPinpointTracking(){
        return new InstantCommand(() -> BarnRobot.getInstance().pinpointLocalizer.driver.resetPosAndIMU(), this);
    }

    public Command reset() {
        return new InstantCommand(() -> BarnRobot.getInstance().pinpointLocalizer.setPose(new Pose2d(BarnRobot.getInstance().pinpointLocalizer.getPose().position.x, BarnRobot.getInstance().pinpointLocalizer.getPose().position.y, 0)));
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

    private double angleWrap(double radians) {
        while (radians > Math.PI) radians -= 2 * Math.PI;
        while (radians < -Math.PI) radians += 2 * Math.PI;
        return radians;
    }

    public void displayPinpointDataTelemetry(){
        BarnRobot.getInstance().telemetry.addData("pinpoint x", BarnRobot.getInstance().pinpointLocalizer.getPose().position.x);
        BarnRobot.getInstance().telemetry.addData("pinpoint y", BarnRobot.getInstance().pinpointLocalizer.getPose().position.y);
        BarnRobot.getInstance().telemetry.addData("pinpoint heading", getBotAbsoluteHeading());
        BarnRobot.getInstance().telemetry.addData("distance from goal", getDistanceFromGoal());
    }

    // ============================================================
    //                           PERIODIC
    // ============================================================

    @Override
    public void periodic() {
        pidControllerYaw.setPID(pYaw, 0, dYaw);
    }
}
