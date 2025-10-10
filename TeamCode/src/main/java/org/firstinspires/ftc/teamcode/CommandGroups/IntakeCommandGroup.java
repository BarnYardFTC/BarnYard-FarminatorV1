package org.firstinspires.ftc.teamcode.CommandGroups;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;

public class IntakeCommandGroup extends SequentialCommandGroup {

    public Command IntakeCommand() {
        return new SequentialCommandGroup(
                BarnRobot.getInstance().intake.activateIntake(),
                new WaitCommand(4000),
                BarnRobot.getInstance().intake.deactivateIntake());
    }
}
