package org.firstinspires.ftc.teamcode.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;

@TeleOp
public class TeleopDefault extends CommandOpMode {

    private GamepadEx gamepadEx1, gamepadEx2;

    private BarnRobot farminator;

    private static final double SLOW_MODE_TRIGGER_THRESHOLD = 0.05;


    @Override
    public void initialize() {

        // ------------------------
        // Initialize Robot Systems
        // ------------------------
        farminator = BarnRobot.getInstance();
        farminator.initBarnRobotSystems(hardwareMap, gamepad1, gamepad2);

        // ------------------------
        // Initialize Gamepads
        // ------------------------
        gamepadEx1 = farminator.gamepadEx1;
        gamepadEx2 = farminator.gamepadEx2;


        // ------------------------
        // Button Mappings
        // ------------------------

        // Left trigger toggles between slow and fast mode
        Trigger leftTriggerCondition = new Trigger(
                () -> gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > SLOW_MODE_TRIGGER_THRESHOLD
        )
                .whenActive(new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateSlowMode()))
                .whenInactive(new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateFastMode()));

        // Reset heading with X
        gamepadEx1.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(() -> farminator.drive.resetHeading());

        // Y for transfer
         farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
                 .whenPressed(farminator.transfer.transferCommand());


    }

    @Override
    public void run() {
        super.run();
        telemetry.update();
    }
}
