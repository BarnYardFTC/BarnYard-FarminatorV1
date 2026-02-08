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
        farminator = BarnRobot.getInstance();

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
                .toggleWhenActive(
                        farminator.shooter.runShooter(1000)
                );

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.A).whenPressed(
                RobotCommands.collectCommand()
        );

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
                RobotCommands.collectStopCommand()
        );

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X).whenPressed(
                RobotCommands.shootAllCommand()
        );
    }
    @Override
    public void run() {
        super.run();
        farminator.periodic();
    }
}