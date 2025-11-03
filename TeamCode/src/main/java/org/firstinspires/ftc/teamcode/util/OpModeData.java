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
    public static OpModeData defaultOpmodeData =
            new OpModeData(AllianceColor.RED, 0, 0, OpModeType.TELEOP);

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

    /** Constructor without mode type (uses default mode). */
    public OpModeData(AllianceColor allianceColor,
                      double initialBotHeading,
                      double fieldReferenceHeading) {
        this(allianceColor, initialBotHeading, fieldReferenceHeading, defaultOpmodeData.opModeType);
    }

    /** Constructor with only alliance color (everything else default). */
    public OpModeData(AllianceColor allianceColor) {
        this(allianceColor,
                defaultOpmodeData.initialBotHeading,
                defaultOpmodeData.fieldReferenceHeading,
                defaultOpmodeData.opModeType);
    }

    /** Constructor with only initial heading (everything else default). */
    public OpModeData(double initialBotHeading) {
        this(defaultOpmodeData.allianceColor,
                initialBotHeading,
                defaultOpmodeData.fieldReferenceHeading,
                defaultOpmodeData.opModeType);
    }

    /** Constructor with only field reference heading (everything else default). */
    public OpModeData(double fieldReferenceHeading, boolean isFieldReference) {
        this(defaultOpmodeData.allianceColor,
                defaultOpmodeData.initialBotHeading,
                fieldReferenceHeading,
                defaultOpmodeData.opModeType);
    }

    /** Constructor with only mode type (everything else default). */
    public OpModeData(OpModeType opModeType) {
        this(defaultOpmodeData.allianceColor,
                defaultOpmodeData.initialBotHeading,
                defaultOpmodeData.fieldReferenceHeading,
                opModeType);
    }

    /** Default constructor — uses all default values. */
    public OpModeData() {
        this(defaultOpmodeData.allianceColor,
                defaultOpmodeData.initialBotHeading,
                defaultOpmodeData.fieldReferenceHeading,
                defaultOpmodeData.opModeType);
    }
}
