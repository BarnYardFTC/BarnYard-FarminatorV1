package org.firstinspires.ftc.teamcode.opmodes.auto.red.far;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;

import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

public class RedFarTemp {

    public static final int SHOOT_TIME_MS = 1500;

    public static final Pose2d startPose = new Pose2d( 60, 15.0, Math.toRadians(180));
    public static final Pose2d shootPose = new Pose2d(54, 15, Math.toRadians(-203.0));
    public static final Pose2d lastPose = new Pose2d(37, 15, Math.toRadians(-199.0));
    public static final Pose2d rightCollectPose = new Pose2d(34.5, 60.0 , Math.toRadians(-270.0));
    public static final Pose2d rightLoadZoneReady = new Pose2d(63, 40, Math.toRadians(-270.0));


    public static final Pose2d leftLoadZone = new Pose2d(52, 63, Math.toRadians(-270.0));
    public static final Pose2d midLoadZoneReady = new Pose2d(55, 40, Math.toRadians(-270.0));
    public static final Pose2d midLoadZone = new Pose2d(52, 63, Math.toRadians(-270.0));
    public static final Pose2d rightLoadZone = new Pose2d(63, 63, Math.toRadians(-270.0));

    public static final Pose2d midCollectPose = new Pose2d(10, 70.0, Math.toRadians(-270.0));


    public static final Pose2d loadZoneReady = new Pose2d(34.5, 63.0, Math.toRadians(0)); // Maybe y needs some changes

    public static final Pose2d LoadZoneCollect = new Pose2d(63, 63.0, Math.toRadians(-270.0));

    private static final TranslationalVelConstraint fastToShoot = new TranslationalVelConstraint(RoadRunnerMecanumDrive.PARAMS.maxWheelVel*1.4);
    private static final TranslationalVelConstraint fastToShoot2 = new TranslationalVelConstraint(RoadRunnerMecanumDrive.PARAMS.maxWheelVel*1.2);

    // paths nigga ----------------------------------------------------------------- no ai stamp only rawdogging
    public static TrajectoryActionBuilder goShootRight;
    public static TrajectoryActionBuilder goShootMid;

    public static TrajectoryActionBuilder goShootLeftGate;

    public static TrajectoryActionBuilder goShootPre;
    public static TrajectoryActionBuilder goCollectRight;
    public static TrajectoryActionBuilder goCollectMid;
    public static TrajectoryActionBuilder goCollectLeft;
    public static TrajectoryActionBuilder goCollectLeftGate;

    public static TrajectoryActionBuilder goReadyCollectLoadZone, goCollectLoadZone, goShootLoadZone, goFromLine, goStartToLoadZoneCollect, goLoadZoneShoot;

    public static void createPath(RoadRunnerMecanumDrive drive) {
        goShootPre = drive.actionBuilder(startPose)
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
                .splineTo(rightCollectPose.component1(), new Rotation2d(0,1.1), fastToShoot2);

        goShootRight = goCollectRight.endTrajectory().fresh()
                .setTangent(Math.toRadians(-90))
                .splineTo(shootPose.component1(), shootPose.component2().toDouble()-Math.toRadians(180));

        goReadyCollectLoadZone = goShootRight.endTrajectory().fresh()
                .setTangent(shootPose.component2())
                .splineToLinearHeading(loadZoneReady, new Rotation2d(0,1.1), fastToShoot);

        goCollectLoadZone = goReadyCollectLoadZone.endTrajectory().fresh()
                .strafeToConstantHeading(LoadZoneCollect.component1(), fastToShoot);

        goShootLoadZone = goCollectLoadZone.endTrajectory().fresh()
                .setTangent(Math.toRadians(-180))
                .splineToSplineHeading(shootPose, new Rotation2d(1,-2));

        //experimental
        goCollectMid = goShootLoadZone.endTrajectory().fresh()
                .setTangent(Math.toRadians(200))
                .splineToSplineHeading(midCollectPose, midCollectPose.heading, fastToShoot);

        goShootMid = goCollectMid.endTrajectory().fresh()
                .setTangent(Math.toRadians(270))
                .splineToLinearHeading(shootPose, Math.toRadians(150.0), fastToShoot);

        goFromLine = drive.actionBuilder(shootPose)
                .strafeToConstantHeading(lastPose.component1());


    }
}
