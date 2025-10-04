package org.firstinspires.ftc.teamcode.SubSystems;




import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.BarnRobot;

public class DriveTrain extends SubsystemBase {

    // IMU and heading
    public IMU imu;
    private double headingOffset;

    // Mecanum drive component
    public final MecanumDriveComponent mecanumDriveComponent;

    // IMU orientation configuration
    private static final RevHubOrientationOnRobot.LogoFacingDirection LOGO_FACING =
            RevHubOrientationOnRobot.LogoFacingDirection.UP;
    private static final RevHubOrientationOnRobot.UsbFacingDirection USB_FACING =
            RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;

    private final IMU.Parameters imuParameters =
            new IMU.Parameters(new RevHubOrientationOnRobot(LOGO_FACING, USB_FACING));

    // Constructor with default heading offset = 0
    public DriveTrain() {
        this(0.0);
    }

    // Constructor with custom heading offset
    public DriveTrain(double headingOffset) {
        // Initialize mecanum motors
        mecanumDriveComponent = new MecanumDriveComponent(
                BarnRobot.getInstance().hardwareMap.get(DcMotorEx.class, "leftFront"),
                BarnRobot.getInstance().hardwareMap.get(DcMotorEx.class, "leftBack"),
                BarnRobot.getInstance().hardwareMap.get(DcMotorEx.class, "rightFront"),
                BarnRobot.getInstance().hardwareMap.get(DcMotorEx.class, "rightBack")
        );

        // Initialize IMU
        imu = BarnRobot.getInstance().hardwareMap.get(IMU.class, "imu");
        imu.initialize(imuParameters);
        imu.resetYaw();

        // Set heading offset
        this.headingOffset = headingOffset;
    }

    // Reset IMU heading to zero
    public void resetHeading() {
        imu.resetYaw();
    }

    // Get current robot heading in radians, including optional offset
    public double getHeadingRadians() {
        // IMU yaw is positive clockwise, negate if needed for field-centric math
        return Math.toRadians(imu.getRobotYawPitchRollAngles().getYaw() + headingOffset);
    }

    // Drive the robot with field-centric controls
    public void drive(double x, double y, double turn) {
        mecanumDriveComponent.driveFieldCentric(x, y, turn, getHeadingRadians());
    }

}
