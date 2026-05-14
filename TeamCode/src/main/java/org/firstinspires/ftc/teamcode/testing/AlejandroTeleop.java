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
    Servo servo1;
    GamepadEx gamepad;

    @Override
    public void runOpMode() {
        DcMotor leftFrontDrivetrain = hardwareMap.get(DcMotor.class, "leftFrontDrivetrain");
        DcMotor rightFrontDrivetrain = hardwareMap.get(DcMotor.class, "rightFrontDrivetrain");
        DcMotor leftBackDrivetrain = hardwareMap.get(DcMotor.class, "leftBackDrivetrain");
        DcMotor rightBackDrivetrain = hardwareMap.get(DcMotor.class, "rightBackDrivetrain");

        leftFrontDrivetrain.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrivetrain.setDirection(DcMotor.Direction.REVERSE);

        IMU imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        imu.initialize(parameters);

        servo1 = hardwareMap.get(Servo.class, "shooterHood");

        gamepad = new GamepadEx(this.gamepad1);

        servo1.setDirection(Servo.Direction.REVERSE);
        servo1.scaleRange(0,1);

        double x, y, rx,
                leftFrontDrivetrainPower, rightFrontDrivetrainPower, leftBackDrivetrainPower, rightBackDrivetrainPower,
                drivetrainPower = 1, minDrivetrainPower = 0.2, maxDrivetrainPower = 1,
                denominator, botHeading, rotX, rotY;

        boolean isFieldCentricDrive = false;

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            y = gamepad.getLeftY();
            x = gamepad.getLeftX();
            rx = gamepad.getRightX();

            if(gamepad.wasJustPressed(GamepadKeys.Button.X))
                isFieldCentricDrive = !isFieldCentricDrive;

            if(gamepad.wasJustPressed(GamepadKeys.Button.A)) {
                if(drivetrainPower - 0.1 > minDrivetrainPower) drivetrainPower -= 0.1;
                else drivetrainPower = minDrivetrainPower;
            } else if (gamepad.wasJustPressed(GamepadKeys.Button.B)) {
                if(drivetrainPower + 0.1 < maxDrivetrainPower) drivetrainPower += 0.1;
                else drivetrainPower = maxDrivetrainPower;
            }

            if (gamepad.wasJustPressed(GamepadKeys.Button.OPTIONS)) {
                imu.resetYaw();
            }

            telemetry.addData("X", x);
            telemetry.addData("Y", y);
            telemetry.addData("isFieldCentricDrive", isFieldCentricDrive);

            if(isFieldCentricDrive) {

                botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

                rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
                rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

                telemetry.addData("rotX", rotX);
                telemetry.addData("rotY", rotY);

                x = rotX * 1.1;
                y = rotY;
            }

            denominator = Math.max(Math.abs(x) + Math.abs(y) + Math.abs(rx), 1);

            leftFrontDrivetrainPower = (y + x + rx) / denominator;
            leftBackDrivetrainPower = (y - x + rx) / denominator;
            rightFrontDrivetrainPower = (y - x - rx) / denominator;
            rightBackDrivetrainPower = (y + x - rx) / denominator;

            /*if (gamepad.wasJustPressed(GamepadKeys.Button.X)) {
                servoUp();
            } else if (gamepad.wasJustPressed(GamepadKeys.Button.Y)) {
                servoDown();
            }*/

            leftFrontDrivetrain.setPower(leftFrontDrivetrainPower * drivetrainPower);
            leftBackDrivetrain.setPower(leftBackDrivetrainPower * drivetrainPower);
            rightFrontDrivetrain.setPower(rightFrontDrivetrainPower * drivetrainPower);
            rightBackDrivetrain.setPower(rightBackDrivetrainPower * drivetrainPower);

            telemetry.update();

            gamepad.readButtons();
        }
    }
}
