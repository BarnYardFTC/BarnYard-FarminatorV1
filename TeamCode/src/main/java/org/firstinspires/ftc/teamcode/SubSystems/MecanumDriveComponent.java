package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.seattlesolvers.solverslib.drivebase.RobotDrive;
import com.seattlesolvers.solverslib.geometry.Vector2d;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

public class MecanumDriveComponent extends RobotDrive {
    private double rightSideMultiplier;

    private DcMotorEx leftBack, leftFront, rightBack, rightFront;

    private IMU imu;


    public MecanumDriveComponent(DcMotorEx leftFront, DcMotorEx leftBack, DcMotorEx rightBack, DcMotorEx rightFront) {

    }



    public boolean isRightSideInverted() {
        return rightSideMultiplier == -1.0;
    }


    public void setRightSideInverted(boolean isInverted) {
        rightSideMultiplier = isInverted ? -1.0 : 1.0;
    }

    /**
     * Sets the range of the input, see RobotDrive for more info.
     *
     * @param min The minimum value of the range.
     * @param max The maximum value of the range.
     */
    public void setRange(double min, double max) {
        super.setRange(min, max);
    }

    /**
     * Sets the max speed of the drivebase, see RobotDrive for more info.
     *
     * @param value The maximum output speed.
     */
    public void setMaxSpeed(double value) {
        super.setMaxSpeed(value);
    }


    @Override
    public void stop() {
        leftBack.setPower(0);
        rightBack.setPower(0);
        leftFront.setPower(0);
        rightFront.setPower(0);
    }


    public void driveFieldCentric(double strafeSpeed, double forwardSpeed,
                                  double turnSpeed, double gyroAngle) {
        strafeSpeed = clipRange(strafeSpeed);
        forwardSpeed = clipRange(forwardSpeed);
        turnSpeed = clipRange(turnSpeed);

        Vector2d input = new Vector2d(strafeSpeed, forwardSpeed);
        input = input.rotateBy(-gyroAngle);

        double theta = input.angle();

        double[] wheelSpeeds = new double[4];
        wheelSpeeds[MotorType.kFrontLeft.value] = Math.sin(theta + Math.PI / 4);
        wheelSpeeds[MotorType.kFrontRight.value] = Math.sin(theta - Math.PI / 4);
        wheelSpeeds[MotorType.kBackLeft.value] = Math.sin(theta - Math.PI / 4);
        wheelSpeeds[MotorType.kBackRight.value] = Math.sin(theta + Math.PI / 4);

        normalize(wheelSpeeds, input.magnitude());

        wheelSpeeds[MotorType.kFrontLeft.value] += turnSpeed;
        wheelSpeeds[MotorType.kFrontRight.value] -= turnSpeed;
        wheelSpeeds[MotorType.kBackLeft.value] += turnSpeed;
        wheelSpeeds[MotorType.kBackRight.value] -= turnSpeed;

        normalize(wheelSpeeds);

        driveWithMotorPowers(
                wheelSpeeds[MotorType.kFrontLeft.value],
                wheelSpeeds[MotorType.kFrontRight.value],
                wheelSpeeds[MotorType.kBackLeft.value],
                wheelSpeeds[MotorType.kBackRight.value]
        );
    }


    public void driveFieldCentric(double xSpeed, double ySpeed, double turnSpeed, double gyroAngle, boolean squareInputs) {
        xSpeed = squareInputs ? clipRange(squareInput(xSpeed)) : clipRange(xSpeed);
        ySpeed = squareInputs ? clipRange(squareInput(ySpeed)) : clipRange(ySpeed);
        turnSpeed = squareInputs ? clipRange(squareInput(turnSpeed)) : clipRange(turnSpeed);

        driveFieldCentric(xSpeed, ySpeed, turnSpeed, gyroAngle);
    }

    public void driveWithMotorPowers(double frontLeftSpeed, double frontRightSpeed,
                                     double backLeftSpeed, double backRightSpeed) {
       leftBack.setPower(backLeftSpeed);
       leftFront.setPower(frontLeftSpeed);
       rightBack.setPower(backRightSpeed);
       rightFront.setPower(frontRightSpeed);

    }

}
