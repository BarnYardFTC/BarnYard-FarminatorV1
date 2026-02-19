
package org.firstinspires.ftc.teamcode.commandGroups;


import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;

@Config
public class CommandGroup extends SequentialCommandGroup {

    public static int SHOOTING_TIME_MS = 1500;


    public static Command smartShootCommand() {
        return new SequentialCommandGroup(
                new ParallelRaceGroup(
                        BarnRobot.getInstance().shooter.runShooterBasedOnDistance(),
                        new SequentialCommandGroup(
                                new WaitUntilCommand(CommandGroup::isReadyToShoot),
                                intakeAndTransferActivateCommand(),
                                BarnRobot.getInstance().gate.openCommand(),
                                new WaitUntilCommand(() -> !BarnRobot.getInstance().shooter.isReady()),
                                BarnRobot.getInstance().gate.closeCommand(),
                                new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                                BarnRobot.getInstance().gate.openCommand(),
                                new WaitUntilCommand(() -> !BarnRobot.getInstance().shooter.isReady()),
                                BarnRobot.getInstance().gate.closeCommand(),
                                new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                                BarnRobot.getInstance().gate.openCommand(),
                                new WaitUntilCommand(() -> !BarnRobot.getInstance().shooter.isReady())
                        )
                ),
                BarnRobot.getInstance().gate.closeCommand(),
                deactivateIntakeAndTransferCommand(),
                BarnRobot.getInstance().colorSensor.setCheckFalse(),
                new WaitUntilCommand(() -> BarnRobot.getInstance().gate.isClosed())
        );
    }

    private static boolean isReadyToShoot() {
        if (BarnRobot.getInstance().webcam.isLocalizationTagDetected()) {
            return BarnRobot.getInstance().drive.isInsideLaunchZone() && (BarnRobot.getInstance().shooter.isReady() && BarnRobot.getInstance().limelight.isAlignedToGoal());
        }
        return false;
    }

    public static Command shootCommand() {
        return new ParallelRaceGroup(
                BarnRobot.getInstance().shooter.runShooterBasedOnDistance(),
                new SequentialCommandGroup(
                        BarnRobot.getInstance().gate.openCommand(),
                        new ParallelRaceGroup(
                                new WaitCommand(SHOOTING_TIME_MS),
                                new RunCommand(() -> checkShooterReadiness())
                        ),
                        BarnRobot.getInstance().gate.closeCommand(),
                        CommandGroup.deactivateIntakeAndTransferCommand(),
                        BarnRobot.getInstance().colorSensor.setCheckFalse(),
                        new WaitUntilCommand(() -> BarnRobot.getInstance().gate.isClosed())
                )
        );
    }

    private static void checkShooterReadiness() {
        if (BarnRobot.getInstance().shooter.isReady()) {
            BarnRobot.getInstance().intake.setPower(1);
            BarnRobot.getInstance().transfer.setTransferMotorPower(1);
        } else {
            BarnRobot.getInstance().intake.setPower(0);
            BarnRobot.getInstance().transfer.setTransferMotorPower(0);
        }
    }

    public static Command intakeAndTransferGateCommand() {
        return new ParallelCommandGroup(BarnRobot.getInstance().gate.closeCommand(), BarnRobot.getInstance().intake.activateIntakeCommand(), BarnRobot.getInstance().transfer.activateTransfer());
    }

    public static Command intakeAndTransferActivateCommand() {
        return new ParallelCommandGroup(BarnRobot.getInstance().intake.activateIntakeCommand(), BarnRobot.getInstance().transfer.activateTransfer());
    }


    public static Command smartIntakeAndTransfer() {
        return new ParallelCommandGroup(BarnRobot.getInstance().intake.smartIntakeCommand(), BarnRobot.getInstance().transfer.smartTransferCommand());
    }

    public static Command deactivateIntakeAndTransferCommand() {
        return new ParallelCommandGroup(BarnRobot.getInstance().intake.deactivateIntakeCommand(), BarnRobot.getInstance().transfer.deactivateTransfer());
    }
}
