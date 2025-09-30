package org.firstinspires.ftc.teamcode.Commands;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.SubSystems.ArmSubSystem;

public class IntakeCommandGroup {
    public static Command prepareIntake() {
        return new ParallelCommandGroup(
                BarnRobot.getInstance().arm.setPositionCommand(ArmSubSystem.middle),
                BarnRobot.getInstance().claw.release()
        );
    }

    public static Command Intake() {
        return new SequentialCommandGroup(
                BarnRobot.getInstance().arm.setPositionCommand(ArmSubSystem.down),
                new WaitCommand(200),
                BarnRobot.getInstance().claw.grab(),
                new WaitCommand(200),
                BarnRobot.getInstance().arm.setPositionCommand(ArmSubSystem.up)
        );
    }
}
