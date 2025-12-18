package com.example.basicjavaworkspace.meepmeep.blue.close;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;


public class Blue_Close_ThreePlusZero {
    public static double POSE4_X = -10;
    public static double POSE4_Y = 45;
    public static double SOUTH_HEADING = Math.toRadians(130);
    public static double SHOOTING_POSE_X = -41;
    public static double SHOOTING_POSE_Y = 25;

    public static double SHOOTING_HEADING = Math.toRadians(250);


    public static double POSE1_X = -37;
    public static double POSE1_Y = -53;
    public static double POSE1_HEADING = Math.toRadians(90);

    public static double POSE2_X = -30;
    public static double POSE2_Y = -30;
    public static double POSE2_HEADING = Math.toRadians(225);

    // -----------------------------
    // Main Simulation
    // -----------------------------
    public static void main(String[] args) {

        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(13.157, 18.03044)
                .build();

        TrajectoryActionBuilder path1 = myBot.getDrive().actionBuilder(new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE2_X, POSE2_Y), POSE2_HEADING, new TranslationalVelConstraint(25) );

        TrajectoryActionBuilder path4 = myBot.getDrive().actionBuilder(new Pose2d(POSE4_X, POSE4_Y, SOUTH_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y), SHOOTING_HEADING - Math.toRadians(20));

        // Run the trajectory
        myBot.runAction(
                new SequentialAction(
//                        path1.build(),
                        path4.build()
                )
        );

        // MeepMeep visualization
        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
