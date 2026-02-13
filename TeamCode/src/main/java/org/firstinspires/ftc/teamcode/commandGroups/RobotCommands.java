package org.firstinspires.ftc.teamcode.commandGroups;

import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;

/**
 * Contains complex command groups with commands from different classes.
 * It's here to make the code more object oriented and something :)
 */
@Config
public class RobotCommands {

    /** Timings for intake and shooter in milliseconds. */
    private static final int TRANSFER_ONE_DURATION = 1500;
    private static final int TRANSFER_ALL_DURATION = TRANSFER_ONE_DURATION * 3;

    private static String lastCommand;



    /** Opens the gate shoots three artifacts */
    public static Command shootAllCommand() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> lastCommand = "shootAllCommand()"),
                BarnRobot.getInstance().gate.openCommand(),
                new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                BarnRobot.getInstance().intake.activateIntakeCommand(),
                BarnRobot.getInstance().transfer.activateTransfer(),
                new WaitCommand(TRANSFER_ALL_DURATION),
                BarnRobot.getInstance().intake.deactivateIntakeCommand(),
                BarnRobot.getInstance().transfer.deactivateTransfer()
        );
    }

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

    /** Keeps transfer and intake activated if robot isn't full
     * (needs add all smart function to this branch)*/
//    public static Command smartCollectCommand(){
//        return new ParallelCommandGroup(
//                new InstantCommand(() -> lastCommand = "smartTransferAndIntake()"),
//                BarnRobot.getInstance().gate.closeCommand(),
//                BarnRobot.getInstance().intake.smartIntakeCommand(),
//                BarnRobot.getInstance().transfer.smartTransferCommand());
//    }

    /** Shoots until there is no artifacts left
     * (needs add all smart function to this branch)*/
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

    public static String getLastCommand() {
        return lastCommand;
    }

    public static void displayTelemetry() {
        BarnRobot.getInstance().telemetry.addData("last command: ", getLastCommand());
    }
}