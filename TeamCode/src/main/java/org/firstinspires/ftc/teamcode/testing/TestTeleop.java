package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.CommandGroup;
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
                new Pose2d(0, 0, Math.toRadians(270)),
                180
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
        farminator.drive.setDefaultCommand(farminator.drive.driveTwoDriversCommand());


        // ==========================================================
        // Gamepad 1 Controls
        // ==========================================================

        // ------------------------
        // Transfer System
        // ------------------------






        // Left Trigger → Intake active (transfer + intake)
        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0)
                .whenActive(new ParallelCommandGroup(
                    CommandGroup.intakeAndTransferCommand()
                ))
                .whenInactive(new ParallelCommandGroup(
                        CommandGroup.deactivateIntakeAndTransferCommand ()
                ));

        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0)
                .whenActive(new ParallelCommandGroup(
                        farminator.intake.customIntakeCommand(-0.5)
                ))
                .whenInactive(new ParallelCommandGroup(
                        farminator.intake.deactivateIntakeCommand()
                ));

//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
//                .toggleWhenActive(
//                        farminator.drive.maintainPosCommand(gamepad1.left_stick_x, farminator.pinpointLocalizer.getPose())
//                );


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_STICK_BUTTON)
                .toggleWhenActive(
                        new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateSlowMode()),
                        new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateFastMode())
                );

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
                        .toggleWhenActive(
                                farminator.drive.alignToTagCommand());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(CommandGroup.shootCommand());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X).whenPressed(
                new InstantCommand(() -> farminator.pinpointLocalizer.setPose(new Pose2d(0,0,Math.toRadians(270)))));

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .toggleWhenPressed(farminator.shooterHood.goToPositionCommand());
    }

    @Override
    public void run() {
        super.run();
        farminator.shooterHood.displayTelemetry();
        farminator.drive.displayPinpointDataTelemetry();
        telemetry.addData("shooter sensor distance", farminator.shooterColorSensor.getArtifactDistance());
        telemetry.addData("mid sensor distance", farminator.midColorSensor.getArtifactDistance());
        telemetry.addData("intake sensor distance", farminator.intakeColoseSensor.getArtifactDistance());
        farminator.periodic();
    }
}

