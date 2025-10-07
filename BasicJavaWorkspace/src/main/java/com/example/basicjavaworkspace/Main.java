package com.example.basicjavaworkspace;

public class Main {
    public static void main(String[] args) {
        GlobalData globalData = new GlobalData(GlobalData.AllianceSide.RED);
        System.out.println(globalData.LEFT_FRONT_MOTOR);
        System.out.println(GlobalData.allianceSide);
        System.out.println(GlobalData.PI);
    }
}