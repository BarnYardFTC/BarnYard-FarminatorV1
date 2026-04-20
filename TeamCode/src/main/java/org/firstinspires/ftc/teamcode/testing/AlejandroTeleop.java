package org.firstinspires.ftc.teamcode.testing;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "AlejandroTeleop", group = "Aboba")
public class AlejandroTeleop extends LinearOpMode {
    DcMotor LF, RF, LB, RB;
    @Override
    public void runOpMode() {
        LF = hardwareMap.get(DcMotor.class, "leftFrontDrivetrain");
        RF = hardwareMap.get(DcMotor.class, "rightFrontDrivetrain");
        LB = hardwareMap.get(DcMotor.class, "leftBackDrivetrain");
        RB = hardwareMap.get(DcMotor.class, "rightBackDrivetrain");

        LF.setDirection(DcMotor.Direction.REVERSE);
        LB.setDirection(DcMotor.Direction.REVERSE);

        double x, y, z, LFPower, RFPower, LBPower, RBPower, max;
        waitForStart();

        while (opModeIsActive()) {
            y = -gamepad1.left_stick_y;
            x = gamepad1.left_stick_x;
            z = gamepad1.right_stick_x;

            LFPower = y + x + z;
            RFPower = y - x - z;
            LBPower = y - x + z;
            RBPower = y + x - z;

            max = Math.max(LFPower, RFPower);
        }
    }
}
