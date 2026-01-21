package org.firstinspires.ftc.teamcode.commandGroups;


import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;

@Config
public class CommandGroup extends SequentialCommandGroup {

    public static int SHOOTING_TIME_MS = 2000;

    public static Command shootCommand(){
        return new ParallelRaceGroup(
                BarnRobot.getInstance().shooter.runShooterBasedOnDistance(),
                new SequentialCommandGroup(
                        new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                        BarnRobot.getInstance().gate.openCommand(),
                        BarnRobot.getInstance().transfer.activateTransfer(),
                        BarnRobot.getInstance().intake.activateIntakeCommand(),
                        new ParallelRaceGroup(
                                new WaitUntilCommand(() -> !BarnRobot.getInstance().shooter.isReady()),
                                new WaitCommand(SHOOTING_TIME_MS)
                        ),
                        deactivateIntakeAndTransferCommand(),
                        new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                        BarnRobot.getInstance().transfer.activateTransfer(),
                        BarnRobot.getInstance().intake.activateIntakeCommand(),
                        new ParallelRaceGroup(
                                new WaitUntilCommand(() -> !BarnRobot.getInstance().shooter.isReady()),
                                new WaitCommand(SHOOTING_TIME_MS)
                        ),
                        deactivateIntakeAndTransferCommand(),
                        new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                        BarnRobot.getInstance().transfer.activateTransfer(),
                        BarnRobot.getInstance().intake.activateIntakeCommand(),
                        new ParallelRaceGroup(
                                new WaitUntilCommand(() -> !BarnRobot.getInstance().shooter.isReady()),
                                new WaitCommand(SHOOTING_TIME_MS)
                        ),
                        BarnRobot.getInstance().gate.closeCommand(),
                        deactivateIntakeAndTransferCommand()
            )
        );
    }

    public static Command intakeAndTransferCommand(){
        return new ParallelCommandGroup(BarnRobot.getInstance().gate.closeCommand(), BarnRobot.getInstance().intake.activateIntakeCommand(), BarnRobot.getInstance().transfer.activateTransfer());
    }

    public static Command deactivateIntakeAndTransferCommand(){
        return new ParallelCommandGroup(BarnRobot.getInstance().intake.deactivateIntakeCommand(), BarnRobot.getInstance().transfer.deactivateTransfer());
    }
}
