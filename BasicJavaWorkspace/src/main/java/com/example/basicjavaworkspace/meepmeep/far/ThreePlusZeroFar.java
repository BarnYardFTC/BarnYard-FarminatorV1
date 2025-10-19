package com.example.basicjavaworkspace.meepmeep.far;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

// The class name now matches the original filename
public class ThreePlusZeroFar {

    public static double POSE1_X = 60;
    public static double POSE1_Y = -8.8;
    public static double POSE1_HEADING = Math.toRadians(180);

    public static double POSE2_X = 57;
    public static double POSE2_Y = -23;
    public static double POSE2_HEADING = Math.toRadians(200);

    public static double POSE3_X = 32;
    public static double POSE3_Y = -22;
    public static double POSE3_HEADING = Math.toRadians(180);



    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(13.157, 18.03044)
                .build();

        TrajectoryActionBuilder path1 = myBot.getDrive().actionBuilder(new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE2_X, POSE2_Y), POSE2_HEADING);

        TrajectoryActionBuilder path2 = myBot.getDrive().actionBuilder(new Pose2d(POSE2_X, POSE2_Y, POSE2_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE3_X, POSE3_Y), POSE3_HEADING);

        myBot.runAction(
                new SequentialAction(
                        path1.build(),
                        new SleepAction(10),
                        path2.build()
                ));


        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}