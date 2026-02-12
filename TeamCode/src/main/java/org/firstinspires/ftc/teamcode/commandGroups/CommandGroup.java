
package org.firstinspires.ftc.teamcode.commandGroups;


import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;

@Config
public class CommandGroup extends SequentialCommandGroup {

    public static int SHOOTING_TIME_MS = 3000;

    public static Command shootCommand(){
        return new ParallelRaceGroup(
                BarnRobot.getInstance().shooter.runShooterBasedOnDistance(),
                new SequentialCommandGroup(
                        new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                        BarnRobot.getInstance().gate.openCommand(),
                        intakeAndTransferActivateCommand(),
                        new WaitCommand(1500),
                        BarnRobot.getInstance().gate.closeCommand(),
                        deactivateIntakeAndTransferCommand(),
                        new WaitUntilCommand(() -> BarnRobot.getInstance().gate.isClosed())
                )
        );
    }

    public static Command intakeAndTransferGateCommand(){
        return new ParallelCommandGroup(BarnRobot.getInstance().gate.closeCommand(), BarnRobot.getInstance().intake.activateIntakeCommand(), BarnRobot.getInstance().transfer.activateTransfer());
    }

    public static Command intakeAndTransferActivateCommand(){
        return new ParallelCommandGroup(BarnRobot.getInstance().intake.activateIntakeCommand(), BarnRobot.getInstance().transfer.activateTransfer());
    }

    public static boolean robotContainsArtifacts(){
        return BarnRobot.getInstance().midSensor.isPoseBusy() ||
                BarnRobot.getInstance().shooterSensor.isPoseBusy();
    }

    public static Command deactivateIntakeAndTransferCommand(){
        return new ParallelCommandGroup(BarnRobot.getInstance().intake.deactivateIntakeCommand(), BarnRobot.getInstance().transfer.deactivateTransfer());
    }

    public static Command smartIntakeAndTransferCommand(){
        return new ParallelCommandGroup(BarnRobot.getInstance().gate.closeCommand(), BarnRobot.getInstance().intake.smartIntakeCommand(), BarnRobot.getInstance().transfer.smartTransfer());
    }





    public static void shootPreset(int n){
        if (n == 1){
//            BarnRobot.getInstance().shooterHood.setCustomPosition(0.4);
            BarnRobot.getInstance().shooter.customDistance = 1.2;
        }
        else if (n == 2){
//            BarnRobot.getInstance().shooterHood.setCustomPosition(1);
            BarnRobot.getInstance().shooter.customDistance = 2.2;
        } else if (n == 3){
//            BarnRobot.getInstance().shooterHood.setCustomPosition(1);
            BarnRobot.getInstance().shooter.customDistance = 3;
        }
    }

    public static Command shootCommandPreset(int n){
        return new InstantCommand(() -> shootPreset(n));
    }
}
