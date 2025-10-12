package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.roadrunner.MecanumDrive;

import java.util.Collections;

public class AutoDefault extends CommandOpMode {
    private BarnRobot farminator;
    private MecanumDrive drive;

    public static Pose2d POSE1 = new Pose2d(0,0,Math.toRadians(0));
    public static double POSE2_X = 10;
    public static double POSE2_Y = 10;
    public static double POSE2_HEADING = Math.toRadians(90);


    @Override
    public void initialize() {

        // ------------------------
        // Initialize Robot Systems
        // ------------------------
        farminator = BarnRobot.getInstance();
        farminator.initBarnRobotSystemsTeleop(this, new OpModeData());
        drive = new MecanumDrive(hardwareMap, POSE1);


        TrajectoryActionBuilder path1 = drive.actionBuilder(POSE1)
                .strafeToLinearHeading(new Vector2d(POSE2_X, POSE2_Y), POSE2_HEADING);

        new SequentialCommandGroup(
                new DriveActionCommand(path1.build())
        ).schedule();

    }

    @Override
    public void run() {
        super.run();
        farminator.periodic();
    }
}
