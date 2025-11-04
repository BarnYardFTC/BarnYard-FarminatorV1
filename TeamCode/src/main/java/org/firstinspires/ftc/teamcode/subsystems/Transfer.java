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

@Config // Allows tuning constants via dashboard
public class Transfer extends SubsystemBase {

    // ------------------------------------------------------------
    // Hardware
    // ------------------------------------------------------------
    private final CRServo leftFrontTrans;
    private final CRServo leftBackTrans;
    private final CRServo rightFrontTrans;
    private final CRServo rightBackTrans;

    public static double DEFAULT_TRANSFER_POWER = 1;

    // ------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------

    // ------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------
    public Transfer() {
        BarnRobot robot = BarnRobot.getInstance();
        leftFrontTrans = robot.robotHardware.leftFrontTransfer;
        leftBackTrans = robot.robotHardware.leftBackTransfer;
        rightFrontTrans = robot.robotHardware.rightFrontTransfer;
        rightBackTrans = robot.robotHardware.rightBackTransfer;

        // Positive power = forward transfer; left servos reversed
        leftFrontTrans.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBackTrans.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    // ------------------------------------------------------------
    // Low-level control methods
    // ------------------------------------------------------------

    /** Sets all four transfer servos to the given power. */
    public void setAllTransferPower(double power) {
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

    // ------------------------------------------------------------
    // Command wrappers
    // ------------------------------------------------------------

    public Command setFrontPowerCommand(double power){
        return new InstantCommand(() -> setFrontPower(power));
    }

    public Command setBackPowerCommand(double power){
        return new InstantCommand(() -> setBackPower(power));
    }

    public Command setEntireTransferPowerCommand(double power){
        return new InstantCommand(() -> setAllTransferPower(power));
    }
}
