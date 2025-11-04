package org.firstinspires.ftc.teamcode.commandGroups;

import static org.firstinspires.ftc.teamcode.subsystems.Transfer.TRANSFER_ALL_DURATION;
import static org.firstinspires.ftc.teamcode.subsystems.Transfer.TRANSFER_ONE_DURATION;

import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;

/**
 * Command group sequences for shooting and feeding game elements to the shooter.
 *
 * Provides ready-to-use sequences for activating the shooter, transferring elements,
 * and waiting until the shooter is up to speed.
 */
@Config
public class ShootSequenceCommandGroup extends SequentialCommandGroup {

    /** Default preparation time for shooter in milliseconds. */
    public static int SHOOT_PREP_TIME = 4000;

    /**
     * Full shoot-all sequence:
     * - Activate shooter and wait for it to reach speed
     * - Activate transfer and intake
     * - Wait for all elements to transfer
     * - Deactivate transfer, intake, and shooter
     *
     * @return Sequential command executing the full shoot-all routine
     */
    public static Command shootAllCommand() {
        BarnRobot robot = BarnRobot.getInstance();

        return new SequentialCommandGroup(
                robot.shooter.activateShooterCommand(),
                new WaitCommand(SHOOT_PREP_TIME),
                robot.transfer.activateTransferCommand(),
                robot.intake.activateIntakeCommand(),
                new WaitCommand(TRANSFER_ALL_DURATION),
                robot.transfer.deactivateTransferCommand(),
                robot.shooter.deactivateShooterCommand(),
                robot.intake.deactivateIntakeCommand()
        );
    }

    /**
     * Parallel command to activate intake and transfer simultaneously.
     *
     * @return Parallel command activating intake and transfer
     */
    public static Command activateTransferToShooter() {
        BarnRobot robot = BarnRobot.getInstance();

        return new ParallelCommandGroup(
                robot.transfer.activateTransferCommand(),
                robot.intake.activateIntakeCommand()
        );
    }

    /**
     * Sequence to shoot one element when shooter reaches ready velocity.
     * Includes multiple checks to ensure the motor is up to speed.
     *
     * @return Sequential command shooting when shooter is ready
     */
    public static Command shootWhenReady() {
        BarnRobot robot = BarnRobot.getInstance();

        return new SequentialCommandGroup(
                new WaitUntilCommand(() -> robot.shooter.isMotorReady()),
                new WaitCommand(200),
                new WaitUntilCommand(() -> robot.shooter.isMotorReady()),
                new WaitCommand(200),
                new WaitUntilCommand(() -> robot.shooter.isMotorReady()),
                new ParallelCommandGroup(
                        robot.intake.activateIntakeCommand(),
                        robot.transfer.activateTransferCommand()
                ),
                new WaitCommand(TRANSFER_ONE_DURATION),
                new ParallelCommandGroup(
                        robot.transfer.deactivateTransferCommand(),
                        robot.intake.deactivateIntakeCommand()
                )
        );
    }

    /**
     * Sequence to shoot one element when shooter reaches ready velocity for a specific range.
     *
     * @param range The target distance for shooter speed calculation
     * @return Sequential command shooting when shooter is ready for the given range
     */
    public static Command customShootWhenReady(double range) {
        BarnRobot robot = BarnRobot.getInstance();

        return new SequentialCommandGroup(
                new WaitUntilCommand(() -> robot.shooter.customIsMotorReady(range)),
                new WaitCommand(200),
                new WaitUntilCommand(() -> robot.shooter.customIsMotorReady(range)),
                new WaitCommand(200),
                new WaitUntilCommand(() -> robot.shooter.customIsMotorReady(range)),
                new ParallelCommandGroup(
                        robot.intake.activateIntakeCommand(),
                        robot.transfer.activateTransferCommand()
                ),
                new WaitCommand(TRANSFER_ONE_DURATION),
                new ParallelCommandGroup(
                        robot.transfer.deactivateTransferCommand(),
                        robot.intake.deactivateIntakeCommand()
                )
        );
    }

    /**
     * Parallel command to deactivate intake and transfer simultaneously.
     *
     * @return Parallel command deactivating intake and transfer
     */
    public static Command deactivateTransferToShooter() {
        BarnRobot robot = BarnRobot.getInstance();

        return new ParallelCommandGroup(
                robot.transfer.deactivateTransferCommand(),
                robot.intake.deactivateIntakeCommand()
        );
    }

    /**
     * Shoot a single element:
     * - Activate transfer for one element duration
     * - Deactivate transfer and shooter
     *
     * @return Sequential command shooting one element
     */
    public static Command shootOneCommand() {
        BarnRobot robot = BarnRobot.getInstance();

        return new SequentialCommandGroup(
                robot.transfer.activateTransferCommand(),
                new WaitCommand(TRANSFER_ONE_DURATION),
                robot.transfer.deactivateTransferCommand(),
                robot.shooter.deactivateShooterCommand()
        );
    }
}
