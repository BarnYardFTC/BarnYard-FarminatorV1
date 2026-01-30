package com.example.basicjavaworkspace.meepmeep.blue.close;

import com.acmerobotics.roadrunner.*;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import java.lang.Math;

public class Blue_Close_ThreePlusFifteen {

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
    public static double NORTH_HEADING = Math.toRadians(90);


    public static double LEFT_COLLECT_POSE_X = -11.5;
    public static double MID_COLLECT_POSE_X = 12;
    public static double RIGHT_COLLECT_POSE_X = 35;

    public static double LOAD_ZONE_COLLECTION_MID_X=62;
    public static double LOAD_ZONE_COLLECTION_RIGHT_X=59;
    public static double LOAD_ZONE_COLLECTION_LEFT_X=65;
    public static double LOAD_ZONE_COLLECTION_Y=-62;

    public static double GATE_POSE_X = -5;
    public static double GATE_POSE_Y = -44; // currently unused in your paths

    public static double GATE_COLLECTION_X = 10;
    public static double GATE_COLLECTION_Y = -60;


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

        Vector2d leftReadyVec = new Vector2d(LEFT_COLLECT_POSE_X, SOUTH_READY_POSE_Y);
        Vector2d midCollectVec = new Vector2d(MID_COLLECT_POSE_X, SOUTH_READY_POSE_Y);
        Vector2d leftCollectVec = new Vector2d(LEFT_COLLECT_POSE_X, SOUTH_COLLECT_POSE_Y);
        Vector2d gateAtCollectYVec = new Vector2d(GATE_POSE_X, SOUTH_COLLECT_POSE_Y);

        Vector2d shootVec = new Vector2d(SHOOT_POSE_X, SHOOT_POSE_Y);
        Pose2d shootPose = new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING);


        Pose2d midCollectPose = new Pose2d(
                MID_COLLECT_POSE_X,
                SOUTH_COLLECT_POSE_Y + MID_Y_OFFSET,
                SOUTH_HEADING
        );

        Pose2d leftCollectPose = new Pose2d(
                LEFT_COLLECT_POSE_X,
                SOUTH_COLLECT_POSE_Y,
                SOUTH_HEADING
        );

        Pose2d rightCollectPose = new Pose2d(
                RIGHT_COLLECT_POSE_X,
                SOUTH_COLLECT_POSE_Y + RIGHT_Y_OFFSET,
                SOUTH_HEADING
        );


        Pose2d gatePose = new Pose2d(
                GATE_POSE_X,
                GATE_POSE_Y-MID_Y_OFFSET,
                NORTH_HEADING
        );

        Pose2d endPose = new Pose2d(
                ENDING_POSE_X,
                ENDING_POSE_Y,
                SHOOT_HEADING + END_HEADING_OFFSET_RAD
        );

        Pose2d gateCollectPose = new Pose2d(
                GATE_COLLECTION_X,
                GATE_COLLECTION_Y,
                SHOOT_HEADING + END_HEADING_OFFSET_RAD
        );
        Pose2d LoadCollectMidPose = new Pose2d(
                LOAD_ZONE_COLLECTION_MID_X,
                LOAD_ZONE_COLLECTION_Y,
                SOUTH_HEADING
        );

        Pose2d LoadCollectLeftPose = new Pose2d(
                LOAD_ZONE_COLLECTION_LEFT_X,
                LOAD_ZONE_COLLECTION_Y,
                SOUTH_HEADING
        );
        Pose2d LoadCollectRightPose = new Pose2d(
                LOAD_ZONE_COLLECTION_RIGHT_X,
                LOAD_ZONE_COLLECTION_Y,
                SOUTH_HEADING
        );


        TranslationalVelConstraint fastToShoot = new TranslationalVelConstraint(VEL_TO_SHOOT);

        TrajectoryActionBuilder path1 = myBot.getDrive().actionBuilder(startPose)
                .strafeToLinearHeading(shootVec, SHOOT_HEADING)
                .splineToLinearHeading(midCollectPose, new Rotation2d(0, -3));

        TrajectoryActionBuilder path2 = myBot.getDrive().actionBuilder(midCollectPose)
                .strafeToLinearHeading(shootVec, SHOOT_HEADING, fastToShoot);

        TrajectoryActionBuilder path3 = myBot.getDrive().actionBuilder(shootPose)
                .splineToLinearHeading(leftCollectPose, new Rotation2d(0, -3));

        TrajectoryActionBuilder path4 = myBot.getDrive().actionBuilder(leftCollectPose)
                .splineToLinearHeading(shootPose, new Rotation2d(-2, -1), fastToShoot)
                .splineToConstantHeading(gateAtCollectYVec, new Rotation2d(0,-3))
                .splineToLinearHeading(gateCollectPose, new Rotation2d(0,-5))
                .splineToLinearHeading(shootPose, new Rotation2d(-2,-2), fastToShoot);



        TrajectoryActionBuilder path5 = myBot.getDrive().actionBuilder(shootPose)
                .splineToLinearHeading(rightCollectPose, new Rotation2d(1, -3));

        TrajectoryActionBuilder path7 = myBot.getDrive().actionBuilder(rightCollectPose)
                .splineToLinearHeading(shootPose, new Rotation2d(-2, -2), fastToShoot);

//        TrajectoryActionBuilder path8 = myBot.getDrive().actionBuilder(shootPose);
//                .splineToConstantHeading(LoadCollectMidPose, new Rotation2d(0,3);
//                .splineToConstantHeading(LoadCollectLeftPose, new Rotation2d(0,3)
//                .splineToConstantHeading(LoadCollectRightPose, new Rotation2d(0,3);






        // ================== RUN ==================
        myBot.runAction(new SequentialAction(
                new SleepAction(SHOOT_TIME_SEC),
                path1.build(),
                path2.build(),
                new SleepAction(SHOOT_TIME_SEC),
                path3.build(),
                path4.build(),
                new SleepAction(SHOOT_TIME_SEC),
                path5.build(),
                path7.build(),

                new SleepAction(SHOOT_TIME_SEC)
        ));

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
