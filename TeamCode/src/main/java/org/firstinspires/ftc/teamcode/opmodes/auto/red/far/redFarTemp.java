package org.firstinspires.ftc.teamcode.opmodes.auto.red.far;


import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

public class redFarTemp {

    public static final int SHOOT_TIME_MS =1500;
    public static final Pose2d startPose = new Pose2d( 60, 15, 135);
    public static final Pose2d shootPose = new Pose2d(45, 0, 137);
    //    public static final Pose2d leftReadyPose = new Pose2d(-11.5, -25, 270);
//    public static final Pose2d midReadyPose = new Pose2d(-11.5, -25, 270);
//    public static final Pose2d rightReadyPose = new Pose2d(34.5, -25, 270);
    public static final Pose2d leftCollectPose = new Pose2d(-11.5, 63, 90);
    public static final Pose2d midCollectPose = new Pose2d(12, 63, 90);
    public static final Pose2d rightCollectPose = new Pose2d(34.5, 63, 90);

    public static final Pose2d parkPose = new Pose2d(-40, 22, 127);

    public static final Pose2d gateOpenPose = new Pose2d(0, 50, 270);
    public static final Pose2d gateCollectPose = new Pose2d(10,57, 127);

    public static final Pose2d leftLoadZoneCollect = new Pose2d(50, 65, 90);
    public static final Pose2d rightLoadZoneCollect = new Pose2d(52, 65, 90);
    public static final Pose2d midLoadZoneCollect = new Pose2d(54, 65, 90);

    public static final Pose2d leftLoadZoneReady = new Pose2d(50, 57, 90);
    public static final Pose2d rightLoadZoneReady = new Pose2d(52, -57, 90);
    public static final Pose2d midLoadZoneReady = new Pose2d(54, -57, 90);

    // paths nigga ----------------------------------------------------------------- no ai stamp only rawdogging
    public static TrajectoryActionBuilder goShoot;
    public static TrajectoryActionBuilder goPark;
    public static TrajectoryActionBuilder goCollectLeft;
    public static TrajectoryActionBuilder goCollectMid;
    public static TrajectoryActionBuilder goCollectRight;

    public static TrajectoryActionBuilder openGate;
    public static TrajectoryActionBuilder gateCollection;

    public static TrajectoryActionBuilder takeFromLoadZone;

    public static Vector2d shootVec = new Vector2d(-22,23);




    public static void ShootnPark(RoadRunnerMecanumDrive drive) {
        goShoot = drive.actionBuilder(startPose)
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2());

        goPark = goShoot.endTrajectory()
                .strafeToLinearHeading(parkPose.component1(),parkPose.component2());

    }

    public static void CollectRightnShoot(RoadRunnerMecanumDrive drive) {
        goCollectRight = drive.actionBuilder(shootPose)
                .strafeToLinearHeading(rightCollectPose.component1(),rightCollectPose.component2());
        goShoot = goCollectRight.endTrajectory()
                .splineToConstantHeading(shootPose.component1(), shootPose.component2());
    }

    public static void CollectMidnShoot(RoadRunnerMecanumDrive drive) {
        goCollectMid = drive.actionBuilder(shootPose)
                .strafeToLinearHeading(midCollectPose.component1(),midCollectPose.component2());

        goShoot = goCollectMid.endTrajectory()
                .splineToConstantHeading(shootPose.component1(), shootPose.component2());

    }

    public static void CollectLeftnShoot(RoadRunnerMecanumDrive drive) {
        goCollectLeft = drive.actionBuilder(shootPose)
                .strafeToLinearHeading(leftCollectPose.component1(),leftCollectPose.component2());
        goShoot = goCollectLeft.endTrajectory()
                .splineToConstantHeading(shootPose.component1(), shootPose.component2());
    }

    public static void CollectLoadZonenShoot(RoadRunnerMecanumDrive drive){
        takeFromLoadZone = drive.actionBuilder(shootPose)
                .splineToLinearHeading(leftLoadZoneCollect,90)
                .splineToConstantHeading(midLoadZoneCollect.component1(),midLoadZoneCollect.component2())
                .splineToConstantHeading(rightLoadZoneCollect.component1(),rightLoadZoneCollect.component2());
        goShoot = takeFromLoadZone.endTrajectory()
                .splineToConstantHeading(shootVec, 90);//vec,h
        new WaitCommand(SHOOT_TIME_MS);  //change to smart shooting later niggers
    }

    public static void GateCollectnShoot(RoadRunnerMecanumDrive drive){
        openGate = drive.actionBuilder(shootPose)
                .splineToConstantHeading(gateOpenPose.component1(),gateOpenPose.component2());

        gateCollection = openGate.endTrajectory()
                .splineToConstantHeading(gateCollectPose.component1(),gateCollectPose.component2());

        goShoot = gateCollection.endTrajectory()
                .splineToConstantHeading(shootVec, 90);
    }



}
