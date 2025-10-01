package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DriveSubsystem extends SubsystemBase {

    public IMU imu;
    public final MecanumDriveComponent drive;

    // Define hub mounting orientation
    RevHubOrientationOnRobot.LogoFacingDirection logoFacingDirection =
            RevHubOrientationOnRobot.LogoFacingDirection.UP;       // Change if logo is not facing up
    RevHubOrientationOnRobot.UsbFacingDirection usbFacingDirection =
            RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;   // Change if USB is not facing forward

    // Create IMU parameters using hub orientation
    IMU.Parameters imuParameters = new IMU.Parameters(
            new RevHubOrientationOnRobot(logoFacingDirection, usbFacingDirection)
    );

//    private final Encoder m_left, m_right;
//
//    private final double WHEEL_DIAMETER;

    /**
     * Creates a new DriveSubsystem.
     */
    public DriveSubsystem(HardwareMap hw) {
//        m_left = leftMotor.encoder;
//        m_right = rightMotor.encoder;

//        WHEEL_DIAMETER = diameter;
        drive = new MecanumDriveComponent(
                hw.get(DcMotorEx.class, "leftFront"),
                hw.get(DcMotorEx.class, "leftBack"),
                hw.get(DcMotorEx.class, "rightFront"),
                hw.get(DcMotorEx.class, "rightBack")
                );

        imu = hw.get(IMU.class, "imu");
        imu.initialize(imuParameters);
    }

    public double getHeading(){
        return imu.getRobotYawPitchRollAngles().getYaw();
    }

    public void drive(double x, double y, double turn) {
        drive.driveFieldCentric(x, y, turn, getHeading());
    }

//    public double getLeftEncoderVal() {
//        return m_left.getPosition();
//    }
//
//    public double getLeftEncoderDistance() {
//        return m_left.getRevolutions() * WHEEL_DIAMETER * Math.PI;
//    }

//    public double getRightEncoderVal() {
//        return m_right.getPosition();
//    }
//
//    public double getRightEncoderDistance() {
//        return m_right.getRevolutions() * WHEEL_DIAMETER * Math.PI;
//    }

//    public void resetEncoders() {
//        m_left.reset();
//        m_right.reset();
//    }
//
//    public double getAverageEncoderDistance() {
//        return (getLeftEncoderDistance() + getRightEncoderDistance()) / 2.0;
//    }

}