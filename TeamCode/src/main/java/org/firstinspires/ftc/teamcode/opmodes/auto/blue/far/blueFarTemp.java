package org.firstinspires.ftc.teamcode.opmodes.auto.blue.far;


import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

public class blueFarTemp {

    public static final int SHOOT_TIME_MS =1500;
    public static final Pose2d startPose = new Pose2d( 60, -15, 225);
    public static final Pose2d shootPose = new Pose2d(45, 0, 227);
    //    public static final Pose2d leftReadyPose = new Pose2d(-11.5, -25, 270);
//    public static final Pose2d midReadyPose = new Pose2d(-11.5, -25, 270);
//    public static final Pose2d rightReadyPose = new Pose2d(34.5, -25, 270);
    public static final Pose2d leftCollectPose = new Pose2d(-11.5, -63, 270);
    public static final Pose2d midCollectPose = new Pose2d(12, -63, 270);
    public static final Pose2d rightCollectPose = new Pose2d(34.5, -63, 270);

    public static final Pose2d parkPose = new Pose2d(-40, -22, 227);

    public static final Pose2d gateOpenPose = new Pose2d(0, -50, 90);
    public static final Pose2d gateCollectPose = new Pose2d(10,-57, 227);

    public static final Pose2d leftLoadZoneCollect = new Pose2d(50, -65, 270);
    public static final Pose2d rightLoadZoneCollect = new Pose2d(52, -65, 270);
    public static final Pose2d midLoadZoneCollect = new Pose2d(54, -65, 270);

    public static final Pose2d leftLoadZoneReady = new Pose2d(50, -57, 270);
    public static final Pose2d rightLoadZoneReady = new Pose2d(52, -57, 270);
    public static final Pose2d midLoadZoneReady = new Pose2d(54, -57, 270);
    public static final Pose2d lastShootPose = new Pose2d(-40, -22, Math.toRadians(241));

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
        goPark = goLastShoot.endTrajectory();

}
}
