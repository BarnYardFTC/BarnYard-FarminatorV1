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

@Config
public class ShootSequenceCommandGroup extends SequentialCommandGroup {
    public static int SHOOT_PREP_TIME = 4000;
    public static Command shootAllCommand(){
        return new SequentialCommandGroup(
                BarnRobot.getInstance().shooter.activateShooterCommand(),
                new WaitCommand(SHOOT_PREP_TIME),
                BarnRobot.getInstance().transfer.activateTransferCommand(),
                BarnRobot.getInstance().intake.activateIntakeCommand(),
                new WaitCommand(TRANSFER_ALL_DURATION),
                BarnRobot.getInstance().transfer.deactivateTransferCommand(),
                BarnRobot.getInstance().shooter.deactivateShooterCommand(),
                BarnRobot.getInstance().intake.deactivateIntakeCommand());
    }

    public static Command activateTransferToShooter(){
        return new ParallelCommandGroup(
                BarnRobot.getInstance().transfer.activateTransferCommand(),
                BarnRobot.getInstance().intake.activateIntakeCommand()
        );
    }
    public static Command shootWhenReady(){
        return new SequentialCommandGroup(
                new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isMotorReady()),
                new WaitCommand(200),
                new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isMotorReady()),
                new WaitCommand(200),
                new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isMotorReady()),
                new ParallelCommandGroup(
                    BarnRobot.getInstance().intake.activateIntakeCommand(),
                    BarnRobot.getInstance().transfer.activateTransferCommand()
                ),
                new WaitCommand(TRANSFER_ONE_DURATION),
                new ParallelCommandGroup(
                    BarnRobot.getInstance().transfer.deactivateTransferCommand(),
                    BarnRobot.getInstance().intake.deactivateIntakeCommand()
                )
        );
    }

    public static Command deactivateTransferToShooter(){
        return new ParallelCommandGroup(
                BarnRobot.getInstance().transfer.deactivateTransferCommand(),
                BarnRobot.getInstance().intake.deactivateIntakeCommand()
        );
    }

    public static Command shootOneCommand(){
        return new SequentialCommandGroup(
                BarnRobot.getInstance().transfer.activateTransferCommand(),
                new WaitCommand(TRANSFER_ONE_DURATION),
                BarnRobot.getInstance().transfer.deactivateTransferCommand(),
                BarnRobot.getInstance().shooter.deactivateShooterCommand());
    }
}
