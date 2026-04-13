package com.example.basicjavaworkspace.meepmeep.blue.close;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class Blue_Close_ThreePlusFifteen {
    public static final int SHOOT_TIME_MS = 1500;

    public static final Pose2d startPose = new Pose2d(-53.333, -45.5, Math.toRadians(225));
    public static final Pose2d shootPose = new Pose2d(-23, -22, Math.toRadians(224));
    public static final Pose2d lastShootPose = new Pose2d(-36, -8, Math.toRadians(247));


    public static final Pose2d leftCollectPose = new Pose2d(-11.5, -60, Math.toRadians(270));
    public static final Pose2d midCollectPose = new Pose2d(12, -60, Math.toRadians(270));
    public static final Pose2d rightCollectPose = new Pose2d(35.5, -60, Math.toRadians(270));


    public static final Pose2d leftLoadZoneReady = new Pose2d(-11.5, -20, Math.toRadians(270));
    public static final Pose2d rightLoadZoneReady = new Pose2d(12, -20, Math.toRadians(270));
    public static final Pose2d midLoadZoneReady = new Pose2d(34.5, -20, Math.toRadians(270));

    public static final Pose2d parkPose = new Pose2d(-40, -22, Math.toRadians(227));
    public static final Pose2d gateOpenPose = new Pose2d(0, -60, Math.toRadians(180));
    public static final Pose2d gateCollectPose = new Pose2d(10, -59, Math.toRadians(227));

//    private static final TranslationalVelConstraint fastToShoot = new TranslationalVelConstraint(RoadRunnerMecanumDrive.PARAMS.maxWheelVel*1.5);

    // paths nigga ----------------------------------------------------------------- no ai stamp only rawdogging
    public static TrajectoryActionBuilder goShootLast;
    public static TrajectoryActionBuilder goShootMid;
    public static TrajectoryActionBuilder goShootLeft;
    public static TrajectoryActionBuilder goShootRight;

    public static TrajectoryActionBuilder goShootGate;


    public static TrajectoryActionBuilder goShootLeftGate;

    public static TrajectoryActionBuilder goShootPre;
    public static TrajectoryActionBuilder goPark;
    public static TrajectoryActionBuilder goCollectRight;
    public static TrajectoryActionBuilder goCollectMid;
    public static TrajectoryActionBuilder goCollectLeft;
    public static TrajectoryActionBuilder goCollectLeftGate;

    public static TrajectoryActionBuilder openGate;
    public static TrajectoryActionBuilder gateCollection;
    public static TrajectoryActionBuilder goLastShoot;

    public static TrajectoryActionBuilder goShootPreLeave;
    public static TrajectoryActionBuilder goShootMidLeave;
    public static TrajectoryActionBuilder goShootLeftLeave;
    public static TrajectoryActionBuilder  goOpenAndCollectGate;

    public static void main(String[] args) {

        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(13.157, 18.03044)
                .build();


        goShootPre = myBot.getDrive().actionBuilder(startPose)
                .strafeToLinearHeading(shootPose.component1(), shootPose.component2());

        goCollectLeft = goShootPre.endTrajectory().fresh()
                .splineToLinearHeading(leftCollectPose, new Rotation2d(-.1, -2));

        goCollectLeftGate = goShootPre.endTrajectory().fresh()
                .splineToLinearHeading(leftCollectPose, new Rotation2d(-.1, -2))
                .setTangent(new Rotation2d(1, 1))
                .splineToLinearHeading(gateOpenPose, Math.toRadians(-100));

        goShootLeftGate = goCollectLeftGate.endTrajectory().fresh()
                .strafeToLinearHeading(shootPose.component1(), shootPose.component2());

        goShootLeft = goCollectLeft.endTrajectory().fresh()
                .strafeToLinearHeading(shootPose.component1(), shootPose.component2());

        goCollectMid = goShootLeftGate.endTrajectory().fresh()
                .setTangent(new Rotation2d(2.1, 0.4))
                .splineToLinearHeading(midCollectPose, Math.toRadians(-90));

        goShootMid = goCollectMid.endTrajectory().fresh()
                .setTangent(new Rotation2d(0, 1))
                .splineToLinearHeading(shootPose, Math.toRadians(150));
        goOpenAndCollectGate = goShootMid.endTrajectory().fresh()
                .setTangent(new Rotation2d(1,1))
                .splineToLinearHeading(gateCollectPose,new Rotation2d(0,-3));
        goShootGate = goOpenAndCollectGate.endTrajectory().fresh()
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2());

        goCollectRight = goShootMid.endTrajectory().fresh()
                .setTangent(new Rotation2d(2.1, 0.4))
                .splineToLinearHeading(rightCollectPose, Math.toRadians(-90));
//                        new TranslationalVelConstraint(RoadRunnerMecanumDrive.PARAMS.maxWheelVel*1.25));
        goShootRight = goCollectRight.endTrajectory().fresh()
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2());



        goShootPreLeave = myBot.getDrive().actionBuilder(startPose)
                .strafeToLinearHeading(lastShootPose.component1(), lastShootPose.component2());

        goShootLeftLeave = goCollectLeft.endTrajectory().fresh()
                .setTangent(Math.toRadians(120))
                .splineToLinearHeading(lastShootPose, Math.toRadians(198));

        goShootMidLeave = goCollectMid.endTrajectory().fresh()
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(lastShootPose, Math.toRadians(198));

        goShootLast = goCollectRight.endTrajectory().fresh()
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(lastShootPose, Math.toRadians(198));


        myBot.runAction(new SequentialAction(
                goShootPre.build(),
                goCollectMid.build(),
                goShootMid.build(),
                new SleepAction(1.5),
                goOpenAndCollectGate.build(),
                goShootGate.build(),
                new SleepAction(1.5),
                goOpenAndCollectGate.build(),
                goShootGate.build(),
                new SleepAction(1.5),
                goCollectLeft.build(),
                goShootLeft.build(),
                new SleepAction(1.5),
                goCollectRight.build(),
                goShootLast.build(),
               new SleepAction(1.5)



        ));



        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}


