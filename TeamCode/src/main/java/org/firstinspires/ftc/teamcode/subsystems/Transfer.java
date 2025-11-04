package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.BarnRobot;

/**
 * Transfer subsystem controls the four transfer CRServos on the robot.
 *
 * This subsystem allows activating/deactivating front, back, or all transfer servos individually
 * or together. Commands are provided to integrate with the command-based framework.
 */

/*
TODO:
    - Make class more dynamic. Too many functions that do the same thing
 */
@Config // Allows tuning constants via dashboard
public class Transfer extends SubsystemBase {

    // ------------------------------------------------------------
    // Hardware
    // ------------------------------------------------------------
    private final CRServo leftFrontTrans;
    private final CRServo leftBackTrans;
    private final CRServo rightFrontTrans;
    private final CRServo rightBackTrans;

    // ------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------
    public static double DEFAULT_POWER = 1;           // default power for transfer
    public static int TRANSFER_ONE_DURATION = 800;    // duration for single transfer step (ms)
    public static int TRANSFER_ALL_DURATION = TRANSFER_ONE_DURATION * 3; // full transfer duration

    // ------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------
    public Transfer() {
        BarnRobot robot = BarnRobot.getInstance();
        leftFrontTrans = robot.farminatorHardware.leftFrontTransfer;
        leftBackTrans = robot.farminatorHardware.leftBackTransfer;
        rightFrontTrans = robot.farminatorHardware.rightFrontTransfer;
        rightBackTrans = robot.farminatorHardware.rightBackTransfer;

        // Positive power = forward transfer; left servos reversed
        leftFrontTrans.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBackTrans.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    // ------------------------------------------------------------
    // Low-level control methods
    // ------------------------------------------------------------

    /** Sets all four transfer servos to the given power. */
    public void setPower(double power) {
        leftFrontTrans.setPower(power);
        leftBackTrans.setPower(power);
        rightFrontTrans.setPower(power);
        rightBackTrans.setPower(power);
    }

    /** Sets only back transfer servos to the given power. */
    public void setBackPower(double power) {
        leftBackTrans.setPower(power);
        rightBackTrans.setPower(power);
    }

    /** Sets only front transfer servos to the given power. */
    public void setFrontPower(double power) {
        leftFrontTrans.setPower(power);
        rightFrontTrans.setPower(power);
    }

    // ------------------------------------------------------------
    // High-level actions
    // ------------------------------------------------------------

    //todo: remove the "High-level actions" functions, you don't need them, you can write them in the "Command wrappers"
    public void deactivateTransfer() { setPower(0); }
    public void activateTransfer() { setPower(DEFAULT_POWER); }

    public void activateBackTransfer() { setBackPower(DEFAULT_POWER); }
    public void activateBackTransfer(double power) { setBackPower(power); }
    public void deactivateBackTransfer() { setBackPower(0); }

    public void activateFrontTransfer() { setFrontPower(DEFAULT_POWER); }
    public void deactivateFrontTransfer() { setFrontPower(0); }

    // ------------------------------------------------------------
    // Command wrappers
    // ------------------------------------------------------------

    public Command activateTransferCommand() {
        return new InstantCommand(this::activateTransfer, this);
    }

    public Command unloadTransferCommand() {
        return new InstantCommand(() -> setPower(-DEFAULT_POWER), this);
    }

    public Command deactivateTransferCommand() {
        return new InstantCommand(this::deactivateTransfer, this);
    }

    public Command activateBackTransferCommand() {
        return new InstantCommand(this::activateBackTransfer, this);
    }

    public Command activateBackTransferCommand(double power) {
        return new InstantCommand(() -> activateBackTransfer(power), this);
    }

    public Command deactivateBackTransferCommand() {
        return new InstantCommand(this::deactivateBackTransfer, this);
    }

    public Command activateFrontTransferCommand() {
        return new InstantCommand(this::activateFrontTransfer, this);
    }

    public Command deactivateFrontTransferCommand() {
        return new InstantCommand(this::deactivateFrontTransfer, this);
    }
}
