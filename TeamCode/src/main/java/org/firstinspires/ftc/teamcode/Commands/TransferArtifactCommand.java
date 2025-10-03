package org.firstinspires.ftc.teamcode.Commands;

import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.SubSystems.Transfer;

public class TransferArtifactCommand extends SequentialCommandGroup {

    private Transfer transfer;

    public TransferArtifactCommand(Transfer transfer){
        addCommands(new StartTransferCommand(transfer),
                new WaitCommand(2000),
                new StopTransferCommand(transfer));
        addRequirements(transfer);
    }
}
