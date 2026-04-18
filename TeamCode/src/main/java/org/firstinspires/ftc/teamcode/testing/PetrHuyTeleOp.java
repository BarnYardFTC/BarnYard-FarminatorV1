package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.subsystems.ServoWheel;

@TeleOp(name = "PetyaPid23r", group = "main")
@Config
public class PetrHuyTeleOp extends LinearOpMode {
    DcMotor LF;
    public ServoWheel wheel;

    @Override
    public void runOpMode() {
        wheel = new ServoWheel(hardwareMap);

        LF = hardwareMap.get(DcMotor.class, "main_motor");

        waitForStart();

        double LFPower = 0, spd = 0, turn = 0;

        while (opModeIsActive()) {
//            double servoPos = (turn + 1.0) / 2.0;
//            wheel.turn(servoPos);

            spd = gamepad1.left_stick_y;
            turn = gamepad1.right_stick_x + 0.5;

            LFPower = spd;


            if (LFPower > 1) {

                LFPower /= LFPower;

            }
            if(gamepad1.y){
                wheel.setServoPos(0);
            }

            if (gamepad1.x) {
                wheel.turnLeft();
            }

            if (gamepad1.b) {
                wheel.turnRight();
            }

            if (gamepad1.left_bumper) {
                wheel.setServoPos(0.1);

            }

            if (gamepad1.right_bumper) {
                wheel.setServoPos(-0.1);

            }
            wheel.setServoPos(turn);
            LF.setPower(LFPower);
        }
    }
}
