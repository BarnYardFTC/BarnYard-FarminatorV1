package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.roadrunner.Pose2d;

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
    public Pose2d initialPose2d;

    /**Pipeline of limelight*/
    public int limelightPipeline;

    /**the heading in which the autonomous has ended*/
    private static double autoFinishHeading;

    // ------------------------------------------------------------
    // Enums
    // ------------------------------------------------------------
    public enum AllianceColor { RED, BLUE }
    public enum OpModeType { TELEOP, AUTONOMOUS }

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /** Teleop constructor */
    public OpModeData(AllianceColor allianceColor,
                      Pose2d initialPose2d,
                      double fieldReferenceHeading,
                      OpModeType opModeType, int limelightPipeline) {
        this.allianceColor = allianceColor;
        this.fieldReferenceHeading = fieldReferenceHeading;
        this.opModeType = opModeType;
        this.limelightPipeline = limelightPipeline;
        this.initialPose2d = initialPose2d;

    }

    /** Autonomous constructor */
    public OpModeData(AllianceColor allianceColor,
                      OpModeType opModeType, int limelightPipeline, Pose2d initialPose2d) {
        autoFinishHeading = 0;
        this.fieldReferenceHeading = 0;
        this.initialPose2d = initialPose2d;
        this.allianceColor = allianceColor;
        this.opModeType = opModeType;
        this.limelightPipeline = limelightPipeline;
    }


    public static double getAutoFinishHeading(){
        return autoFinishHeading;
    }


    public static void setAutoFinishHeading(double heading){
        autoFinishHeading = heading;
    }
}
