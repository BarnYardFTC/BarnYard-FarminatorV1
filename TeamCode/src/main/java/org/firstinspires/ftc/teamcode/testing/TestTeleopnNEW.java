package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.CommandGroup;
import org.firstinspires.ftc.teamcode.commandGroups.SmartCommandGroups;
import org.firstinspires.ftc.teamcode.subsystems.ColorSensor;
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
    public ColorSensor colorSensor;

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
        colorSensor = new ColorSensor();
        farminator.shooterHood.setDefaultCommand(farminator.shooterHood.defaultHoodCommand());
        farminator.drive.setDefaultCommand(farminator.drive.driveOneDriverCommand());
        farminator.shooter.setDefaultCommand(farminator.shooter.runShooterBasedOnDistance());


        // ==========================================================
        // Gamepad 1 Controls
        // ==========================================================

        // ------------------------
        // Transfer System
        // ------------------------

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.OPTIONS)
                .toggleWhenPressed(
                        farminator.colorSensor.changeMode()
                );


        //This shit need to be in shoot button
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.SHARE)
                .whileActiveOnce(
                        farminator.colorSensor.setCheckFalse()
                );



        // Left Trigger → Intake active (transfer + intake)
        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0)
                .whenActive(
                        new ParallelCommandGroup(
                                new ConditionalCommand(
                                        SmartCommandGroups.smartIntakesTransfers(),
                                        CommandGroup.intakeAndTransferActivateCommand(),
                                        () -> farminator.colorSensor.getIntakeMode()
                                ),
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

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                        .whileActiveOnce(
                                CommandGroup.shootCommand()
                        );




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

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(new InstantCommand(() -> farminator.shooterHood.raise()).alongWith(
                        BarnRobot.getInstance().gate.closeCommand()
                ));

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(new InstantCommand(() -> farminator.shooterHood.lower()).alongWith(
                        BarnRobot.getInstance().gate.closeCommand()
                ));

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(new ParallelCommandGroup(
                        CommandGroup.shootCommandPreset(1),
                        rumbleCommand()
                ).alongWith(
                        BarnRobot.getInstance().gate.closeCommand()
                ));

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(new ParallelCommandGroup(
                        CommandGroup.shootCommandPreset(2),
                        rumbleCommand()
                ).alongWith(
                        BarnRobot.getInstance().gate.closeCommand()
                ));

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
                .whenPressed(new ParallelCommandGroup(
                        CommandGroup.shootCommandPreset(3),
                        rumbleCommand()
                ).alongWith(
                        BarnRobot.getInstance().gate.closeCommand()
                ));

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(CommandGroup.shootCommand());


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
                .toggleWhenPressed(
                        farminator.shooter.turnOff(),
                        farminator.shooter.runShooterBasedOnDistance()
                );
    }

    @Override
    public void run() {
        super.run();
        farminator.intake.displayTelemetry(telemetry);
        farminator.transfer.displayTelemetry(telemetry);
        telemetry.addData("Toggle intake pos: ", farminator.colorSensor.getIntakeMode());
        farminator.colorSensor.displayTelemetry(telemetry);

        farminator.periodic();
    }

    private InstantCommand rumbleCommand() {
        return new InstantCommand(() -> gamepad1.rumble(200));
    }
}

