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

@TeleOp(name = "LamLam", group = "test")
@Config
public class LimelightTestTeleop extends CommandOpMode {


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
        farminator.shooterHood.setDefaultCommand(farminator.shooterHood.defaultHoodCommand());
        farminator.drive.setDefaultCommand(farminator.drive.driveOneDriverCommand());
        farminator.shooter.setDefaultCommand(farminator.shooter.runShooterBasedOnDistance());


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


//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
//                .toggleWhenActive(
//                        farminator.drive.alignToTagCommand());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(
                new InstantCommand(() -> farminator.pinpointLocalizer.setPose(new Pose2d(0,0,Math.toRadians(270)))));

//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
//                .toggleWhenPressed(farminator.shooterHood.goToPositionCommand());


//mb needed
//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
//                .whenPressed(new InstantCommand(() -> farminator.shooterHood.raise()).alongWith(
//                        BarnRobot.getInstance().gate.closeCommand()
//                ));
//
//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
//                .whenPressed(new InstantCommand(() -> farminator.shooterHood.lower()).alongWith(
//                        BarnRobot.getInstance().gate.closeCommand()
//                ));

//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.A)
//                .whenPressed(new ParallelCommandGroup(
//                        CommandGroup.shootCommandPreset(1),
//                        rumbleCommand()
//                ).alongWith(
//                        BarnRobot.getInstance().gate.closeCommand()
//                ));

//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
//                .whenPressed(new ParallelCommandGroup(
//                        CommandGroup.shootCommandPreset(2),
//                        rumbleCommand()
//                ).alongWith(
//                        BarnRobot.getInstance().gate.closeCommand()
//                ));

//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
//                .whenPressed(new ParallelCommandGroup(
//                        CommandGroup.shootCommandPreset(3),
//                        rumbleCommand()
//                ).alongWith(
//                        BarnRobot.getInstance().gate.closeCommand()
//                ));

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(CommandGroup.shootCommand());


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
                .toggleWhenPressed(
                        farminator.shooter.turnOff(),
                        farminator.shooter.runShooterBasedOnDistance()
                );

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
                .toggleWhenPressed(farminator.drive.alignToTagCommand());
    }

    @Override
    public void run() {
        super.run();
        farminator.shooterHood.displayTelemetry();
        farminator.drive.displayPinpointDataTelemetry();
        farminator.shooter.displayTelemetry();
        telemetry.addData("ang", farminator.pinpointLocalizer.getPose().heading.toDouble());
        telemetry.addData("ang", Math.toDegrees(farminator.pinpointLocalizer.getPose().heading.toDouble()));
        telemetry.addData("custom distance", farminator.shooter.customDistance);
        telemetry.addData("Loop Time (ms)", getRuntime() * 1000);
        telemetry.addData("default drive command: ", farminator.drive.getDefaultCommand());

        farminator.periodic();
    }

    private InstantCommand rumbleCommand() {
        return new InstantCommand(() -> gamepad1.rumble(200));
    }
}
