package org.firstinspires.ftc.teamcode.Commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.SubSystems.Transfer;

public class StartTransferCommand extends CommandBase {

    private Transfer transfer;

    public StartTransferCommand(Transfer transfer) {
        this.transfer = transfer;
        addRequirements(transfer);
    }


    @Override
    public void initialize() {
        transfer.transfer();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
