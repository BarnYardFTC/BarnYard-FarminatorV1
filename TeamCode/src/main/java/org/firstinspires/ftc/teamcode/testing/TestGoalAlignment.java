package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.OpModeData;

@TeleOp
public class TestGoalAlignment extends CommandOpMode {


    private BarnRobot farminator;

    @Override
    public void initialize() {

        // ------------------------
        // Initialize Robot Systems
        // ------------------------
        farminator = BarnRobot.getInstance();
        farminator.initBarnRobotSystems(this, new OpModeData(OpModeData.AllianceColor.BLUE));

        // ------------------------
        // Button Mappings
        // ------------------------

        // Reset heading with X
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(farminator.drive.resetHeadingCommand());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
                .whileHeld(farminator.drive.alignToTagCommand());

    }

    @Override
    public void run() {
        super.run();
        farminator.limelight.displayTelemetry();
        farminator.drive.displayPower();
        telemetry.update();
    }
}
