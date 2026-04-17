package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
// or use @Autonomous if needed

@TeleOp(name = "Basic Drive", group = "Learning")
public class EmptyLinearOpMode extends LinearOpMode {

    DcMotor lf;
    DcMotor lb;
    DcMotor rf;
    DcMotor rb;

    @Override
    public void runOpMode() {
        lf = hardwareMap.get(DcMotor.class, "leftFront");
        lb = hardwareMap.get(DcMotor.class, "leftBack");
        rf = hardwareMap.get(DcMotor.class, "rightFront");
        rb = hardwareMap.get(DcMotor.class, "rightBack");
        lf.setDirection(DcMotor.Direction.REVERSE);
        lb.setDirection(DcMotor.Direction.REVERSE);

        // === INIT PHASE (before start is pressed) ===


        waitForStart();


        // === RUN PHASE (after start is pressed) ===
        while (opModeIsActive()) {
            double power = 0.6;
            double lfpower = 0;
            double lbpower = 0;
            double rfpower = 0;
            double rbpower = 0;
            if (gamepad1.y) {
                lfpower = power;
                lbpower = power;
                rfpower = power;
                rbpower = power;
            } else if (gamepad1.a) {
                lfpower = -power;
                lbpower = -power;
                rfpower = -power;
                rbpower = -power;
            }
            else if (gamepad1.b) { //right
                lfpower = power;
                lbpower = -power;
                rfpower = -power;
                rbpower = power;
            }
            else if (gamepad1.x) {
                lfpower = -power;
                lbpower = power;
                rfpower = power;
                rbpower = -power;
            }
            else if (gamepad1.right_bumper) { //right
                lfpower = power;
                lbpower = power;
                rfpower = -power;
                rbpower = -power;
            }
            else if (gamepad1.left_bumper) { //right
                lfpower = -power;
                lbpower = -power;
                rfpower = power;
                rbpower = power;
            }
            lf.setPower(lfpower);
            lb.setPower(lbpower);
            rf.setPower(rfpower);
            rb.setPower(rbpower);



        }


        // === STOP PHASE (optional, runs after opMode ends) ===


    }
}