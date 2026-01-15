package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Configuration Test", group = "test")
@Disabled
public class ConfigurationTest extends LinearOpMode {

    // Mechanisms
    DcMotorEx shooter, intake;

    // Transfers
    CRServo leftFront, leftBack, rightFront, rightBack;

    // Drivetrain
    DcMotorEx leftFrontDrive, leftBackDrive, rightFrontDrive, rightBackDrive;

    @Override
    public void runOpMode() throws InterruptedException {
        // --- Mechanisms ---
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        intake = hardwareMap.get(DcMotorEx.class, "intake");

        // --- Transfers ---
        leftBack = hardwareMap.get(CRServo.class, "leftBackTransfer");
        leftFront = hardwareMap.get(CRServo.class, "leftFrontTransfer");
        rightBack = hardwareMap.get(CRServo.class, "rightBackTransfer");
        rightFront = hardwareMap.get(CRServo.class, "rightFrontTransfer");

        // --- Drivetrain Motors ---
        leftFrontDrive = hardwareMap.get(DcMotorEx.class, "leftFrontDrivetrain");
        leftBackDrive = hardwareMap.get(DcMotorEx.class, "leftBackDrivetrain");
        rightFrontDrive = hardwareMap.get(DcMotorEx.class, "rightFrontDrivetrain");
        rightBackDrive = hardwareMap.get(DcMotorEx.class, "rightBackDrivetrain");

        waitForStart();

        while (opModeIsActive()) {

            // --- Transfer Servos Test ---
            leftBack.setPower(gamepad1.a ? 1 : 0);
            leftFront.setPower(gamepad1.b ? 1 : 0);
            rightBack.setPower(gamepad1.x ? 1 : 0);
            rightFront.setPower(gamepad1.y ? 1 : 0);

            // --- Shooter & Intake Test ---
            shooter.setPower(gamepad1.left_trigger > 0.5 ? gamepad1.left_trigger : 0);
            intake.setPower(gamepad1.right_trigger > 0.5 ? gamepad1.right_trigger : 0);

            // --- Drivetrain Motors Test ---
            // Each direction on the dpad controls one motor
            leftFrontDrive.setPower(gamepad1.dpad_up ? 0.5 : 0);
            rightFrontDrive.setPower(gamepad1.dpad_right ? 0.5 : 0);
            leftBackDrive.setPower(gamepad1.dpad_left ? 0.5 : 0);
            rightBackDrive.setPower(gamepad1.dpad_down ? 0.5 : 0);

            // --- Telemetry Feedback ---
            telemetry.addLine("=== Transfer Servos ===");
            telemetry.addData("Left Back (A)", gamepad1.a);
            telemetry.addData("Left Front (B)", gamepad1.b);
            telemetry.addData("Right Back (X)", gamepad1.x);
            telemetry.addData("Right Front (Y)", gamepad1.y);

            telemetry.addLine("\n=== Mechanisms ===");
            telemetry.addData("Shooter (Left Trigger)", gamepad1.left_trigger);
            telemetry.addData("Intake (Right Trigger)", gamepad1.right_trigger);

            telemetry.addLine("\n=== Drivetrain Motors ===");
            telemetry.addData("Left Front (Dpad Up)", gamepad1.dpad_up);
            telemetry.addData("Right Front (Dpad Right)", gamepad1.dpad_right);
            telemetry.addData("Left Back (Dpad Left)", gamepad1.dpad_left);
            telemetry.addData("Right Back (Dpad Down)", gamepad1.dpad_down);

            telemetry.update();
        }
    }
}
