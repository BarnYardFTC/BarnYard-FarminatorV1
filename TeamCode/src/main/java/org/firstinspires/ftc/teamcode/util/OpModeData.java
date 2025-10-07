package org.firstinspires.ftc.teamcode.util;

public class OpModeData {

    public AllianceColor allianceColor;

    public double initialBotHeading;

    public enum AllianceColor {
        RED,
        BLUE
    }

    public static OpModeData defaultOpmodeData = new OpModeData(AllianceColor.RED, 0);

    public OpModeData(AllianceColor allianceColor, double initialBotHeading){
        this.allianceColor = allianceColor;
        this.initialBotHeading = initialBotHeading;
    }

    public OpModeData(AllianceColor allianceColor){
        this(allianceColor, defaultOpmodeData.initialBotHeading);
    }

    public OpModeData(double initialBotHeading){
        this(defaultOpmodeData.allianceColor, initialBotHeading);
    }

    public OpModeData(){
        this(defaultOpmodeData.allianceColor, defaultOpmodeData.initialBotHeading);
    }

}
