package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.roadrunner.Pose2d;

public class OpModeData {

    public AllianceColor allianceColor;

    public double initialBotHeading;

    public Pose2d startPose;

    public enum AllianceColor {
        RED,
        BLUE
    }

    public static OpModeData defaultOpmodeData = new OpModeData(AllianceColor.RED, 0);

    public OpModeData(AllianceColor allianceColor, double initialBotHeading){
        this.allianceColor = allianceColor;
        this.initialBotHeading = initialBotHeading;
    }

    public OpModeData(AllianceColor allianceColor, Pose2d startPose){
        this.allianceColor = allianceColor;
        this.initialBotHeading = initialBotHeading;
        this.startPose = startPose;
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
