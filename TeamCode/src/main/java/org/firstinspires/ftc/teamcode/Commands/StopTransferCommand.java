package org.firstinspires.ftc.teamcode.Commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.SubSystems.Transfer;

public class StopTransferCommand extends CommandBase {

    private Transfer transfer;

    public StopTransferCommand(Transfer transfer) {
        this.transfer = transfer;
        addRequirements(transfer);
    }

    @Override
    public void initialize() {
        transfer.stop();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
