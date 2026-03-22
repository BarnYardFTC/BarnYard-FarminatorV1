package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.CommandGroup;
import org.firstinspires.ftc.teamcode.opmodes.teleop.TeleopTemplate;
import org.firstinspires.ftc.teamcode.util.OpModeData;

@TeleOp(name = "align", group = "test")

public class LocalzationAlignmentTest extends CommandOpMode {
    private double fieldOrientedReferenceHeading;
    private BarnRobot farminator = BarnRobot.getInstance();

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
        initControls(180);

    }

    @Override
    public void run() {
        super.run();
        farminator.shooterHood.displayTelemetry();
        farminator.drive.displayPinpointDataTelemetry();
        farminator.shooter.displayTelemetry();
//        telemetry.addData("ang", farminator.pinpointLocalizer.getPose().heading.toDouble());
        telemetry.addData("ang", Math.toDegrees(farminator.pinpointLocalizer.getPose().heading.toDouble()));
//        telemetry.addData("Loop Time (ms)", getRuntime() * 1000);
//        telemetry.addData("default drive command: ", farminator.drive.getDefaultCommand());
        telemetry.addData("Is smart functions ON? ", farminator.colorSensor.getIntakeMode());
        farminator.colorSensor.displayTelemetry(telemetry);
        telemetry.addData("is in zone", farminator.drive.isInsideLaunchZone());

        telemetry.addData("gate left pos", farminator.gate.getLeftPos());
        farminator.limelight.displayTelemetry();
        farminator.periodic();
    }


    public void initControls(double fieldOrientedReferenceHeading){

        this.fieldOrientedReferenceHeading = fieldOrientedReferenceHeading;

        farminator.shooterHood.setDefaultCommand(farminator.shooterHood.autoHoodAlignment());
        farminator.drive.setDefaultCommand(farminator.drive.driveOneDriverCommand());
        farminator.shooter.setDefaultCommand(farminator.shooter.runShooterBasedOnDistance());


        // ==========================================================
        // Gamepad 1 Controls
        // ==========================================================

        // ------------------------
        // Transfer System
        // ------------------------



        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.SHARE)
                .whileActiveOnce(
                        new SequentialCommandGroup(
                                farminator.colorSensor.changeMode(),
                                farminator.colorSensor.setCheckFalse()
                        )

                );

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.CIRCLE)
                .toggleWhenPressed(farminator.drive.alignToTagLamLamCommand());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .toggleWhenPressed(farminator.drive.alignToTagCommand());


        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.05)
                .whenActive(new ParallelCommandGroup(
                        farminator.shooter.setAutoDistance(),
                        farminator.drive.alignToTagLamLamCommand(),
                        CommandGroup.smartShootCommand()
                )).whenInactive(
                        farminator.drive.driveOneDriverCommand()
                );

        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenActive(new ParallelCommandGroup(
                        CommandGroup.shootCommand()
                )).whenInactive(
                        farminator.drive.driveOneDriverCommand()
                );
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenActive(new ParallelCommandGroup(
                        CommandGroup.shootCommand()
                )).whenInactive(
                        farminator.drive.driveOneDriverCommand()
                );


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_STICK_BUTTON)
                .whenHeld(new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateSlowMode()))
                .whenReleased(new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateFastMode()));



//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_STICK_BUTTON)
//                .toggleWhenActive(
//                        new InstantCommand(() -> farminator.drive.setDefaultCommand(farminator.drive.maintainPosCommand())),
//                        new InstantCommand(() -> farminator.drive.setDefaultCommand(farminator.drive.driveNonFieldOrientedCommand()))
//                );




//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
//                .toggleWhenPressed(
//                        farminator.shooter.turnOff(),
//                        farminator.shooter.runShooterBasedOnDistance()
//                );



        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .toggleWhenPressed(
                        new RunCommand(() -> farminator.kickStand.activate()),
                        new RunCommand(() -> farminator.kickStand.deactivate()));


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
                .toggleWhenPressed(
                        () -> new ParallelCommandGroup(
                                farminator.shooter.turnOff(),
                                farminator.intake.deactivateIntakeRunCommandZeroPower()
                        ),
                        () -> farminator.shooter.runShooterBasedOnDistance()
                );


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(() -> farminator.pinpointLocalizer.setPose(
                        new Pose2d(
                                farminator.pinpointLocalizer.getPose().position.x,
                                farminator.pinpointLocalizer.getPose().position.y,
                                Math.toRadians(fieldOrientedReferenceHeading)
                        )
                ));

        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(() -> farminator.pinpointLocalizer.setPose(
                        new Pose2d(
                                farminator.pinpointLocalizer.getPose().position.x,
                                farminator.pinpointLocalizer.getPose().position.y,
                                Math.toRadians(fieldOrientedReferenceHeading)
                        )
                ));



        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .toggleWhenPressed(
                        new RunCommand(() -> farminator.kickStand.activateCommand()),
                        new RunCommand(() -> farminator.kickStand.deactivateCommand()));


        // Left Trigger → Intake active (transfer + intake)
        new Trigger(() -> farminator.gamepadEx2.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0)
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

        new Trigger(() -> farminator.gamepadEx2.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0)
                .whenActive(
                        CommandGroup.shootCommand()
                );


        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.LEFT_STICK_BUTTON)
                .toggleWhenActive(
                        new InstantCommand(() -> farminator.drive.setDefaultCommand(farminator.drive.maintainPosCommand())),
                        new InstantCommand(() -> farminator.drive.setDefaultCommand(farminator.drive.driveNonFieldOrientedCommand()))
                );


//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
//                .toggleWhenActive(
//                        farminator.drive.alignToTagCommand());


//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
//                .toggleWhenPressed(farminator.shooterHood.goToPositionCommand());




        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenActive(
                        new ParallelCommandGroup(
                                farminator.shooter.setShooterManualDistance(1.2),
                                farminator.shooter.setManualDistance(),
                                farminator.shooterHood.setHoodPosition(0.71)
                        ))
        ;


        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenActive(
                        new ParallelCommandGroup(
                                farminator.shooter.setShooterManualDistance(3),
                                farminator.shooter.setManualDistance(),
                                farminator.shooterHood.setHoodPosition(1)
                        ))

        ;

        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenActive(
                        new ParallelCommandGroup(
                                farminator.shooter.setShooterManualDistance(1.4),
                                farminator.shooter.setManualDistance(),
                                farminator.shooterHood.setHoodPosition(0.85)
                        ));

        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.B)
                .whenActive(
                        () -> farminator.drive.mecanumDriveComponent.activateSlowMode()
                )
                .whenInactive(() -> farminator.drive.mecanumDriveComponent.activateFastMode());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
                .whenActive(
                        () -> farminator.drive.mecanumDriveComponent.activateSlowMode()
                )
                .whenInactive(() -> farminator.drive.mecanumDriveComponent.activateFastMode());


        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.Y)
                .toggleWhenPressed(
                        new ParallelCommandGroup(
                                farminator.shooter.turnOff(),
                                farminator.intake.deactivateIntakeRunCommandZeroPower()
                        ),
                        farminator.shooter.runShooterBasedOnDistance()
                );

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
                .toggleWhenPressed(
                        new ParallelCommandGroup(
                                farminator.shooter.turnOff(),
                                farminator.intake.deactivateIntakeRunCommandZeroPower()
                        ),
                        farminator.shooter.runShooterBasedOnDistance()
                );
    }
}
