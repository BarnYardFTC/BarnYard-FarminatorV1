package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

@TeleOp(name="Itamar", group="Linear OpMode")

public class Lesson3Itamar extends LinearOpMode {

    DcMotor LF, RF, LB, RB;

    // האם מצב איטי פעיל
    boolean slowMode = false;

    // בודק אם X היה לחוץ בלופ הקודם
    boolean lastX = false;

    @Override
    public void runOpMode(){

        LF = hardwareMap.get(DcMotor.class, "leftFrontDrivetrain");
        RF = hardwareMap.get(DcMotor.class, "rightFrontDrivetrain");
        LB = hardwareMap.get(DcMotor.class, "leftBackDrivetrain");
        RB = hardwareMap.get(DcMotor.class, "rightBackDrivetrain");

        LF.setDirection(DcMotorSimple.Direction.REVERSE);
        LB.setDirection(DcMotorSimple.Direction.REVERSE);


        IMU imu = hardwareMap.get(IMU.class, "imu");

        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));

        imu.initialize(parameters);

        waitForStart();

        while (opModeIsActive()) {

            double speed = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;
            double strafe = gamepad1.left_stick_x;

            // =========================
            // TOGGLE של מצב איטי
            // =========================

            // אם X לחוץ ועכשיו הוא לא היה לחוץ קודם
            if (gamepad1.x && !lastX) {

                // מחליף בין true ל false
                slowMode = !slowMode;
            }

            // שומר האם X לחוץ עכשיו
            lastX = gamepad1.x;

            // =========================
            // מהירות רגילה / איטית
            // =========================

            double speedMultiplier = 1.0;

            if (slowMode) {
                speedMultiplier = 0.4;
            }

            double leftFront = speed + turn + strafe;
            double leftRear = speed + turn - strafe;
            double rightFront = speed - turn - strafe;
            double rightRear = speed - turn + strafe;

            double leftMax = Math.max(Math.abs(leftRear), Math.abs(leftFront));
            double rightMax = Math.max(Math.abs(rightRear), Math.abs(rightFront));

            double max = Math.max(leftMax, rightMax);

            if (max > 1) {
                leftRear /= max;
                leftFront /= max;
                rightRear /= max;
                rightFront /= max;
            }

            // האטת הרובוט
            leftFront *= speedMultiplier;
            leftRear *= speedMultiplier;
            rightFront *= speedMultiplier;
            rightRear *= speedMultiplier;

            LF.setPower(leftFront);
            LB.setPower(leftRear);
            RF.setPower(rightFront);
            RB.setPower(rightRear);

            telemetry.addData("Slow Mode", slowMode);
            telemetry.update();
        }
    }
}