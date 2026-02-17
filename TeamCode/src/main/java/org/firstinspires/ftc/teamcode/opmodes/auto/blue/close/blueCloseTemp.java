package org.firstinspires.ftc.teamcode.opmodes.auto.blue.close;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

public class blueCloseTemp {

    public static final int SHOOT_TIME_MS = 1500;

    public static final Pose2d startPose = new Pose2d( -53.333, -45.5, Math.toRadians(225));
    public static final Pose2d shootPose = new Pose2d(-23, -22, Math.toRadians(227));
    public static final Pose2d lastShootPose = new Pose2d(-40, -22, Math.toRadians(241));


    public static final Pose2d leftCollectPose = new Pose2d(-11.5, -63, Math.toRadians(270));
    public static final Pose2d midCollectPose = new Pose2d(12, -63, Math.toRadians(270));
    public static final Pose2d rightCollectPose = new Pose2d(34.5, -63, Math.toRadians(270));

    public static final Pose2d leftCollectReady = new Pose2d(-11.5, -20, Math.toRadians(270));
    public static final Pose2d midCollectReady = new Pose2d(34.5, -20, Math.toRadians(270));
    public static final Pose2d rightCollectReady = new Pose2d(12, -20, Math.toRadians(270));

    public static final Pose2d parkPose = new Pose2d(-40, -22, Math.toRadians(227));
    public static final Pose2d gateOpenPose = new Pose2d(0, -50, Math.toRadians(90));
    public static final Pose2d gateCollectPose = new Pose2d(10,-57, Math.toRadians(227));

// paths nigga ----------------------------------------------------------------- no ai stamp only rawdogging
    public static TrajectoryActionBuilder goShootLeft, goShootMid, goShootRight, goShootPre, goLastShoot;
    public static TrajectoryActionBuilder goCollectLeft, goCollectMid, goCollectRight;
    public static TrajectoryActionBuilder goCollectReadyLeft, goCollectReadyMid, goCollectReadyRight;

    public static TrajectoryActionBuilder goPark;
    public static TrajectoryActionBuilder openGate;
    public static TrajectoryActionBuilder gateCollection;

    public static void createPath(RoadRunnerMecanumDrive drive){
        goShootPre = drive.actionBuilder(startPose)
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2());
        goCollectRight = goShootPre.endTrajectory()
                .strafeToLinearHeading(rightCollectPose.component1(),rightCollectPose.component2());
        goShootRight = goCollectRight.endTrajectory()
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2());
        goCollectMid = goShootRight.endTrajectory()
                .strafeToLinearHeading(midCollectPose.component1(),midCollectPose.component2());
        goShootMid = goCollectMid.endTrajectory()
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2());
        goCollectLeft = goShootMid.endTrajectory()
                .strafeToLinearHeading(leftCollectPose.component1(),leftCollectPose.component2());
        goLastShoot = goCollectLeft.endTrajectory().fresh()
                .strafeToLinearHeading(lastShootPose.component1(),lastShootPose.component2());
//        goShootLeft = goCollectLeft.endTrajectory()
//                .strafeToLinearHeading(shootPose.component1(),shootPose.component2());
//        goPark = goShootLeft.endTrajectory();

    }
}
