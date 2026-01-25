package com.example.basicjavaworkspace.meepmeep.blue.close;

import com.acmerobotics.roadrunner.*;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.lang.Math;

public class Blue_Close_ThreePlusSix {

    // ================== FIELD / POSES ==================
    public static double SHOOTING_PROG_POSE_X = -35;
    public static double SHOOTING_PROG_POSE_Y = -30;

    // SleepAction is in seconds (double). Keep as seconds.
    public static double SHOOT_TIME_SEC = 2.0;

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

    public static double GATE_POSE_X = 0;
    public static double GATE_POSE_Y = -44; // currently unused in your paths

    public static double ENDING_POSE_X = -40;
    public static double ENDING_POSE_Y = -22;

    // ================== BOT / SIM CONFIG ==================
    private static final int WINDOW_SIZE = 800;

    private static final double MAX_VEL = 60;
    private static final double MAX_ACCEL = 60;
    private static final double MAX_ANG_VEL = Math.toRadians(180);
    private static final double MAX_ANG_ACCEL = Math.toRadians(180);
    private static final double TRACK_WIDTH = 15;

    private static final double BOT_WIDTH = 15.07;
    private static final double BOT_HEIGHT = 16.961;

    // ================== PATH TUNING (no Rotation2d here) ==================
    private static final double START_Y_NUDGE = 5.0;
    private static final double MID_Y_OFFSET = 8.0;
    private static final double RIGHT_Y_OFFSET = 15.0;

    private static final double VEL_TO_SHOOT = 150.0;

    private static final double END_HEADING_OFFSET_RAD = Math.toRadians(15);

    public static void main(String[] args) {

        MeepMeep meepMeep = new MeepMeep(WINDOW_SIZE);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(MAX_VEL, MAX_ACCEL, MAX_ANG_VEL, MAX_ANG_ACCEL, TRACK_WIDTH)
                .setDimensions(BOT_WIDTH, BOT_HEIGHT)
                .build();

        //---LEFT CYCLE

        TrajectoryActionBuilder path1 = myBot.getDrive().actionBuilder(new Pose2d(START_POSE_X, START_POSE_Y, START_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOT_POSE_X, SHOOT_POSE_Y), SHOOT_HEADING);
        TrajectoryActionBuilder path2 = myBot.getDrive().actionBuilder(new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING))
                .strafeToLinearHeading(new Vector2d(LEFT_COLLECT_POSE_X, SOUTH_READY_POSE_Y), SOUTH_HEADING);
        TrajectoryActionBuilder path3 = myBot.getDrive().actionBuilder(new Pose2d(LEFT_COLLECT_POSE_X, SOUTH_READY_POSE_Y, SOUTH_HEADING))
                .strafeToLinearHeading(new Vector2d(LEFT_COLLECT_POSE_X, SOUTH_COLLECT_POSE_Y), SOUTH_HEADING);
        TrajectoryActionBuilder path4 = myBot.getDrive().actionBuilder(new Pose2d(LEFT_COLLECT_POSE_X,SOUTH_COLLECT_POSE_Y, SOUTH_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOT_POSE_X, SHOOT_POSE_Y), SHOOT_HEADING, new TranslationalVelConstraint(25) );
        // --- MID CYCLE
        TrajectoryActionBuilder path5 = myBot.getDrive().actionBuilder(new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING))
                .strafeToLinearHeading(new Vector2d(MID_COLLECT_POSE_X, SOUTH_READY_POSE_Y), SOUTH_HEADING);
        TrajectoryActionBuilder path6 = myBot.getDrive().actionBuilder(new Pose2d(MID_COLLECT_POSE_X, SOUTH_READY_POSE_Y, SOUTH_HEADING))
                .strafeToLinearHeading(new Vector2d(MID_COLLECT_POSE_X, SOUTH_COLLECT_POSE_Y), SOUTH_HEADING);
        TrajectoryActionBuilder path7 = myBot.getDrive().actionBuilder(new Pose2d(MID_COLLECT_POSE_X,SOUTH_COLLECT_POSE_Y, SOUTH_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOT_POSE_X, SHOOT_POSE_Y), SHOOT_HEADING, new TranslationalVelConstraint(25) );
        TrajectoryActionBuilder path8 = myBot.getDrive().actionBuilder(new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOT_POSE_X-20, SHOOT_POSE_Y), SHOOT_HEADING);
////             --- RIGHT CYCLE
//        TrajectoryActionBuilder path8 = myBot.getDrive().actionBuilder(new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING))
//                .strafeToLinearHeading(new Vector2d(RIGHT_COLLECT_POSE_X, SOUTH_READY_POSE_Y), SOUTH_HEADING);
//        TrajectoryActionBuilder path9 = myBot.getDrive().actionBuilder(new Pose2d(RIGHT_COLLECT_POSE_X, SOUTH_READY_POSE_Y, SOUTH_HEADING))
//                .strafeToLinearHeading(new Vector2d(RIGHT_COLLECT_POSE_X, SOUTH_COLLECT_POSE_Y), SOUTH_HEADING);
//        TrajectoryActionBuilder path10 = myBot.getDrive().actionBuilder(new Pose2d(RIGHT_COLLECT_POSE_X,SOUTH_COLLECT_POSE_Y, SOUTH_HEADING))
//                .strafeToLinearHeading(new Vector2d(SHOOT_POSE_X, SHOOT_POSE_Y), SHOOT_HEADING, new TranslationalVelConstraint(25) );



        // ================== RUN ==================
        myBot.runAction(new SequentialAction(
                new SleepAction(SHOOT_TIME_SEC),
                path1.build(),
                path2.build(),
                path3.build(),
                path4.build(),
                new SleepAction(SHOOT_TIME_SEC),
                path5.build(),
                path6.build(),
                path7.build(),
                new SleepAction(SHOOT_TIME_SEC),
                path8.build()
//                path9.build(),
//                path10.build(),
        ));

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
