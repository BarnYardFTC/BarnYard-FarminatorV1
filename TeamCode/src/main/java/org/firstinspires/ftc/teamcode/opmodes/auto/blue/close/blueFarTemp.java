package org.firstinspires.ftc.teamcode.opmodes.auto.blue.close;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;

import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

public class blueFarTemp {

    public static final int SHOOT_TIME_MS = 1500;

    public static final Pose2d startPose = new Pose2d( 60, -15, Math.toRadians(225));
    public static final Pose2d shootPose = new Pose2d(-23, -22, Math.toRadians(227));
    public static final Pose2d lastShootPose = new Pose2d(-40, -22, Math.toRadians(242));


    public static final Pose2d leftCollectPose = new Pose2d(-11.5, -53, Math.toRadians(270));
    public static final Pose2d midCollectPose = new Pose2d(12, -53, Math.toRadians(270));
    public static final Pose2d rightCollectPose = new Pose2d(34.5, -53, Math.toRadians(270));

    public static final Pose2d leftLoadZoneReady = new Pose2d(-11.5, -20, Math.toRadians(270));
    public static final Pose2d rightLoadZoneReady = new Pose2d(12, -20, Math.toRadians(270));
    public static final Pose2d midLoadZoneReady = new Pose2d(34.5, -20, Math.toRadians(270));

    public static final Pose2d parkPose = new Pose2d(-40, -22, Math.toRadians(227));
    public static final Pose2d gateOpenPose = new Pose2d(0, -70, Math.toRadians(180));
    public static final Pose2d gateCollectPose = new Pose2d(10,-57, Math.toRadians(227));

    TranslationalVelConstraint fastToShoot = new TranslationalVelConstraint(150);

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
        goCollectLeft = drive.actionBuilder(shootPose)
                .splineToLinearHeading(leftCollectPose, new Rotation2d(0, -4))
                .splineToSplineHeading(gateOpenPose, new Rotation2d(3, -5));
        goShootLeft = drive.actionBuilder(gateOpenPose)
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2());
        goCollectMid = drive.actionBuilder(shootPose)
                .splineToLinearHeading(midCollectPose, new Rotation2d(0, -3));
        goShootMid = drive.actionBuilder(midCollectPose)
                .splineToLinearHeading(shootPose, new Rotation2d(-2, -1));
        openGate = drive.actionBuilder(shootPose)
                .splineToLinearHeading(gateOpenPose, new Rotation2d(0, -2));
        gateCollection = drive.actionBuilder(gateOpenPose)
                .splineToLinearHeading(gateCollectPose, new Rotation2d(0,-1));
        goShootLast = drive.actionBuilder(gateCollectPose)
                .splineToLinearHeading(lastShootPose, new Rotation2d(0,-2));

//        goCollectRight = drive.actionBuilder(shootPose)
//                .splineToLinearHeading(rightCollectPose, new Rotation2d(1, -2));
//        goShootRight = drive.actionBuilder(rightCollectPose)
//                .splineToLinearHeading(lastShootPose, new Rotation2d(-1.8, -1));
    }
}
