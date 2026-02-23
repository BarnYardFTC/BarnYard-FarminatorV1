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

    public static final Pose2d startPose = new Pose2d( 60, 15.0, Math.toRadians(-225.0));
    public static final Pose2d shootPose = new Pose2d(45, 00.0, Math.toRadians(-227.0));
    public static final Pose2d lastShootPose = new Pose2d(40, 22.0, Math.toRadians(-242.0));


    public static final Pose2d leftCollectPose = new Pose2d(-11.5, 53.0, Math.toRadians(-270.0));
    public static final Pose2d midCollectPose = new Pose2d(12, 53.0, Math.toRadians(-270.0));
    public static final Pose2d rightCollectPose = new Pose2d(34.5, 53.0, Math.toRadians(-270.0));

    public static final Pose2d leftLoadZoneReady = new Pose2d(-11.5, 20.0, Math.toRadians(-270.0));
    public static final Pose2d rightLoadZoneReady = new Pose2d(12, 20.0, Math.toRadians(-270.0));
    public static final Pose2d midLoadZoneReady = new Pose2d(34.5, 20.0, Math.toRadians(-270.0));

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
                    .strafeToLinearHeading(shootPose.component1(),shootPose.component2());

            goCollectRight = goShootPre.endTrajectory().fresh()
                    .splineToLinearHeading(rightCollectPose, new Rotation2d(1, 0.1));

            goShootRight = goCollectRight.endTrajectory().fresh()
                    .strafeToLinearHeading(shootPose.component1(),shootPose.component2());


            goCollectMid = goShootRight.endTrajectory().fresh()
                    .setTangent(90)
                    .splineToLinearHeading(midCollectPose, Math.toRadians(90.0));

            goShootMid = goCollectMid.endTrajectory().fresh()
                    .setTangent(new Rotation2d(-0.0,1))
                    .splineToLinearHeading(shootPose, Math.toRadians(-150.0));

            goCollectLeftGate = goShootMid.endTrajectory().fresh()
                    .splineToLinearHeading(leftCollectPose, new Rotation2d(0.1, -2))
                    .setTangent(new Rotation2d(-1.0,1))
                    .splineToLinearHeading(gateOpenPose, Math.toRadians(100.0));

            goShootLeftGate = goCollectLeftGate.endTrajectory().fresh()
                    .strafeToLinearHeading(shootPose.component1(),shootPose.component2());

            goCollectLeft = goShootMid.endTrajectory().fresh()
                    .strafeToLinearHeading(shootPose.component1(),shootPose.component2());

            goShootLeft = goCollectLeft.endTrajectory().fresh()
                    .strafeToLinearHeading(shootPose.component1(),shootPose.component2());

            goCollectRight = goShootMid.endTrajectory().fresh()
                    .setTangent(new Rotation2d(-2.1,1))
                    .splineToSplineHeading(rightCollectPose, new Rotation2d(-0.0, -1.5));

            goShootRight = goCollectRight.endTrajectory().fresh()
                    .setTangent(Math.toRadians(-120.0))
                    .splineToLinearHeading(lastShootPose, Math.toRadians(-198.0));


            // Run the trajectory
            myBot.runAction(
                    new SequentialAction(
                            goShootPre.build(),
                            goCollectRight.build(),
                            goShootRight.build(),
                            goCollectMid.build(),
                            goShootMid.build(),
                            goCollectLeftGate.build(),
                            goShootLeftGate.build()
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
