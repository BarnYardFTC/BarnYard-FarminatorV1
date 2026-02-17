package org.firstinspires.ftc.teamcode.commandGroups;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.StartEndCommand;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;

@Config
public class AutoController extends SequentialCommandGroup {

    // Turn on intake before shooting -> path -> autoAlign -> shooting
    public static Command shootCommandPathIntake(TrajectoryActionBuilder path){
        return new SequentialCommandGroup(
            intakeAndTransferGateCommand(),
            new DriveActionCommand(path),
            new ParallelRaceGroup(
                BarnRobot.getInstance().drive.alignToTagLamLamCommand(),
                new WaitCommand(1000)
            ),
            shootCommand()
        );
    }

    // Path for shooting pose -> autoAlign -> shooting
    public static Command shootCommandPath(TrajectoryActionBuilder shootingPath){
        return new SequentialCommandGroup(
                new DriveActionCommand(shootingPath),
                new ParallelRaceGroup(
                        BarnRobot.getInstance().drive.alignToTagLamLamCommand(),
                        new WaitCommand(1000)
                ),
                shootCommand()
        );
    }

    // Command for shoot
    public static int SHOOTING_TIME_MS = 1500;
    public static Command shootCommand() {
        return new SequentialCommandGroup(
                BarnRobot.getInstance().gate.openCommand(),
                new ParallelRaceGroup(
                        new WaitCommand(SHOOTING_TIME_MS),
                        new RunCommand(() -> checkShooterReadiness())
                ),
                BarnRobot.getInstance().gate.closeCommand(),
                CommandGroup.deactivateIntakeAndTransferCommand(),
                new WaitUntilCommand(() -> BarnRobot.getInstance().gate.isClosed())
        );
    }

    private static void checkShooterReadiness(){
        if (BarnRobot.getInstance().shooter.isReady()) {
            BarnRobot.getInstance().intake.setPower(1);
            BarnRobot.getInstance().transfer.setTransferMotorPower(1);
        } else {
            BarnRobot.getInstance().intake.setPower(0);
            BarnRobot.getInstance().transfer.setTransferMotorPower(0);
        }
    }

    public static Command deactivateIntakeAndTransferCommand(){
        return new ParallelCommandGroup(BarnRobot.getInstance().intake.deactivateIntakeCommand(), BarnRobot.getInstance().transfer.deactivateTransfer());
    }

    public static Command intakeAndTransferGateCommand(){
        return new ParallelCommandGroup(BarnRobot.getInstance().gate.closeCommand(), BarnRobot.getInstance().intake.activateIntakeCommand(), BarnRobot.getInstance().transfer.activateTransfer());
    }

    // Command for intake
    public static Command intakeCommandPath(TrajectoryActionBuilder path){
        return new SequentialCommandGroup(
                BarnRobot.getInstance().intake.activateIntakeCommand(),
                BarnRobot.getInstance().transfer.activateTransfer(),
                new DriveActionCommand(path),
                CommandGroup.deactivateIntakeAndTransferCommand()
        );
    }


}