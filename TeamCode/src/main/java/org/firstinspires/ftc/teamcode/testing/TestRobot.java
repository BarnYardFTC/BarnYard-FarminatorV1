package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class TestRobot extends LinearOpMode
{

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor shooter = hardwareMap.get(DcMotor.class, "shooter");
        DcMotor intake = hardwareMap.get(DcMotor.class, "intake");
        CRServo leftFrontTransfer = hardwareMap.get(CRServo.class, "leftFrontTransfer");
        CRServo rightFrontTransfer = hardwareMap.get(CRServo.class, "rightFrontTransfer");
        CRServo leftBackTransfer = hardwareMap.get(CRServo.class, "leftBackTransfer");
        CRServo rightBackTransfer = hardwareMap.get(CRServo.class, "rightBackTransfer");

        leftBackTransfer.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFrontTransfer.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while(opModeIsActive()){
            shooter.setPower(gamepad1.right_trigger);
            intake.setPower(gamepad1.left_trigger);

            if (gamepad1.a){
                rightFrontTransfer.setPower(1);
                rightBackTransfer.setPower(1);
                leftFrontTransfer.setPower(1);
                leftBackTransfer.setPower(1);
            }
            else {
                rightFrontTransfer.setPower(0);
                rightBackTransfer.setPower(0);
                leftFrontTransfer.setPower(0);
                leftBackTransfer.setPower(0);
            }

            telemetry.addData("shooter power", shooter.getPower());
            telemetry.addData("intake power", intake.getPower());
            if (rightFrontTransfer.getPower() != 0) telemetry.addData("transfer", "on");
            else telemetry.addData("transfer", "off");
            telemetry.update();

        }


    }
}
