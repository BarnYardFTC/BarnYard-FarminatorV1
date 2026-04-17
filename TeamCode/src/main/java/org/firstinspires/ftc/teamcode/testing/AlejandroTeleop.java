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

        waitForStart();

        double max = 0, LFPower = 0, RFPower = 0, LBPower = 0, RBPower = 0;
        double str = 0, spd = 0, trn =0;

        while (opModeIsActive()) {

            str = -gamepad1.left_stick_x;
            spd = -gamepad1.left_stick_y;
            trn = gamepad1.right_stick_x;

            LFPower = spd-str+trn; //power
            LBPower = spd+str+trn;
            RFPower = spd+str-trn;
            RBPower = spd-str-trn;

            max = Math.abs(Math.max(Math.max(LFPower, LBPower), Math.max(RFPower, RBPower)));

            if (max>1){

                LFPower /= max;
                LBPower /= max;
                RFPower /= max;
                RBPower /= max;

            }

            LF.setPower(LFPower);
            RF.setPower(RFPower);
            LB.setPower(LBPower);
            RB.setPower(RBPower);
        }
    }
}
