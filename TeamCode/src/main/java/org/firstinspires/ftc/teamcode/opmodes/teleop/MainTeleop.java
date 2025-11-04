package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.OpModeData;

/**
 * Main TeleOp mode for the Barnyard FTC robot.
 *
 * Handles all subsystem controls via command-based triggers and gamepad mappings.
 *
 * Structure:
 * - Robot initialization
 * - Gamepad 1 and Gamepad 2 button/trigger mappings
 * - Periodic updates
 */
@TeleOp(name = "Main Teleop", group = "main")
public class MainTeleop extends CommandOpMode {

    /** Singleton instance of the robot. */
    private BarnRobot farminator;

    /**
     * Initializes all robot subsystems and maps gamepad buttons to commands.
     */
    @Override
    public void initialize() {
        // Initialize robot systems
        farminator = BarnRobot.getInstance();
        farminator.init(this, new OpModeData(
                OpModeData.AllianceColor.BLUE,250, 270, OpModeData.OpModeType.TELEOP
                ));

        // -----------------------------------------------------
        // Gamepad 1 Mappings
        // -----------------------------------------------------

        /* Shooter Control. */
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(farminator.shooter.activateShooterCommand())
                .whenInactive(farminator.shooter.deactivateShooterCommand());

        /* Transfer Controls */
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(farminator.transfer.activateFrontTransferCommand())
                .whenInactive(farminator.transfer.deactivateFrontTransferCommand());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
                .whenPressed(farminator.transfer.activateBackTransferCommand())
                .whenInactive(farminator.transfer.deactivateBackTransferCommand());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(farminator.transfer.activateBackTransferCommand(-1))
                .whenInactive(farminator.transfer.activateBackTransferCommand(0));

        /* Intake Controls */
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.A)
                .whenActive(farminator.intake.activateIntakeCommand())
                .whenInactive(farminator.intake.deactivateIntakeCommand());

        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0)
                .whenActive(farminator.intake.customIntakeCommand(
                        farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)))
                .whenInactive(farminator.intake.deactivateIntakeCommand());

        /* Drive Controls */
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .toggleWhenActive(
                        new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateSlowMode()),
                        new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateFastMode())
                );

        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.05)
                .whenActive(farminator.drive.alignToTagCommand())
                .whenInactive(farminator.drive.driveCommand());

        // -----------------------------------------------------
        // Gamepad 2 Mappings
        // -----------------------------------------------------
        // TODO: Add Gamepad 2 controls here
    }

    /**
     * Runs the command scheduler and updates periodic robot logic.
     */
    @Override
    public void run() {
        super.run();
        farminator.periodic();
    }
}
