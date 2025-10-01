package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DriveSubsystem extends SubsystemBase {

    public IMU imu;
    public double headingOffset;

    public final MecanumDriveComponent mecanumDriveComponent;

    RevHubOrientationOnRobot.LogoFacingDirection logoFacingDirection =
            RevHubOrientationOnRobot.LogoFacingDirection.UP;       // Change if logo is not facing up
    RevHubOrientationOnRobot.UsbFacingDirection usbFacingDirection =
            RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;   // Change if USB is not facing forward

    IMU.Parameters imuParameters = new IMU.Parameters(
            new RevHubOrientationOnRobot(logoFacingDirection, usbFacingDirection)
    );


    public DriveSubsystem(HardwareMap hw) {

        mecanumDriveComponent = new MecanumDriveComponent(
                hw.get(DcMotorEx.class, "leftFront"),
                hw.get(DcMotorEx.class, "leftBack"),
                hw.get(DcMotorEx.class, "rightFront"),
                hw.get(DcMotorEx.class, "rightBack")
                );

        imu = hw.get(IMU.class, "imu");
        imu.initialize(imuParameters);

        headingOffset = 0;
    }
    public DriveSubsystem(HardwareMap hw, double headingOffset) {

        mecanumDriveComponent = new MecanumDriveComponent(
                hw.get(DcMotorEx.class, "leftFront"),
                hw.get(DcMotorEx.class, "leftBack"),
                hw.get(DcMotorEx.class, "rightFront"),
                hw.get(DcMotorEx.class, "rightBack")
        );

        imu = hw.get(IMU.class, "imu");
        imu.initialize(imuParameters);

        this.headingOffset = headingOffset;
    }




    public double getHeading(){
        return imu.getRobotYawPitchRollAngles().getYaw() + headingOffset;
    }

    public void drive(double x, double y, double turn) {
        mecanumDriveComponent.driveFieldCentric(x, y, turn, getHeading());
    }

}