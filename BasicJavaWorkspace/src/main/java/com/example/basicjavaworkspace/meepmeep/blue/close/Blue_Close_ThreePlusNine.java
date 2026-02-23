package com.example.basicjavaworkspace.meepmeep.blue.close;

import com.acmerobotics.roadrunner.*;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import java.lang.Math;

public class Blue_Close_ThreePlusNine {

    public static final int SHOOT_TIME_MS = 1500;

    public static final Pose2d startPose = new Pose2d( -53.333, 45.5, Math.toRadians(180));
    public static final Pose2d shootPose = new Pose2d(-23, 22.0, Math.toRadians(-224.0));
    public static final Pose2d lastShootPose = new Pose2d(-36, 15.0, Math.toRadians(-242.0));


    public static final Pose2d leftCollectPose = new Pose2d(-11.5, 53.0, Math.toRadians(-270.0));
    public static final Pose2d midCollectPose = new Pose2d(12, 62.0, Math.toRadians(-270.0));
    public static final Pose2d rightCollectPose = new Pose2d(35.5, 65.0, Math.toRadians(-270.0));


    public static final Pose2d leftLoadZoneReady = new Pose2d(-11.5, 20.0, Math.toRadians(-270.0));
    public static final Pose2d rightLoadZoneReady = new Pose2d(12, 20.0, Math.toRadians(-270.0));
    public static final Pose2d midLoadZoneReady = new Pose2d(34.5, 20.0, Math.toRadians(-270.0));

    public static final Pose2d parkPose = new Pose2d(-40, 22.0, Math.toRadians(-227.0));
    public static final Pose2d gateOpenPose = new Pose2d(0, 65, Math.toRadians(360 - 180));
    public static final Pose2d gateCollectPose = new Pose2d(10,57.0, Math.toRadians(-227.0));

//    private static final TranslationalVelConstraint fastToShoot = new TranslationalVelConstraint(RoadRunnerMecanumDrive.PARAMS.maxWheelVel*1.5);

    // paths nigga ----------------------------------------------------------------- no ai stamp only rawdogging
    public static TrajectoryActionBuilder goShootLast;
    public static TrajectoryActionBuilder goShootMid;
    public static TrajectoryActionBuilder goShootLeft;

    public static TrajectoryActionBuilder goShootLeftGate;

    public static TrajectoryActionBuilder goShootPre;
    public static TrajectoryActionBuilder goPark;
    public static TrajectoryActionBuilder goCollectRight;
    public static TrajectoryActionBuilder goCollectMid;
    public static TrajectoryActionBuilder goCollectLeft;

    public static TrajectoryActionBuilder openGate;
    public static TrajectoryActionBuilder gateCollection;
    public static TrajectoryActionBuilder goLastShoot;

    public static TrajectoryActionBuilder goShootPreLeave;
    public static TrajectoryActionBuilder goShootMidLeave;
    public static TrajectoryActionBuilder goShootLeftLeave;
    public static void main(String[] args) {

        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(13.157, 18.03044)
                .build();


        goShootPre = myBot.getDrive().actionBuilder(startPose)
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2());

        goCollectLeft = goShootPre.endTrajectory().fresh()
//                .setTangent(Math.toRadians(-224))
                .splineToLinearHeading(leftCollectPose, new Rotation2d(0, 3))
                .setTangent(new Rotation2d(1,-4.0))
                .splineToLinearHeading(gateOpenPose, Math.toRadians(100.0));


        goShootLeft = goCollectLeft.endTrajectory().fresh()
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2());

        goCollectMid = goShootLeft.endTrajectory().fresh()  // REady
                .setTangent(Math.toRadians(75))
                .splineToSplineHeading(midCollectPose, new Rotation2d(0,3));

        goShootMid = goCollectMid.endTrajectory().fresh()
                .setTangent(Math.toRadians(270))
                .splineToLinearHeading(shootPose, Math.toRadians(-150.0));

        goCollectRight = goShootMid.endTrajectory().fresh()
                .setTangent(Math.toRadians(50))
                .splineToSplineHeading(rightCollectPose, new Rotation2d(0, 3));

        goShootLast = goCollectRight.endTrajectory().fresh                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          ()
                .setTangent(Math.toRadians(-120.0))
                .splineToLinearHeading(lastShootPose, Math.toRadians(-198.0));

        goShootPreLeave = myBot.getDrive().actionBuilder(startPose)
                .strafeToLinearHeading(lastShootPose.component1(),lastShootPose.component2());

        goShootLeftLeave = goCollectLeft.endTrajectory().fresh                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          ()
                .setTangent(Math.toRadians(-120.0))
                .splineToLinearHeading(lastShootPose, Math.toRadians(-198.0));

        goShootMidLeave = goCollectMid.endTrajectory().fresh                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          ()
                .setTangent(Math.toRadians(-120.0))
                .splineToLinearHeading(lastShootPose, Math.toRadians(-198.0));

        // ================== RUN ==================
        myBot.runAction(new SequentialAction(
                goShootPre.build(),
                goCollectLeft.build(),
                goShootLeft.build(),
                goCollectMid.build(),
                goShootMid.build(),
                goCollectRight.build(),
                goShootLast.build()
                ));

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
