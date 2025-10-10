package org.firstinspires.ftc.teamcode.CommandGroups;

import static org.firstinspires.ftc.teamcode.subsystems.Transfer.TRANSFER_ALL_DURATION;
import static org.firstinspires.ftc.teamcode.subsystems.Transfer.TRANSFER_ONE_DURATION;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ScheduleCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;

public class ShootSequenceCommandGroup extends SequentialCommandGroup {
    public Command shootAllCommand(){
        return new SequentialCommandGroup(
                BarnRobot.getInstance().shooter.activateShooterCommand(),
                new WaitCommand(1000),
                BarnRobot.getInstance().transfer.activateTransferCommand(),
                new WaitCommand(TRANSFER_ALL_DURATION),
                BarnRobot.getInstance().transfer.deactivateTransferCommand(),
                BarnRobot.getInstance().shooter.deactivateShooterCommand());
    }

    public Command shootOneCommand(){
        return new SequentialCommandGroup(
                BarnRobot.getInstance().transfer.activateTransferCommand(),
                new WaitCommand(TRANSFER_ONE_DURATION),
                BarnRobot.getInstance().transfer.deactivateTransferCommand(),
                BarnRobot.getInstance().shooter.deactivateShooterCommand());
    }
}
