package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDController;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.components.MecanumDriveComponent;

public class DriveTrain extends SubsystemBase {


    // -------------------- IMU and Heading --------------------
    public IMU imu;
    private double headingOffset;

    private final PIDController pidControllerYaw;
    public static double pYaw = 0.05, dYaw = 0; // ToDo: tune PID

    private final double TARGET_RANGE = 0.1;


    // -------------------- Mecanum Drive --------------------
    public final MecanumDriveComponent mecanumDriveComponent;


    // -------------------- IMU Orientation Configuration --------------------
    private static final RevHubOrientationOnRobot.LogoFacingDirection LOGO_FACING =
            RevHubOrientationOnRobot.LogoFacingDirection.UP;
    private static final RevHubOrientationOnRobot.UsbFacingDirection USB_FACING =
            RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;

    private final IMU.Parameters imuParameters =
            new IMU.Parameters(new RevHubOrientationOnRobot(LOGO_FACING, USB_FACING));


    // -------------------- Constructors --------------------
    // Default heading offset = 0
    public DriveTrain() {
        this(0.0);
    }


    // Custom heading offset
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

        pidControllerYaw = new PIDController(pYaw, 0, dYaw);
    }


    // -------------------- IMU & Heading Methods --------------------
    public void resetHeading() {
        imu.resetYaw();
    }

    // Get current robot heading in radians (with offset)
    public double getHeadingRadians() {
        // IMU yaw is positive clockwise — negate if needed for field-centric math
        return Math.toRadians(imu.getRobotYawPitchRollAngles().getYaw() + headingOffset);
    }


    // -------------------- Drive Methods --------------------
    public void drive(double x, double y, double turn) {
        mecanumDriveComponent.driveFieldCentric(x, y, turn, getHeadingRadians());
    }

    public void alignToGoal(double yawDiff) {
        double spdT = diffToSpeed(yawDiff);
        drive(0, 0, spdT);
    }


    // -------------------- Commands --------------------
    public Command driveCommand() {
        return new RunCommand(
                () -> drive(
                        BarnRobot.getInstance().gamepadEx1.getLeftX(),
                        BarnRobot.getInstance().gamepadEx1.getLeftY(),
                        BarnRobot.getInstance().gamepadEx1.getRightX()
                ),
                this
        );
    }

    private double diffToSpeed(double yawDiff) {
        double target = yawDiff > 0 ? TARGET_RANGE : -TARGET_RANGE;
        return pidControllerYaw.calculate(yawDiff, target);
    }

    public Command alignToTag() {
        return new RunCommand(
                () -> alignToGoal(
                        BarnRobot.getInstance().limelight.getDyaw()
                ),
                this
        );
    }
}
