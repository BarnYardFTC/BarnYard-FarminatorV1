package org.firstinspires.ftc.teamcode.util;

public class OpModeData {

    public AllianceColor allianceColor;
    public double fieldReferenceHeading;
    public double initialBotHeading;

    public enum AllianceColor {
        RED,
        BLUE
    }

    public static OpModeData defaultOpmodeData = new OpModeData(AllianceColor.RED, 0, 0);

    public OpModeData(AllianceColor allianceColor, double initialBotHeading, double fieldReferenceHeading) {
        this.allianceColor = allianceColor;
        this.initialBotHeading = initialBotHeading;
        this.fieldReferenceHeading = fieldReferenceHeading;
    }

    public OpModeData(AllianceColor allianceColor){
        this(allianceColor, defaultOpmodeData.initialBotHeading, defaultOpmodeData.fieldReferenceHeading);
    }

    public OpModeData(double initialBotHeading){
        this(defaultOpmodeData.allianceColor, initialBotHeading, defaultOpmodeData.fieldReferenceHeading);
    }

    public OpModeData(){
        this(defaultOpmodeData.allianceColor, defaultOpmodeData.initialBotHeading, defaultOpmodeData.fieldReferenceHeading);
    }

}
