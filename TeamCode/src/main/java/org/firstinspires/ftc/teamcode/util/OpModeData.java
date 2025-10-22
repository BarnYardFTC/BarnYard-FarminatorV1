package org.firstinspires.ftc.teamcode.util;

import android.graphics.Path;

public class OpModeData {

    public OpModeType opModeType;
    public AllianceColor allianceColor;
    public double fieldReferenceHeading;
    public double initialBotHeading;

    public enum AllianceColor {
        RED,
        BLUE
    }
    public enum OpModeType{
        TELEOP,
        AUTONOMOUS
    }

    public static OpModeData defaultOpmodeData = new OpModeData(AllianceColor.RED, 0, 0, OpModeType.TELEOP);

    public OpModeData(AllianceColor allianceColor, double initialBotHeading, double fieldReferenceHeading, OpModeType opModeType) {
        this.opModeType = opModeType;
        this.allianceColor = allianceColor;
        this.initialBotHeading = initialBotHeading;
        this.fieldReferenceHeading = fieldReferenceHeading;
    }
    public OpModeData(AllianceColor allianceColor, double initialBotHeading, double fieldReferenceHeading) {
        this(allianceColor, initialBotHeading, fieldReferenceHeading, defaultOpmodeData.opModeType);
    }

    public OpModeData(AllianceColor allianceColor){
        this(allianceColor, defaultOpmodeData.initialBotHeading, defaultOpmodeData.fieldReferenceHeading, defaultOpmodeData.opModeType);
    }

    public OpModeData(double initialBotHeading){
        this(defaultOpmodeData.allianceColor, initialBotHeading, defaultOpmodeData.fieldReferenceHeading, defaultOpmodeData.opModeType);
    }

    public OpModeData(){
        this(defaultOpmodeData.allianceColor, defaultOpmodeData.initialBotHeading, defaultOpmodeData.fieldReferenceHeading, defaultOpmodeData.opModeType);
    }

}
