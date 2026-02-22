package com.example.basicjavaworkspace.meepmeep.red.close;

import com.acmerobotics.roadrunner.*;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.lang.Math;


public class Red_Close_ThreePlusSix {

    public static final int SHOOT_TIME_MS = 1500;


    public static final Pose2d startPose = new Pose2d( -49.333, 45.5, Math.toRadians(360 - 225));
    public static final Pose2d shootPose = new Pose2d(-23, 22, Math.toRadians(360 - 224));
    public static final Pose2d lastShootPose = new Pose2d(-36, 5, Math.toRadians(360 - 240));


    public static final Pose2d leftCollectPose = new Pose2d(-11.5, 58, Math.toRadians(360 - 270));
    public static final Pose2d midCollectPose = new Pose2d(12, 72, Math.toRadians(360 - 270));
    public static final Pose2d rightCollectPose = new Pose2d(35.5, 72, Math.toRadians(360 - 270));


    public static final Pose2d leftLoadZoneReady = new Pose2d(-11.5, 20, Math.toRadians(360 - 270));
    public static final Pose2d rightLoadZoneReady = new Pose2d(12, 20, Math.toRadians(360 - 270));
    public static final Pose2d midLoadZoneReady = new Pose2d(34.5, 20, Math.toRadians(360 - 270));

    public static final Pose2d parkPose = new Pose2d(-40, 22, Math.toRadians(360 - 227));
    public static final Pose2d gateOpenPose = new Pose2d(0, 65, Math.toRadians(360 - 180));
    public static final Pose2d gateCollectPose = new Pose2d(10,57, Math.toRadians(360 - 227));

//    private static final TranslationalVelConstraint fastToShoot = new TranslationalVelConstraint(RoadRunnerMecanumDrive.PARAMS.maxWheelVel*1.5);

    // paths nigga ----------------------------------------------------------------- no ai stamp only rawdogging
    public static TrajectoryActionBuilder goShootLast;
    public static TrajectoryActionBuilder goShootMid;
    public static TrajectoryActionBuilder goShootLeft;

    public static TrajectoryActionBuilder goShootPre;
    public static TrajectoryActionBuilder goPark;
    public static TrajectoryActionBuilder goCollectRight;
    public static TrajectoryActionBuilder goCollectMid;
    public static TrajectoryActionBuilder goCollectLeft;

    public static TrajectoryActionBuilder openGate;
    public static TrajectoryActionBuilder gateCollection;
    public static TrajectoryActionBuilder goLastShoot;





    public static void main(String[] args) {

        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(15.07, 16.961)
                .build();

        //---LEFT CYCLE
        goShootPre = myBot.getDrive().actionBuilder(startPose)
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2());
        goCollectLeft = goShootPre.endTrajectory().fresh()
                .splineToLinearHeading(leftCollectPose, new Rotation2d(-.1, 2.0))
                .setTangent(new Rotation2d(1,-4.0))
                .splineToLinearHeading(gateOpenPose, Math.toRadians(100.0));//
        goShootLeft = goCollectLeft.endTrajectory().fresh()
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2());//
        goCollectMid = goShootLeft.endTrajectory().fresh()
                .setTangent(new Rotation2d(2.7,-0.8))
                .splineToLinearHeading(midCollectPose, Math.toRadians(90.0));//
        goShootMid = goCollectMid.endTrajectory().fresh()
                .setTangent(new Rotation2d(0,-1.0))
                .splineToLinearHeading(shootPose, Math.toRadians(-150.0));
        goCollectRight = goShootMid.endTrajectory().fresh()
                .setTangent(new Rotation2d(2.1,-0.2))
                .splineToSplineHeading(rightCollectPose, new Rotation2d(0, 1.5));

//                        new TranslationalVelConstraint(RoadRunnerMecanumDrive.PARAMS.maxWheelVel*1.25));
        goShootLast = goCollectRight.endTrajectory().fresh                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          ()
                .splineToLinearHeading(lastShootPose, new Rotation2d(-1,1));


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
