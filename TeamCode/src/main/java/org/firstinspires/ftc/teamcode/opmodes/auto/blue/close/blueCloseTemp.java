package org.firstinspires.ftc.teamcode.opmodes.auto.blue.close;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;

import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

public class blueCloseTemp {
    public static final Pose2d startPose = new Pose2d( -53.333, -45.5, 225);
    public static final Pose2d shootPose = new Pose2d(-23, -22, 227);
    public static final Pose2d leftReadyPose = new Pose2d(-11.5, -25, 270);
    public static final Pose2d midReadyPose = new Pose2d(-11.5, -25, 270);
    public static final Pose2d rightReadyPose = new Pose2d(34.5, -25, 270);
    public static final Pose2d leftCollectPose = new Pose2d(-11.5, -63, 270);
    public static final Pose2d midCollectPose = new Pose2d(12, -63, 270);
    public static final Pose2d rightCollectPose = new Pose2d(34.5, -63, 270);

    public static final Pose2d parkPose = new Pose2d(-40, -22, 227);

    public static final Pose2d gateOpenPose = new Pose2d(0, -50, 90);

    public static final Pose2d leftLoadZoneCollect = new Pose2d(50, -65, 270);
    public static final Pose2d rightLoadZoneCollect = new Pose2d(52, -65, 270);
    public static final Pose2d midLoadZoneCollect = new Pose2d(54, -65, 270);

    public static final Pose2d leftLoadZoneReady = new Pose2d(50, -57, 270);
    public static final Pose2d rightLoadZoneReady = new Pose2d(52, -57, 270);
    public static final Pose2d midLoadZoneReady = new Pose2d(54, -57, 270);

// paths nigga ----------------------------------------------------------------- no ai stamp only rawdogging
    public static TrajectoryActionBuilder goShoot;
    public static TrajectoryActionBuilder goPark;
    public static TrajectoryActionBuilder goCollectLeft;
    public static TrajectoryActionBuilder goCollectMid;
    public static TrajectoryActionBuilder goCollectRight;

    public static TrajectoryActionBuilder openGate;

    public static TrajectoryActionBuilder takeFromLoadZone;


    public static void goShoot(RoadRunnerMecanumDrive drive) {
        goShoot = drive.actionBuilder(startPose)
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2());

        goPark = goShoot.endTrajectory()
                .strafeToLinearHeading(parkPose.component1(),parkPose.component2());


    }


}
