package com.example.basicjavaworkspace.meepmeep.red.close;

import com.acmerobotics.roadrunner.*;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.lang.Math;

public class Red_Close_ThreePlusSix {

    // ================== FIELD / POSES ==================
    public static double START_POSE_X = -53.333;
    public static double START_POSE_Y = 45.5;
    public static double START_HEADING = Math.toRadians(135);

    public static double SHOOT_POSE_X = -23;
    public static double SHOOT_POSE_Y = 22;
    public static double SHOOT_HEADING = Math.toRadians(137);

    public static double SOUTH_READY_POSE_Y = 25;
    public static double SOUTH_COLLECT_POSE_Y = 63;

    public static double NORTH_HEADING = Math.toRadians(90);

    public static double LEFT_COLLECT_POSE_X = -11.5;
    public static double MID_COLLECT_POSE_X = 12;
    public static double GATE_POSE_X = -2;
    public static double GATE_POSE_Y = -44; // currently unused in your paths

    public static double ENDING_POSE_X = -40;
    public static double ENDING_POSE_Y = 22;

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

    private static final double VEL_TO_SHOOT = 120.0;

    private static final double END_HEADING_OFFSET_RAD = Math.toRadians(15);


    public static void main(String[] args) {

        MeepMeep meepMeep = new MeepMeep(WINDOW_SIZE);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(MAX_VEL, MAX_ACCEL, MAX_ANG_VEL, MAX_ANG_ACCEL, TRACK_WIDTH)
                .setDimensions(BOT_WIDTH, BOT_HEIGHT)
                .build();

        //---LEFT CYCLE

        Pose2d startPose = new Pose2d(START_POSE_X, START_POSE_Y, START_HEADING);

        Vector2d startNudge = new Vector2d(START_POSE_X, START_POSE_Y + START_Y_NUDGE);

        Vector2d leftReady = new Vector2d(LEFT_COLLECT_POSE_X, SOUTH_READY_POSE_Y);
        Vector2d leftCollect = new Vector2d(LEFT_COLLECT_POSE_X, SOUTH_COLLECT_POSE_Y);
        Vector2d gateAtCollectY = new Vector2d(GATE_POSE_X, SOUTH_COLLECT_POSE_Y);

        Vector2d shootVec = new Vector2d(SHOOT_POSE_X, SHOOT_POSE_Y);
        Pose2d shootPose = new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING);

        Pose2d midCollectPose = new Pose2d(
                MID_COLLECT_POSE_X,
                SOUTH_COLLECT_POSE_Y - MID_Y_OFFSET,
                NORTH_HEADING
        );

        Pose2d leftReadyPose = new Pose2d(
                LEFT_COLLECT_POSE_X, SOUTH_READY_POSE_Y, NORTH_HEADING
        );


        Pose2d endPose = new Pose2d(
                ENDING_POSE_X,
                ENDING_POSE_Y,
                SHOOT_HEADING -   END_HEADING_OFFSET_RAD
        );


        //      ===== Trajectory constraints =====
        TranslationalVelConstraint fastToShoot = new TranslationalVelConstraint(VEL_TO_SHOOT);


        //         ===== Paths =====
        TrajectoryActionBuilder firstShoot = myBot.getDrive().actionBuilder(startPose)
                .strafeToLinearHeading(startNudge, NORTH_HEADING)
                .strafeToLinearHeading(shootVec, SHOOT_HEADING, fastToShoot);

        TrajectoryActionBuilder collectLeftArts = myBot.getDrive().actionBuilder(shootPose)
                .strafeToLinearHeading(leftReady, NORTH_HEADING)
                // keep Rotation2d hardcoded (as requested)
                .strafeToConstantHeading(leftCollect, new TranslationalVelConstraint(60));

        TrajectoryActionBuilder leftToShoot = myBot.getDrive().actionBuilder(new Pose2d(LEFT_COLLECT_POSE_X, SOUTH_COLLECT_POSE_Y, NORTH_HEADING))
                .strafeToLinearHeading(shootVec, SHOOT_HEADING, fastToShoot);

        TrajectoryActionBuilder collectMidArts = myBot.getDrive().actionBuilder(shootPose)
                .splineToLinearHeading(midCollectPose, new Rotation2d(0, 3));

        TrajectoryActionBuilder midToShoot = myBot.getDrive().actionBuilder(midCollectPose)
                .splineToLinearHeading(endPose, new Rotation2d(-1.8, 1), fastToShoot);




        // ================== RUN ==================
        myBot.runAction(new SequentialAction(
                firstShoot.build(),
                collectLeftArts.build(),
                leftToShoot.build(),
                collectMidArts.build(),
                midToShoot.build()
        ));

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
