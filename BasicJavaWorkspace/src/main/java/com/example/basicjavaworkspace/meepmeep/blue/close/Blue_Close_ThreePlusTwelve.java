package com.example.basicjavaworkspace.meepmeep.blue.close;

import com.acmerobotics.roadrunner.*;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.lang.Math;

public class Blue_Close_ThreePlusTwelve {

    public static double SHOOTING_PROG_POSE_X = -35;
    public static double SHOOTING_PROG_POSE_Y = -30;

    public static long SHOOT_TIME = 2;
    public static long GATE_COLLECTING_TIME = 1;


    public static double START_POSE_X = -53.333;
    public static double START_POSE_Y = -45.5;
    public static double START_HEADING = Math.toRadians(225);

    public static double SHOOT_POSE_X = -23;
    public static double SHOOT_POSE_Y = -22;
    public static double SHOOT_HEADING = Math.toRadians(227);

    public static double SOUTH_READY_POSE_Y = -30;
    public static double SOUTH_COLLECT_POSE_Y = -53;

    public static double SOUTH_HEADING = Math.toRadians(270);

    public static double LEFT_COLLECT_POSE_X = -11.5;
    public static double MID_COLLECT_POSE_X = 12;
    public static double RIGHT_COLLECT_POSE_X = 35;

    public static double GATE_POSE_X = 8;
    public static double GATE_POSE_Y = -55;

    public static double ENDING_POSE_X = -40;
    public static double ENDING_POSE_Y = -22;

    public static void main(String[] args) {

        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(15.07, 16.961)
                .build();

        TrajectoryActionBuilder path1;
        path1 = myBot.getDrive().actionBuilder(
                        new Pose2d(START_POSE_X, START_POSE_Y, START_HEADING))
                .strafeToLinearHeading(new Vector2d(START_POSE_X, START_POSE_Y+5), SOUTH_HEADING)
                .splineToConstantHeading(new Vector2d(MID_COLLECT_POSE_X, SOUTH_READY_POSE_Y), SOUTH_HEADING)
                .splineToConstantHeading(new Vector2d(MID_COLLECT_POSE_X, SOUTH_COLLECT_POSE_Y), new Rotation2d(0,0))

                .splineToLinearHeading(
                        new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING),
                        new Rotation2d(-2,-1),
                        new TranslationalVelConstraint(100))

                .strafeToLinearHeading(new Vector2d(GATE_POSE_X, SOUTH_READY_POSE_Y), SOUTH_HEADING)
                .strafeToLinearHeading(new Vector2d(GATE_POSE_X, GATE_POSE_Y), SHOOT_HEADING);

        TrajectoryActionBuilder path2 = myBot.getDrive().actionBuilder(
                        new Pose2d(GATE_POSE_X, GATE_POSE_Y, SHOOT_HEADING))
                .strafeToLinearHeading(new Vector2d(GATE_POSE_X, SOUTH_READY_POSE_Y), SOUTH_HEADING)
                .splineToLinearHeading(
                        new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING),
                        new Rotation2d(-2,-1),
                        new TranslationalVelConstraint(100))


                .splineToLinearHeading(
                        new Pose2d(LEFT_COLLECT_POSE_X, SOUTH_COLLECT_POSE_Y+8, SOUTH_HEADING),
                        new Rotation2d(0, -4)
                )

                .splineToLinearHeading(
                        new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING),
                        new Rotation2d(-2,-1),
                        new TranslationalVelConstraint(100))

                ;


        TrajectoryActionBuilder path3 = myBot.getDrive().actionBuilder(
                        new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING))

                .splineToLinearHeading(
                        new Pose2d(RIGHT_COLLECT_POSE_X, SOUTH_COLLECT_POSE_Y+15, SOUTH_HEADING),
                        new Rotation2d(1, -3)
                )

                .splineToLinearHeading(
                        new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING),
                        new Rotation2d(-1.8,-1 ),
                        new TranslationalVelConstraint(150));


        TrajectoryActionBuilder path4 = myBot.getDrive().actionBuilder(
                        new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING))
                .strafeToLinearHeading(
                        new Vector2d(ENDING_POSE_X, ENDING_POSE_Y),
                        SHOOT_HEADING);

        myBot.runAction(
                new SequentialAction(
                        new SleepAction(SHOOT_TIME),
                        path1.build(),
                        new SleepAction(GATE_COLLECTING_TIME),
                        path2.build(),
                        new SleepAction(SHOOT_TIME),
                        path3.build(),
                        new SleepAction(SHOOT_TIME),
                        path4.build()
                )
        );

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
