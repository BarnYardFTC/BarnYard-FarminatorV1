package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
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
 * RedMainTeleop
 *
 * Main TeleOp mode for the Barnyard FTC robot (Red Alliance).
 */
@TeleOp(name = "Red Main Teleop", group = "main")
@Config
public class RedMainTeleop extends CommandOpMode {

    // ------------------------
    // Robot Instance
    // ------------------------
    private BarnRobot farminator;

    private ElapsedTime opModeTimer;
    private boolean hasRumbled = false;

    @Override
    public void initialize() {

        Pose2d autoFinishPose = OpModeData.getAutoFinishPose();
        OpModeData opModeData = new OpModeData(
                OpModeData.AllianceColor.RED,
                OpModeData.OpModeType.TELEOP,
                LimeLight.RED_LOCALIZATION_PIPELINE,
                autoFinishPose,
                90
        );


        hasRumbled = false;
        opModeTimer = new ElapsedTime();
        opModeTimer.reset();

        // ==========================================================
        // Robot Initialization
        // ==========================================================
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        farminator.shooterHood.setDefaultCommand(
                farminator.shooterHood.autoHoodAlignment()
        );

        // ==========================================================
        // Gamepad mappings
        // ==========================================================

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(
                        new ParallelCommandGroup(
                                farminator.transfer.setEntireTransferPowerCommand(-Transfer.DEFAULT_TRANSFER_POWER)
                        )
                )
                .whenInactive(farminator.transfer.setEntireTransferPowerCommand(0));

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(
                        farminator.transfer.setEntireTransferPowerCommand(Transfer.DEFAULT_TRANSFER_POWER)
                )
                .whenInactive(farminator.transfer.setEntireTransferPowerCommand(0));

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenPressed(farminator.shooterHood.lower());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(farminator.shooterHood.raise());

        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0)
                .whenActive(farminator.intake.activateIntakeCommand())
                .whenInactive(farminator.intake.deactivateIntakeCommand());

        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0)
                .whenActive(farminator.intake.customIntakeCommand(-0.5))
                .whenInactive(farminator.intake.deactivateIntakeCommand());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
                .toggleWhenActive(
                        farminator.shooter.runShooterBasedOnDistance(),
                        farminator.shooter.turnOff()
                );

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.A)
                .toggleWhenActive(
                        farminator.drive.alignToTagCommand(),
                        farminator.drive.driveCommand()
                );

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_STICK_BUTTON)
                .toggleWhenActive(
                        new InstantCommand(() ->
                                farminator.drive.mecanumDriveComponent.activateSlowMode()
                        ),
                        new InstantCommand(() ->
                                farminator.drive.mecanumDriveComponent.activateFastMode()
                        )
                );

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_STICK_BUTTON)
                .whenPressed(
                        farminator.drive.updatePinpointPose(
                                new Pose2d(0, 0, Math.toRadians(270))
                        )
                );
    }

    @Override
    public void run() {
        super.run();
        telemetry.addData("x", BarnRobot.getInstance().pinpointLocalizer.getPose().position.x);
        telemetry.addData("y", BarnRobot.getInstance().pinpointLocalizer.getPose().position.y);
        telemetry.addData("absolute heading", farminator.drive.getBotAbsoluteHeading());
        rumbleGamepadsEndgame();
        farminator.periodic();
    }

    private void rumbleGamepadsEndgame() {
        if (!hasRumbled && opModeTimer.seconds() >= 100) {
            gamepad1.rumble(1.0, 1.0, 2000);
            gamepad2.rumble(1.0, 1.0, 2000);
            hasRumbled = true;
        }
    }
}
