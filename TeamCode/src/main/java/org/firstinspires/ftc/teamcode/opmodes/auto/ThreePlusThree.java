package org.firstinspires.ftc.teamcode.opmodes.auto;

import static org.firstinspires.ftc.teamcode.subsystems.Transfer.TRANSFER_ONE_DURATION;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.ShootSequenceCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.roadrunner.MecanumDrive;

@Config
@Autonomous(name="3+3 close", group = "main")
public class ThreePlusThree extends CommandOpMode {
    private BarnRobot farminator;
    private MecanumDrive drive;
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

        // ------------------------
        // Initialize Robot Systems
        // ------------------------
        farminator = BarnRobot.getInstance();
        farminator.init(this, new OpModeData(OpModeData.AllianceColor.BLUE, 0, 0, OpModeData.OpModeType.AUTONOMOUS));
        drive = new MecanumDrive(hardwareMap, new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING));
        farminator.limelight.switchPipeline(LimeLight.OBELISK_PIPELINE);

        TrajectoryActionBuilder path1 = drive.actionBuilder(new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE2_X, POSE2_Y), POSE2_HEADING);

        TrajectoryActionBuilder path2 = drive.actionBuilder(new Pose2d(POSE2_X, POSE2_Y, POSE2_HEADING))
                .strafeToLinearHeading(new Vector2d(GPP_PRE_COLLECT_X, GPP_PRE_COLLECT_Y), GPP_PRE_COLLECT_HEADING);


        TrajectoryActionBuilder path3 = drive.actionBuilder(new Pose2d(POSE2_X, POSE2_Y, POSE2_HEADING))
                .strafeToLinearHeading(new Vector2d(PGP_PRE_COLLECT_X, PGP_PRE_COLLECT_Y), PGP_PRE_COLLECT_HEADING);

        TrajectoryActionBuilder path4 = drive.actionBuilder(new Pose2d(POSE2_X, POSE2_Y, POSE2_HEADING))
                .strafeToLinearHeading(new Vector2d(PPG_PRE_COLLECT_X, PPG_PRE_COLLECT_Y), PPG_PRE_COLLECT_HEADING);


        new SequentialCommandGroup(
                new WaitUntilCommand(this::opModeIsActive),
                farminator.shooter.customShooterCommand(farminator.shooter.rangeDependentVelocity(1.8)),
                new DriveActionCommand(path1),
                ShootSequenceCommandGroup.customShootWhenReady(1.8),
                ShootSequenceCommandGroup.customShootWhenReady(1.8),
                ShootSequenceCommandGroup.customShootWhenReady(1.8),
                farminator.shooter.deactivateShooterCommand(),


                new ConditionalCommand(
                        new SequentialCommandGroup(new DriveActionCommand(path2)),
                        new SequentialCommandGroup(),
                        () -> (farminator.limelight.getObeliskPattern() == LimeLight.Pattern.GPP)
                ),

                new ConditionalCommand(
                        new SequentialCommandGroup(new DriveActionCommand(path3)),
                        new SequentialCommandGroup(),
                        () -> (farminator.limelight.getObeliskPattern() == LimeLight.Pattern.PGP)
                ),

                new ConditionalCommand(
                        new SequentialCommandGroup(new DriveActionCommand(path4)),
                        new SequentialCommandGroup(),
                        () -> (farminator.limelight.getObeliskPattern() == LimeLight.Pattern.PPG)
                )
        ).schedule();

    }

    @Override
    public void run() {
        super.run();
        BarnRobot.getInstance().limelight.periodic();
        if (farminator.limelight.isDataValid() && !farminator.limelight.isPatternFound()){
            farminator.limelight.findPattern();
        }
        else if (farminator.limelight.isPatternFound()){
            if (farminator.limelight.currentPipeline == LimeLight.OBELISK_PIPELINE) farminator.limelight.switchPipeline(LimeLight.BLUE_PIPELINE);
            BarnRobot.getInstance().limelight.findRange(Math.toDegrees(drive.localizer.getPose().heading.real));
        }
        farminator.limelight.displayTelemetry();
        farminator.shooter.displayTelemetry();
        farminator.periodic();
    }
}