package org.firstinspires.ftc.teamcode.opmodes.auto.red.far;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;

import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

public class RedFarTemp {

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

    private static final TranslationalVelConstraint fastToShoot = new TranslationalVelConstraint(RoadRunnerMecanumDrive.PARAMS.maxWheelVel*1.5);

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
    public static TrajectoryActionBuilder goCollectLeftGate;

    public static TrajectoryActionBuilder openGate;
    public static TrajectoryActionBuilder gateCollection;
    public static TrajectoryActionBuilder goLastShoot;

    public static TrajectoryActionBuilder goShootPreLeave;
    public static TrajectoryActionBuilder goShootMidLeave;
    public static TrajectoryActionBuilder goShootLeftLeave;

    public static void createPath(RoadRunnerMecanumDrive drive){
        goShootPre = drive.actionBuilder(startPose)
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2());

        goCollectLeft = goShootPre.endTrajectory().fresh()
                .splineToLinearHeading(leftCollectPose, new Rotation2d(0.1, -2));

        goCollectLeftGate = goShootPre.endTrajectory().fresh()
                .splineToLinearHeading(leftCollectPose, new Rotation2d(0.1, -2))
                .setTangent(new Rotation2d(-1.0,1))
                .splineToLinearHeading(gateOpenPose, Math.toRadians(100.0), fastToShoot);

        goShootLeftGate = goCollectLeftGate.endTrajectory().fresh()
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2(), fastToShoot);

        goShootLeft = goCollectLeft.endTrajectory().fresh()
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2(), fastToShoot);

        goCollectMid = goShootLeft.endTrajectory().fresh()
                .setTangent(new Rotation2d(-2.1,0.4))
                .splineToLinearHeading(midCollectPose, Math.toRadians(90.0), fastToShoot);

        goShootMid = goCollectMid.endTrajectory().fresh()
                .setTangent(new Rotation2d(-0.0,1))
                .splineToLinearHeading(shootPose, Math.toRadians(-150.0));

        goCollectRight = goShootMid.endTrajectory().fresh()
                .setTangent(new Rotation2d(-2.1,1))
                .splineToSplineHeading(rightCollectPose, new Rotation2d(-0.0, -1.5),
                        new TranslationalVelConstraint(RoadRunnerMecanumDrive.PARAMS.maxWheelVel*1.25));

        goShootLast = goCollectRight.endTrajectory().fresh()
                .setTangent(Math.toRadians(-120.0))
                .splineToLinearHeading(lastShootPose, Math.toRadians(-198.0), fastToShoot);

        goShootPreLeave = drive.actionBuilder(startPose)
                .strafeToLinearHeading(lastShootPose.component1(),lastShootPose.component2());

        goShootLeftLeave = goCollectLeft.endTrajectory().fresh                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          ()
                .setTangent(Math.toRadians(-120.0))
                .splineToLinearHeading(lastShootPose, Math.toRadians(-198.0), fastToShoot);

        goShootMidLeave = goCollectMid.endTrajectory().fresh                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          ()
                .setTangent(Math.toRadians(-120.0))
                .splineToLinearHeading(lastShootPose, Math.toRadians(-198.0), fastToShoot);
    }
}
