package org.firstinspires.ftc.teamcode.commandGroups;

import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;

/**
 * Command group for operating the intake and front transfer subsystem.
 *
 * Provides sequences for activating and deactivating the intake system,
 * including timed operation for intaking game elements.
 */


@Config
public class IntakeCommandGroup extends SequentialCommandGroup {

    /** Default intake operation duration in milliseconds. */
    public static int INTAKE_TIME = 4000;

    /**
     * Creates a full intake sequence:
     * - Activate intake and front transfer
     * - Wait for INTAKE_TIME milliseconds
     * - Deactivate transfer and intake
     *
     * @return A sequential command executing the full intake routine
     */
    public Command autoIntakeSequence() {
        BarnRobot robot = BarnRobot.getInstance();

        return new SequentialCommandGroup(
                robot.intake.activateIntakeCommand(),
                robot.transfer.setEntireTransferPowerCommand(Transfer.DEFAULT_TRANSFER_POWER),
                new WaitCommand(INTAKE_TIME),
                robot.transfer.setEntireTransferPowerCommand(0),
                robot.intake.deactivateIntakeCommand()
        );
    }

    /**
     * Command group to activate intake and front transfer simultaneously.
     *
     * @return A sequential command that activates the intake and front transfer
     */
    public static Command intakeAndFrontTransferCommand() {
        BarnRobot robot = BarnRobot.getInstance();
        return new ParallelCommandGroup(
                robot.intake.activateIntakeCommand(),
                robot.transfer.setFrontPowerCommand(Transfer.DEFAULT_TRANSFER_POWER)
        );
    }

}
