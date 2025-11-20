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
import org.firstinspires.ftc.teamcode.subsystems.Intake;
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

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X) // Run transfer + intake
                .whenPressed(farminator.transfer.setEntireTransferPowerCommand(Transfer.DEFAULT_TRANSFER_POWER))
                .whenPressed(farminator.intake.activateIntakeCommand())
                .whenInactive(farminator.transfer.setEntireTransferPowerCommand(0))
                .whenInactive(farminator.intake.deactivateIntakeCommand());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B) // Run transfer + intake backwards
                .whenPressed(farminator.transfer.setEntireTransferPowerCommand(-Transfer.DEFAULT_TRANSFER_POWER))
                .whenPressed(farminator.intake.customIntakeCommand(-Intake.DEFAULT_POWER))
                .whenInactive(farminator.transfer.setEntireTransferPowerCommand(0))
                .whenInactive(farminator.intake.deactivateIntakeCommand());

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
