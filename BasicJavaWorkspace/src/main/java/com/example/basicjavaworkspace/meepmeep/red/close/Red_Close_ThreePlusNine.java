package com.example.basicjavaworkspace.meepmeep.red.close;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class Red_Close_ThreePlusNine {


    public static long SHOOT_TIME = 2;
    public static double START_POSE_X = -53.333;
    public static double START_POSE_Y = 45.5;
    public static double START_HEADING = Math.toRadians(125);

    public static double SHOOT_POSE_X = -23;
    public static double SHOOT_POSE_Y = 22;
    public static double SHOOT_HEADING = Math.toRadians(135);

    public static double NORTH_READY_POSE_Y = 22;
    public static double NORTH_COLLECT_POSE_Y = 53;
    public static double NORTH_HEADING = Math.toRadians(90);

    public static double LEFT_COLLECT_POSE_X = -11;
    public static double MID_COLLECT_POSE_X = 11.8;
    public static double RIGHT_COLLECT_POSE_X = 35;

    public static double GATE_POSE_Y = 44;

    public static double GATE_POSE_X = 0;

    public static double ENDING_POSE_X = -40;
    public static double ENDING_POSE_Y = 22;

    public static double defaultVel = 50;

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
                .strafeToLinearHeading(new Vector2d(LEFT_COLLECT_POSE_X, NORTH_READY_POSE_Y), NORTH_HEADING, new TranslationalVelConstraint(defaultVel*4));

        TrajectoryActionBuilder path2 = myBot.getDrive().actionBuilder(new Pose2d(LEFT_COLLECT_POSE_X, NORTH_READY_POSE_Y, NORTH_HEADING))
                .strafeToLinearHeading(new Vector2d(LEFT_COLLECT_POSE_X, NORTH_COLLECT_POSE_Y), NORTH_HEADING, new TranslationalVelConstraint(defaultVel * 0.5));

        TrajectoryActionBuilder path3 = myBot.getDrive().actionBuilder(new Pose2d(LEFT_COLLECT_POSE_X, NORTH_COLLECT_POSE_Y, NORTH_HEADING))
                .strafeToLinearHeading(new Vector2d(GATE_POSE_X, GATE_POSE_Y),NORTH_HEADING)
                .strafeToLinearHeading(new Vector2d(GATE_POSE_X, NORTH_COLLECT_POSE_Y), NORTH_HEADING)
                .waitSeconds(0.5)
                .strafeToLinearHeading(new Vector2d(SHOOT_POSE_X, SHOOT_POSE_Y), SHOOT_HEADING, new TranslationalVelConstraint(defaultVel*4));


        TrajectoryActionBuilder path4 = myBot.getDrive().actionBuilder(new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING))
                .strafeToLinearHeading(new Vector2d(MID_COLLECT_POSE_X, NORTH_READY_POSE_Y), NORTH_HEADING);

        TrajectoryActionBuilder path5 = myBot.getDrive().actionBuilder(new Pose2d(MID_COLLECT_POSE_X, NORTH_READY_POSE_Y, NORTH_HEADING))
                .strafeToLinearHeading(new Vector2d(MID_COLLECT_POSE_X,NORTH_COLLECT_POSE_Y), NORTH_HEADING, new TranslationalVelConstraint(defaultVel * 0.5))
                .strafeToLinearHeading(new Vector2d(MID_COLLECT_POSE_X,NORTH_COLLECT_POSE_Y-8), NORTH_HEADING);

        TrajectoryActionBuilder path6 = myBot.getDrive().actionBuilder(new Pose2d(MID_COLLECT_POSE_X, NORTH_COLLECT_POSE_Y-8, NORTH_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOT_POSE_X, SHOOT_POSE_Y), SHOOT_HEADING);

        TrajectoryActionBuilder path7 = myBot.getDrive().actionBuilder(new Pose2d(SHOOT_POSE_X,SHOOT_POSE_Y,SHOOT_HEADING))
                .strafeToLinearHeading(new Vector2d(RIGHT_COLLECT_POSE_X, NORTH_READY_POSE_Y), NORTH_HEADING);

        TrajectoryActionBuilder path8 = myBot.getDrive().actionBuilder(new Pose2d(RIGHT_COLLECT_POSE_X, NORTH_READY_POSE_Y, NORTH_HEADING))
                .strafeToLinearHeading(new Vector2d(RIGHT_COLLECT_POSE_X, NORTH_COLLECT_POSE_Y), NORTH_HEADING, new TranslationalVelConstraint(defaultVel * 0.5))
                .strafeToLinearHeading(new Vector2d(RIGHT_COLLECT_POSE_X, NORTH_COLLECT_POSE_Y - 8), NORTH_HEADING);

        TrajectoryActionBuilder path9 = myBot.getDrive().actionBuilder(new Pose2d(RIGHT_COLLECT_POSE_X, NORTH_COLLECT_POSE_Y-8, NORTH_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOT_POSE_X,SHOOT_POSE_Y ),SHOOT_HEADING, new TranslationalVelConstraint(defaultVel * 4));

        TrajectoryActionBuilder path10 = myBot.getDrive().actionBuilder(new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING))
                .strafeToLinearHeading(new Vector2d(ENDING_POSE_X,ENDING_POSE_Y),SHOOT_HEADING);


                        // Run the trajectory
                        myBot.runAction(

                                new SequentialAction(
                                        new SleepAction(SHOOT_TIME),
                                        path1.build(),
                                        path2.build(),
                                        path3.build(),
                                        new SleepAction(SHOOT_TIME),
                                        path4.build(),
                                        path5.build(),
                                        path6.build(),
                                        new SleepAction(SHOOT_TIME),
                                        path7.build(),
                                        path8.build(),
                                        path9.build(),
                                        new SleepAction(SHOOT_TIME),
                                        path10.build()

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
