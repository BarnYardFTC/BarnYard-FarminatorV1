package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.OpModeData;

@TeleOp(name = "Cmd", group = "test")
public class AccelerationTesting extends CommandOpMode {
    private BarnRobot farminator;

    @Override
    public void initialize() {
        OpModeData opModeData = new OpModeData(
                OpModeData.AllianceColor.BLUE,
                OpModeData.OpModeType.TELEOP,
                new Pose2d(23, 0, Math.toRadians(180)),
                180
        );
        farminator = BarnRobot.getInstance();
        farminator.init(
                this,
                opModeData
        );
        farminator.drive.setDefaultCommand(farminator.drive.driveOneDriverCommand());
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
