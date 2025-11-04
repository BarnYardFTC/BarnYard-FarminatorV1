package org.firstinspires.ftc.teamcode.util;

/**
 * Stores configuration and runtime data for an OpMode.
 *
 * This class lets us pass essential info to the robot systems during init,
 * such as alliance color, starting heading, and whether we're running TeleOp or Auto.
 *
 * Every OpMode creates or uses an existing {@link OpModeData} object
 * and sends it to {@link org.firstinspires.ftc.teamcode.BarnRobot#init}.
 */
public class OpModeData {

    /** Whether this OpMode is TeleOp or Autonomous. */
    public OpModeType opModeType;

    /** Alliance color for this match (used for strategies, field orientation, etc). */
    public AllianceColor allianceColor;

    /** The field’s 0° reference direction (used for field-centric control). */
    public double fieldReferenceHeading;

    /** The robot’s starting heading on the field at the beginning of the OpMode. */
    public double initialBotHeading;

    // ------------------------------------------------------------
    // Enums
    // ------------------------------------------------------------
    public enum AllianceColor { RED, BLUE }
    public enum OpModeType { TELEOP, AUTONOMOUS }

    // ------------------------------------------------------------
    // Defaults
    // ------------------------------------------------------------

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /** Full constructor (all fields specified). */
    public OpModeData(AllianceColor allianceColor,
                      double initialBotHeading,
                      double fieldReferenceHeading,
                      OpModeType opModeType) {
        this.allianceColor = allianceColor;
        this.initialBotHeading = initialBotHeading;
        this.fieldReferenceHeading = fieldReferenceHeading;
        this.opModeType = opModeType;
    }

}
