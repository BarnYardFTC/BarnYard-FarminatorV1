package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.IMU;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.components.MecanumDriveComponent;

public class DriveTrain extends SubsystemBase {


    // -------------------- IMU and Heading --------------------
    public IMU imu;
    private final double initialBotHeading;

    // -------------------- Mecanum Drive --------------------
    public final MecanumDriveComponent mecanumDriveComponent;


    // -------------------- Constructors --------------------


    public DriveTrain() {
        // Initialize mecanum motors
        mecanumDriveComponent = new MecanumDriveComponent();

        // Initialize IMU
        this.imu = BarnRobot.getInstance().farminatorHardware.imu;
        imu.initialize(BarnRobot.getInstance().farminatorHardware.IMU_PARAMETERS);
        imu.resetYaw();

        initialBotHeading = BarnRobot.getInstance().opmodeData.initialBotHeading;

    }


    // -------------------- IMU & Heading Methods --------------------
    public void resetHeading() {
        imu.resetYaw();
    }

    // Get current robot heading in radians (with offset)
    public double getHeading() {
        // IMU yaw is positive clockwise — negate if needed for field-centric math
        return ((imu.getRobotYawPitchRollAngles().getYaw() + 360) % 360 + initialBotHeading) % 360;

    }


    // -------------------- Drive Methods --------------------
    public void drive(double x, double y, double turn) {
        mecanumDriveComponent.driveFieldCentric(x, y, turn);
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
}
