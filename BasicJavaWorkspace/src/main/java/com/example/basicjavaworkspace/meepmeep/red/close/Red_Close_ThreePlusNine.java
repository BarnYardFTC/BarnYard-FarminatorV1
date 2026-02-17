package com.example.basicjavaworkspace.meepmeep.red.close;

import com.acmerobotics.roadrunner.*;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.lang.Math;

public class Red_Close_ThreePlusNine {
//
//    // ================== FIELD / POSES ==================
//    // SleepAction is in seconds (double). Keep as seconds.
//    public static double SHOOT_TIME_SEC = 2.0;
//
//    // Mirror of Blue:
//    // X stays same, Y flips sign, heading becomes (2π - heading)
//    public static double START_POSE_X = -53.333;
//    public static double START_POSE_Y = 45.5;
//    public static double START_HEADING = Math.toRadians(135);   // mirror of 225°
//
//    public static double SHOOT_POSE_X = -23;
//    public static double SHOOT_POSE_Y = 22;
//    public static double SHOOT_HEADING = Math.toRadians(133);   // mirror of 227°
//
//    public static double SOUTH_READY_POSE_Y = 30;
//    public static double SOUTH_COLLECT_POSE_Y = 53;
//
//    public static double SOUTH_HEADING = Math.toRadians(90);    // mirror of 270°
//
//    public static double LEFT_COLLECT_POSE_X = -11.5;
//    public static double MID_COLLECT_POSE_X = 12;
//    public static double RIGHT_COLLECT_POSE_X = 35;
//
//    public static double GATE_POSE_X = 0;
//    public static double GATE_POSE_Y = 44; // unused in these paths (kept for symmetry)
//
//    public static double ENDING_POSE_X = -40;
//    public static double ENDING_POSE_Y = 22;
//
//    // ================== BOT / SIM CONFIG ==================
//    private static final int WINDOW_SIZE = 800;
//
//    private static final double MAX_VEL = 60;
//    private static final double MAX_ACCEL = 60;
//    private static final double MAX_ANG_VEL = Math.toRadians(180);
//    private static final double MAX_ANG_ACCEL = Math.toRadians(180);
//    private static final double TRACK_WIDTH = 15;
//
//    private static final double BOT_WIDTH = 15.07;
//    private static final double BOT_HEIGHT = 16.961;
//
//    // ================== PATH TUNING (no Rotation2d here) ==================
//    private static final double START_Y_NUDGE = 5.0;
//    private static final double MID_Y_OFFSET = 8.0;
//    private static final double RIGHT_Y_OFFSET = 15.0;
//
//    private static final double VEL_TO_SHOOT = 150.0;
//
//    private static final double END_HEADING_OFFSET_RAD = Math.toRadians(15);
//
//    public static void main(String[] args) {
//
//        MeepMeep meepMeep = new MeepMeep(WINDOW_SIZE);
//
//        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
//                .setConstraints(MAX_VEL, MAX_ACCEL, MAX_ANG_VEL, MAX_ANG_ACCEL, TRACK_WIDTH)
//                .setDimensions(BOT_WIDTH, BOT_HEIGHT)
//                .build();
//
//        // ===== Common objects (cleaner, no magic numbers) =====
//        Pose2d startPose = new Pose2d(START_POSE_X, START_POSE_Y, START_HEADING);
//
//        // In Blue: START_POSE_Y + 5.  After mirroring, it becomes START_POSE_Y - 5.
//        Vector2d startNudge = new Vector2d(START_POSE_X, START_POSE_Y - START_Y_NUDGE);
//
//        Vector2d leftReady = new Vector2d(LEFT_COLLECT_POSE_X, SOUTH_READY_POSE_Y);
//        Vector2d leftCollect = new Vector2d(LEFT_COLLECT_POSE_X, SOUTH_COLLECT_POSE_Y);
//        Vector2d gateAtCollectY = new Vector2d(GATE_POSE_X, SOUTH_COLLECT_POSE_Y);
//
//        Vector2d shootVec = new Vector2d(SHOOT_POSE_X, SHOOT_POSE_Y);
//        Pose2d shootPose = new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING);
//
//        Pose2d midCollectPose = new Pose2d(
//                MID_COLLECT_POSE_X,
//                SOUTH_COLLECT_POSE_Y - MID_Y_OFFSET,   // mirror of +8 => -8 (relative to +53)
//                SOUTH_HEADING
//        );
//
//        Pose2d rightCollectPose = new Pose2d(
//                RIGHT_COLLECT_POSE_X,
//                SOUTH_COLLECT_POSE_Y - RIGHT_Y_OFFSET, // mirror of +15 => -15 (relative to +53)
//                SOUTH_HEADING
//        );
//
//        // In Blue end heading = SHOOT + 15deg; mirror means SHOOT - 15deg
//        Pose2d endPose = new Pose2d(
//                ENDING_POSE_X,
//                ENDING_POSE_Y,
//                SHOOT_HEADING - END_HEADING_OFFSET_RAD
//        );
//
//        TranslationalVelConstraint fastToShoot = new TranslationalVelConstraint(VEL_TO_SHOOT);
//
//        // ================== PATHS (same structure as Blue, mirrored) ==================
//
//        // Start -> Left ready -> Left collect -> Gate -> Shoot
//        TrajectoryActionBuilder path1 = myBot.getDrive().actionBuilder(startPose)
//                .strafeToLinearHeading(startNudge, SOUTH_HEADING)
//                .splineToConstantHeading(leftReady, SOUTH_HEADING)
//                // keep Rotation2d hardcoded (as requested)
//                .splineToConstantHeading(leftCollect, new Rotation2d(0, 0))
//                .splineToConstantHeading(gateAtCollectY, new Rotation2d(3, 8))
//                .strafeToLinearHeading(shootVec, SHOOT_HEADING, fastToShoot);
//
//        // Shoot -> Mid collect -> Shoot
//        TrajectoryActionBuilder path2 = myBot.getDrive().actionBuilder(shootPose)
//                .splineToLinearHeading(midCollectPose, new Rotation2d(0, -3))
//                .splineToLinearHeading(shootPose, new Rotation2d(2, -1), fastToShoot);
//
//        // Shoot -> Right collect -> End
//        TrajectoryActionBuilder path3 = myBot.getDrive().actionBuilder(shootPose)
//                .splineToLinearHeading(rightCollectPose, new Rotation2d(1, 2))
//                .splineToLinearHeading(endPose, new Rotation2d(1.8, -1), fastToShoot);
//
//        // ================== RUN ==================
//        myBot.runAction(new SequentialAction(
//                new SleepAction(SHOOT_TIME_SEC),
//                path1.build(),
//
//                new SleepAction(SHOOT_TIME_SEC),
//                path2.build(),
//
//                new SleepAction(SHOOT_TIME_SEC),
//                path3.build(),
//
//                new SleepAction(SHOOT_TIME_SEC)
//        ));
//
//        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
//                .setDarkMode(true)
//                .setBackgroundAlpha(0.95f)
//                .addEntity(myBot)
//                .start();
//    }

