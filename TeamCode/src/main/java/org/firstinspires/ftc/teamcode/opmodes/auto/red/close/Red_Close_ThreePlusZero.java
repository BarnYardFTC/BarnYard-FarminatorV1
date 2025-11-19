package org.firstinspires.ftc.teamcode.opmodes.auto.red.close;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.ShootSequenceCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.roadrunner.RoadRunnerMecanumDrive;

/**
 * Autonomous routine "3+0 Close":
 * - Starts at a defined pose
 * - Prepares shooter and shoots three preloaded elements
 * - Does not collect additional elements after shooting
 */
@Config
@Autonomous(name = "3+0 Close", group = "main")
public class Red_Close_ThreePlusZero extends CommandOpMode {

    /** Robot and drive system instances */
    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;

    public static double PERPENDICULAR_TO_GOAL_HEADING = Math.toRadians(130);
    public static double POSE1_X = -50;
    public static double POSE1_Y = 50;

    public static double POSE2_X = -25;
    public static double POSE2_Y = 25;


    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.RED,
            OpModeData.OpModeType.AUTONOMOUS,
            LimeLight.RED_LOCALIZATION_PIPELINE,
            new Pose2d(POSE1_X, POSE1_Y, PERPENDICULAR_TO_GOAL_HEADING));

    @Override
    public void initialize() {

        /** Initialize robot and drive system */
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        drive = new RoadRunnerMecanumDrive(hardwareMap, new Pose2d(POSE1_X, POSE1_Y, PERPENDICULAR_TO_GOAL_HEADING));

        /** Switch limelight to obelisk detection pipeline */
        farminator.limelight.switchPipeline(LimeLight.OBELISK_PIPELINE);

        /** Define trajectory to shooting pose */
        TrajectoryActionBuilder path1 = drive.actionBuilder(new Pose2d(POSE1_X, POSE1_Y, PERPENDICULAR_TO_GOAL_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE2_X, POSE2_Y), PERPENDICULAR_TO_GOAL_HEADING);

        /** Schedule autonomous sequence */
        new SequentialCommandGroup(
                new WaitUntilCommand(this::opModeIsActive),
                farminator.shooter.customShooterCommand(farminator.shooter.rangeDependentVelocity(1.8)),
                new DriveActionCommand(path1),
                ShootSequenceCommandGroup.shootWhenReady(1.8),
                ShootSequenceCommandGroup.shootWhenReady(1.8),
                ShootSequenceCommandGroup.shootWhenReady(1.8),
                farminator.shooter.deactivateShooterCommand()
        ).schedule();

        new SequentialCommandGroup(
                new WaitCommand(10000)
        );

    }

    @Override
    public void run() {
        super.run();

        /** Update limelight and shooter telemetry */
        farminator.limelight.periodic();

        if (farminator.limelight.isDataValid() && !farminator.limelight.isPatternFound()) {
            farminator.limelight.findPattern();
        } else if (farminator.limelight.isPatternFound()) {
            if (farminator.limelight.currentPipeline == LimeLight.OBELISK_PIPELINE) {
                farminator.limelight.switchPipeline(LimeLight.RED_LOCALIZATION_PIPELINE);
            }
            farminator.limelight.findRange(Math.toDegrees(drive.localizer.getPose().heading.real));
        }

        farminator.periodic();
    }

    /**
     * runs when the autonomous is finished
     */
    @Override
    public void end(){
        // store the finish heading of the auto
        OpModeData.setAutoFinishPose(drive.localizer.getPose());
    }
}


















