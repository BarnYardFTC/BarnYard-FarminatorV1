package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Button;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;

@TeleOp
public class TestTransTeleop extends CommandOpMode {

    private BarnRobot farminator;

    @Override
    public void initialize() {

        farminator = BarnRobot.getInstance();
        farminator.initBarnRobotSystems(hardwareMap, gamepad1, gamepad2);

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
                .whenPressed(farminator.transfer.transferCommand());

    }

    @Override
    public void run() {
        super.run();
        telemetry.update();
    }
}
