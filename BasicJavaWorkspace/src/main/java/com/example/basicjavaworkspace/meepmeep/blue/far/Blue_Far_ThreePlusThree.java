package com.example.basicjavaworkspace.meepmeep.blue.far;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class Blue_Far_ThreePlusThree {
    public static long SHOOT_TIME = 2;

    public static double START_POSE_X = 60;
    public static double START_POSE_Y = -15;
    public static double START_HEADING = Math.toRadians(180);

    public static double SHOOTING_POSE_X = 55;
    public static double SHOOTING_POSE_Y = -10;
    public static double SHOOT_HEADING = Math.toRadians(215);

    public static double COLLECT_POSE_X = 60;
    public static double COLLECT_POSE_Y = -60;
    public static double COLLECT_HEADING = Math.toRadians(270);

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
                .strafeToLinearHeading(new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y), SHOOT_HEADING);
        TrajectoryActionBuilder path2 = myBot.getDrive().actionBuilder(new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, SHOOT_HEADING))
                .strafeToLinearHeading(new Vector2d(COLLECT_POSE_X,COLLECT_POSE_Y ),COLLECT_HEADING );
        TrajectoryActionBuilder path3 = myBot.getDrive().actionBuilder(new Pose2d(COLLECT_POSE_X,COLLECT_POSE_Y,COLLECT_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y), SHOOT_HEADING);


        // Run the trajectory
        myBot.runAction(
                new SequentialAction(
                        path1.build(),
                        path2.build(),
                        path3.build()
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
