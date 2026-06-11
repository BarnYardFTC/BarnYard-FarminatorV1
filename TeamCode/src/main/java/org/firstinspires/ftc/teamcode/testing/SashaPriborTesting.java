package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;

@TeleOp(name = "SashaPriborTesting", group = "Linear Opmode")
public class SashaPriborTesting extends LinearOpMode {

    private static final int MIN_SHOOTER_VELOCITY = 800;
    private static final int MAX_SHOOTER_VELOCITY = 1300;

    private static final int SHOOTER_VELOCITY_STAGES = 10;

    private DcMotorEx shooterLeft;
    private DcMotorEx shooterRight;

    private GamepadEx gamepad;

    private boolean shooterEnabled = false;

    private int currentShooterVelocityStage = 1;

    private double targetShooterVelocity;

    @Override
    public void runOpMode() {

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

        shooterLeft = hardwareMap.get(DcMotorEx.class, "shooterLeft");
        shooterRight = hardwareMap.get(DcMotorEx.class, "shooterRight");

        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterRight.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        gamepad = new GamepadEx(this.gamepad1);
    }

    private void updateInput() {

        gamepad.readButtons();

        if(gamepad.wasJustPressed(GamepadKeys.Button.X)) shooterEnabled = !shooterEnabled;

        if (gamepad.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) currentShooterVelocityStage = Math.max(1, currentShooterVelocityStage - 1);
        else if (gamepad.wasJustPressed(GamepadKeys.Button.DPAD_UP)) currentShooterVelocityStage = Math.min(
                    SHOOTER_VELOCITY_STAGES,
                    currentShooterVelocityStage + 1
            );
    }

    private void updateDriver() {

        double shooterRange = MAX_SHOOTER_VELOCITY - MIN_SHOOTER_VELOCITY;

        targetShooterVelocity = MIN_SHOOTER_VELOCITY + ((shooterRange / (SHOOTER_VELOCITY_STAGES - 1)) * (currentShooterVelocityStage - 1));

        if (shooterEnabled) {
            shooterLeft.setVelocity(targetShooterVelocity);
            shooterRight.setVelocity(targetShooterVelocity);
        } else {
            shooterLeft.setVelocity(0);
            shooterRight.setVelocity(0);
        }
    }

    private void telemetryUpdate() {

        telemetry.addData("Shooter Enabled", shooterEnabled);
        telemetry.addLine("Shooter Velocity Stage: " + currentShooterVelocityStage + "/" + SHOOTER_VELOCITY_STAGES);
        telemetry.addData("Target Shooter Velocity", targetShooterVelocity);

        telemetry.update();
    }
}
