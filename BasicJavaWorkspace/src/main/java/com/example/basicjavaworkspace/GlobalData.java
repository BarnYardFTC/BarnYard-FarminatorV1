package com.example.basicjavaworkspace;

public class GlobalData {
    public static final double PI = Math.PI;
    public String LEFT_FRONT_MOTOR;
    public static AllianceSide allianceSide = AllianceSide.BLUE;

    public GlobalData(AllianceSide allianceSide) {
        GlobalData.allianceSide = allianceSide;
//        LEFT_FRONT_MOTOR = "leftFrontMotor";
    }

    public enum AllianceSide {
        BLUE,
        RED
    }
}