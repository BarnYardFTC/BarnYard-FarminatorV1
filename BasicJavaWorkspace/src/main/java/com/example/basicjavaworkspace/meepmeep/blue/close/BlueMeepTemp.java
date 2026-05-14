package com.example.basicjavaworkspace.meepmeep.blue.close;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;


public class BlueMeepTemp {

    public static final int SHOOT_TIME_MS =1500;
    public static final Pose2d startPose = new Pose2d( -53.333, -45.5, 225);
    public static final Pose2d shootPose = new Pose2d(-23, -22, 227);
    //    public static final Pose2d leftReadyPose = new Pose2d(-11.5, -25, 270);
//    public static final Pose2d midReadyPose = new Pose2d(-11.5, -25, 270);
//    public static final Pose2d rightReadyPose = new Pose2d(34.5, -25, 270);
    public static final Pose2d leftCollectPose = new Pose2d(-11.5, -63, 270);
    public static final Pose2d midCollectPose = new Pose2d(12, -63, 270);
    public static final Pose2d rightCollectPose = new Pose2d(34.5, -63, 270);

    public static final Pose2d parkPose = new Pose2d(-40, -22, 227);

    public static final Pose2d gateOpenPose = new Pose2d(0, -50, 90);
    public static final Pose2d gateCollectPose = new Pose2d(10,-57, 227);

    public static final Pose2d leftLoadZoneCollect = new Pose2d(50, -65, 270);
    public static final Pose2d rightLoadZoneCollect = new Pose2d(52, -65, 270);
    public static final Pose2d midLoadZoneCollect = new Pose2d(54, -65, 270);

    public static final Pose2d leftLoadZoneReady = new Pose2d(50, -57, 270);
    public static final Pose2d rightLoadZoneReady = new Pose2d(52, -57, 270);
    public static final Pose2d midLoadZoneReady = new Pose2d(54, -57, 270);

    // paths nigga ----------------------------------------------------------------- no ai stamp only rawdogging
    public static TrajectoryActionBuilder goShootLeft;
    public static TrajectoryActionBuilder goShootMid;
    public static TrajectoryActionBuilder goShootRight;

    public static TrajectoryActionBuilder goShootPre;
    public static TrajectoryActionBuilder goPark;
    public static TrajectoryActionBuilder goCollectLeft;
    public static TrajectoryActionBuilder goCollectMid;
    public static TrajectoryActionBuilder goCollectRight;

    public static TrajectoryActionBuilder openGate;
    public static TrajectoryActionBuilder gateCollection;

    public static TrajectoryActionBuilder takeFromLoadZone;

    public static Vector2d shootVec = new Vector2d(-22,-23);



    public static void createPath(){
        MeepMeep meepMeep = new MeepMeep(800);
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(13.157, 18.03044)
                .build();
        goShootPre = myBot.getDrive().actionBuilder(startPose)
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2());
        goCollectLeft = goShootPre.endTrajectory().fresh()
                .strafeToLinearHeading(leftCollectPose.component1(),leftCollectPose.component2());
        goShootLeft = goCollectLeft.endTrajectory().fresh()
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2());
        goCollectMid = goShootLeft.endTrajectory().fresh()
                .strafeToLinearHeading(midCollectPose.component1(),midCollectPose.component2());
        goShootMid = goCollectMid.endTrajectory().fresh()
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2());
        goCollectRight = goShootMid.endTrajectory().fresh()
                .strafeToLinearHeading(rightCollectPose.component1(),rightCollectPose.component2());
        goShootRight = goCollectRight.endTrajectory().fresh()
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2());
        goPark = goShootMid.endTrajectory().fresh()
                .strafeToLinearHeading(parkPose.component1(),parkPose.component2());


    }


}
