package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.roadrunner.RoadRunnerMecanumDrive;

/**
 * Autonomous routine "3+0 Far":
 * - Starts from the far starting position
 * - Drives along a two-segment path to reach the shooting zone
 * - No element collection after shooting
 */
@Config
@Autonomous(name = "3+0 Far", group = "main")
public class ThreePlusZeroFar extends CommandOpMode {

    /** Robot and drive system instances */
    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;

    /** Starting pose */
    public static double POSE1_X = -39;
    public static double POSE1_Y = -53;
    public static double POSE1_HEADING = Math.toRadians(90);

    /** Midpoint shooting pose */
    public static double POSE2_X = -45;
    public static double POSE2_Y = -31;
    public static double POSE2_HEADING = Math.toRadians(235);

    /** Final shooting pose */
    public static double POSE3_X = 32;
    public static double POSE3_Y = -22;
    public static double POSE3_HEADING = Math.toRadians(180);

    @Override
    public void initialize() {

        /* Initialize robot and drive system */
        farminator = BarnRobot.getInstance();
        farminator.init(this, new OpModeData(OpModeData.AllianceColor.BLUE));
        drive = new RoadRunnerMecanumDrive(hardwareMap, new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING));

        /* Define trajectories */
        TrajectoryActionBuilder path1 = drive.actionBuilder(new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE2_X, POSE2_Y), POSE2_HEADING);

        TrajectoryActionBuilder path2 = path1.endTrajectory()
                .strafeToLinearHeading(new Vector2d(POSE3_X, POSE3_Y), POSE3_HEADING);

        /* Schedule autonomous drive sequence */
        new SequentialCommandGroup(
                new DriveActionCommand(path1),
                new DriveActionCommand(path2)
        ).schedule();
    }

    @Override
    public void initialize_loop() {
        farminator.limelight.findPattern();
        farminator.limelight.periodic();
        farminator.limelight.displayTelemetry();
        farminator.periodic();
    }

    @Override
    public void run() {
        super.run();
        farminator.periodic();
    }
}
