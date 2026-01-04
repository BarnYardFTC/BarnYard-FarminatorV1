package com.example.basicjavaworkspace.meepmeep.blue.close;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class Blue_Close_ThreePlusNine {

    public static double SHOOT_TIME = 2;
    public static double STARTPOSE_X = -53.33;
    public static double STARTPOSE_Y = -45.5;
    public static double STARTPOSE_HEADING = Math.toRadians(235);

    public static double SHOOTPOSE_X = -23;
    public static double SHOOTPOSE_Y = -22;
    public static double SHOOTPOSE_HEADING = Math.toRadians(227);


    public static double LEFTREADYPOSE_X = -13;
    public static double LEFTREADYPOSE_Y = -22;
    public static double SOUTHPOSE_HEADING = Math.toRadians(270);



    // -----------------------------
    // Main Simulation
    // -----------------------------
    public static void main(String[] args) {

        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(13.157, 18.03044)
                .build();

        TrajectoryActionBuilder path1 = myBot.getDrive().actionBuilder(new Pose2d(STARTPOSE_X, STARTPOSE_Y, STARTPOSE_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOTPOSE_X, SHOOTPOSE_Y), SHOOTPOSE_HEADING);
        TrajectoryActionBuilder path2 = myBot.getDrive().actionBuilder(new Pose2d(SHOOTPOSE_X, SHOOTPOSE_Y, SHOOTPOSE_HEADING))
                .strafeToLinearHeading(new Vector2d(LEFTREADYPOSE_X, LEFTREADYPOSE_Y), SOUTHPOSE_HEADING);
        TrajectoryActionBuilder path3 = myBot.getDrive().actionBuilder(new Pose2d(SHOOTPOSE_X, SHOOTPOSE_Y, SHOOTPOSE_HEADING))
                .strafeToLinearHeading(new Vector2d(LEFTREADYPOSE_X, LEFTREADYPOSE_Y), SOUTHPOSE_HEADING);






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
