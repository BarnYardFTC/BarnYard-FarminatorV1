package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;
import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.TankDrive;
import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.tuning.TuningOpModes;
@Config

@Autonomous(name="raodrunner tuning test", group="test")
public class RoadRunnerTuningTest extends LinearOpMode {
    public static double radius = 10;
    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d beginPose = new Pose2d(15, 0, Math.PI/2);
        if (TuningOpModes.DRIVE_CLASS.equals(RoadRunnerMecanumDrive.class)) {
            RoadRunnerMecanumDrive drive = new RoadRunnerMecanumDrive(hardwareMap, beginPose);

            waitForStart();

            while (opModeIsActive()){
                Actions.runBlocking(
                        drive.actionBuilder(new Pose2d(radius, 0, Math.PI/2))
                                .splineTo(new Vector2d(0, radius), Math.PI)
                                .waitSeconds(0.5)
                                .splineTo(new Vector2d(-radius, 0), Math.PI*1.5)
                                .waitSeconds(0.5)
                                .splineTo(new Vector2d(0, -radius), Math.PI*2)
                                .waitSeconds(0.5)
                                .splineTo(new Vector2d(radius, 0), Math.PI/2)
                                .waitSeconds(0.5)
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
