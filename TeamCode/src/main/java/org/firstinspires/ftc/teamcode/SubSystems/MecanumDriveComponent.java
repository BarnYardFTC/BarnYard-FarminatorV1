package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class MecanumDriveComponent {

    /* =========================
       HARDWARE REFERENCES
       ========================= */
    private final DcMotor leftFront;
    private final DcMotor rightFront;
    private final DcMotor leftBack;
    private final DcMotor rightBack;

    /* =========================
       MOVEMENT STATE
       ========================= */
    private double spdX;
    private double spdY;
    private double spdTurn;

    private double speedModifier;

    /* =========================
       CONSTANTS
       ========================= */

    private static final double SLOW_SPEED = 0.3;
    private static final double FAST_SPEED = 1.0;

    /* =========================
       CONSTRUCTOR
       ========================= */
    public MecanumDriveComponent(DcMotor leftFront, DcMotor leftBack, DcMotor rightFront, DcMotor rightBack) {
        this.leftFront = leftFront;
        this.rightFront = rightFront;
        this.leftBack = leftBack;
        this.rightBack = rightBack;

        initMotor(DcMotorSimple.Direction.REVERSE, leftFront);
        initMotor(DcMotorSimple.Direction.FORWARD, rightFront);
        initMotor(DcMotorSimple.Direction.REVERSE, leftBack);
        initMotor(DcMotorSimple.Direction.FORWARD, rightBack);

        initData();
    }


    /* =========================
       INITIALIZATION HELPERS
       ========================= */
    private void initMotor(DcMotorSimple.Direction direction, DcMotor motor) {
        motor.setDirection(direction);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    private void initData() {
        spdX = 0;
        spdY = 0;
        spdTurn = 0;
        activateFastMode();
    }

    /* =========================
       SPEED MODE CONTROL
       ========================= */
    public void activateSlowMode() {
        speedModifier = SLOW_SPEED;
    }

    public void activateFastMode() {
        speedModifier = FAST_SPEED;
    }


    /* =========================
       MOVEMENT CONTROL
       ========================= */
    public void setSpeed(double spdX, double spdY, double spdTurn) {
        this.spdX = spdX;
        this.spdY = spdY;
        this.spdTurn = spdTurn;
    }

    public void translateSpeedToPower() {
        double lf = spdY + spdX + spdTurn;
        double lb = spdY - spdX + spdTurn;
        double rf = spdY - spdX - spdTurn;
        double rb = spdY + spdX - spdTurn;

//        // Normalize powers if needed
        double maxPower = Math.max(Math.max(Math.abs(lf), Math.abs(lb)),
                Math.max(Math.abs(rf), Math.abs(rb)));
        if (maxPower > 1.0) {
            lf /= maxPower;
            lb /= maxPower;
            rf /= maxPower;
            rb /= maxPower;
        }

        // Apply speed modifier
        lf *= speedModifier;
        lb *= speedModifier;
        rf *= speedModifier;
        rb *= speedModifier;

        // Set motor powers
        leftFront.setPower(lf);
        leftBack.setPower(lb);
        rightFront.setPower(rf);
        rightBack.setPower(rb);
    }

    public void adjustSpeedForHeading(double heading) {
        double adjustedX = spdX * Math.cos(heading) + spdY * Math.sin(heading);
        double adjustedY = - spdX * Math.sin(heading) + spdY * Math.cos(heading);

        spdX = adjustedX;
        spdY = adjustedY;
    }



    public double getSpdX(){
        return spdX;
    }

    public double getSpdY(){
        return spdY;
    }

    public double getSpdTurn(){
        return spdTurn;
    }

    public void driveFieldCentric(double x, double y, double turn, double heading) {
        setSpeed(x, y, turn);
        adjustSpeedForHeading(heading);
        translateSpeedToPower();
    }
}