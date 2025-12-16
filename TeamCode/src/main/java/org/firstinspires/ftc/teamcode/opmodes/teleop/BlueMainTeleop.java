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
        tenSecondCount = 0;
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


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.A)
                .toggleWhenActive(
                        farminator.drive.alignToTagCommand(),
                        farminator.drive.driveCommand()
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

        // Every 10 seconds
        if (now - lastTickTime >= 10.0) {
            tenSecondCount++;
            lastTickTime = now;
        }

        // After 10 ticks (≈100 seconds)
        if (!hasRumbled && tenSecondCount >= 10) {

            // 🔒 SAFEST OPTION: rumble ONE gamepad
            gamepad2.rumble(1.0, 1.0, 250);

            // If you REALLY want both (riskier):
            // gamepad2.rumble(1.0, 1.0, 150);

            hasRumbled = true;
        }
    }


    private void reportHeldButtons() {

        StringBuilder gp1 = new StringBuilder();
        StringBuilder gp2 = new StringBuilder();

        // -------- Gamepad 1 --------
        if (gamepad1.a) gp1.append("A ");
        if (gamepad1.b) gp1.append("B ");
        if (gamepad1.x) gp1.append("X ");
        if (gamepad1.y) gp1.append("Y ");

        if (gamepad1.left_bumper) gp1.append("LB ");
        if (gamepad1.right_bumper) gp1.append("RB ");

        if (gamepad1.left_stick_button) gp1.append("LSB ");
        if (gamepad1.right_stick_button) gp1.append("RSB ");

        if (gamepad1.dpad_up) gp1.append("DPAD_UP ");
        if (gamepad1.dpad_down) gp1.append("DPAD_DOWN ");
        if (gamepad1.dpad_left) gp1.append("DPAD_LEFT ");
        if (gamepad1.dpad_right) gp1.append("DPAD_RIGHT ");

        if (gamepad1.left_trigger > 0.1) gp1.append("LT ");
        if (gamepad1.right_trigger > 0.1) gp1.append("RT ");

        // -------- Gamepad 2 --------
        if (gamepad2.a) gp2.append("A ");
        if (gamepad2.b) gp2.append("B ");
        if (gamepad2.x) gp2.append("X ");
        if (gamepad2.y) gp2.append("Y ");

        if (gamepad2.left_bumper) gp2.append("LB ");
        if (gamepad2.right_bumper) gp2.append("RB ");

        if (gamepad2.left_stick_button) gp2.append("LSB ");
        if (gamepad2.right_stick_button) gp2.append("RSB ");

        if (gamepad2.dpad_up) gp2.append("DPAD_UP ");
        if (gamepad2.dpad_down) gp2.append("DPAD_DOWN ");
        if (gamepad2.dpad_left) gp2.append("DPAD_LEFT ");
        if (gamepad2.dpad_right) gp2.append("DPAD_RIGHT ");

        if (gamepad2.left_trigger > 0.1) gp2.append("LT ");
        if (gamepad2.right_trigger > 0.1) gp2.append("RT ");

        telemetry.addData("Held GP1", gp1.length() == 0 ? "none" : gp1.toString());
        telemetry.addData("Held GP2", gp2.length() == 0 ? "none" : gp2.toString());
    }

}
