package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.util.OpModeData;

@TeleOp(name = "LED test teleop 1", group = "test")
@Config
@Disabled
public class LEDAutoTest extends CommandOpMode {

    // ------------------------
    // Robot Instance
    // ------------------------
    private BarnRobot farminator;

    private ElapsedTime opModeTimer;
    private int tenSecondCount = 0;
    private double lastTickTime = 0;
    private boolean hasRumbled = false;
    private int lastRumbleSecond = -1;


    @Override
    public void initialize() {

        Pose2d autoFinishPose = OpModeData.getAutoFinishPose(); // use the pose in which the auto has ended
        OpModeData opModeData = new OpModeData(
                OpModeData.AllianceColor.BLUE,
                OpModeData.OpModeType.TELEOP,
                autoFinishPose,
                270
        );

        hasRumbled = false;
        lastRumbleSecond = 0;
        lastTickTime = 0;
        opModeTimer = new ElapsedTime();
        opModeTimer.reset();

        // ==========================================================
        // Robot Initialization
        // ==========================================================
        farminator = BarnRobot.getInstance();
        farminator.init(
                this,
                opModeData
        );

        farminator.drive.setDefaultCommand(farminator.drive.driveTwoDriversCommand());

        farminator.shooterHood.setDefaultCommand(farminator.shooterHood.autoHoodAlignment());
//
//        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.X)
//                .whenPressed(farminator.blinkin.setRedCommand());
//
//        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.Y)
//                .whenPressed(farminator.blinkin.setBlueCommand());
//
//        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.A)
//                .whenPressed(farminator.blinkin.setGreenCommand());
//
//        farminator.gamepadEx2.getGamepadButton(GamepadKeys.Button.B)
//                .whenPressed(farminator.blinkin.setPurpleCommand());
    }

    @Override
    public void run() {
        super.run();
        telemetry.addData("x", BarnRobot.getInstance().pinpointLocalizer.getPose().position.x);
        telemetry.addData("y", BarnRobot.getInstance().pinpointLocalizer.getPose().position.y);
        telemetry.addData("heading", BarnRobot.getInstance().pinpointLocalizer.getPose().heading.toDouble());
        telemetry.addData("blinkin last update time", farminator.blinkin.getLastUpdateTime());
        farminator.periodic();
//        farminator.blinkin.update();
        rumpleGamepadsEndgame();
    }

    private void rumpleGamepadsEndgame() {

        double now = opModeTimer.seconds();

        // Endgame window: last 20 seconds (100s → 120s)
        if (now >= 100 && now <= 120) {

            int currentSecond = (int) now;

            // Rumble once per second
            if (currentSecond != lastRumbleSecond) {

                // Short, clear pulse
                gamepad2.rumble(1.0, 1.0, 200);

                lastRumbleSecond = currentSecond;
            }
        }
    }



}
