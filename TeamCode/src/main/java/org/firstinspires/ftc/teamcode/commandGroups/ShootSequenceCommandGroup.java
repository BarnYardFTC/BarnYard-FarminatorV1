package org.firstinspires.ftc.teamcode.commandGroups;


import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;

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


    public static int TRANSFER_ONE_DURATION = 1500;
    public static int TRANSFER_ALL_DURATION = TRANSFER_ONE_DURATION * 3;

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
//                robot.shooter.activateShooterCommand(),
                new WaitCommand(SHOOT_PREP_TIME),
                robot.transfer.setFrontPowerCommand(Transfer.DEFAULT_TRANSFER_POWER),
                robot.intake.activateIntakeCommand(),
                new WaitCommand(TRANSFER_ALL_DURATION),
                robot.transfer.setFrontPowerCommand(0),
//                robot.shooter.deactivateShooterCommand(),
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
                robot.transfer.setEntireTransferPowerCommand(Transfer.DEFAULT_TRANSFER_POWER),
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
                new WaitUntilCommand(() -> robot.shooter.isReady()),
                robot.transfer.setBackPowerCommand(1),
                new WaitUntilCommand(() -> robot.shooter.isShotDetected(robot.shooter.SHOOTER_DEFAULT_VELOCITY_CLOSE)),
                robot.transfer.setFrontPowerCommand(1),
                new WaitCommand(1000),
                robot.transfer.setBackPowerCommand(0),
                robot.intake.activateIntakeCommand(),
                new WaitCommand(TRANSFER_ONE_DURATION),
                robot.transfer.setFrontPowerCommand(0),
                robot.intake.deactivateIntakeCommand(),
                new WaitCommand(2000)
        );
    }

    /**
     * Sequence to shoot one element when shooter reaches ready velocity for a specific range.
     *
     * @param velocity The target distance for shooter speed calculation
     * @return Sequential command shooting when shooter is ready for the given range
     */
//    public static Command shootWhenReady(double velocity) {
//        BarnRobot robot = BarnRobot.getInstance();
//
//        return new SequentialCommandGroup(
//                new WaitUntilCommand(() -> robot.shooter.isReady()),
//                new WaitCommand(100),
//                new ParallelCommandGroup(
//                        robot.intake.activateIntakeCommand(),
//                        robot.transfer.setFrontPowerCommand(Transfer.DEFAULT_TRANSFER_POWER)
//                ),
//                new WaitCommand(TRANSFER_ONE_DURATION),
//                new ParallelCommandGroup(
//                        robot.transfer.setEntireTransferPowerCommand(0),
//                        robot.intake.deactivateIntakeCommand()
//                )
//        );
//    }

    /**
     * Parallel command to deactivate intake and transfer simultaneously.
     *
     * @return Parallel command deactivating intake and transfer
     */
    public static Command deactivateTransferToShooter() {
        BarnRobot robot = BarnRobot.getInstance();

        return new ParallelCommandGroup(
                robot.transfer.setEntireTransferPowerCommand(0),
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
                robot.transfer.setEntireTransferPowerCommand(Transfer.DEFAULT_TRANSFER_POWER),
                new WaitCommand(TRANSFER_ONE_DURATION),
                robot.transfer.setEntireTransferPowerCommand(0)
        );
    }
}
