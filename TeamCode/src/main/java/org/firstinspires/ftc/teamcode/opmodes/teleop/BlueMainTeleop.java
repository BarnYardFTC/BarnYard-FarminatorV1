package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.util.OpModeData;

/**
 * BlueMainTeleop
 *
 * Main TeleOp mode for the Barnyard FTC robot (Blue Alliance).
 * Controls all robot subsystems using the command-based architecture.
 *
 * Structure:
 * - Initialization (robot setup)
 * - Gamepad mappings
 * - Periodic updates
 */
@TeleOp(name = "BlueMainTeleop", group = "main")
@Config
public class BlueMainTeleop extends CommandOpMode {

    // ------------------------
    // Robot Instance
    // ------------------------
    private BarnRobot farminator;

    private static final double INITIAL_BOT_HEADING = 270;

    @Override
    public void initialize() {

        Pose2d autoFinishPose = OpModeData.getAutoFinishPose(); // use the pose in which the auto has ended
        OpModeData opModeData = new OpModeData(
                OpModeData.AllianceColor.BLUE,
                OpModeData.OpModeType.TELEOP,
                LimeLight.BLUE_LOCALIZATION_PIPELINE,
                autoFinishPose
        );

        // ==========================================================
        // Robot Initialization
        // ==========================================================
        farminator = BarnRobot.getInstance();
        farminator.init(
                this,
                opModeData
        );
        farminator.limelight.switchPipeline(LimeLight.BLUE_LOCALIZATION_PIPELINE);

        // ==========================================================
        // Gamepad 1 Controls
        // ==========================================================

        // ------------------------
        // Transfer System
        // ------------------------

        // Left Bumper → Run back transfer backward
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(farminator.transfer.setBackPowerCommand(-Transfer.DEFAULT_TRANSFER_POWER))
                .whenInactive(farminator.transfer.setBackPowerCommand(0));

        // Right Bumper → Run all transfer motors forward
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(farminator.transfer.setEntireTransferPowerCommand(Transfer.DEFAULT_TRANSFER_POWER))
                .whenInactive(farminator.transfer.setEntireTransferPowerCommand(0));

        // Left Trigger → Intake active (transfer + intake)
        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0)
                .whenActive(new ParallelCommandGroup(
                        farminator.transfer.setEntireTransferPowerCommand(Transfer.DEFAULT_TRANSFER_POWER),
                        farminator.intake.activateIntakeCommand()
                ))
                .whenInactive(new ParallelCommandGroup(
                        farminator.transfer.setEntireTransferPowerCommand(0),
                        farminator.intake.deactivateIntakeCommand()
                ));

        // Right Trigger → Shooter active
        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0)
                .whenActive(farminator.shooter.activateShooterCommand())
                .whenInactive(farminator.shooter.deactivateShooterCommand());

        // ------------------------
        // Drive System
        // ------------------------

        // Right Stick Button → Toggle between slow and fast drive modes
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_STICK_BUTTON)
                .toggleWhenActive(
                        new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateSlowMode()),
                        new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateFastMode())
                );
    }

    @Override
    public void run() {
        // ==========================================================
        // Periodic Updates
        // ==========================================================

        super.run();
        farminator.periodic();
    }
}
