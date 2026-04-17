package org.firstinspires.ftc.teamcode.testing;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.subsystems.ServoWheel;

@TeleOp(name = "PetyaPid23r", group = "main")
@Config
public class petrHuyTeleOp extends LinearOpMode {
    DcMotor LF;
    public ServoWheel wheel;

    @Override
    public void runOpMode() {
        wheel = new ServoWheel(hardwareMap);

        LF = hardwareMap.get(DcMotor.class, "main_motor");

        waitForStart();

        double LFPower = 0, spd = 0, turn = 0;

        while (opModeIsActive()) {

            double servoPos = (turn + 1.0) / 2.0;
            wheel.turn(servoPos);

            spd = gamepad1.left_stick_y;
            turn = gamepad1.right_stick_x;

            LFPower = spd;



            if (LFPower>1){

                LFPower /= LFPower;

            }

            LF.setPower(LFPower);
        }
    }
}
