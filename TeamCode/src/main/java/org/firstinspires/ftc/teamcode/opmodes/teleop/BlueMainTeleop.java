package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.button.Trigger;
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
@TeleOp(name = "BlueMainTeleop", group = "main")
public class BlueMainTeleop extends CommandOpMode {

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
        farminator.init(this, new OpModeData(OpModeData.AllianceColor.BLUE, 270, 270, OpModeData.OpModeType.AUTONOMOUS, LimeLight.BLUE_LOCALIZATION_PIPELINE));
        farminator.limelight.switchPipeline(LimeLight.BLUE_LOCALIZATION_PIPELINE);

        // -----------------------------------------------------
        // Gamepad 1 Mappings
        // -----------------------------------------------------

        /* ========== Transfer Controls ========== */
        // B: Front transfer forward
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(farminator.transfer.activateFrontTransferCommand())
                .whenInactive(farminator.transfer.deactivateFrontTransferCommand());

        // Y: Back transfer forward
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(farminator.transfer.activateBackTransferCommand())
                .whenInactive(farminator.transfer.deactivateBackTransferCommand());


        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0)
                .whenActive(new ParallelCommandGroup(
                        farminator.transfer.activateTransferCommand(),
                        farminator.intake.activateIntakeCommand())
                )
                .whenInactive(new ParallelCommandGroup(
                        farminator.transfer.deactivateTransferCommand(),
                        farminator.intake.deactivateIntakeCommand()
                ));

        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0)
                .whenActive(farminator.shooter.activateShooterCommand())
                .whenInactive(farminator.shooter.deactivateShooterCommand());

        /* ========== Intake Controls ========== */
        // A: Activate/deactivate intake
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.A)
                .whenActive(farminator.drive.alignToTagCommand())
                .whenInactive(farminator.drive.driveCommand());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_STICK_BUTTON)
                .whenActive(farminator.transfer.activateBackTransferCommand(-1))
                .whenInactive(farminator.transfer.activateBackTransferCommand(0));

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_STICK_BUTTON)
                .toggleWhenActive(
                        new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateSlowMode()),
                        new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateFastMode())
                );

    }

    @Override
    public void run() {
        // Run command scheduler and periodic updates
        super.run();
        farminator.periodic();
    }
}
