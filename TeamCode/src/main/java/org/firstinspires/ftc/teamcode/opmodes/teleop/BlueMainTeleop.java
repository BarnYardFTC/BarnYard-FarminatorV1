package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.CommandGroup;
import org.firstinspires.ftc.teamcode.util.OpModeData;

import java.util.function.BooleanSupplier;


@TeleOp(name = "!!BLUE TELEOP", group = "!")
public class BlueMainTeleop extends CommandOpMode{

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
                270
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
        farminator.drive.setDefaultCommand(farminator.drive.driveOneDriverCommand());
        farminator.shooter.setDefaultCommand(farminator.shooter.runShooterBasedOnDistance());


        // ==========================================================
        // Gamepad 1 Controls
        // ==========================================================

        // ------------------------
        // Transfer System
        // ------------------------
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenActive(
                        new ParallelCommandGroup(
                                farminator.shooter.runShooterBasedOnConstantDistance(1.2),
                                farminator.shooterHood.setHoodPosition(0.71),
                                CommandGroup.shootCommand()
                        ))
                .whenInactive(CommandGroup.forceCloseGateCommand())
        ;


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                        .whenActive(new ParallelCommandGroup(
                                farminator.shooter.runShooterBasedOnConstantDistance(3),
                                farminator.shooterHood.setHoodPosition(1),
                                CommandGroup.shootCommand()
                        ))        .whenInactive(CommandGroup.forceCloseGateCommand())
        ;

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenActive(new ParallelCommandGroup(
                        farminator.shooter.runShooterBasedOnConstantDistance(1.4),
                        farminator.shooterHood.setHoodPosition(0.85),
                        CommandGroup.shootCommand()
                ))        .whenInactive(CommandGroup.forceCloseGateCommand())
        ;


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
                        new RunCommand(this::intakeSwitcher)
                )
                .whenInactive(
                        CommandGroup.deactivateIntakeAndTransferCommand().alongWith(
                                BarnRobot.getInstance().gate.closeCommand()
                        )
                );

        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.05)
                .whenActive(new ParallelCommandGroup(
                        farminator.drive.alignToTagLamLamCommand(),
                        CommandGroup.smartShootCommand()
//                        farminator.intake.customIntakeCommand(-1),
//                        BarnRobot.getInstance().gate.closeCommand()
                )).whenInactive(new ParallelCommandGroup(
                        farminator.drive.driveOneDriverCommand(),
                        CommandGroup.forceCloseGateCommand())
                );


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_STICK_BUTTON)
                .whenHeld(new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateSlowMode()))
                .whenReleased(new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateFastMode()));



//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_STICK_BUTTON)
//                .toggleWhenActive(
//                        new InstantCommand(() -> farminator.drive.setDefaultCommand(farminator.drive.maintainPosCommand())),
//                        new InstantCommand(() -> farminator.drive.setDefaultCommand(farminator.drive.driveNonFieldOrientedCommand()))
//                );


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X).whenPressed(
                new InstantCommand(() -> farminator.pinpointLocalizer.setPose(new Pose2d(0,0,Math.toRadians(180)))));


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenActive(
                        new ParallelCommandGroup(
                                farminator.intake.customIntakeCommand(-1),
                                BarnRobot.getInstance().gate.closeCommand())
                );


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
                .toggleWhenPressed(
                        farminator.shooter.turnOff(),
                        farminator.shooter.runShooterBasedOnDistance()
                );

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
                .toggleWhenPressed(
                        farminator.drive.alignToTagLamLamCommand()
                );

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(new InstantCommand(() -> farminator.shooterHood.raise()));

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(new InstantCommand(() -> farminator.shooterHood.lower()));
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

    private void intakeSwitcher(){
        if (farminator.colorSensor.getIntakeMode()){
            farminator.intake.smartIntakeCommand().schedule();
            farminator.transfer.smartTransferCommand().schedule();
            telemetry.addLine("\n\n\n====SMART====\n\n\n");
        }else{
            farminator.intake.activateIntakeCommand().schedule();
            farminator.transfer.activateTransfer().schedule();
            telemetry.addLine("\n\n\n====DUMB====\n\n\n");
        }
    }

    private InstantCommand rumbleCommand() {
        return new InstantCommand(() -> gamepad1.rumble(200));
    }
}