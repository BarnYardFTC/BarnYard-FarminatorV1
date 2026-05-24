package org.firstinspires.ftc.teamcode.testing;


import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/*
Tasks:
- drivetrain (field oriented)
- intake (when {button} is held down -> power 1 to motor | when not held -> power 0
- transfer ""
- gate (when {button} pressed changes position from opened to closed)
- kickstand (when {button} pressed changes position from normal to standing)
 */
@TeleOp(name = "AlejandroTeleop", group = "Aboba")
public class AlejandroTeleop extends LinearOpMode {

    private static final double MIN_DRIVE_POWER = 0.2;
    private static final double MAX_DRIVE_POWER = 1.0;

    private static final double MIN_INTAKE = 0.0;
    private static final double MAX_INTAKE = 1.0;

    private static final double MIN_TRANSFER = 0.0;
    private static final double MAX_TRANSFER = 1.0;

    private static final double MIN_SHOOTER_HOOD = 0.35;
    private static final double MAX_SHOOTER_HOOD = 0.95;

    private static final double STRAFE_COMPENSATION = 1.1;

    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftBack;
    private DcMotor rightBack;

    private DcMotor intake;

    private DcMotor transfer;

    private IMU imu;
    private Servo shooterHood;

    private GamepadEx gamepad;

    private double x;
    private double y;
    private double rx;

    private double drivetrainPower = 1;

    private double currentShooterHood = 0;

    private boolean fieldCentricEnabled = false;

    private boolean isIntake = false;
    private boolean isTransfer = false;

    @Override
    public void runOpMode() {
        initializeHardware();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            updateInput();
            updateDrive();
            updateServos();
            updateTelemetry();
        }
    }

    private void initializeHardware() {

        leftFront = hardwareMap.get(DcMotor.class, "leftFrontDrivetrain");
        rightFront = hardwareMap.get(DcMotor.class, "rightFrontDrivetrain");
        leftBack = hardwareMap.get(DcMotor.class, "leftBackDrivetrain");
        rightBack = hardwareMap.get(DcMotor.class, "rightBackDrivetrain");

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);

        intake = hardwareMap.get(DcMotor.class, "intake");

        transfer = hardwareMap.get(DcMotor.class, "transfer");

        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        imu.initialize(parameters);

        shooterHood = hardwareMap.get(Servo.class, "shooterHood");

        shooterHood.setDirection(Servo.Direction.REVERSE);
        shooterHood.scaleRange(0,1);

        gamepad = new GamepadEx(this.gamepad1);
    }

    private void updateInput() {

        gamepad.readButtons();

        y = applyDeadzone(gamepad.getLeftY());
        x = applyDeadzone(gamepad.getLeftX());
        rx = applyDeadzone(gamepad.getRightX());

        if(gamepad.wasJustPressed(GamepadKeys.Button.X))
            fieldCentricEnabled = !fieldCentricEnabled;

        if(gamepad.wasJustPressed(GamepadKeys.Button.A)) {
            if(drivetrainPower - 0.1 > MIN_DRIVE_POWER) drivetrainPower -= 0.1;
            else drivetrainPower = MIN_DRIVE_POWER;
        } else if (gamepad.wasJustPressed(GamepadKeys.Button.B)) {
            if(drivetrainPower + 0.1 < MAX_DRIVE_POWER) drivetrainPower += 0.1;
            else drivetrainPower = MAX_DRIVE_POWER;
        }

        if(gamepad.wasJustPressed(GamepadKeys.Button.RIGHT_BUMPER)) {
            if(currentShooterHood - 0.1 > MIN_SHOOTER_HOOD) currentShooterHood -= 0.1;
            else currentShooterHood = MIN_SHOOTER_HOOD;
        } else if (gamepad.wasJustPressed(GamepadKeys.Button.LEFT_BUMPER)) {
            if(currentShooterHood + 0.1 < MAX_SHOOTER_HOOD) currentShooterHood += 0.1;
            else currentShooterHood = MAX_SHOOTER_HOOD;
        }

        if(gamepad.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {
            isIntake = !isIntake;
            isTransfer = !isTransfer;
        }

        if (gamepad.wasJustPressed(GamepadKeys.Button.Y)) {
            imu.resetYaw();
        }
    }

    private void updateDrive() {

        double driveX = x;
        double driveY = y;

        if(fieldCentricEnabled) {

            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            double rotX = driveX * Math.cos(-botHeading) - driveY * Math.sin(-botHeading);
            double rotY = driveX * Math.sin(-botHeading) + driveY * Math.cos(-botHeading);

            telemetry.addData("rotX", rotX);
            telemetry.addData("rotY", rotY);

            driveX = rotX * STRAFE_COMPENSATION;
            driveY = rotY;
        }

        double denominator = Math.max(Math.abs(driveX) + Math.abs(driveY) + Math.abs(rx), 1);

        double leftFrontPower = (driveY + driveX + rx) / denominator;
        double leftBackPower = (driveY - driveX + rx) / denominator;
        double rightFrontPower = (driveY - driveX - rx) / denominator;
        double rightBackPower = (driveY + driveX - rx) / denominator;

        intake.setPower(isIntake ? MAX_INTAKE : MIN_INTAKE);
        transfer.setPower(isTransfer ? MAX_TRANSFER : MIN_TRANSFER);

        setDrivePower(leftFrontPower * drivetrainPower, rightFrontPower * drivetrainPower, leftBackPower * drivetrainPower, rightBackPower * drivetrainPower);
    }

    private void updateServos() {
        shooterHood.setPosition(currentShooterHood);
    }

    private void updateTelemetry() {
        telemetry.addData("X", x);
        telemetry.addData("Y", y);
        telemetry.addData("isFieldCentricDrive", fieldCentricEnabled);
        telemetry.addData("Shooter Hood", currentShooterHood);

        telemetry.update();
    }

    private void setDrivePower(
            double lf,
            double rf,
            double lb,
            double rb
    ) {
        leftFront.setPower(lf);
        rightFront.setPower(rf);
        leftBack.setPower(lb);
        rightBack.setPower(rb);
    }

    private double applyDeadzone(double value) {
        return Math.abs(value) > 0.05 ? value : 0;
    }
}
