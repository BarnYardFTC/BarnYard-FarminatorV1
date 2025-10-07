package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.GlobalData;

@TeleOp
public class TeleopDefault extends CommandOpMode {

    private GamepadEx gamepadEx1, gamepadEx2;

    private BarnRobot farminator;

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

    }

    @Override
    public void run() {
        super.run();
        telemetry.update();
    }
}
