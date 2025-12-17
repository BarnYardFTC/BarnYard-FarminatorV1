package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
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
@TeleOp(name = "Blue Main Teleop", group = "main")
@Config
public class BlueMainTeleop extends CommandOpMode {

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

        Pose2d autoFinishPose = OpModeData.getAutoFinishPose(); // use the pose in which the auto has ended
        OpModeData opModeData = new OpModeData(
                OpModeData.AllianceColor.BLUE,
                OpModeData.OpModeType.TELEOP,
                LimeLight.BLUE_LOCALIZATION_PIPELINE,
                autoFinishPose,
                270
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



        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
                .toggleWhenActive(
                        farminator.shooter.runShooterBasedOnDistance(),
                        farminator.shooter.turnOff());




        // Right Stick Button → Toggle between slow and fast drive modes
        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.B)
                .toggleWhenActive(
                        new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateSlowMode()),
                        new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateFastMode())
                );


        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.A)
                .toggleWhenActive(
                        farminator.drive.alignToTagCommand(),
                        farminator.drive.driveCommand()
                );
        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(farminator.drive.updatePinpointPose(new Pose2d(0,0,Math.toRadians(270))));


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_STICK_BUTTON)
                .toggleWhenActive(
                        new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateSlowMode()),
                        new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateFastMode())
                );


        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(farminator.drive.resetPinpointTracking());


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_STICK_BUTTON)
                .whenPressed(farminator.drive.updatePinpointPose(new Pose2d(0,0,Math.toRadians(270))));



    }

    @Override
    public void run() {
        super.run();
        farminator.periodic();
        rumpleGamepadsEndgame();
    }

    private void rumpleGamepadsEndgame() {

        double now = opModeTimer.seconds();

        // Endgame window: last 20 seconds (100s → 120s)
        if (now >= 100 && now <= 120) {

            int currentSecond = (int) now;

            // Rumble once per second
            if (currentSecond != lastRumbleSecond) {

                // Short, clear pulse
//                gamepad1.rumble(1.0, 1.0, 200 );
                gamepad2.rumble(1.0, 1.0, 200);

                lastRumbleSecond = currentSecond;
            }
        }
    }



}
