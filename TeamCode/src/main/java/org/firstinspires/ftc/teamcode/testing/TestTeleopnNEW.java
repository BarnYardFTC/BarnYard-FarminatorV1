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
@TeleOp(name = "Test Teleop new", group = "test")
@Config
public class TestTeleopnNEW extends CommandOpMode {


    // ------------------------
    // Robot Instance
    // ------------------------
    private BarnRobot farminator;

    @Override
    public void initialize() {

        OpModeData opModeData = new OpModeData(
                OpModeData.AllianceColor.BLUE,
                OpModeData.OpModeType.TELEOP,
                new Pose2d(0, 0, Math.toRadians(180)),
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
        farminator.shooterHood.setDefaultCommand(farminator.shooterHood.setCustomDashboardPos());
        farminator.drive.setDefaultCommand(farminator.drive.driveOneDriverCommand());
        farminator.shooter.setDefaultCommand(farminator.shooter.turnOff());


        // ==========================================================
        // Gamepad 1 Controls
        // ==========================================================

        // ------------------------
        // Transfer System
        // ------------------------


        // Left Trigger → Intake active (transfer + intake)
        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0)
                .whenActive(
                        new ParallelCommandGroup(
                                farminator.intake.activateIntakeCommand(),
                                farminator.transfer.activateTransfer(),
                                BarnRobot.getInstance().gate.closeCommand()
                        )
                )
                .whenInactive(
                        CommandGroup.deactivateIntakeAndTransferCommand().alongWith(
                                BarnRobot.getInstance().gate.closeCommand()
                        )
                );

        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0)
                .whenActive(new ParallelCommandGroup(
                        farminator.intake.customIntakeCommand(-1),
                        BarnRobot.getInstance().gate.closeCommand()
                ))
                .whenInactive(new ParallelCommandGroup(
                        farminator.intake.deactivateIntakeCommand(),
                        BarnRobot.getInstance().gate.closeCommand()
                ));

//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
//                .toggleWhenActive(
//                        farminator.drive.maintainPosCommand(gamepad1.left_stick_x, farminator.pinpointLocalizer.getPose())
//                );
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenHeld(new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateSlowMode()))
                .whenReleased(new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateFastMode()));



        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_STICK_BUTTON)
                .toggleWhenActive(
                        new InstantCommand(() -> farminator.drive.setDefaultCommand(farminator.drive.maintainPosCommand())),
                        new InstantCommand(() -> farminator.drive.setDefaultCommand(farminator.drive.driveNonFieldOrientedCommand()))
                );

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(farminator.kickStand.raiseCommand());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(farminator.kickStand.lowerCommand());


//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
//                .toggleWhenActive(
//                        farminator.drive.alignToTagCommand());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(
                new InstantCommand(() -> farminator.pinpointLocalizer.setPose(new Pose2d(0,0,Math.toRadians(270)))));




        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(CommandGroup.smartShootCommand());


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
                .toggleWhenPressed(
                        farminator.shooter.turnOff(),
                        farminator.shooter.runShooterBasedOnDistance()
                );
    }

    @Override
    public void run() {
        super.run();
        telemetry.addLine("===PINPOINT DATA===");
        farminator.drive.displayPinpointDataTelemetry();
        telemetry.addLine("===WEBCAM DATA===");
        farminator.webcam.displayTelemetry();

        farminator.periodic();
    }

    private InstantCommand rumbleCommand() {
        return new InstantCommand(() -> gamepad1.rumble(200));
    }
}

