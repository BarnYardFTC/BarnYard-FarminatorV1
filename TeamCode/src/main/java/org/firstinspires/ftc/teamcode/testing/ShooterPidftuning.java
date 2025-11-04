package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.util.OpModeData;

/**
 * Bue TeleOp mode for the Barnyard FTC robot.
 *
 * Controls all subsystems through command-based triggers and gamepad mappings.
 *
 * Structure:
 * - Robot Initialization
 * - Gamepad Bindings (Buttons + Triggers)
 * - Periodic Updates
 */
@TeleOp(name = "pidf tuner", group = "main")
public class ShooterPidftuning extends CommandOpMode {

    // ------------------------
    // Robot Instance
    // ------------------------
    private BarnRobot farminator;

    private final double INITIAL_BOT_HEADING = 270;

    private VoltageSensor voltageSensor;

    @Override
    public void initialize() {

        // ------------------------
        // Initialize Robot Systems
        // ------------------------
        farminator = BarnRobot.getInstance();
        farminator.init(this, new OpModeData(OpModeData.AllianceColor.BLUE, 270, 270, OpModeData.OpModeType.AUTONOMOUS, LimeLight.BLUE_LOCALIZATION_PIPELINE));

        this.voltageSensor = farminator.robotHardware.voltageSensor;

        //kol ha kod
//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.A)
//                .whenPressed(farminator.shooter.setPower(0));
//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
//                .whenPressed(farminator.shooter.setPower(0.5));
//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
//                .whenPressed(farminator.shooter.setPower(1));

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(farminator.shooter.setPower(10/voltageSensor.getVoltage()));


    }

    @Override
    public void run() {
        // Run command scheduler and periodic updates
        super.run();
        farminator.telemetry.addData("motor velocity", farminator.shooter.getVelocity());
        farminator.periodic();
    }



}