    public static double START_POSE_X = -53.333;
    public static double START_POSE_Y = 45.5;
    public static double START_HEADING = Math.toRadians(135);

    public static double SHOOT_POSE_X = -23;
    public static double SHOOT_POSE_Y = 22;
    public static double SHOOT_HEADING = Math.toRadians(137);
    public static double LEFT_SHOOT_HEADING = Math.toRadians(120);
    public static double MID_SHOOT_HEADING = Math.toRadians(115);
    public static double FAR_SHOOT_HEADING = Math.toRadians(110);

    public static double SOUTH_READY_POSE_Y = 26;
    public static double SOUTH_COLLECT_POSE_Y = 53;

    public static double NORTH_HEADING = Math.toRadians(90);

    public static double LEFT_COLLECT_POSE_X = -11.5;
    public static double MID_COLLECT_POSE_X = 12;
    public static double RIGHT_COLLECT_POSE_X = 35;

    public static double GATE_POSE_X = -5;
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


        // ===== Common objects (cleaner, no magic numbers) =====
        Pose2d startPose = new Pose2d(START_POSE_X, START_POSE_Y, START_HEADING);

        Vector2d startNudge = new Vector2d(START_POSE_X, START_POSE_Y + START_Y_NUDGE);

        Vector2d leftReady = new Vector2d(LEFT_COLLECT_POSE_X, SOUTH_READY_POSE_Y);
        Vector2d leftCollect = new Vector2d(LEFT_COLLECT_POSE_X, SOUTH_COLLECT_POSE_Y);
        Vector2d gateAtCollectY = new Vector2d(GATE_POSE_X, SOUTH_COLLECT_POSE_Y);

