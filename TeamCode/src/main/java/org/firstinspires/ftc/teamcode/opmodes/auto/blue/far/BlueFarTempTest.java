package org.firstinspires.ftc.teamcode.opmodes.auto.blue.far;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;

import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

public class BlueFarTempTest {

    public static final int SHOOT_TIME_MS = 1500;

    public static final Pose2d startPose = new Pose2d( 60, -15.0, Math.toRadians(-180.0));
    public static final Pose2d shootPose = new Pose2d(54, -15.0, Math.toRadians(203.0));
    public static final Pose2d lastPose = new Pose2d(37, -15.0, Math.toRadians(199.0));
    public static final Pose2d rightCollectPose = new Pose2d(34.5, -60.0 , Math.toRadians(270.0));

    public static final Pose2d loadZoneReady = new Pose2d(34.5, -63.0, Math.toRadians(-0.0)); // Maybe y needs some changes

    public static final Pose2d LoadZoneCollect = new Pose2d(63, -63.0, Math.toRadians(360));

    public static final Pose2d HPS = new Pose2d(63, -63.0, Math.toRadians(270));



//    private static final TranslationalVelConstraint fastToShoot = new TranslationalVelConstraint(RoadRunnerMecanumDrive.PARAMS.maxWheelVel*1.4);
//    private static final TranslationalVelConstraint fastToShoot2 = new TranslationalVelConstraint(RoadRunnerMecanumDrive.PARAMS.maxWheelVel*1.2);

    // paths nigga ----------------------------------------------------------------- no ai stamp only rawdogging
    public static TrajectoryActionBuilder goShootRight;
    public static TrajectoryActionBuilder goShootMid;

    public static TrajectoryActionBuilder goShootLeftGate;

    public static TrajectoryActionBuilder goShootPre;
    public static TrajectoryActionBuilder goCollectRight;
    public static TrajectoryActionBuilder goCollectMid;
    public static TrajectoryActionBuilder goCollectLeft;
    public static TrajectoryActionBuilder goCollectLeftGate;

    public static TrajectoryActionBuilder goReadyCollectLoadZone, goCollectLoadZone, goShootLoadZone, goFromLine;

    public static TrajectoryActionBuilder goHPSCycle, goHPSCycleAlt;
    public static TrajectoryActionBuilder goShootCycle;

    public static void createPath(RoadRunnerMecanumDrive drive) {
        goShootPre = drive.actionBuilder(startPose)
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2().toDouble());

        goCollectRight = goShootPre.endTrajectory().fresh()
                .setTangent(shootPose.component2())
                .splineTo(rightCollectPose.component1(), new Rotation2d(0,-2));

        goShootRight = goCollectRight.endTrajectory().fresh()
                .setTangent(Math.toRadians(90.0))
                .splineTo(shootPose.component1(), shootPose.component2().toDouble()-Math.toRadians(-180.0));

        goReadyCollectLoadZone = goShootRight.endTrajectory().fresh()
                .setTangent(shootPose.component2())
                .splineToLinearHeading(loadZoneReady, new Rotation2d(-0.0,-1.1));

        goCollectLoadZone = goShootRight.endTrajectory().fresh()
                .setTangent(Math.toRadians(45))
                .splineToSplineHeading(LoadZoneCollect, new Rotation2d(3,-0.2));

        goShootLoadZone = goCollectLoadZone.endTrajectory().fresh()
                .setTangent(Math.toRadians(180.0))
                .splineToSplineHeading(shootPose, new Rotation2d(1.0,2));

        goFromLine = goShootLoadZone.endTrajectory().fresh()
                .strafeToConstantHeading(lastPose.component1());

        goHPSCycleAlt = goShootLoadZone.endTrajectory().fresh() //another variant just in case
                .setTangent(Math.toRadians(45))
                .splineToSplineHeading(HPS, new Rotation2d(0,-1));

        goHPSCycle = goShootLoadZone.endTrajectory().fresh()
                .setTangent(Math.toRadians(-135))
                .splineToSplineHeading(HPS, new Rotation2d(-0.3,-2));

        goShootCycle = goHPSCycle.endTrajectory().fresh()
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(shootPose, new Rotation2d(0,1));

    }
}
