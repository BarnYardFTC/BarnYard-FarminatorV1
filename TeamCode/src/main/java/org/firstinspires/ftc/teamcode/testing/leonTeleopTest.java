package org.firstinspires.ftc.teamcode.testing;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class leonTeleopTest extends LinearOpMode {
    DcMotorEx shooter, intake;
    CRServo leftFront, leftBack, rightFront, rightBack;


    @Override
    public void runOpMode() throws InterruptedException {
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        leftBack = hardwareMap.get(CRServo.class, "leftBack");
        leftFront = hardwareMap.get(CRServo.class, "leftFront");
        rightBack = hardwareMap.get(CRServo.class, "rightBack");
        rightFront = hardwareMap.get(CRServo.class, "rightFront");

        waitForStart();
        while(opModeIsActive()){

            shooter.setPower(0.7);
            intake.setPower(1);
            leftBack.setPower(1);
            leftFront.setPower(1);
            rightBack.setPower(1);
            rightFront.setPower(1);
        }
    }
}
