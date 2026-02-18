package org.firstinspires.ftc.teamcode.commandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;

public class SmartCommandGroups {

    public static Command smartTransferAndIntake(boolean switc) {
        if (switc) {
            return new ParallelCommandGroup(BarnRobot.getInstance().intake.smartIntakeCommand(), BarnRobot.getInstance().transfer.smartTransferCommand());
        } else {
            return new ParallelCommandGroup(BarnRobot.getInstance().intake.activateIntakeCommand(), BarnRobot.getInstance().transfer.activateTransfer());
        }
    }

    public static Command smartIntakesTransfers(){
        return new ParallelCommandGroup(BarnRobot.getInstance().intake.smartIntakeCommand(), BarnRobot.getInstance().transfer.smartTransferCommand());
    }
}
