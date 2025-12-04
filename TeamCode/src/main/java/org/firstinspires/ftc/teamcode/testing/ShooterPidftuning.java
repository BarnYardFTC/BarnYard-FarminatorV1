package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.util.OpModeData;

//How to tune?


@Config
@TeleOp(name = "shooter pidf tuner", group = "tuning")
public class ShooterPidftuning extends CommandOpMode {

    // ------------------------
    // Robot Instance
    // ------------------------
    private BarnRobot farminator;

    private Telemetry telemetry2;
    private OpModeData opModedata = new OpModeData(
            OpModeData.AllianceColor.BLUE,
            OpModeData.OpModeType.TELEOP,
            LimeLight.BLUE_LOCALIZATION_PIPELINE,
            new Pose2d(0,0,0)
            );

    @Override
    public void initialize() {

        // ------------------------
        // Initialize Robot Systems
        // ------------------------
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModedata);

        telemetry2 = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
                .whenActive(
                        farminator.shooter.runShooterFar()
                )
                .whenInactive(
                        farminator.shooter.turnOff()
                );
    }

    @Override
    public void run() {
        // Run command scheduler and periodic updates
        super.run();
        telemetry2.addData("current velocity", farminator.shooter.getVelocity());
        telemetry2.addData("target velocity", Shooter.SHOOTER_VELOCITY_RANGE_4);
        telemetry2.update();
    }



}
