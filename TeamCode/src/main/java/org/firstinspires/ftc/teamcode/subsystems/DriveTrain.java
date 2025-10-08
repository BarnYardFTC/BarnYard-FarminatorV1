package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.IMU;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDController;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.components.MecanumDriveComponent;

public class DriveTrain extends SubsystemBase {


    // -------------------- IMU and Heading --------------------
    public IMU imu;
    private final double initialBotHeading;

    // -------------------- Mecanum Drive --------------------
    public final MecanumDriveComponent mecanumDriveComponent;


    private final PIDController pidControllerYaw;
    public static double pYaw = 0.05, dYaw = 0; // ToDo: tune PID

    private final double TARGET_RANGE = 0.1;


    // -------------------- Constructors --------------------


    public DriveTrain() {
        // Initialize mecanum motors
        mecanumDriveComponent = new MecanumDriveComponent();

        // Initialize IMU
        this.imu = BarnRobot.getInstance().farminatorHardware.imu;
        imu.initialize(BarnRobot.getInstance().farminatorHardware.IMU_PARAMETERS);
        imu.resetYaw();

        initialBotHeading = BarnRobot.getInstance().opmodeData.initialBotHeading;

        pidControllerYaw = new PIDController(pYaw, 0, dYaw);
    }


    // -------------------- IMU & Heading Methods --------------------
    public void resetHeading() {
        imu.resetYaw();
    }

    // Get current robot heading in radians (with offset)
    public double getHeadingRadians() {
        // IMU yaw is positive clockwise — negate if needed for field-centric math
        return Math.toRadians(imu.getRobotYawPitchRollAngles().getYaw() + initialBotHeading);
    }


    // -------------------- Drive Methods --------------------
    public void drive(double x, double y, double turn) {
        mecanumDriveComponent.driveFieldCentric(x, y, turn, getHeadingRadians());
    }


    public void alignToGoal(double yawDiff) {
        if(!BarnRobot.getInstance().limelight.isValid())return;
        double spdT = diffToSpeed(yawDiff);
        drive(0, 0, spdT);
    }



    private double diffToSpeed(double yawDiff) {
//        double target = yawDiff > 0 ? TARGET_RANGE : -TARGET_RANGE;
        return pidControllerYaw.calculate(yawDiff, 0);
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

    public Command resetHeadingCommand() {
        return new InstantCommand(
                () -> resetHeading(), this
        );
    }

    public Command alignToTagCommand() {
        return new RunCommand(
                () -> alignToGoal(
                        BarnRobot.getInstance().limelight.getDyaw()
                ),
                this
        );
    }
    public void displayPower(){
        BarnRobot.getInstance().telemetry.addData("spdX x:", mecanumDriveComponent.getSpdX());
        BarnRobot.getInstance().telemetry.addData("spdX y:", mecanumDriveComponent.getSpdY());
        BarnRobot.getInstance().telemetry.addData("spdX t:", mecanumDriveComponent.getSpdTurn());
    }
}
