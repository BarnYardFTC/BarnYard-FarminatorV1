package org.firstinspires.ftc.teamcode.testing;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "AlejandroTeleop", group = "Aboba")
public class AlejandroTeleop extends LinearOpMode {
    DcMotor LF, RF, LB, RB;
    IMU imu;

    @Override
    public void runOpMode() {
        LF = hardwareMap.get(DcMotor.class, "leftFrontDrivetrain");
        RF = hardwareMap.get(DcMotor.class, "rightFrontDrivetrain");
        LB = hardwareMap.get(DcMotor.class, "leftBackDrivetrain");
        RB = hardwareMap.get(DcMotor.class, "rightBackDrivetrain");
        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
        ));
        imu.initialize(parameters);
        LF.setDirection(DcMotor.Direction.REVERSE);
        LB.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        double max = 0, LFPower = 0, RFPower = 0, LBPower = 0, RBPower = 0;
        double str = 0, spd = 0, trn = 0;

        while (opModeIsActive()) {

            str = gamepad1.left_stick_x; // lx - str
            spd = -gamepad1.left_stick_y; // ly - spd
            trn = gamepad1.right_stick_x;

            if (gamepad1.x) {
                imu.resetYaw();
                
            }

            double heading = -imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
            double adjustedStr = -spd * Math.sin(heading) + str * Math.cos(heading);
            double adjustedSpd = spd * Math.cos(heading) - str * Math.sin(heading);


            LFPower = adjustedSpd + adjustedStr + trn; //power
            LBPower = adjustedSpd - adjustedStr + trn;
            RFPower = adjustedSpd - adjustedStr - trn;
            RBPower = adjustedSpd + adjustedStr - trn;

            max = Math.abs(Math.max(Math.max(LFPower, LBPower), Math.max(RFPower, RBPower)));

            if (max > 1) {

                LFPower /= max;
                LBPower /= max;
                RFPower /= max;
                RBPower /= max;

            }

            LF.setPower(LFPower);
            RF.setPower(RFPower);
            LB.setPower(LBPower);
            RB.setPower(RBPower);
//-----------------
            telemetry.addData("heading (deg)", Math.toDegrees(heading));
            telemetry.addData("adjSpd", adjustedSpd);
            telemetry.addData("adjStr", adjustedStr);

            telemetry.addData("LF", LFPower);
            telemetry.addData("LB", LBPower);
            telemetry.addData("RF", RFPower);
            telemetry.addData("RB", RBPower);


            telemetry.update();

        }
    }
}
