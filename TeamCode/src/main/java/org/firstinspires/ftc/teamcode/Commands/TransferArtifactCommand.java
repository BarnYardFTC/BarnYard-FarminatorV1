package org.firstinspires.ftc.teamcode.Commands;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.SubSystems.Transfer;

public class TransferArtifactCommand extends SequentialCommandGroup {

    public Command transferArtifact() {
        return new SequentialCommandGroup(
                BarnRobot.getInstance().transfer.transfer(1),
                new WaitCommand(2000),
                BarnRobot.getInstance().transfer.transfer(0));
    }
}
