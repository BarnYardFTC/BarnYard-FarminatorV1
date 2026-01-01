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
    public int webcamPipeline;

    /**the heading in which the autonomous has ended*/
    private static Pose2d autoFinishPose;

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
                      OpModeType opModeType,
                      int webcamPipeline,
                      Pose2d initialPose2d,
                      double fieldReferenceHeading) {

        this.allianceColor = allianceColor;
        this.fieldReferenceHeading = fieldReferenceHeading;
        this.opModeType = opModeType;
        this.webcamPipeline = webcamPipeline;
        this.initialPose2d = initialPose2d;
    }



    /** Autonomous constructor */
    public OpModeData(AllianceColor allianceColor,
                      OpModeType opModeType,
                      int webcamPipeline,
                      Pose2d initialPose2d) {

        autoFinishPose = new Pose2d(0,0,0); //reset autoFinishPose
        this.initialPose2d = initialPose2d;
        this.allianceColor = allianceColor;
        this.opModeType = opModeType;
        this.webcamPipeline = webcamPipeline;

        // Default val
        this.fieldReferenceHeading = 0;
    }


    public static Pose2d getAutoFinishPose(){
        if(autoFinishPose == null) return new Pose2d(0,0,0);
        return autoFinishPose;
    }

    public static void setAutoFinishPose(Pose2d autoFinishPose){
        OpModeData.autoFinishPose = autoFinishPose;
    }


}
