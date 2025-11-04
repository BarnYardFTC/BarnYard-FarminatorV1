package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.ShootSequenceCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.roadrunner.MecanumDrive;

/**
 * Autonomous routine "3+3 Close":
 * - Starts at a defined pose
 * - Prepares shooter and shoots three preloaded elements
 * - Detects the obelisk pattern and navigates to the appropriate pre-collection pose
 */
@Config
@Autonomous(name = "3+3 Close", group = "main")
public class ThreePlusThreePrototype extends CommandOpMode {

    /** Robot and drive system instances */
    private BarnRobot farminator;
    private MecanumDrive drive;

    /** Initial and intermediate poses */
    public static double POSE1_X = -37;
    public static double POSE1_Y = -53;
    public static double POSE1_HEADING = Math.toRadians(90);

    public static double POSE2_X = -25;
    public static double POSE2_Y = -15;
    public static double POSE2_HEADING = Math.toRadians(250);

    public static final double POSE3_X = 34;
    public static final double POSE3_Y = -23.1;
    public static final double POSE3_HEADING = Math.toRadians(270);

    public static final double POSE4_X = 34;
    public static final double POSE4_Y = -51;
    public static final double POSE4_HEADING = Math.toRadians(270);

    /** Pre-collection positions for each obelisk pattern */
    public static final double GPP_PRE_COLLECT_X = 34;
    public static final double GPP_PRE_COLLECT_Y = -23.1;
    public static final double GPP_PRE_COLLECT_HEADING = Math.toRadians(270);

    public static final double PGP_PRE_COLLECT_X = 10.6;
    public static final double PGP_PRE_COLLECT_Y = -23.1;
    public static final double PGP_PRE_COLLECT_HEADING = Math.toRadians(270);

    public static final double PPG_PRE_COLLECT_X = -12;
    public static final double PPG_PRE_COLLECT_Y = -23.1;
    public static final double PPG_PRE_COLLECT_HEADING = Math.toRadians(270);

    @Override
    public void initialize() {

        /** Initialize robot and drive */
        farminator = BarnRobot.getInstance();
        farminator.init(this, new OpModeData(OpModeData.AllianceColor.BLUE, 0, 0, OpModeData.OpModeType.AUTONOMOUS));
        drive = new MecanumDrive(hardwareMap, new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING));

        /** Switch to obelisk detection pipeline */
        farminator.limelight.switchPipeline(LimeLight.OBELISK_PIPELINE);

        /** Define trajectories to pre-collection positions */
        TrajectoryActionBuilder path1 = drive.actionBuilder(new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE2_X, POSE2_Y), POSE2_HEADING);

        TrajectoryActionBuilder path2 = drive.actionBuilder(new Pose2d(POSE2_X, POSE2_Y, POSE2_HEADING))
                .strafeToLinearHeading(new Vector2d(GPP_PRE_COLLECT_X, GPP_PRE_COLLECT_Y), GPP_PRE_COLLECT_HEADING);

        TrajectoryActionBuilder path3 = drive.actionBuilder(new Pose2d(POSE2_X, POSE2_Y, POSE2_HEADING))
                .strafeToLinearHeading(new Vector2d(PGP_PRE_COLLECT_X, PGP_PRE_COLLECT_Y), PGP_PRE_COLLECT_HEADING);

        TrajectoryActionBuilder path4 = drive.actionBuilder(new Pose2d(POSE2_X, POSE2_Y, POSE2_HEADING))
                .strafeToLinearHeading(new Vector2d(PPG_PRE_COLLECT_X, PPG_PRE_COLLECT_Y), PPG_PRE_COLLECT_HEADING);

        /** Schedule autonomous sequence */
        new SequentialCommandGroup(
                new WaitUntilCommand(this::opModeIsActive), //todo:what is that
                farminator.shooter.customShooterCommand(farminator.shooter.rangeDependentVelocity(1.8)),
                new DriveActionCommand(path1),
                ShootSequenceCommandGroup.customShootWhenReady(1.8),
                ShootSequenceCommandGroup.customShootWhenReady(1.8),
                ShootSequenceCommandGroup.customShootWhenReady(1.8),
                farminator.shooter.deactivateShooterCommand(),


                new ConditionalCommand(
                        new SequentialCommandGroup(new DriveActionCommand(path2)),
                        new SequentialCommandGroup(),
                        () -> farminator.limelight.getObeliskPattern() == LimeLight.Pattern.GPP
                ),

                new ConditionalCommand(
                        new SequentialCommandGroup(new DriveActionCommand(path3)),
                        new SequentialCommandGroup(),
                        () -> farminator.limelight.getObeliskPattern() == LimeLight.Pattern.PGP
                ),

                new ConditionalCommand(
                        new SequentialCommandGroup(new DriveActionCommand(path4)),
                        new SequentialCommandGroup(),
                        () -> farminator.limelight.getObeliskPattern() == LimeLight.Pattern.PPG
                )
        ).schedule();
    }

    @Override
    public void run() {
        super.run();

        /** Update limelight and shooter telemetry */
        farminator.limelight.periodic();

        //todo: it's better to do this with runCommand
        //todo: once you detected the pattern stop searching
        //if you don't detect the pattern you don't calc the range, you don't have to do it this way
        if (farminator.limelight.isDataValid() && !farminator.limelight.isPatternFound()) {
            farminator.limelight.findPattern();
        } else if (farminator.limelight.isPatternFound()) {
            if (farminator.limelight.currentPipeline == LimeLight.OBELISK_PIPELINE)
                farminator.limelight.switchPipeline(LimeLight.BLUE_PIPELINE);

            farminator.limelight.findRange(Math.toDegrees(drive.localizer.getPose().heading.real));
        }

        farminator.limelight.displayTelemetry();
        farminator.shooter.displayTelemetry();
        farminator.periodic();
    }
}
