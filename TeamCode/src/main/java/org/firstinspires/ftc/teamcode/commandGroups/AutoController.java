package org.firstinspires.ftc.teamcode.commandGroups;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;

@Config
public class AutoController extends SequentialCommandGroup {
    public static Command shootCommandPathIntake(TrajectoryActionBuilder path){
        return new SequentialCommandGroup(
                new ParallelRaceGroup(
                        BarnRobot.getInstance().shooter.runShooterBasedOnDistance(),
                        new SequentialCommandGroup(
                                CommandGroup.intakeAndTransferGateCommand(),
                                new DriveActionCommand(path),
                                new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                                BarnRobot.getInstance().gate.openCommand(),
                                BarnRobot.getInstance().intake.activateIntakeCommand(),
                                BarnRobot.getInstance().transfer.activateTransfer(),
                                new WaitCommand(SHOOTING_TIME_MS),
//                        new WaitUntilCommand(() -> !CommandGroup.robotContainsArtifacts()),
                                BarnRobot.getInstance().gate.closeCommand(),
                                CommandGroup.deactivateIntakeAndTransferCommand()
                        )
                ),
                BarnRobot.getInstance().shooter.turnOffInstant()
        );
    }

    public static int SHOOTING_TIME_MS = 1500;
    public static Command shootCommand() {
        return new SequentialCommandGroup(
                new ParallelRaceGroup(
                        BarnRobot.getInstance().shooter.runShooterBasedOnDistance(),
                        new SequentialCommandGroup(
                                new WaitCommand(1500),
                                BarnRobot.getInstance().gate.openCommand(),
                                BarnRobot.getInstance().transfer.activateTransfer(),
                                BarnRobot.getInstance().intake.activateIntakeCommand(),
                                new WaitCommand(SHOOTING_TIME_MS),
                                //                        new WaitUntilCommand(() -> !CommandGroup.robotContainsArtifacts()),
                                BarnRobot.getInstance().gate.closeCommand(),
                                CommandGroup.deactivateIntakeAndTransferCommand()
                        )
                ),
                BarnRobot.getInstance().shooter.turnOffInstant()
        );
    }

    public static Command deactivateIntakeAndTransferCommand(){
        return new ParallelCommandGroup(BarnRobot.getInstance().intake.deactivateIntakeCommand(), BarnRobot.getInstance().transfer.deactivateTransfer());
    }
    public static Command shootCommandPath(TrajectoryActionBuilder shootingPath){
        return new SequentialCommandGroup(
                new ParallelRaceGroup(
                        BarnRobot.getInstance().shooter.runShooterBasedOnDistance(),
                        new SequentialCommandGroup(
                                new DriveActionCommand(shootingPath),
                                new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                                BarnRobot.getInstance().gate.openCommand(),
                                BarnRobot.getInstance().intake.activateIntakeCommand(),
                                BarnRobot.getInstance().transfer.activateTransfer(),
                                new WaitCommand(SHOOTING_TIME_MS),
//                            new WaitUntilCommand(() -> !CommandGroup.robotContainsArtifacts()),
                                BarnRobot.getInstance().gate.closeCommand(),
                                CommandGroup.deactivateIntakeAndTransferCommand()
                        )
                ),
                BarnRobot.getInstance().shooter.turnOffInstant()
        );
    }

    public static Command intakeCommandPath(TrajectoryActionBuilder path){
        return new SequentialCommandGroup(
                BarnRobot.getInstance().intake.activateIntakeCommand(),
                BarnRobot.getInstance().transfer.activateTransfer(),
                new DriveActionCommand(path),
                CommandGroup.deactivateIntakeAndTransferCommand()
        );
    }

}
