package org.firstinspires.ftc.teamcode.testing;


import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.ftccommon.internal.manualcontrol.parameters.ServoPulseWidthParameters;


@TeleOp(name = "AlejandroTeleop", group = "Aboba")
public class AlejandroTeleop extends LinearOpMode {
    DcMotor LF, RF, LB, RB;
    Servo servo1;
    GamepadEx gamepad;
    public double servoPos = 0.5;
    public double MIN = 0.35;
    public double MAX = 0.9;
    @Override
    public void runOpMode() {
        LF = hardwareMap.get(DcMotor.class, "leftFrontDrivetrain");
        RF = hardwareMap.get(DcMotor.class, "rightFrontDrivetrain");
        LB = hardwareMap.get(DcMotor.class, "leftBackDrivetrain");
        RB = hardwareMap.get(DcMotor.class, "rightBackDrivetrain");

        servo1 = hardwareMap.get(Servo.class, "shooterHood");

        gamepad = new GamepadEx(this.gamepad1);


        servo1.setDirection(Servo.Direction.REVERSE);
        servo1.scaleRange(0,1);

        LF.setDirection(DcMotor.Direction.REVERSE);
        LB.setDirection(DcMotor.Direction.REVERSE);

        double x, y, z, LFPower, RFPower, LBPower, RBPower, max;

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad.wasJustPressed(GamepadKeys.Button.X)) {
                servoUp();
            } else if (gamepad.wasJustPressed(GamepadKeys.Button.Y)) {
                servoDown();
            }

            gamepad.readButtons();
        }
    }

    public void servoUp() {
        servoPos += 0.15;
        if (servoPos > MAX) servoPos = MAX;
        servo1.setPosition(servoPos);
    }
    public void servoDown() {
        servoPos -= 0.15;
        if (servoPos < MIN) servoPos = MIN;
        servo1.setPosition(servoPos);
    }
}
