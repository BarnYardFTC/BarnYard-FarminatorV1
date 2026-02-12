package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.RobotCommands;
import org.firstinspires.ftc.teamcode.util.OpModeData;

@TeleOp(name = "CmdTeleop", group = "test")
public class KharaTesting extends CommandOpMode {
    private BarnRobot farminator;

    @Override
    public void initialize() {
        OpModeData opModeData = new OpModeData(
                OpModeData.AllianceColor.BLUE,
                OpModeData.OpModeType.TELEOP,
                new Pose2d(0, 0, Math.toRadians(180)),
                180
        );

        farminator = BarnRobot.getInstance();
        farminator.init(
                this,
                opModeData
        );
        farminator.shooterHood.setDefaultCommand(farminator.shooterHood.defaultHoodCommand());
        farminator.drive.setDefaultCommand(farminator.drive.driveNonFieldOrientedCommand());
        farminator.shooter.setDefaultCommand(farminator.shooter.runShooterBasedOnDistance());


        new Trigger(() -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0)
                .whenActive(
                        RobotCommands.smartCollectCommand()
                )
                .whenInactive(
                        RobotCommands.collectStopCommand()
                );

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
                .whenHeld(new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateSlowMode()))
                .whenReleased(new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateFastMode()));

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X).whenPressed(
                new InstantCommand(() -> farminator.pinpointLocalizer.setPose(new Pose2d(0,0,Math.toRadians(270)))));

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(RobotCommands.smartShootCommand());


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
                .toggleWhenPressed(
                        farminator.shooter.turnOff(),
                        farminator.shooter.runShooterBasedOnDistance()
                );
    }

    @Override
    public void run() {
        super.run();
        farminator.periodic();
        RobotCommands.displayTelemetry();
    }
}
