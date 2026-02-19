package org.firstinspires.ftc.teamcode.opmodes.auto.red.close;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

public class redCloseTemp {

    public static final int SHOOT_TIME_MS = 1500;

    public static final Pose2d startPose = new Pose2d(53.333, 45.5, Math.toRadians(180));
    public static final Pose2d shootPose = new Pose2d(23.0, 22, Math.toRadians(47.0));
    public static final Pose2d lastShootPose = new Pose2d(40.0, 22, Math.toRadians(61.0));


    public static final Pose2d leftCollectPose = new Pose2d(11.5, 63, Math.toRadians(90.0));
    public static final Pose2d midCollectPose = new Pose2d(-12.0, 63, Math.toRadians(90.0));
    public static final Pose2d rightCollectPose = new Pose2d(-34.5, 63, Math.toRadians(90.0));

    public static final Pose2d leftLoadZoneReady = new Pose2d(11.5, 20, Math.toRadians(90.0));
    public static final Pose2d rightLoadZoneReady = new Pose2d(-12.0, 20, Math.toRadians(90.0));
    public static final Pose2d midLoadZoneReady = new Pose2d(-34.5, 20, Math.toRadians(90.0));

    public static final Pose2d parkPose = new Pose2d(40.0, 22, Math.toRadians(47.0));
    public static final Pose2d gateOpenPose = new Pose2d(-0.0, 50, Math.toRadians(270));
    public static final Pose2d gateCollectPose = new Pose2d(-10.0,57, Math.toRadians(47.0));

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
    }
}
