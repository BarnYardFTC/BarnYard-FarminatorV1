package org.firstinspires.ftc.teamcode.commandGroups;

import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;

@Config
public class CommandGroup extends SequentialCommandGroup {

    public static int SHOOTING_TIME_MS = 1500;

    public static Command shootCommand(){
        return new ParallelRaceGroup(
                BarnRobot.getInstance().shooter.runShooterBasedOnDistance(),
                new SequentialCommandGroup(
                        new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                        BarnRobot.getInstance().gate.openCommand(),
                        BarnRobot.getInstance().intake.activateIntakeCommand(),
                        new WaitCommand(SHOOTING_TIME_MS),
                        BarnRobot.getInstance().intake.deactivateIntakeCommand(),
                        BarnRobot.getInstance().gate.closeCommand()
            )
        );
    }

    public static Command intakeCommand(){
        return new ParallelCommandGroup(BarnRobot.getInstance().gate.closeCommand(), BarnRobot.getInstance().intake.activateIntakeCommand());
    }
}
