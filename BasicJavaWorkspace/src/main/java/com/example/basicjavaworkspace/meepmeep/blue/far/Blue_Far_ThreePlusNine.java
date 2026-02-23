package com.example.basicjavaworkspace.meepmeep.blue.far;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class Blue_Far_ThreePlusNine {
    public static final int SHOOT_TIME_MS = 1500;

    public static final Pose2d startPose = new Pose2d( 60, -15.0, Math.toRadians(-180.0));
    public static final Pose2d shootPose = new Pose2d(54, -15.0, Math.toRadians(203.0));
    public static final Pose2d lastPose = new Pose2d(37, -15.0, Math.toRadians(199.0));
    public static final Pose2d rightCollectPose = new Pose2d(34.5, -60.0 , Math.toRadians(270.0));
    public static final Pose2d rightLoadZoneReady = new Pose2d(63, -40.0, Math.toRadians(270.0));


    public static final Pose2d leftLoadZone = new Pose2d(52, -63.0, Math.toRadians(270.0));
    public static final Pose2d midLoadZoneReady = new Pose2d(55, -40.0, Math.toRadians(270.0));
    public static final Pose2d midLoadZone = new Pose2d(52, -63.0, Math.toRadians(270.0));
    public static final Pose2d rightLoadZone = new Pose2d(63, -63.0, Math.toRadians(270.0));


    public static final Pose2d loadZoneReady = new Pose2d(34.5, -63.0, Math.toRadians(-0.0)); // Maybe y needs some changes

    public static final Pose2d LoadZoneCollect = new Pose2d(63, -63.0, Math.toRadians(270.0));



    public static TrajectoryActionBuilder goShootRight;
    public static TrajectoryActionBuilder goShootMid;

    public static TrajectoryActionBuilder goShootLeftGate;

    public static TrajectoryActionBuilder goShootPre;
    public static TrajectoryActionBuilder goCollectRight;
    public static TrajectoryActionBuilder goCollectMid;
    public static TrajectoryActionBuilder goCollectLeft;
    public static TrajectoryActionBuilder goCollectLeftGate;

    public static TrajectoryActionBuilder goReadyCollectLoadZone, goCollectLoadZone, goShootLoadZone, goFromLine, goStartToLoadZoneCollect, goLoadZoneShoot;





    // -----------------------------
    // Main Simulation
    // -----------------------------
    public static void main(String[] args) {

        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(13.157, 18.03044)
                .build();


        goShootPre = myBot.getDrive().actionBuilder(startPose)
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2().toDouble());

        goStartToLoadZoneCollect = goShootPre.endTrajectory().fresh()
                .strafeToLinearHeading(midLoadZoneReady.component1(), midLoadZoneReady.component2())
                .strafeToLinearHeading(midLoadZone.component1(), midLoadZone.component2())
                .strafeToLinearHeading(midLoadZoneReady.component1(), midLoadZoneReady.component2())
                .strafeToLinearHeading(midLoadZone.component1(), midLoadZone.component2())
                .strafeToLinearHeading(rightLoadZoneReady.component1(), midLoadZoneReady.component2())
                .strafeToLinearHeading(rightLoadZone.component1(), rightLoadZone.component2());

        goLoadZoneShoot = goStartToLoadZoneCollect.endTrajectory().fresh()
                .strafeToLinearHeading(shootPose.component1(), shootPose.component2());


        goCollectRight = goShootPre.endTrajectory().fresh()
                .setTangent(shootPose.component2())
                .splineTo(rightCollectPose.component1(), new Rotation2d(-0.0,-1.1));

        goShootRight = goCollectRight.endTrajectory().fresh()
                .setTangent(Math.toRadians(90.0))
                .splineTo(shootPose.component1(), shootPose.component2().toDouble()-Math.toRadians(-180.0));

        goReadyCollectLoadZone = goShootRight.endTrajectory().fresh()
                .setTangent(shootPose.component2())
                .splineToLinearHeading(loadZoneReady, new Rotation2d(-0.0,-1.1));

        goCollectLoadZone = goReadyCollectLoadZone.endTrajectory().fresh()
                .strafeToConstantHeading(LoadZoneCollect.component1());

        goShootLoadZone = goCollectLoadZone.endTrajectory().fresh()
                .setTangent(Math.toRadians(180.0))
                .splineToSplineHeading(shootPose, new Rotation2d(1.0,2));

        goFromLine = goShootLoadZone.endTrajectory().fresh()
                .strafeToConstantHeading(lastPose.component1());



        // Run the trajectory
        myBot.runAction(
                new SequentialAction(
                        goShootPre.build(),
                        goCollectRight.build(),
                        goShootRight.build(),
                        goReadyCollectLoadZone.build(),
                        goCollectLoadZone.build(),
                        goShootLoadZone.build(),
                        goFromLine.build()
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
