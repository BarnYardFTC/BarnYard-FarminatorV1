package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.GlobalData;

@TeleOp
public class TestGoalAlignment extends CommandOpMode {

    private GamepadEx gamepadEx1, gamepadEx2;

    private BarnRobot farminator;

    private GlobalData.Alliance teamColor = GlobalData.Alliance.BLUE;


    @Override
    public void initialize() {

        // ------------------------
        // Initialize Robot Systems
        // ------------------------
        farminator = BarnRobot.getInstance();
        farminator.initBarnRobotSystems(hardwareMap, gamepad1, gamepad2, teamColor);

        // ------------------------
        // Initialize Gamepads
        // ------------------------
        gamepadEx1 = farminator.gamepadEx1;
        gamepadEx2 = farminator.gamepadEx2;


        // ------------------------
        // Button Mappings
        // ------------------------

        // Reset heading with X
        gamepadEx1.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(() -> farminator.drive.resetHeading());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .toggleWhenPressed(farminator.drive.alignToTag());

    }

    @Override
    public void run() {
        super.run();
        farminator.limelight.displayTelemetry();
        telemetry.update();
    }
}
