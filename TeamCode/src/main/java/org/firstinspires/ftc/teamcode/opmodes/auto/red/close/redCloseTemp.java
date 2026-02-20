package org.firstinspires.ftc.teamcode.opmodes.auto.red.close;

import com.acmerobotics.roadrunner.MecanumKinematics;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;

import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

public class redCloseTemp {

    public static final int SHOOT_TIME_MS = 1500;

    public static final Pose2d startPose = new Pose2d(53.333, -45.5, Math.toRadians(-45.0));
    public static final Pose2d shootPose = new Pose2d(23.0, -22, Math.toRadians(-44.0));
    public static final Pose2d lastShootPose = new Pose2d(36.0, -15, Math.toRadians(-62.0));


    public static final Pose2d leftCollectPose = new Pose2d(11.5, -53, Math.toRadians(-90.0));
    public static final Pose2d midCollectPose = new Pose2d(-12.0, -62, Math.toRadians(-90.0));
    public static final Pose2d rightCollectPose = new Pose2d(-35.5, -65, Math.toRadians(-90.0));


    public static final Pose2d leftLoadZoneReady = new Pose2d(11.5, -20, Math.toRadians(-90.0));
    public static final Pose2d rightLoadZoneReady = new Pose2d(-12.0, -20, Math.toRadians(-90.0));
    public static final Pose2d midLoadZoneReady = new Pose2d(-34.5, -20, Math.toRadians(-90.0));

    public static final Pose2d parkPose = new Pose2d(40.0, -22, Math.toRadians(-47.0));
    public static final Pose2d gateOpenPose = new Pose2d(-0.0, -70, Math.toRadians(0.0));
    public static final Pose2d gateCollectPose = new Pose2d(-10.0,-57, Math.toRadians(-47.0));

    private static final TranslationalVelConstraint fastToShoot = new TranslationalVelConstraint(RoadRunnerMecanumDrive.PARAMS.maxWheelVel*1.5);

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

    public static void createPath(RoadRunnerMecanumDrive drive){
        goShootPre = drive.actionBuilder(startPose)
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2());
        goCollectLeft = goShootPre.endTrajectory().fresh()
                .splineToLinearHeading(leftCollectPose, new Rotation2d(-.1, 2.0))
                .setTangent(new Rotation2d(1,-1.0))
                .splineToLinearHeading(gateOpenPose, Math.toRadians(280.0), fastToShoot);
        goShootLeft = goCollectLeft.endTrajectory().fresh()
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2(), fastToShoot);
        goCollectMid = goShootLeft.endTrajectory().fresh()
                .setTangent(new Rotation2d(2.1,-0.4))
                .splineToLinearHeading(midCollectPose, Math.toRadians(270.0), fastToShoot);
        goShootMid = goCollectMid.endTrajectory().fresh()
                .setTangent(new Rotation2d(0,-1.0))
                .splineToLinearHeading(shootPose, Math.toRadians(30.0));
        goCollectRight = goShootMid.endTrajectory().fresh()
                .setTangent(new Rotation2d(2.1,-0.2))
                .splineToSplineHeading(rightCollectPose, new Rotation2d(0, 1.5),
                        new TranslationalVelConstraint(RoadRunnerMecanumDrive.PARAMS.maxWheelVel*1.25));
        goShootLast = goCollectRight.endTrajectory().fresh                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          ()
                .setTangent(Math.toRadians(60.0))
                .splineToLinearHeading(lastShootPose, Math.toRadians(-18.0), fastToShoot);
    }
}
