package org.firstinspires.ftc.teamcode.commandGroups;

import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;

@Config
public class IntakeCommandGroup extends SequentialCommandGroup {
    public static int INTAKE_TIME = 4000;

    public Command intakeSequence() {
        return new SequentialCommandGroup(
                BarnRobot.getInstance().intake.activateIntakeCommand(),
                BarnRobot.getInstance().transfer.activateFrontTransferCommand(),
                new WaitCommand(INTAKE_TIME),
                BarnRobot.getInstance().transfer.deactivateTransferCommand(),
                BarnRobot.getInstance().intake.deactivateIntakeCommand());
    }

    public static Command activateIntakeCommand(){
        return new SequentialCommandGroup(
                BarnRobot.getInstance().intake.activateIntakeCommand(),
                BarnRobot.getInstance().transfer.activateFrontTransferCommand());
    }

    public static Command deactivateIntakeCommand(){
        return new SequentialCommandGroup(
                BarnRobot.getInstance().intake.deactivateIntakeCommand(),
                BarnRobot.getInstance().transfer.deactivateTransferCommand());
    }
}
