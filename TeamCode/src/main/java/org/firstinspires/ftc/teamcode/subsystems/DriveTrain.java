package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.IMU;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDController;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.components.MecanumDriveComponent;

@Config
public class DriveTrain extends SubsystemBase {

    // -------------------- Constants --------------------
    public static double pYaw = 0.05, dYaw = 0; // TODO: Tune PID

    // -------------------- Hardware --------------------
    private final IMU imu;
    private final MecanumDriveComponent mecanumDriveComponent;

    // -------------------- Control --------------------
    private final PIDController pidControllerYaw;
    private final double initialBotHeading;

    // -------------------- Constructor --------------------
    public DriveTrain() {
        // Initialize drive component
        mecanumDriveComponent = new MecanumDriveComponent();

        // Initialize IMU
        imu = BarnRobot.getInstance().farminatorHardware.imu;
        imu.initialize(BarnRobot.getInstance().farminatorHardware.IMU_PARAMETERS);
        imu.resetYaw();

        // Heading offset and PID
        initialBotHeading = BarnRobot.getInstance().opmodeData.initialBotHeading;
        pidControllerYaw = new PIDController(pYaw, 0, dYaw);
    }

    // ============================================================
    //                       HEADING & IMU
    // ============================================================

    public void resetHeading() {
        imu.resetYaw();
    }

    /** Get current robot heading in radians (with initial offset) */
    public double getHeadingRadians() {
        // IMU yaw is positive clockwise — negate if needed for field-centric math
        return Math.toRadians(imu.getRobotYawPitchRollAngles().getYaw() + initialBotHeading);
    }

    /** Get current robot heading in degrees */
    public double getHeadingDegrees() {
        return Math.toDegrees(getHeadingRadians());
    }

    // ============================================================
    //                         DRIVING
    // ============================================================

    public void drive(double x, double y, double turn) {
        mecanumDriveComponent.driveFieldCentric(x, y, turn, getHeadingRadians());
    }

    /** Displays current drivetrain power output on telemetry */
    public void displayPower() {
        BarnRobot.getInstance().telemetry.addData("spdX x", mecanumDriveComponent.getSpdX());
        BarnRobot.getInstance().telemetry.addData("spdY y", mecanumDriveComponent.getSpdY());
        BarnRobot.getInstance().telemetry.addData("spdTurn t", mecanumDriveComponent.getSpdTurn());
    }

    // ============================================================
    //                      AUTO ALIGNMENT
    // ============================================================

    private double upperPointData() {
        switch (BarnRobot.getInstance().opmodeData.allianceColor) {
            case BLUE: return -90;
            case RED:  return 90;
            default:   return 0;
        }
    }

    /** Determines turn direction when Limelight is invalid */
    private double determineDirection() {
        double upperPoint = Math.abs(getHeadingDegrees() - upperPointData());
        double sidePoint = Math.abs(getHeadingDegrees());
        return upperPoint > sidePoint ? 1 : -1;
    }

    /** Aligns robot to the AprilTag or approximate field direction */
    private void alignToGoal() {
        double turnSpeed;

        if (!BarnRobot.getInstance().limelight.isValid()) {
            turnSpeed = determineDirection(); // fallback turn direction
        } else {
            double yawDiff = BarnRobot.getInstance().limelight.getDyaw();
            turnSpeed = diffToSpeed(yawDiff);
        }

        drive(0, 0, turnSpeed);
    }


    /** Converts yaw difference to turn speed using PID */
    private double diffToSpeed(double yawDiff) {
        double output = pidControllerYaw.calculate(yawDiff, 0);
        double minTurn = 0.12; // ensure wheels actually move
        if (Math.abs(output) < minTurn && Math.abs(yawDiff) > 2)
            output = Math.copySign(minTurn, output);
        return output;
    }

    // ============================================================
    //                         COMMANDS
    // ============================================================

    /** Default manual drive command (field-centric) */
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

    /** Instantly resets IMU heading */
    public Command resetHeadingCommand() {
        return new InstantCommand(this::resetHeading, this);
    }

    /** Continuously aligns robot to the target (using Limelight or fallback) */
    public Command alignToTagCommand() {
        return new RunCommand(this::alignToGoal, this);
    }

    public void periodic() {
        super.periodic();
        pidControllerYaw.setPID(pYaw, 0, dYaw);
    }
}
