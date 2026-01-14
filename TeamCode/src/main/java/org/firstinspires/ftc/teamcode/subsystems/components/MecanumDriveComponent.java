package org.firstinspires.ftc.teamcode.subsystems.components;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.BarnRobot;

public class MecanumDriveComponent {

    /* =========================
       HARDWARE REFERENCES
       ========================= */
    private final DcMotorEx leftFront;
    private final DcMotorEx rightFront;
    private final DcMotorEx leftBack;
    private final DcMotorEx rightBack;

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

    private static final double SLOW_SPEED = 0.5;
    private static final double FAST_SPEED = 1.0;

    /* =========================
       CONSTRUCTOR
       ========================= */
    public MecanumDriveComponent() {
        this.leftFront = BarnRobot.getInstance().robotHardware.leftFrontDrivetrain;
        this.rightFront = BarnRobot.getInstance().robotHardware.rightFrontDrivetrain;
        this.leftBack = BarnRobot.getInstance().robotHardware.leftBackDrivetrain;
        this.rightBack = BarnRobot.getInstance().robotHardware.rightBackDrivetrain;


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

    public void adjustSpeedForHeading() {
        double heading = Math.toRadians(
                (getHeading() - BarnRobot.getInstance().opmodeData.fieldReferenceHeading + 540) % 360 -180
        );
        double adjustedX = spdX * Math.cos(heading) + spdY * Math.sin(heading);
        double adjustedY = - spdX * Math.sin(heading) + spdY * Math.cos(heading);

        spdX = adjustedX;
        spdY = adjustedY;
    }

    private double getHeading(){
        return Math.toDegrees(BarnRobot.getInstance().pinpointLocalizer.getPose().heading.toDouble());
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

    public void driveFieldCentric(double x, double y, double turn) {
        setSpeed(x, y, turn);
        adjustSpeedForHeading();
        translateSpeedToPower();
    }

    public void driveNonFieldCentric(double x, double y, double turn){
        setSpeed(x, y, turn);
        translateSpeedToPower();
    }

    public void turnOnly(double turn) {
        setSpeed(0,0,turn);
        translateSpeedToPower();
    }

    //func to maintain position, should be implemented
    //the same way as turnOnly()
    public void maintainPos(double turn, Pose2d stopPose) {
        double speedX;
        double speedY;
        Pose2d currentPose = BarnRobot.getInstance().pinpointLocalizer.getPose();
        //speed determination probably needs adjustment
        speedX = (stopPose.position.x - currentPose.position.x);
        speedY = (stopPose.position.y - currentPose.position.y);
        setSpeed(speedX, speedY, turn);
        translateSpeedToPower();
    }
}