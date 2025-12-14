package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
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
@TeleOp(name = "TestTeleop", group = "test")
@Config
public class TestTeleop extends CommandOpMode {

    // ------------------------
    // Robot Instance
    // ------------------------
    private BarnRobot farminator;

    @Override
    public void initialize() {

        OpModeData opModeData = new OpModeData(
                OpModeData.AllianceColor.BLUE,
                OpModeData.OpModeType.TELEOP,
                LimeLight.BLUE_LOCALIZATION_PIPELINE,
                new Pose2d(62.5, -60.5, Math.toRadians(270)),
                270
        );

        // ==========================================================
        // Robot Initialization
        // ==========================================================
        farminator = BarnRobot.getInstance();
        farminator.init(
                this,
                opModeData
        );
        farminator.shooterHood.setDefaultCommand(farminator.shooterHood.autoHoodAlignment());


        // ==========================================================
        // Gamepad 1 Controls
        // ==========================================================

        // ------------------------
        // Transfer System
        // ------------------------

        // Left Bumper → Run back transfer backward
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(
                        new ParallelCommandGroup(
                                farminator.transfer.setEntireTransferPowerCommand(-1 * Transfer.DEFAULT_TRANSFER_POWER)
                        )
                )
                .whenInactive(farminator.transfer.setEntireTransferPowerCommand(0));

        // Right Bumper → Run all transfer motors forward
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(farminator.transfer.setEntireTransferPowerCommand(Transfer.DEFAULT_TRANSFER_POWER))
                .whenInactive(farminator.transfer.setEntireTransferPowerCommand(0));


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenPressed(farminator.shooterHood.lower());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(farminator.shooterHood.raise());


        // Left Trigger → Intake active (transfer + intake)
        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0)
                .whenActive(new ParallelCommandGroup(
                        farminator.intake.activateIntakeCommand()
                ))
                .whenInactive(new ParallelCommandGroup(
                        farminator.intake.deactivateIntakeCommand()
                ));

        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0)
                .whenActive(new ParallelCommandGroup(
                        farminator.intake.customIntakeCommand(-0.5)
                ))
                .whenInactive(new ParallelCommandGroup(
                        farminator.intake.deactivateIntakeCommand()
                ));


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whileHeld(farminator.shooterHood.goToPositionCommand());



        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
                .toggleWhenActive(
                        farminator.shooter.runShooterBasedOnDistance(),
                        farminator.shooter.turnOff());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.A)
                        .toggleWhenActive(
                                farminator.drive.alignToTagCommand(),
                                farminator.drive.driveCommand()
                        );


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_STICK_BUTTON)
                .toggleWhenActive(
                        new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateSlowMode()),
                        new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateFastMode())
                );

        // Right Stick Button → Toggle between slow and fast drive modes
        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.A)
                .toggleWhenActive(
                        new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateSlowMode()),
                        new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateFastMode())
                );

        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(farminator.drive.updatePinpointPose(new Pose2d(62.5, -60.5, Math.toRadians(270))));

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_STICK_BUTTON)
                .whenPressed(farminator.drive.updatePinpointPose(new Pose2d(62.5, -60.5, Math.toRadians(270))));

    }

    @Override
    public void run() {
        super.run();
        telemetry.addData("x", farminator.pinpointLocalizer.getPose().position.x * 0.0254);
        telemetry.addData("y", farminator.pinpointLocalizer.getPose().position.y * 0.0254);
        telemetry.addData("heading", farminator.drive.getBotAbsoluteHeading());
        telemetry.addData("distance from goal", farminator.drive.getDistanceFromGoal());
        telemetry.addData("shooter velocity", farminator.shooter.getVelocity());
        farminator.shooterHood.displayTelemetry();
        farminator.periodic();
    }
}

