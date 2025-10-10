package com.example.basicjavaworkspace.dev;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

// The class name now matches the original filename
public class ThreePlusZeroTraj {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-37, -53, Math.toRadians(90)))
                .strafeToLinearHeading(new Vector2d(-11, -11), 180)
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(-12, -23), Math.toRadians(270))
                .strafeToLinearHeading(new Vector2d(-11, -51), Math.toRadians(270))
                .strafeToLinearHeading(new Vector2d(-11, -11), 180)
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(10.6, -23.1), Math.toRadians(270))
                .strafeToLinearHeading(new Vector2d(10.6, -51), Math.toRadians(270))
                .strafeToLinearHeading(new Vector2d(-11, -11), 180)

                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}