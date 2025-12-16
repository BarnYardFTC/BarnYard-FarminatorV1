package com.example.basicjavaworkspace.meepmeep.red.close.threePlusThree;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class PGP {
    // -----------------------------
    // Pose Constants
    // -----------------------------
    public static final double POSE1_X = -37;
    public static final double POSE1_Y = -53;
    public static final double POSE1_HEADING = Math.toRadians(90);

    public static final double POSE2_X = -15.5;
    public static final double POSE2_Y = -15.5;
    public static final double POSE2_HEADING = Math.toRadians(225);

    public static final double POSE3_X = 11.5;
    public static final double POSE3_Y = -17;
    public static final double POSE3_HEADING = Math.toRadians(270);

    public static final double POSE4_X = 11.5;
    public static final double POSE4_Y = -62;
    public static final double POSE4_HEADING = Math.toRadians(270);

    public static final double POSE5_X =11;
    public static final double POSE5_Y = -40;
    public static final double POSE5_HEADING = Math.toRadians(270);


    // -----------------------------
    // Main Simulation
    // -----------------------------
    public static void main(String[] args) {

        MeepMeep meepMeep = new MeepMeep(800);

        // Normal bot (full speed)
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(18, 18)
                .build();

        // Path 1: Pose1 → Pose2 → Pose3
        TrajectoryActionBuilder path1 = myBot.getDrive().actionBuilder(
                        new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE2_X, POSE2_Y), POSE2_HEADING)
                .strafeToLinearHeading(new Vector2d(POSE3_X, POSE3_Y), POSE3_HEADING);

        // Slow path: Pose3 → Pose4
        RoadRunnerBotEntity slowBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(30, 30, Math.toRadians(180), Math.toRadians(180), 15) // half speed
                .setDimensions(18, 18)
                .build();

        TrajectoryActionBuilder slowPath = slowBot.getDrive().actionBuilder(
                        new Pose2d(POSE3_X, POSE3_Y, POSE3_HEADING))

                .strafeToLinearHeading(new Vector2d(POSE4_X, POSE4_Y), POSE4_HEADING);



        // Path 3: Pose4 → Pose5 → Pose2
        TrajectoryActionBuilder path3 = myBot.getDrive().actionBuilder(
                        new Pose2d(POSE4_X, POSE4_Y, POSE4_HEADING))
                .splineTo(new Vector2d(POSE2_X, POSE2_Y), POSE2_HEADING);


        // Combine all paths
        myBot.runAction(
                new SequentialAction(
                        path1.build(),
                        new SleepAction(0.2),
                        slowPath.build(),
                        new SleepAction(0.2),
                        path3.build(),
                        new SleepAction(0.2)
                )
        );

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