        Vector2d shootVec = new Vector2d(SHOOT_POSE_X, SHOOT_POSE_Y);
        Pose2d shootPose = new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING);
        Vector2d midShoot = new Vector2d(MID_COLLECT_POSE_X, SOUTH_READY_POSE_Y);
        Vector2d farShoot = new Vector2d(RIGHT_COLLECT_POSE_X, SOUTH_READY_POSE_Y);
        Vector2d leftShoot = new Vector2d(LEFT_COLLECT_POSE_X, SOUTH_READY_POSE_Y);


        Pose2d leftCollectPose = new Pose2d(
                LEFT_COLLECT_POSE_X,
                SOUTH_COLLECT_POSE_Y,
                NORTH_HEADING
        );

        Pose2d leftShootPose = new Pose2d(
                LEFT_COLLECT_POSE_X,
                SOUTH_READY_POSE_Y,
                LEFT_SHOOT_HEADING
        );

        Pose2d midCollectPose = new Pose2d(
                MID_COLLECT_POSE_X,
                SOUTH_COLLECT_POSE_Y + MID_Y_OFFSET,
                NORTH_HEADING
        );

        Pose2d midShootPose = new Pose2d(
                MID_COLLECT_POSE_X,
                SOUTH_READY_POSE_Y,
                MID_SHOOT_HEADING
        );

        Pose2d farShootHeading = new Pose2d(
                RIGHT_COLLECT_POSE_X,
                SOUTH_READY_POSE_Y,
                FAR_SHOOT_HEADING
        );

        Pose2d rightCollectPose = new Pose2d(
                RIGHT_COLLECT_POSE_X + 2,
                SOUTH_COLLECT_POSE_Y,
                NORTH_HEADING
        );

        Pose2d endPose = new Pose2d(
                ENDING_POSE_X,
                ENDING_POSE_Y,
                SHOOT_HEADING + END_HEADING_OFFSET_RAD
        );

        TranslationalVelConstraint fastToShoot = new TranslationalVelConstraint(VEL_TO_SHOOT);

        TrajectoryActionBuilder path1 = myBot.getDrive().actionBuilder(startPose)
                .strafeToLinearHeading(startNudge, NORTH_HEADING)
                .splineToConstantHeading(leftReady, NORTH_HEADING)
                // keep Rotation2d hardcoded (as requested)
                .splineToConstantHeading(leftCollect, new Rotation2d(0, 0));

        TrajectoryActionBuilder path2 = myBot.getDrive().actionBuilder(startPose)
                .strafeToLinearHeading(leftShoot, LEFT_SHOOT_HEADING, fastToShoot);

        TrajectoryActionBuilder path3 = myBot.getDrive().actionBuilder(startPose)
                .splineToLinearHeading(midCollectPose, new Rotation2d(0, -3));


        TrajectoryActionBuilder path4 = myBot.getDrive().actionBuilder(startPose)
                .strafeToLinearHeading(midShoot, MID_SHOOT_HEADING);

        TrajectoryActionBuilder path5 = myBot.getDrive().actionBuilder(startPose)
                .splineToLinearHeading(rightCollectPose, new Rotation2d(0, -4));

        TrajectoryActionBuilder path7 = myBot.getDrive().actionBuilder(startPose)
                .strafeToLinearHeading(farShoot, FAR_SHOOT_HEADING);


        myBot.runAction(new SequentialAction(
             path1.build(),
            path2.build(),
            path3.build(),
            path4.build(),
            path5.build(),
            path7.build()
        ));


    }

}
