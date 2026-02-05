package org.firstinspires.ftc.teamcode.commandGroups;

import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;

import java.util.function.BooleanSupplier;

/**
 * A class that contains complex command groups with commands from different classes.
 * It's here to make the code more object oriented and something :)
 */
@Config
public class RobotCommands {

    /** Timings for intake and shooter in milliseconds. */
    public static int TRANSFER_ONE_DURATION = 1500;
    public static int TRANSFER_ALL_DURATION = TRANSFER_ONE_DURATION * 3;

    /**
     * Shoots one artifact when shooter is ready,
     * not meant for using outside of the class
     */
    private static Command shootOneCommand() {
        return new SequentialCommandGroup(
                new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                BarnRobot.getInstance().intake.activateIntakeCommand(),
                BarnRobot.getInstance().transfer.activateTransfer(),
                new WaitCommand(TRANSFER_ONE_DURATION),
                BarnRobot.getInstance().intake.deactivateIntakeCommand(),
                BarnRobot.getInstance().transfer.deactivateTransfer()
        );
    }

    /** Opens the gate shoots three artifacts */
    public static Command shootAllCommand() {
        return new SequentialCommandGroup(
                BarnRobot.getInstance().gate.openCommand(),
                shootOneCommand(),
                shootOneCommand(),
                shootOneCommand()
        );
    }

    /** Collects artifacts and prevents them from being shot by closing the gate */
    public static Command collectCommand() {
        return new ParallelCommandGroup(
                BarnRobot.getInstance().gate.closeCommand(),
                BarnRobot.getInstance().intake.activateIntakeCommand(),
                BarnRobot.getInstance().transfer.activateTransfer()
        );
    }

    /** Stops Collecting artifacts */
    public static Command collectStopCommand() {
        return new ParallelCommandGroup(
                BarnRobot.getInstance().intake.deactivateIntakeCommand(),
                BarnRobot.getInstance().transfer.deactivateTransfer(),
                BarnRobot.getInstance().gate.openCommand()
        );
    }
}