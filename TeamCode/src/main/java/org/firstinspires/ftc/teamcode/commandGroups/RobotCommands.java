package org.firstinspires.ftc.teamcode.commandGroups;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.RunCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;

/**
 * Contains complex command groups with commands from different classes.
 * It's here to make the code more object oriented and something :)
 */
@Config
public class RobotCommands {

    /** Timings for intake and shooter in milliseconds. */
    private static final int TRANSFER_ONE_DURATION = 1500;
    private static final int TRANSFER_ALL_DURATION = TRANSFER_ONE_DURATION * 3;

    /** Telemetry thingy */
    private static String lastCommand;

    /** Coordinates */
    private static final double PARKING_X = 37;
    private static final double GATE_X = 0;
    private static final double PARKING_BLUE_Y = 32;
    private static final double GATE_BLUE_Y = - 53;
    private static final double PARKING_RED_Y = - PARKING_BLUE_Y;
    private static final double GATE_RED_Y = - GATE_BLUE_Y;

    public static double X = 0, Y = 0;


    /** Opens the gate shoots three artifacts */
//    public static Command shootAllCommand() {
//        return new SequentialCommandGroup(
//                new InstantCommand(() -> lastCommand = "shootAllCommand()"),
//                BarnRobot.getInstance().gate.openCommand(),
//                new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
//                BarnRobot.getInstance().intake.activateIntakeCommand(),
//                BarnRobot.getInstance().transfer.activateTransfer(),
//                new WaitCommand(TRANSFER_ALL_DURATION),
//                BarnRobot.getInstance().intake.deactivateIntakeCommand(),
//                BarnRobot.getInstance().transfer.deactivateTransfer()
//        );
//    }

    /** Collects artifacts and prevents them from being shot by closing the gate */
    public static Command collectCommand() {
        return new ParallelCommandGroup(
                new InstantCommand(() -> lastCommand = "collectCommand()"),
                BarnRobot.getInstance().gate.closeCommand(),
                BarnRobot.getInstance().intake.activateIntakeCommand(),
                BarnRobot.getInstance().transfer.activateTransfer()
        );
    }

    /** Stops Collecting artifacts */
    public static Command collectStopCommand() {
        return new ParallelCommandGroup(
                new InstantCommand(() -> lastCommand = "collectStopCommand()"),
                BarnRobot.getInstance().intake.deactivateIntakeCommand(),
                BarnRobot.getInstance().transfer.deactivateTransfer(),
                BarnRobot.getInstance().gate.openCommand()
        );
    }

    /** Keeps transfer and intake activated if robot isn't full */
//    public static Command smartCollectCommand(){
//        return new ParallelCommandGroup(
//                new InstantCommand(() -> lastCommand = "smartTransferAndIntake()"),
//                BarnRobot.getInstance().gate.closeCommand(),
//                BarnRobot.getInstance().intake.smartIntakeCommand(),
//                BarnRobot.getInstance().transfer.smartTransferCommand());
//    }
//
//    /** Shoots until there is no artifacts left */
//    public static Command smartShootCommand(){
//        return new ParallelRaceGroup(
//                BarnRobot.getInstance().shooter.runShooterBasedOnDistance(),
//                new SequentialCommandGroup(
//                        new InstantCommand(() -> lastCommand = "smartShootCommand()"),
//                        new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
//                        BarnRobot.getInstance().gate.openCommand(),
//                        BarnRobot.getInstance().intake.activateIntakeCommand(),
//                        BarnRobot.getInstance().transfer.activateTransfer(),
//                        new WaitUntilCommand(() -> !BarnRobot.getInstance().colorSensor.isShootAndMidIn()),
//                        BarnRobot.getInstance().gate.closeCommand(),
//                        new WaitUntilCommand(() -> BarnRobot.getInstance().gate.isClosed())
//                )
//        );
//    }

    /** Snaps heading to x axis WIP */
    private static double snapHeading(double heading) {
        //double degrees = Math.toDegrees(heading);
        //return Math.toRadians(Math.round(degrees / 180.0) * 180.0);
        return Math.toRadians(heading);
    }

    /** Auto driving commands */
//    public static Command autoParkCommand(OpModeData opModeData){
//        double y = (opModeData.allianceColor == OpModeData.AllianceColor.BLUE) ? PARKING_BLUE_Y : PARKING_RED_Y;
//        return new RunCommand(() -> {
//            BarnRobot.getInstance().drive.driveToPose(
//                    PARKING_X,
//                    y,
//                    BarnRobot.getInstance().pinpointLocalizer.driver.getHeading(AngleUnit.DEGREES)
//            );
//        }, BarnRobot.getInstance().drive);
//    }
//
//    public static Command autoGateCommand(OpModeData opModeData){
//        double y = (opModeData.allianceColor == OpModeData.AllianceColor.BLUE) ? GATE_BLUE_Y : GATE_RED_Y;
//        return new RunCommand(() -> {
//            BarnRobot.getInstance().drive.driveToPose(
//                    GATE_X,
//                    y,
//                    BarnRobot.getInstance().pinpointLocalizer.driver.getHeading(AngleUnit.DEGREES)
//            );
//        }, BarnRobot.getInstance().drive);
//    }

    public static Command autoParkCommand(OpModeData opModeData){
        Pose2d currentPose = BarnRobot.getInstance().pinpointLocalizer.getPose();
        double y;
        if (opModeData.allianceColor == OpModeData.AllianceColor.BLUE) {
            y = PARKING_BLUE_Y;
        } else y = PARKING_RED_Y;
        return new DriveActionCommand(
                BarnRobot.getInstance().roadRunnerMecanumDrive.actionBuilder(currentPose)
                        .strafeToLinearHeading(new Vector2d(PARKING_X, y), snapHeading(currentPose.heading.toDouble()))
        );
    }

//    public static Command autoGateCommand(OpModeData opModeData) {
//        Pose2d currentPose = BarnRobot.getInstance().pinpointLocalizer.getPose();
//        double y;
//        if (opModeData.allianceColor == OpModeData.AllianceColor.BLUE) {
//            y = GATE_BLUE_Y;
//        } else y = GATE_RED_Y;
//        return new SequentialCommandGroup(
//                new InstantCommand(() -> lastCommand = "autoGateCommand()"),
//                new DriveActionCommand(
//                        BarnRobot.getInstance().roadRunnerMecanumDrive.actionBuilder(currentPose)
//                                .strafeToLinearHeading(new Vector2d(GATE_X, y), snapHeading(currentPose.heading.toDouble()))
//                )
//        );
//    }

    private static DriveActionCommand rrDrive(double x, double y) {
        BarnRobot.getInstance().telemetry.addData("DAC x:", x);
        BarnRobot.getInstance().telemetry.addData("DAC y:", y);
        Pose2d currentPose = BarnRobot.getInstance().pinpointLocalizer.getPose();
        return new DriveActionCommand(BarnRobot.getInstance().roadRunnerMecanumDrive.actionBuilder(currentPose)
                .strafeToLinearHeading(new Vector2d(x, y), currentPose.heading));
    }

    public static Command park() {
        return rrDrive(PARKING_X, PARKING_BLUE_Y);
    }
//    public static Command parkRun() {
//        return new RunCommand(() -> rrDrive(PARKING_X, PARKING_BLUE_Y));
//    }


    /** Telemetry */
    public static String getLastCommand() {
        return lastCommand;
    }

    public static void displayTelemetry() {
        BarnRobot.getInstance().telemetry.addData("last command: ", getLastCommand());
    }
}