package org.firstinspires.ftc.teamcode.opmodes.auto.red.close;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;

import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

public class RedCloseTrajs {

    public static final int SHOOT_TIME_MS = 1500;

    public static final Pose2d startPose = new Pose2d( -53.333, 45.5, Math.toRadians(180));
    public static final Pose2d shootPose = new Pose2d(-23, 22.0, Math.toRadians(-224.0));
    public static final Pose2d lastShootPose = new Pose2d(-36, 5.0, Math.toRadians(-242.0));


    public static final Pose2d leftCollectPose = new Pose2d(-11.5, 60.0, Math.toRadians(-270.0));
    public static final Pose2d midCollectPose = new Pose2d(12, 70.0, Math.toRadians(-270.0));
    public static final Pose2d rightCollectPose = new Pose2d(35.5, 72.0, Math.toRadians(-270.0));


    public static final Pose2d leftLoadZoneReady = new Pose2d(-11.5, 20.0, Math.toRadians(-270.0));
    public static final Pose2d rightLoadZoneReady = new Pose2d(12, 20.0, Math.toRadians(-270.0));
    public static final Pose2d midLoadZoneReady = new Pose2d(34.5, 20.0, Math.toRadians(-270.0));

    public static final Pose2d parkPose = new Pose2d(-40, 22.0, Math.toRadians(-227.0));
    public static final Pose2d gateOpenPose = new Pose2d(0, 65, Math.toRadians(360 - 180));
    public static final Pose2d gateCollectPose = new Pose2d(10,57.0, Math.toRadians(-227.0));

    private static final TranslationalVelConstraint fastToShoot = new TranslationalVelConstraint(RoadRunnerMecanumDrive.PARAMS.maxWheelVel*1.5);
    private static final TranslationalVelConstraint fastToShoot2 = new TranslationalVelConstraint(150);

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

    public static void createPath(RoadRunnerMecanumDrive drive){
        goShootPre = drive.actionBuilder(startPose)
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2());

        goCollectLeft = goShootPre.endTrajectory().fresh()
                .splineToLinearHeading(leftCollectPose, new Rotation2d(0, 3))
                .setTangent(new Rotation2d(1,-4.0))
                .splineToLinearHeading(gateOpenPose, Math.toRadians(100.0));


        goShootLeft = goCollectLeft.endTrajectory().fresh()
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2(), fastToShoot);

        goCollectMid = goShootLeft.endTrajectory().fresh()
                .setTangent(Math.toRadians(125))
                .splineToSplineHeading(midCollectPose, new Rotation2d(0,3));

        goShootMid = goCollectMid.endTrajectory().fresh()
                .setTangent(Math.toRadians(270))
                .splineToLinearHeading(shootPose, Math.toRadians(-150.0), fastToShoot);

        goCollectRight = goShootMid.endTrajectory().fresh()
                .setTangent(Math.toRadians(50))
                .splineToSplineHeading(rightCollectPose, new Rotation2d(0, 3), fastToShoot);

        goShootLast = goCollectRight.endTrajectory().fresh                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          ()
                .setTangent(Math.toRadians(-120.0))
                .splineToLinearHeading(lastShootPose, Math.toRadians(-198.0), fastToShoot);

//        goShootPreLeave = drive.actionBuilder(startPose)
//                .strafeToLinearHeading(lastShootPose.component1(),lastShootPose.component2());

//        goShootLeftLeave = goCollectLeft.endTrajectory().fresh                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          ()
//                .setTangent(Math.toRadians(-120.0))
//                .splineToLinearHeading(lastShootPose, Math.toRadians(-198.0));
//
//        goShootMidLeave = goCollectMid.endTrajectory().fresh                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          ()
//                .setTangent(Math.toRadians(-120.0))
//                .splineToLinearHeading(lastShootPose, Math.toRadians(-198.0));
    }
}
