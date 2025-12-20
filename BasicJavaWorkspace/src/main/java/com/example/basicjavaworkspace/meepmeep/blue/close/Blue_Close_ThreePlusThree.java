package com.example.basicjavaworkspace.meepmeep.blue.close;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class Blue_Close_ThreePlusThree {
    public static double POSE1_X = 62;
    public static double POSE1_Y = 60;
    public static double POSE1_HEADING = Math.toRadians(90);

    public static double POSE2_X = -40;
    public static double POSE2_Y = -15;
    public static double POSE2_HEADING = Math.toRadians(250);

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
                .strafeToLinearHeading(new Vector2d(POSE2_X, POSE2_Y), POSE2_HEADING);



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
