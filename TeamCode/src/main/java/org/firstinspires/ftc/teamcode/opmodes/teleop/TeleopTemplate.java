package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.roadrunner.Pose2d;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.CommandGroup;


public class TeleopTemplate{

    // ------------------------
    // Robot Instance
    // ------------------------

    private double fieldOrientedReferenceHeading;
    private BarnRobot farminator = BarnRobot.getInstance();


    public void initControls(double fieldOrientedReferenceHeading){

        this.fieldOrientedReferenceHeading = fieldOrientedReferenceHeading;

        farminator.shooterHood.setDefaultCommand(farminator.shooterHood.autoHoodAlignment());
        farminator.drive.setDefaultCommand(farminator.drive.driveOneDriverCommand());
        farminator.shooter.setDefaultCommand(farminator.shooter.opearteShooter());


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


        // Left Trigger → Intake active (transfer + intake)
        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.05)

                .whileActiveContinuous(
                        new ParallelCommandGroup(
                            new RunCommand(this::intakeSwitcher),
                            BarnRobot.getInstance().gate.closeCommand()
                        )
                )
                .whenInactive(
                        new ParallelCommandGroup(
                                CommandGroup.deactivateIntakeAndTransferCommand()
                        )
                );

        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.05)
                .whenActive(new ParallelCommandGroup(
                        farminator.shooter.setAutoDistance(),
                        farminator.drive.alignToTagLamLamCommand(),
                        CommandGroup.smartShootCommand()
//                        farminator.intake.customIntakeCommand(-1),
//                        BarnRobot.getInstance().gate.closeCommand()
                )).whenInactive(
                        farminator.drive.driveOneDriverCommand()
                );

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
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


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenActive(
                        new ParallelCommandGroup(
                                farminator.intake.customIntakeCommand(-1),
                                BarnRobot.getInstance().gate.closeCommand())
                );


//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
//                .toggleWhenPressed(
//                        farminator.shooter.turnOff(),
//                        farminator.shooter.runShooterBasedOnDistance()
//                );

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
                .toggleWhenPressed(
                        farminator.drive.alignToTagLamLamCommand()
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
                .whenPressed(new InstantCommand(() -> farminator.kickStand.deactivateCommand()));

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(new InstantCommand(() -> farminator.kickStand.activateCommand()));

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

        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(new InstantCommand(() -> farminator.shooterHood.raise()).alongWith(
                        BarnRobot.getInstance().gate.closeCommand()
                ));

        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(new InstantCommand(() -> farminator.shooterHood.lower()).alongWith(
                        BarnRobot.getInstance().gate.closeCommand()
                ));

        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenActive(
                        new ParallelCommandGroup(
                                farminator.shooter.setShooterManualDistance(1.2),
                                farminator.shooter.setManualDistance(),
                                farminator.shooterHood.setHoodPosition(0.71),
                                CommandGroup.shootCommand()
                        ))
        ;


        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenActive(
                        new ParallelCommandGroup(
                                farminator.shooter.setShooterManualDistance(3),
                                farminator.shooter.setManualDistance(),
                                farminator.shooterHood.setHoodPosition(1),
                                CommandGroup.shootCommand()
                        ))

        ;

        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenActive(
                        new ParallelCommandGroup(
                                farminator.shooter.setShooterManualDistance(1.4),
                                farminator.shooter.setManualDistance(),
                                farminator.shooterHood.setHoodPosition(0.85),
                                CommandGroup.shootCommand()
                        ));

        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(CommandGroup.shootCommand());


        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.Y)
                .toggleWhenPressed(
                        farminator.shooter.turnOff(),
                        farminator.shooter.runShooterBasedOnDistance()
                );
    }

    private void intakeSwitcher(){
        if (farminator.colorSensor.getIntakeMode()){
            farminator.intake.smartIntakeCommand().schedule();
            farminator.transfer.smartTransferCommand().schedule();
        }else{
            farminator.intake.activateIntakeCommand().schedule();
            farminator.transfer.activateTransfer().schedule();
        }
    }



}