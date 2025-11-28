package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.roadrunner.RoadRunnerMecanumDrive;

@Config
@Autonomous(name="Test", group = "main")
public class AutoTest extends CommandOpMode {
    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;

    public static double POSE1_X = 0;
    public static double POSE1_Y = 0;
    public static double POSE1_HEADING = Math.toRadians(0);

    public static double POSE2_X = 24;
    public static double POSE2_Y = 0;
    public static double POSE2_HEADING = Math.toRadians(0);
    public static double POSE3_X = 24;
    public static double POSE3_Y = 24;
    public static double POSE3_HEADING = Math.toRadians(0);
    public static double POSE4_X = 24;
    public static double POSE4_Y = 24;
    public static double POSE4_HEADING = Math.toRadians(90);

    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.BLUE,
            OpModeData.OpModeType.TELEOP,
            LimeLight.BLUE_LOCALIZATION_PIPELINE,
            new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING)
            );

    @Override
    public void initialize() {

        // ------------------------
        // Initialize Robot Systems
        // ------------------------
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        drive = new RoadRunnerMecanumDrive(hardwareMap, new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING));

        TrajectoryActionBuilder path1 = drive.actionBuilder(new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE2_X, POSE2_Y), POSE2_HEADING);

        TrajectoryActionBuilder path2 = drive.actionBuilder(new Pose2d(POSE2_X, POSE2_Y, POSE2_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE3_X, POSE3_Y), POSE3_HEADING);

        TrajectoryActionBuilder path3 = drive.actionBuilder(new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING))
                .turnTo(POSE4_HEADING);


        new SequentialCommandGroup(
                new DriveActionCommand(path1),
                new DriveActionCommand(path2),
                new DriveActionCommand(path3)
        ).schedule();

    }

    @Override
    public void initialize_loop() {

        farminator.periodic();
    }

    @Override
    public void run() {
        super.run();
        farminator.periodic();
    }
}
