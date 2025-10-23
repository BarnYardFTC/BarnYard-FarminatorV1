package com.example.basicjavaworkspace.meepmeep.close;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class ThreePlusZeroClose {

    // -----------------------------
    // Pose Constants
    // -----------------------------
    public static final double START_X = -37;
    public static final double START_Y = -53;
    public static final double START_HEADING = Math.toRadians(90);

    public static final double TARGET_X = -15.5;
    public static final double TARGET_Y = -15.5;
    public static final double TARGET_HEADING = Math.toRadians(225);

    // -----------------------------
    // Main Simulation
    // -----------------------------
    public static void main(String[] args) {

        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        // Build trajectory from start → target
        TrajectoryActionBuilder trajectory = myBot.getDrive().actionBuilder(
                        new Pose2d(START_X, START_Y, START_HEADING))
                .strafeToLinearHeading(new Vector2d(TARGET_X, TARGET_Y), TARGET_HEADING);

        // Run the trajectory
        myBot.runAction(
                new SequentialAction(
                        trajectory.build(),
                        new SleepAction(0.2) // small pause
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
