package com.example.basicjavaworkspace.meepmeep.red.close;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class Red_Close_ThreePlusNine {


    public static double SHOOT_TIME = 2;
    public static double START_POSE_X = -53.333;
    public static double START_POSE_Y = 45.5;
    public static double START_HEADING = Math.toRadians(125);

    public static double SHOOT_POSE_X = -23;
    public static double SHOOT_POSE_Y = 23;
    public static double SHOOT_HEADING = Math.toRadians(130);

    // -----------------------------
    // Main Simulation
    // -----------------------------
    public static void main(String[] args) {

        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(13.157, 18.03044)
                .build();

        TrajectoryActionBuilder path1 = myBot.getDrive().actionBuilder(new Pose2d(START_POSE_X, START_POSE_Y, START_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOT_POSE_X, SHOOT_POSE_Y), SHOOT_HEADING);



        // Run the trajectory
        myBot.runAction(
                new SequentialAction(
                        path1.build()
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
