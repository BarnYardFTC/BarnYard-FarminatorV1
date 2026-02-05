package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.RobotCommands;

@TeleOp(name = "Cmd", group = "test")
public class GovnoTesting extends CommandOpMode {
    //WIP
    private BarnRobot farminator;
    @Override
    public void initialize() {
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X).whenPressed(
                RobotCommands.collectCommand()
        );
    }
    @Override
    public void run() {
        super.run();
        farminator.periodic();
    }
}