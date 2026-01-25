package org.firstinspires.ftc.teamcode.commandGroups;


import android.app.appsearch.BatchResultCallback;
import android.net.wifi.WifiAvailableChannel;

import androidx.appcompat.app.ActionBarDrawerToggle;

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

    public static int SHOOTING_TIME_MS = 3000;

    public static Command shootCommand(){
        return new ParallelRaceGroup(
                BarnRobot.getInstance().shooter.runShooterBasedOnDistance(),
                new WaitUntilCommand(() -> BarnRobot.getInstance().gamepadEx1.wasJustPressed(GamepadKeys.Button.Y)),
                new SequentialCommandGroup(
                        new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                        BarnRobot.getInstance().gate.openCommand(),
                        BarnRobot.getInstance().intake.activateIntakeCommand(),
                        BarnRobot.getInstance().transfer.activateTransfer(),
                        new WaitCommand(1500),
                        BarnRobot.getInstance().gate.closeCommand(),
                        deactivateIntakeAndTransferCommand()
            )
        );
    }

    public static Command intakeAndTransferCommand(){
        return new ParallelCommandGroup(BarnRobot.getInstance().gate.closeCommand(), BarnRobot.getInstance().intake.smartIntakeCommand(), BarnRobot.getInstance().transfer.smartTransfer());
    }

    public static boolean robotContainsArtifacts(){
        return BarnRobot.getInstance().midColorSensor.isMidPosBusy() ||
                BarnRobot.getInstance().shooterColorSensor.isShootPosBusy();
    }

    public static Command deactivateIntakeAndTransferCommand(){
        return new ParallelCommandGroup(BarnRobot.getInstance().intake.deactivateIntakeCommand(), BarnRobot.getInstance().transfer.deactivateTransfer());
    }

    public static Command smartIntakeAndTransferCommand(){
        return new ParallelCommandGroup(BarnRobot.getInstance().gate.closeCommand(), BarnRobot.getInstance().intake.smartIntakeCommand(), BarnRobot.getInstance().transfer.smartTransfer());
    }





    public static void shootPreset(int n){
        if (n == 1){
            BarnRobot.getInstance().shooterHood.setCustomPosition(0.4);
            BarnRobot.getInstance().shooter.customDistance = 1.2;
        }
        else if (n == 2){
            BarnRobot.getInstance().shooterHood.setCustomPosition(1);
            BarnRobot.getInstance().shooter.customDistance = 2.2;
        } else if (n == 3){
            BarnRobot.getInstance().shooterHood.setCustomPosition(1);
            BarnRobot.getInstance().shooter.customDistance = 3;
        }
    }

    public static Command shootCommandPreset(int n){
        return new InstantCommand(() -> shootPreset(n));
    }
}
