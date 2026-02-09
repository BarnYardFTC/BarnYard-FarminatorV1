package org.firstinspires.ftc.teamcode.commandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;

public class SmartCommandGroups {

    public static Command smartTransferAndIntake(){
        return new ParallelCommandGroup(BarnRobot.getInstance().intake.smartIntakeCommand(), BarnRobot.getInstance().transfer.smartTransferCommand());
    }

    public static Command smartShootCommand(){
        return new ParallelRaceGroup(
                BarnRobot.getInstance().shooter.runShooterBasedOnDistance(),
                new SequentialCommandGroup(
                        new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                        BarnRobot.getInstance().gate.openCommand(),
                        CommandGroup.intakeAndTransferActivateCommand(),
                        new WaitUntilCommand(() -> BarnRobot.getInstance().colorSensor.isRobotFull()),
                        BarnRobot.getInstance().gate.closeCommand(),
                        new WaitUntilCommand(() -> BarnRobot.getInstance().gate.isClosed())
                )
        );
    }
}
