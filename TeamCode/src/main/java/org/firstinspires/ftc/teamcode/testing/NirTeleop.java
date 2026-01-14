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

@TeleOp
public class NirTeleop extends CommandOpMode {

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
//        farminator.shooterHood.setDefaultCommand(farminator.shooterHood.autoHoodAlignment());
        farminator.drive.setDefaultCommand(farminator.drive.driveNonFieldOrientedCommand());


        // ==========================================================
        // Gamepad 1 Controls
        // ==========================================================

        // ------------------------
        // Transfer System
        // ------------------------



//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
//                .whenPressed(farminator.shooterHood.lower());
//
//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
//                .whenPressed(farminator.shooterHood.raise());


        // Left Trigger → Intake active (transfer + intake)
        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0)
                .whenActive(new ParallelCommandGroup(
                        CommandGroup.intakeCommand()
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

//
//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_UP)
//                .toggleWhenPressed(
//                        farminator.shooterHood.goToPositionCommand()
//                );



        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
                .toggleWhenActive(
                        farminator.shooter.runShooterBasedOnDistance(),
                        farminator.shooter.turnOff());


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
                .toggleWhenActive(
                        farminator.drive.alignToTagCommand()
                );


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_STICK_BUTTON)
                .toggleWhenActive(
                        new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateSlowMode()),
                        new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateFastMode())
                );

//        // Arms
//
//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.A)
//                .toggleWhenActive(
//                        new InstantCommand(() -> farminator.gate.openCommand()),
//                        new InstantCommand(() -> farminator.gate.closeCommand())
//                );

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(CommandGroup.shootCommand());


    }

    @Override
    public void run() {
        super.run();
        farminator.gate.displayTelemetry();
        farminator.telemetry.addData("absolute heading", farminator.drive.getBotAbsoluteHeading());
        farminator.periodic();
    }
}
