package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.OpModeData;

@TeleOp
public class TestShooter extends CommandOpMode {
    private BarnRobot farminator;

    @Override
    public void initialize() {

        // ------------------------
        // Initialize Robot Systems
        // ------------------------
        farminator = BarnRobot.getInstance();
        farminator.init(this, new OpModeData());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.A)
                .whenActive(farminator.shooter.activateShooterCommand())
                .whenInactive(farminator.shooter.deactivateShooterCommand());


    }

    @Override
    public void run() {
        super.run();
        telemetry.update();
    }
}
