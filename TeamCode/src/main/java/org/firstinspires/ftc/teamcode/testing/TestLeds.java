package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.OpModeData;


@TeleOp(name = "!TEST Blinkin leds", group = "!")
public class TestLeds extends CommandOpMode{

    // ------------------------
    // Robot Instance
    // ------------------------
    private BarnRobot farminator;

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

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(farminator.leds.setRedCommand())
                .whenInactive(farminator.leds.setBlackCommand());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
                .whenPressed(farminator.leds.setPurpleCommand())
                .whenInactive(farminator.leds.setBlackCommand());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(farminator.leds.setGreenCommand())
                .whenInactive(farminator.leds.setBlackCommand());

    }

    @Override
    public void run() {
        super.run();
        telemetry.addData("led color:", farminator.leds.currentPattern);
        telemetry.addData("isOnlyShootPosBusy", farminator.colorSensor.isOnlyShootPosBusy());
        telemetry.addData("isOnlyShootAndMidBusy", farminator.colorSensor.isOnlyShootAndMidBusy());
        telemetry.addData("isRobotFull", farminator.colorSensor.isRobotFull());
        farminator.periodic();
    }

    private InstantCommand rumbleCommand() {
        return new InstantCommand(() -> gamepad1.rumble(200));
    }
}
