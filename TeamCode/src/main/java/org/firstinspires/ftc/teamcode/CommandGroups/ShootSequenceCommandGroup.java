package org.firstinspires.ftc.teamcode.CommandGroups;

import static org.firstinspires.ftc.teamcode.subsystems.Transfer.TRANSFER_ALL_DURATION;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ScheduleCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;

public class ShootSequenceCommandGroup extends SequentialCommandGroup {
    public Command shootCommand(){
        return new SequentialCommandGroup(
                BarnRobot.getInstance().shooter.activateShooterCommand(),
                new WaitCommand(1000),
                BarnRobot.getInstance().transfer.activateTransferCommand(),
                new WaitCommand(TRANSFER_ALL_DURATION),
                BarnRobot.getInstance().transfer.deactivateTransferCommand(),
                BarnRobot.getInstance().shooter.deactivateShooterCommand());
    }
}
