package org.firstinspires.ftc.teamcode.testing;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class teleopSystemTest extends LinearOpMode {
    DcMotorEx shooter, intake;
    CRServo leftFront, leftBack, rightFront, rightBack;


    @Override
    public void runOpMode() throws InterruptedException {
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        leftBack = hardwareMap.get(CRServo.class, "leftBackTransfer");
        leftFront = hardwareMap.get(CRServo.class, "leftFrontTransfer");
        rightBack = hardwareMap.get(CRServo.class, "rightBackTransfer");
        rightFront = hardwareMap.get(CRServo.class, "rightFrontTransfer");

        waitForStart();
        while (opModeIsActive()) {



            if (gamepad1.a) leftBack.setPower(1);
            else leftBack.setPower(0);
            if (gamepad1.b) leftFront.setPower(1);
            else leftFront.setPower(0);
            if (gamepad1.x) rightBack.setPower(1);
            else rightBack.setPower(0);
            if (gamepad1.y) rightFront.setPower(1);
            else rightFront.setPower(0);

            if (gamepad1.left_trigger > 0.5) shooter.setPower(gamepad1.left_trigger);
            else   shooter.setPower(0);
            if (gamepad1.right_trigger > 0.5) intake.setPower(gamepad1.left_trigger);
            else intake.setPower(0);

            telemetry.addData("leftBack a:", gamepad1.a);
            telemetry.addData("leftFront b:", gamepad1.b);
            telemetry.addData("rightBack x:", gamepad1.x);
            telemetry.addData("rightFront y:", gamepad1.y);
            telemetry.addData("intake rTrigger:", gamepad1.right_trigger);
            telemetry.addData("shooter lTrigger:", gamepad1.left_trigger);

        }
    }
}
