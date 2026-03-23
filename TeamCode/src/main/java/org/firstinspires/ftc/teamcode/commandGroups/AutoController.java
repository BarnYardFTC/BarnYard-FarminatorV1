package org.firstinspires.ftc.teamcode.commandGroups;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.ParallelDeadlineGroup;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;

@Config
public class AutoController extends SequentialCommandGroup {

    public static Command robotPathCommands(boolean intake, boolean shoot, TrajectoryActionBuilder path){
        return new SequentialCommandGroup(
                new ConditionalCommand(
                        intakeAndTransferGateCommand(),
                        deactivateIntakeAndTransferCommand(),
                        () -> intake
                ),
                new DriveActionCommand(path),
                new ConditionalCommand(
                        new SequentialCommandGroup(
                            new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                            shootCommand()),
                        new InstantCommand(),
                        () -> shoot
                ),
                deactivateIntakeAndTransferCommand()
        );
    }


    //power saving mode
    public static Command robotPathCommandsPSM(boolean intake, boolean shoot, TrajectoryActionBuilder path){
        return new ParallelRaceGroup(
                new ConditionalCommand(
                        BarnRobot.getInstance().shooter.runShooterBasedOnDistance(),
                        BarnRobot.getInstance().shooter.idleShooterCommand(),
                        () -> shoot
                ),
                robotPathCommands(intake, shoot, path)
        );
    }

    public static Command robotPathCommandsNA(boolean intake, boolean shoot, TrajectoryActionBuilder path){
        return new SequentialCommandGroup(
                new ConditionalCommand(
                        intakeAndTransferGateCommand(),
                        deactivateIntakeAndTransferCommand(),
                        () -> intake
                ),
                new DriveActionCommand(path),
                new ConditionalCommand(
                        new SequentialCommandGroup(
                                new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                                shootCommandNoAlign()),
                        new InstantCommand(),
                        () -> shoot
                ),
                deactivateIntakeAndTransferCommand()
        );
    }


//    public static Command shootCommand(TrajectoryActionBuilder path){
//        return new SequentialCommandGroup(
//                intakeAndTransferGateCommand(),
//                new DriveActionCommand(path)
////                deactivateIntakeAndTransferCommand()
//        );
//    }
//
//    public static Command intakeCommand(TrajectoryActionBuilder path){
//        return new SequentialCommandGroup(
//                shootCommand(),
//                new DriveActionCommand(path),
//                deactivateIntakeAndTransferCommand()
//        );
//    }

    // Command for shoot
    public static int SHOOTING_TIME_MS = 1500;
    public static Command shootCommand() {
        return new SequentialCommandGroup(
                BarnRobot.getInstance().gate.openCommand(),
                new WaitCommand(1500),
                BarnRobot.getInstance().gate.closeCommand(),
                BarnRobot.getInstance().colorSensor.setCheckFalse(),
                new WaitUntilCommand(() -> BarnRobot.getInstance().gate.isClosed())
        );
//
//        return new SequentialCommandGroup(
//                new ParallelRaceGroup(
//                        BarnRobot.getInstance().drive.alignToTagLamLamCommand(),
//                        new WaitCommand(250)
//                ),
//                new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
//                BarnRobot.getInstance().gate.openCommand(),
//                CommandGroup.intakeAndTransferActivateCommand(),
//                new WaitCommand(SHOOTING_TIME_MS),
//                BarnRobot.getInstance().gate.closeCommand(),
//                CommandGroup.deactivateIntakeAndTransferCommand()
//        );
    }

    public static Command shootCommandNoAlign() {
        return new ParallelRaceGroup(
                new SequentialCommandGroup(
                        BarnRobot.getInstance().gate.openCommand(),
                        new ParallelRaceGroup(
                                new WaitCommand(SHOOTING_TIME_MS),
                                new RunCommand(() -> checkShooterReadiness())
                        ),
                        BarnRobot.getInstance().gate.closeCommand(),
                        new WaitUntilCommand(() -> BarnRobot.getInstance().gate.isClosed())
                )
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
}