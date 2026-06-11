package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;

@TeleOp(name = "SashaPriborTesting", group = "Linear Opmode")
public class SashaPriborTesting extends LinearOpMode {

    private static final double MIN_SHOOTER_STRENGTH = 0.1;
    private static final double MAX_SHOOTER_STRENGTH = 0.5;

    private static final int SHOOTER_STRENGTH_STAGES = 10;

    private DcMotor shooterLeft;
    private DcMotor shooterRight;

    private GamepadEx gamepad;

    private boolean shooterEnabled = false;

    private int shooterStrength = 1;

    private double shooterPower;

    @Override
    public void runOpMode() {

        if(MIN_SHOOTER_STRENGTH < 0) telemetry.addLine("ПОЧЕМУ МИНИМУМ МЕНЬШЕ НУЛЯ, БЫСТРО ИСПРАВЛЯЙ");
        if(MAX_SHOOTER_STRENGTH > 1) telemetry.addLine("ПОЧЕМУ МАКСИМУМ БОЛЬШЕ ОДНОГО, БЫСТРО ИСПРАВЛЯЙ");
        if(MAX_SHOOTER_STRENGTH <= MIN_SHOOTER_STRENGTH) telemetry.addLine("СДЕЛАЙ МАКСИМУМ БОЛЬШЕ МИНИМУМА, ПРИДУРОК");

        initHardware();

        waitForStart();

        if (isStopRequested()) return;

        while(opModeIsActive()) {
            updateInput();

            updateDriver();

            telemetryUpdate();
        }
    }

    private void initHardware() {

        shooterLeft = hardwareMap.get(DcMotor.class, "shooterLeft");
        shooterRight = hardwareMap.get(DcMotor.class, "shooterRight");

        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterRight.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        gamepad = new GamepadEx(this.gamepad1);
    }

    private void updateInput() {

        gamepad.readButtons();

        if(gamepad.wasJustPressed(GamepadKeys.Button.X)) shooterEnabled = !shooterEnabled;

        if (gamepad.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {
            if (shooterStrength + 1 > SHOOTER_STRENGTH_STAGES) shooterStrength = SHOOTER_STRENGTH_STAGES;
            else shooterStrength++;
        } else if (gamepad.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
            if (shooterStrength < 2) shooterStrength = 1;
            else shooterStrength--;
        }


    }

    private void updateDriver() {

        double shooterRange = MAX_SHOOTER_STRENGTH - MIN_SHOOTER_STRENGTH;

        shooterPower = MIN_SHOOTER_STRENGTH + ((shooterRange / (SHOOTER_STRENGTH_STAGES - 1)) * (shooterStrength - 1));

        shooterLeft.setPower(shooterEnabled ? shooterPower : 0);
        shooterRight.setPower(shooterEnabled ? shooterPower : 0);
    }

    private void telemetryUpdate() {

        telemetry.addData("Shooter Enabled", shooterEnabled);
        telemetry.addLine("Shooter Strength: " + shooterStrength + "/" + SHOOTER_STRENGTH_STAGES);
        telemetry.addData("Shooter Power", shooterPower);

        telemetry.update();
    }
}
