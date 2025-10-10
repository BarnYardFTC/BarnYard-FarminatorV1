package org.firstinspires.ftc.teamcode.commandGroups;

import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;

@Config
public class IntakeCommandGroup extends SequentialCommandGroup {
    public static int INTAKE_TIME = 4000;

    public Command IntakeCommand() {
        return new SequentialCommandGroup(
                BarnRobot.getInstance().intake.activateIntake(),
                new WaitCommand(4000),
                BarnRobot.getInstance().intake.deactivateIntake());
    }
}
