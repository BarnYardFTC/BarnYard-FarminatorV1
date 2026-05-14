package com.example.basicjavaworkspace.meepmeep.random;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class RoadRunnerTuningTest {

    public static void main(String[] args) {
        double radius = 5;
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(13.157, 18.03044)
                .build();

        // Run the trajectory
        myBot.runAction(
                myBot.getDrive().actionBuilder(new Pose2d(radius, 0, Math.PI/2))
                        .splineTo(new Vector2d(0, radius), Math.PI)
                        .waitSeconds(0.5)
                        .splineTo(new Vector2d(-radius, 0), Math.PI*1.5)
                        .waitSeconds(0.5)
                        .splineTo(new Vector2d(0, -radius), Math.PI*2)
                        .waitSeconds(0.5)
                        .splineTo(new Vector2d(radius, 0), Math.PI/2)
                        .waitSeconds(0.5)
                        .build()
        );

        // MeepMeep visualization
        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
