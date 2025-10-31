package com.example.basicjavaworkspace.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

// The class name now matches the original filename
public class General {

    public static double POSE1_X = 0;
    public static double POSE1_Y = 0;
    public static double POSE1_HEADING = Math.toRadians(0);

    public static double POSE2_X = 24;
    public static double POSE2_Y = 0;
    public static double POSE2_HEADING = Math.toRadians(0);
    public static double POSE3_X = 24;
    public static double POSE3_Y = 24;
    public static double POSE3_HEADING = Math.toRadians(0);
    public static double POSE4_X = 24;
    public static double POSE4_Y = 24;
    public static double POSE4_HEADING = Math.toRadians(90);


    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();


        TrajectoryActionBuilder path3 = myBot.getDrive().actionBuilder(new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING))
                .turnTo(POSE4_HEADING);

        myBot.runAction(
                new SequentialAction(
                        path3.build()
                )
        );

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}