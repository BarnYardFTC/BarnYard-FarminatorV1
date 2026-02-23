package com.example.basicjavaworkspace.meepmeep.red.far;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class Red_Far_ThreePlusThree {


    public static final int SHOOT_TIME_MS = 1500;

    public static final Pose2d startPose = new Pose2d( 60, 15.0, Math.toRadians(180));
    public static final Pose2d shootPose = new Pose2d(54, 15, Math.toRadians(-199.0));
    public static final Pose2d lastPose = new Pose2d(40, 15, Math.toRadians(-205.0));
    public static final Pose2d lastShootPose = new Pose2d(40, 22.0, Math.toRadians(-242.0));


    public static final Pose2d leftCollectPose = new Pose2d(-11.5, 53.0, Math.toRadians(-270.0));
    public static final Pose2d midCollectPose = new Pose2d(12, 53.0, Math.toRadians(-270.0));
    public static final Pose2d rightCollectPose = new Pose2d(34.5, 60.0 , Math.toRadians(-270.0));

    public static final Pose2d loadZoneReady = new Pose2d(34.5, 63.0, Math.toRadians(0)); // Maybe y needs some changes


    public static final Pose2d leftLoadZoneReady = new Pose2d(-11.5, 20.0, Math.toRadians(-270.0));
    public static final Pose2d rightLoadZoneReady = new Pose2d(12, 20.0, Math.toRadians(-270.0));
    public static final Pose2d LoadZoneCollect = new Pose2d(63, 63.0, Math.toRadians(-270.0));

    public static final Pose2d parkPose = new Pose2d(-40, 22.0, Math.toRadians(-227.0));
    public static final Pose2d gateOpenPose = new Pose2d(0, 70.0, Math.toRadians(-180.0));
    public static final Pose2d gateCollectPose = new Pose2d(10,57.0, Math.toRadians(-227.0));

//    private static final TranslationalVelConstraint fastToShoot = new TranslationalVelConstraint(RoadRunnerMecanumDrive.PARAMS.maxWheelVel*1.5);

    // paths nigga ----------------------------------------------------------------- no ai stamp only rawdogging
    public static TrajectoryActionBuilder goShootRight;
    public static TrajectoryActionBuilder goShootMid;
    public static TrajectoryActionBuilder goShootLeft;

    public static TrajectoryActionBuilder goShootLeftGate;

    public static TrajectoryActionBuilder goShootPre;
    public static TrajectoryActionBuilder goCollectRight;
    public static TrajectoryActionBuilder goCollectMid;
    public static TrajectoryActionBuilder goCollectLeft;
    public static TrajectoryActionBuilder goCollectLeftGate;

    public static TrajectoryActionBuilder openGate;
    public static TrajectoryActionBuilder gateCollection;
    public static TrajectoryActionBuilder goLastShoot;

    public static TrajectoryActionBuilder goShootPreLeave;
    public static TrajectoryActionBuilder goShootMidLeave;

    public static TrajectoryActionBuilder goReadyCollectLoadZone;
    public static TrajectoryActionBuilder goCollectLoadZone;
    public static TrajectoryActionBuilder goShootLoadZone;
    public static TrajectoryActionBuilder goFromLine;





        // -----------------------------
        // Main Simulation
        // -----------------------------
        public static void main(String[] args) {

            MeepMeep meepMeep = new MeepMeep(800);

            RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                    .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                    .setDimensions(17, 18.03044)
                    .build();


            goShootPre = myBot.getDrive().actionBuilder(startPose)
                    .strafeToLinearHeading(shootPose.component1(),shootPose.component2());

            goCollectRight = goShootPre.endTrajectory().fresh()
                    .setTangent(shootPose.component2())
                    .splineTo(rightCollectPose.component1(), new Rotation2d(0,1.1));

            goShootRight = goCollectRight.endTrajectory().fresh()
                    .setTangent(Math.toRadians(-90))
                    .splineTo(shootPose.component1(), shootPose.component2().toDouble()-Math.toRadians(180));

            goReadyCollectLoadZone = goShootRight.endTrajectory().fresh()
                    .setTangent(shootPose.component2())
                    .splineToLinearHeading(loadZoneReady, new Rotation2d(0,1.1));

            goCollectLoadZone = goReadyCollectLoadZone.endTrajectory().fresh()
                    .strafeToConstantHeading(LoadZoneCollect.component1());

            goShootLoadZone = goCollectLoadZone.endTrajectory().fresh()
                    .setTangent(Math.toRadians(-180))
                    .splineToSplineHeading(shootPose, new Rotation2d(1,-2));

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
