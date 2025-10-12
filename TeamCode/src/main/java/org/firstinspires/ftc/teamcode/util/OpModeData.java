package org.firstinspires.ftc.teamcode.util;

public class OpModeData {

    public AllianceColor allianceColor;

    public double initialBotHeading;

    public OpModeType opModeType;

    public enum AllianceColor {
        RED,
        BLUE
    }

    public enum OpModeType {
        AUTONOMOUS, TELEOP
    }

    public static OpModeData defaultOpmodeData = new OpModeData(AllianceColor.BLUE, 0, OpModeType.TELEOP);

    public OpModeData(AllianceColor allianceColor, double initialBotHeading, OpModeType opModeType){
        this.allianceColor = allianceColor;
        this.initialBotHeading = initialBotHeading;
        this.opModeType = opModeType;
    }

    public OpModeData(AllianceColor allianceColor, double initialBotHeading){
        this(allianceColor, initialBotHeading, defaultOpmodeData.opModeType);
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
