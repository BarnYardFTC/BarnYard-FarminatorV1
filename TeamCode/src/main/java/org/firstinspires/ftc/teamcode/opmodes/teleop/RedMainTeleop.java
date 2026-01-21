package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.subsystems.Webcam;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;

/**
 * RedMainTeleop
 *
 * Main TeleOp mode for the Barnyard FTC robot (Red Alliance).
 * Mirrors BlueMainTeleop with alliance-specific changes only.
 */
@TeleOp(name = "Red Main Teleop", group = "main")
@Config
public class RedMainTeleop extends CommandOpMode {

    // ------------------------
    // Robot Instance
    // ------------------------
    private BarnRobot farminator;

    private ElapsedTime opModeTimer;
    private int tenSecondCount = 0;
    private double lastTickTime = 0;
    private boolean hasRumbled = false;
    private int lastRumbleSecond = -1;

    @Override
    public void initialize() {

        Pose2d autoFinishPose = OpModeData.getAutoFinishPose();
        OpModeData opModeData = new OpModeData(
                OpModeData.AllianceColor.RED,
                OpModeData.OpModeType.TELEOP,
                autoFinishPose,
                90
        );

        hasRumbled = false;
        lastRumbleSecond = 0;
        lastTickTime = 0;

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
        // Gamepad 1 Controls
        // ==========================================================

        // ------------------------
        // Transfer System
        // ------------------------

        // Left Bumper → Run back transfer backward
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(
                        new ParallelCommandGroup(
                        )
                );
//
//
//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
//                .whenPressed(farminator.shooterHood.lower());
//
//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
//                .whenPressed(farminator.shooterHood.raise());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(new DriveActionCommand(farminator.roadRunnerMecanumDrive.actionBuilder(new Pose2d(farminator.pinpointLocalizer.getPose().position.x,farminator.pinpointLocalizer.getPose().position.y,farminator.pinpointLocalizer.getPose().heading.toDouble()))
                        .strafeToLinearHeading(new Vector2d(36.5,-33),farminator.pinpointLocalizer.getPose().heading.toDouble())));

        // Right Trigger → Intake forward
        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0)
                .whenActive(
                        new ParallelCommandGroup(
                                farminator.intake.activateIntakeCommand()
                        )
                )
                .whenInactive(
                        new ParallelCommandGroup(
                                farminator.intake.deactivateIntakeCommand()
                        )
                );

        // Left Trigger → Intake reverse
        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0)
                .whenActive(
                        new ParallelCommandGroup(
                                farminator.intake.customIntakeCommand(-0.5)
                        )
                )
                .whenInactive(
                        new ParallelCommandGroup(
                                farminator.intake.deactivateIntakeCommand()
                        )
                );

        // Shooter toggle
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
                .toggleWhenActive(
                        farminator.shooter.runShooterBasedOnDistance(),
                        farminator.shooter.turnOff()
                );

        // ==========================================================
        // Gamepad 2 Controls
        // ==========================================================

        // Toggle slow / fast drive
        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.B)
                .toggleWhenActive(
                        new InstantCommand(() ->
                                farminator.drive.mecanumDriveComponent.activateSlowMode()
                        ),
                        new InstantCommand(() ->
                                farminator.drive.mecanumDriveComponent.activateFastMode()
                        )
                );

        // Align to tag toggle
        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.A)
                .toggleWhenActive(
                        farminator.drive.alignToTagCommand(),
                        farminator.drive.driveOneDriverCommand()
                );

        // Update pinpoint pose (field-specific)
        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(
                        farminator.drive.updatePinpointPose(
                                new Pose2d(62, 60, Math.toRadians(90))
                        )
                );

        // Duplicate slow/fast toggle on GP1 right stick
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_STICK_BUTTON)
                .toggleWhenActive(
                        new InstantCommand(() ->
                                farminator.drive.mecanumDriveComponent.activateSlowMode()
                        ),
                        new InstantCommand(() ->
                                farminator.drive.mecanumDriveComponent.activateFastMode()
                        )
                );

        // Reset pose
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_STICK_BUTTON)
                .whenPressed(
                        farminator.drive.updatePinpointPose(
                                new Pose2d(0, 0, Math.toRadians(90))
                        )
                );

        // Close hood + constant shooter distance
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
                .toggleWhenPressed(
                        new ParallelCommandGroup(
                                farminator.shooterHood.setHoodCloseToGoalPos(),
                                farminator.shooter.runShooterBasedOnConstantDistance(
                                        DriveTrain.GOAL_ROBOT_MIN_DISTANCE
                                )
                        ),
                        farminator.shooterHood.autoHoodAlignment()
                );
    }

    @Override
    public void run() {
        super.run();
        farminator.periodic();
        rumbleGamepadsEndgame();
    }

    private void rumbleGamepadsEndgame() {

        double now = opModeTimer.seconds();

        // Endgame window: last 20 seconds (100s → 120s)
        if (now >= 100 && now <= 120) {

            int currentSecond = (int) now;

            // Rumble once per second
            if (currentSecond != lastRumbleSecond) {

                gamepad2.rumble(1.0, 1.0, 200);
                lastRumbleSecond = currentSecond;
            }
        }
    }
}
