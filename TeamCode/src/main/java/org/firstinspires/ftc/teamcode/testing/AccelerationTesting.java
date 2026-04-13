package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.OpModeData;

@TeleOp(name = "acc test", group = "test")
public class AccelerationTesting extends CommandOpMode {
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
        farminator.drive.setDefaultCommand(farminator.drive.driveOneDriverCommand());
        farminator.shooter.setDefaultCommand(farminator.shooter.turnOff());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
                .whenHeld(farminator.drive.maintainPosCommandNew());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X)
                .toggleWhenActive(
                        farminator.drive.maintainPosCommand()
                );

    }

    @Override
    public void run() {
        super.run();
        farminator.periodic();
        telemetry.addData("x velocity: ", farminator.pinpointLocalizer.getPoseVelocity().position.x);
        telemetry.addData("y velocity: ", farminator.pinpointLocalizer.getPoseVelocity().position.y);
        telemetry.addData("heading velocity: ", farminator.pinpointLocalizer.getPoseVelocity().heading.toDouble());
        telemetry.addData("is attacked: ", isAttacked());
    }

    private boolean isAttacked() {
        Pose2d pV = farminator.pinpointLocalizer.getPoseVelocity();
        boolean isMoving = Math.max(
                Math.max(
                        Math.abs(pV.position.x),
                        Math.abs(pV.position.y)),
                Math.abs(pV.heading.toDouble()))
                > 0.2;
        boolean sticksRest = gamepad1.left_stick_x == 0 && gamepad1.left_stick_y == 0 &&
                gamepad1.right_stick_x == 0 && gamepad1.right_stick_y == 0;
        return isMoving && sticksRest;
    }
}
