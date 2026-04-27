package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.opencv.core.Mat;


@TeleOp(name="Itamar", group="Linear OpMode")
@Disabled

public class Lesson2Itamar extends LinearOpMode {
    DcMotor LF, RF, LB, RB;

    @Override
    public void runOpMode(){

        LF = hardwareMap.get(DcMotor.class, "leftFrontDrivetrain");
        RF = hardwareMap.get(DcMotor.class, "rightFrontDrivetrain");
        LB = hardwareMap.get(DcMotor.class, "leftBackDrivetrain");
        RB = hardwareMap.get(DcMotor.class, "rightBackDrivetrain");

        LF.setDirection(DcMotorSimple.Direction.REVERSE);
        LB.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        double speed = -gamepad1.left_stick_y;   // i am coll guy   1
        double turn = gamepad1.right_stick_x;
        double strafe = gamepad1.left_stick_x; //                   1

        double leftFront = speed + turn + strafe; // -3 - 3 power | 2 -> 1
        double leftRear = speed + turn - strafe; // | 0 -> 0
        double rightFront = speed - turn - strafe; // | 0
        double rightRear = speed - turn + strafe; //Bear = back | 1


        Math.max(Math.abs(leftRear), Math.abs(leftFront));
        Math.max(Math.abs(rightRear), Math.abs(rightFront));

        double leftMax = Math.max(Math.abs(leftRear), Math.abs(leftFront));
        double rightMax = Math.max(Math.abs(rightRear), Math.abs(rightFront));

        double max = Math.max(leftMax, rightMax);

        if (max > 1){
            leftRear /= max;
            leftFront /= max;
            rightRear /= max;
            rightFront /= max;
        }

        LF.setPower(leftFront);
        LB.setPower(leftRear);
        RF.setPower(rightFront);
        RB.setPower(rightRear);
    }
}




