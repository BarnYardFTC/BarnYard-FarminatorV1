package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;

import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.util.OpModeData;

@TeleOp(name = "Main Teleop with ftc dashboard", group = "main")
@Config
@Disabled
public class MainDashboard extends CommandOpMode {

    private BarnRobot farminator;
    private FtcDashboard dashboard;

    @Override
    public void initialize() {

        Pose2d autoFinishPose = OpModeData.getAutoFinishPose();
        OpModeData opModeData = new OpModeData(
                OpModeData.AllianceColor.BLUE,
                OpModeData.OpModeType.TELEOP,
                LimeLight.BLUE_LOCALIZATION_PIPELINE,
                autoFinishPose,
                270
        );

        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        // dashboard shi
        dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

        // ---------------------------------------------------------
        // kys nigga
        // ---------------------------------------------------------

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(
                        new ParallelCommandGroup(
                                farminator.transfer.setEntireTransferPowerCommand(-1)
                        )
                )
                .whenInactive(farminator.transfer.setEntireTransferPowerCommand(0));

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenPressed(farminator.transfer.setEntireTransferPowerCommand(1))
                .whenInactive(farminator.transfer.setEntireTransferPowerCommand(0));

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(farminator.shooterHood.lower());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(farminator.shooterHood.raise());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(farminator.shooterHood.setHoodPosition(1));

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(farminator.shooterHood.setHoodPosition(0));

    }

    @Override
    public void run() {
        super.run();

        // ----------------------------------------------------------
        //  Dashboard shi again
        // ----------------------------------------------------------
        TelemetryPacket packet = new TelemetryPacket();
        Canvas field = packet.fieldOverlay();

        Pose2d pose = new Pose2d(
                farminator.pinpointLocalizer.getPose().position.x * 0.0254,
                farminator.pinpointLocalizer.getPose().position.y * 0.0254,
                farminator.pinpointLocalizer.getPose().heading.toDouble()
        );

        double x = pose.position.x;
        double y = pose.position.y;
        double heading = pose.heading.toDouble();

        double half = 7;

        double cos = Math.cos(heading);
        double sin = Math.sin(heading);

        double x1 = x + cos*half - sin*half;
        double y1 = y + sin*half + cos*half;

        double x2 = x - cos*half - sin*half;
        double y2 = y - sin*half + cos*half;

        double x3 = x - cos*half + sin*half;
        double y3 = y - sin*half - cos*half;

        double x4 = x + cos*half + sin*half;
        double y4 = y + sin*half - cos*half;

        field.setStroke("blue");
        field.strokePolyline(
                new double[]{x1, x2, x3, x4, x1},
                new double[]{y1, y2, y3, y4, y1}
        );

        packet.put("x (m)", x);
        packet.put("y (m)", y);
        packet.put("heading (deg)", Math.toDegrees(heading));

        dashboard.sendTelemetryPacket(packet);

        //usual telemetry

        telemetry.addData("x", x);
        telemetry.addData("y", y);
        telemetry.addData("heading", heading);
        telemetry.addData("distance from goal", farminator.drive.getDistanceFromGoal());
        telemetry.addData("shooter velocity", farminator.shooter.getVelocity());
        telemetry.addData("shooter power", farminator.shooter.getPower());
        farminator.shooterHood.displayTelemetry();

        farminator.periodic();
    }
}
//this is not vibecode !!!!!!