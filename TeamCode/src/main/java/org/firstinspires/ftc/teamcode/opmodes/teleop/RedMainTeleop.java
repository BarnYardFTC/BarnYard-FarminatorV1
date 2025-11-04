package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
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
@TeleOp(name = "RedMainTeleop", group = "main")
public class RedMainTeleop extends CommandOpMode {

    // ------------------------
    // Robot Instance
    // ------------------------
    private BarnRobot farminator;

    private final double INITIAL_BOT_HEADING = 270;

    @Override
    public void initialize() {

        // ------------------------
        // Initialize Robot Systems
        // ------------------------
        farminator = BarnRobot.getInstance();
        //TODO: Put the heading in which the autonomous has ended in initialBotHeading
        farminator.init(this, new OpModeData(OpModeData.AllianceColor.BLUE, 270, 270, OpModeData.OpModeType.TELEOP));

        //TODO: Map buttons properly

        // -----------------------------------------------------
        // Gamepad 1 Mappings
        // -----------------------------------------------------

        /* ========== Shooter Controls ========== */
        // X: Activate/deactivate shooter
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(farminator.shooter.activateShooterCommand())
                .whenInactive(farminator.shooter.deactivateShooterCommand());

        /* ========== Transfer Controls ========== */
        // B: Front transfer forward
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(farminator.transfer.activateFrontTransferCommand())
                .whenInactive(farminator.transfer.deactivateFrontTransferCommand());

        // Y: Back transfer forward
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
                .whenPressed(farminator.transfer.activateBackTransferCommand())
                .whenInactive(farminator.transfer.deactivateBackTransferCommand());

        // D-Pad Down: Back transfer reverse
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(farminator.transfer.activateBackTransferCommand(-1))
                .whenInactive(farminator.transfer.activateBackTransferCommand(0));

        /* ========== Intake Controls ========== */
        // A: Activate/deactivate intake
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.A)
                .whenActive(farminator.intake.activateIntakeCommand())
                .whenInactive(farminator.intake.deactivateIntakeCommand());

        // Left Trigger: Variable intake speed based on trigger pressure
        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0)
                .whenActive(farminator.intake.customIntakeCommand(
                        farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)))
                .whenInactive(farminator.intake.deactivateIntakeCommand());

        /* ========== Drive Controls ========== */
        // D-Pad Up: Toggle between slow and fast drive modes
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .toggleWhenActive(
                        new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateSlowMode()),
                        new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateFastMode())
                );

        // Right Trigger: activate yaw goal alignment
        new Trigger(
                () -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.05
        )
                .whenActive(farminator.drive.alignToTagCommand())
                .whenInactive(farminator.drive.driveCommand()); //TODO: Check tag alignment


        // -----------------------------------------------------
        // Gamepad 2 Mappings
        // -----------------------------------------------------

    }

    @Override
    public void run() {
        // Run command scheduler and periodic updates
        super.run();
        farminator.periodic();
    }
}
