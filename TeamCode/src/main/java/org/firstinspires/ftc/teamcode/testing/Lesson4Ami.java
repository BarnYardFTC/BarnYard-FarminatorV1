package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "Basic: farminator Ami", group = "Learning Ami")
public class Lesson4Ami extends LinearOpMode {

    private final RevHubOrientationOnRobot.LogoFacingDirection logoFacingDirection = RevHubOrientationOnRobot.LogoFacingDirection.LEFT;
    private final RevHubOrientationOnRobot.UsbFacingDirection usbFacingDirection = RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;


    private enum DRIVING_MODE {
        FIELD_CENTRIC,
        ROBOT_CENTRIC
    }

    private DRIVING_MODE drivingMode;
    private final double OPEN_GATE_POSITION = 1;
    private final double CLOSE_GATE_POSITION = 0.88;
    private final double OPEN_KICKSTAND_POSITION = 0.7;
    private final double CLOSE_KICKSTAND_POSITION =0.15;



    public void runOpMode() {

        GamepadEx gamepadEx = new GamepadEx(gamepad1);

        drivingMode = DRIVING_MODE.ROBOT_CENTRIC;

        IMU imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        logoFacingDirection,
                        usbFacingDirection
                )
        ));

        imu.resetYaw();

        Servo gateLeft = hardwareMap.get(Servo.class, "leftGate");
        Servo gateRight = hardwareMap.get(Servo.class, "rightGate");
        Servo kickstandLeft = hardwareMap.get(Servo.class,"kickStandRight");
        Servo kickstandRight = hardwareMap.get(Servo.class,"kickStandLeft");
        DcMotor intake = hardwareMap.get(DcMotor.class, "intake");
        DcMotor transfer = hardwareMap.get(DcMotor.class, "transfer");

        DcMotor lfMotor = hardwareMap.get(DcMotor.class, "leftFrontDrivetrain");
        DcMotor rfMotor = hardwareMap.get(DcMotor.class, "rightFrontDrivetrain");
        DcMotor lbMotor = hardwareMap.get(DcMotor.class, "leftBackDrivetrain");
        DcMotor rbMotor = hardwareMap.get(DcMotor.class, "rightBackDrivetrain");
        lfMotor.setDirection(DcMotor.Direction.REVERSE);
        lbMotor.setDirection(DcMotor.Direction.REVERSE);

        gateLeft.setDirection(Servo.Direction.REVERSE);
        gateLeft.setPosition(CLOSE_GATE_POSITION);
        gateRight.setPosition(CLOSE_GATE_POSITION);
        kickstandRight.setDirection(Servo.Direction.REVERSE);
        kickstandLeft.setDirection(Servo.Direction.FORWARD);
        kickstandLeft.setPosition(CLOSE_KICKSTAND_POSITION);
        kickstandRight.setPosition(CLOSE_KICKSTAND_POSITION);



        waitForStart();
        while (opModeIsActive()) {
            double lfPower, rfPower, lbPower, rbPower;
            double speed = gamepadEx.getLeftY();
            double turn = gamepadEx.getRightX();
            double strafe = gamepadEx.getLeftX();

            double heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            double adjustedX = strafe * Math.cos(heading) + speed * Math.sin(heading);
            double adjustedY = -strafe * Math.sin(heading) + speed * Math.cos(heading);

            if (drivingMode == DRIVING_MODE.FIELD_CENTRIC) {
                speed = adjustedY;
                strafe = adjustedX;
            }

            lfPower = speed + turn + strafe;
            lbPower = speed + turn - strafe;
            rfPower = speed - turn - strafe;
            rbPower = speed - turn + strafe;

            Math.max(Math.abs(lfPower), Math.abs(lbPower));
            Math.max(Math.abs(rbPower), Math.abs(rfPower));

            double leftMax = Math.max(Math.abs(lbPower), Math.abs(lfPower));
            double rightMax = Math.max(Math.abs(rbPower), Math.abs(rfPower));

            double max = Math.max(leftMax, rightMax);

            if (max > 1) {
                lbPower /= max;
                lfPower /= max;
                rbPower /= max;
                rfPower /= max;
            }
            lfMotor.setPower(lfPower);
            lbMotor.setPower(lbPower);
            rfMotor.setPower(rfPower);
            rbMotor.setPower(rbPower);

            if (gamepadEx.wasJustPressed(GamepadKeys.Button.B)) {

                if (gateLeft.getPosition() == CLOSE_GATE_POSITION) {
                    gateLeft.setPosition(OPEN_GATE_POSITION);
                    gateRight.setPosition(OPEN_GATE_POSITION);
                } else {
                    gateLeft.setPosition(CLOSE_GATE_POSITION);
                    gateRight.setPosition(CLOSE_GATE_POSITION);
                }
            }

            if (gamepadEx.wasJustPressed(GamepadKeys.Button.DPAD_UP)){
                if (kickstandLeft.getPosition() == CLOSE_KICKSTAND_POSITION){
                    kickstandLeft.setPosition(OPEN_KICKSTAND_POSITION);
                    kickstandRight.setPosition(OPEN_KICKSTAND_POSITION);

                }else {
                    kickstandLeft.setPosition(CLOSE_KICKSTAND_POSITION);
                    kickstandRight.setPosition(CLOSE_KICKSTAND_POSITION);
                }



            }
            if (gamepadEx.wasJustPressed(GamepadKeys.Button.A)) {
                if (drivingMode == DRIVING_MODE.FIELD_CENTRIC) drivingMode = DRIVING_MODE.ROBOT_CENTRIC;
                else drivingMode = DRIVING_MODE.FIELD_CENTRIC;
            }

            if (gamepadEx.wasJustPressed(GamepadKeys.Button.X) && drivingMode == DRIVING_MODE.FIELD_CENTRIC) {
                imu.resetYaw();
            }

            if (gamepadEx.isDown(GamepadKeys.Button.LEFT_BUMPER)) {
                intake.setPower(1);
                transfer.setPower(1);
            } else {
                intake.setPower(0);
                transfer.setPower(0);
            }


            telemetry.addLine("hello world this Is Ami and I am the best");

            telemetry.addData("right gate pos",gateRight.getPosition());
            telemetry.addData("left gate pos",gateLeft.getPosition());
            telemetry.update();
            gamepadEx.readButtons();

        }


    }

}



