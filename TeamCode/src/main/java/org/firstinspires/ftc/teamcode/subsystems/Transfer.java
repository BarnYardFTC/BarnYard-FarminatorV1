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
    // -----------------------------------------------------------

    public static double DEFAULT_TRANSFER_POWER = 1;

    // ------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------

    // ------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------
    public Transfer() {
        BarnRobot robot = BarnRobot.getInstance();

    }


    // ------------------------------------------------------------
    // Low-level control methods
    // ------------------------------------------------------------



    // ------------------------------------------------------------
    // High-level actions
    // ------------------------------------------------------------

    // ------------------------------------------------------------
    // Command wrappers
    // ------------------------------------------------------------

}
