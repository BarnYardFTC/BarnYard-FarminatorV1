package org.firstinspires.ftc.teamcode.util.roadrunner.tuning;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.util.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.roadrunner.TankDrive;

@Disabled
public final class SplineTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d beginPose = new Pose2d(15, 0, Math.PI/2);
        if (TuningOpModes.DRIVE_CLASS.equals(MecanumDrive.class)) {
            MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

            waitForStart();

//            Actions.runBlocking(
//                drive.actionBuilder(beginPose)
//                        .splineTo(new Vector2d(30, 30), Math.PI / 2)
//                        .splineTo(new Vector2d(0, 60), Math.PI)
//                        .build());

            while (opModeIsActive()){
                Actions.runBlocking(
                        drive.actionBuilder(new Pose2d(15, 0, Math.PI/2))
                                .splineTo(new Vector2d(0, 15), Math.PI)
                                .waitSeconds(0.5)
                                .splineTo(new Vector2d(-15, 0), Math.PI*1.5)
                                .waitSeconds(0.5)
                                .splineTo(new Vector2d(0, -15), Math.PI*2)
                                .waitSeconds(0.5)
                                .splineTo(new Vector2d(15, 0), Math.PI/2)
                                .build()
                );
            }
        } else if (TuningOpModes.DRIVE_CLASS.equals(TankDrive.class)) {
            TankDrive drive = new TankDrive(hardwareMap, beginPose);

            waitForStart();

            Actions.runBlocking(
                    drive.actionBuilder(beginPose)
                            .splineTo(new Vector2d(30, 30), Math.PI / 2)
                            .splineTo(new Vector2d(0, 60), Math.PI)
                            .build());
        } else {
            throw new RuntimeException();
        }
    }
}
