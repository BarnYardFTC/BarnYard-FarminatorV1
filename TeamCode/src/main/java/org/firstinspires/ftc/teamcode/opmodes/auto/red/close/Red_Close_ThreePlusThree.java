package org.firstinspires.ftc.teamcode.opmodes.auto.red.close;

import static org.firstinspires.ftc.teamcode.opmodes.auto.blue.close.Blue_Close_PPG.INTAKE_VELOCITY;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.subsystems.Webcam;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

@Autonomous(name = "!RED THREE PLUS THREE", group = "!main")
public class Red_Close_ThreePlusThree extends CommandOpMode {

    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;

    public static double POSE1_X = -37;
    public static double POSE1_Y = 48;
    public static final double POSE1_HEADING = Math.toRadians(270);

    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.RED,
            OpModeData.OpModeType.AUTONOMOUS,
            LimeLight.BLUE_LOCALIZATION_PIPELINE,
            new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING)
    );

    public static double SHOOTING_POSE_X = -41;
    public static double SHOOTING_POSE_Y = 25;
    public static final double SHOOTING_HEADING = Math.toRadians(145);

    public static double OBELISK_X = -37.9;
    public static double OBELISK_Y = 5;
    public static double OBELISK_HEADING = Math.toRadians(206.51);

    public static int SCORE_TIME = 4000;

    @Override
    public void initialize() {
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        drive = new RoadRunnerMecanumDrive(hardwareMap, new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING));

        TrajectoryActionBuilder path1 =
                drive.actionBuilder(new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y),
                                SHOOTING_HEADING
                        );

        TrajectoryActionBuilder obeliskPath =
                drive.actionBuilder(new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, SHOOTING_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(OBELISK_X, OBELISK_Y),
                                OBELISK_HEADING
                        );

        new SequentialCommandGroup(
                new WaitUntilCommand(this::opModeIsActive),
                new ParallelRaceGroup(
                        farminator.shooter.runShooterBasedOnDistance(),
                        new SequentialCommandGroup(
                                new DriveActionCommand(path1),
                                new ParallelRaceGroup(
                                        farminator.drive.alignToTagCommandAuto(),
                                        new WaitCommand(500)
                                ),
                                farminator.drive.stop(),
                                farminator.transfer.setEntireTransferPowerCommand(1),
                                farminator.intake.activateIntakeCommand(),
                                new WaitCommand(SCORE_TIME)
                        )
                ),
                farminator.shooter.turnOffInstant(),
                new DriveActionCommand(obeliskPath),
                new WaitCommand(500),
                new ConditionalCommand(
                        ppg(),
                        new ConditionalCommand(
                                pgp(),
                                gpp(),
                                () -> farminator.webcam.getGamePattern() == Webcam.Pattern.PGP
                        ),
                        () -> farminator.webcam.getGamePattern() == null || farminator.webcam.getGamePattern() == Webcam.Pattern.PPG
                )
        ).schedule();
    }

    public SequentialCommandGroup ppg() {
        // Define trajectory poses
        double ARTIFACTS_POSE_X = -8;
        double ARTIFACTS_POSE_Y = 20;
        double ARTIFACTS_POSE_HEADING = Math.toRadians(90);

        double COLLECT_POSE_X = -8;
        double COLLECT_POSE_Y = 50;
        double COLLECT_POSE_HEADING = Math.toRadians(90);

        double BACK_TO_SHOOT_X = SHOOTING_POSE_X;
        double BACK_TO_SHOOT_Y = SHOOTING_POSE_Y;
        double BACK_TO_SHOOT_HEADING = SHOOTING_HEADING;

        // Build trajectories
        TrajectoryActionBuilder toArtifacts =
                drive.actionBuilder(new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, SHOOTING_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(ARTIFACTS_POSE_X, ARTIFACTS_POSE_Y),
                                ARTIFACTS_POSE_HEADING
                        );

        TrajectoryActionBuilder collect =
                drive.actionBuilder(new Pose2d(ARTIFACTS_POSE_X, ARTIFACTS_POSE_Y, ARTIFACTS_POSE_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(COLLECT_POSE_X, COLLECT_POSE_Y),
                                COLLECT_POSE_HEADING,
                                new TranslationalVelConstraint(INTAKE_VELOCITY)
                        );

        TrajectoryActionBuilder backToShoot =
                drive.actionBuilder(new Pose2d(COLLECT_POSE_X, COLLECT_POSE_Y, COLLECT_POSE_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(BACK_TO_SHOOT_X, BACK_TO_SHOOT_Y),
                                BACK_TO_SHOOT_HEADING
                        );

        return new SequentialCommandGroup(
                new DriveActionCommand(toArtifacts),
                farminator.intake.activateIntakeCommand(),
                farminator.transfer.setFrontPowerCommand(1),
                farminator.transfer.setBackPowerCommand(-0.1),
                new DriveActionCommand(collect),
                farminator.intake.activateIntakeCommand(),
                farminator.transfer.setEntireTransferPowerCommand(0),
                new ParallelRaceGroup(
                        farminator.shooter.runShooterBasedOnDistance(),
                        new SequentialCommandGroup(
                                new DriveActionCommand(backToShoot),
                                new ParallelRaceGroup(
                                        farminator.drive.alignToTagCommandAuto(),
                                        new WaitCommand(500)
                                ),
                                farminator.drive.stop(),
                                farminator.transfer.setEntireTransferPowerCommand(1),
                                new WaitCommand(SCORE_TIME)
                        )
                ),
                farminator.transfer.setEntireTransferPowerCommand(0),
                farminator.intake.deactivateIntakeCommand(),
                farminator.shooter.turnOffInstant()
        );
    }

    public SequentialCommandGroup pgp() {
        // Trajectory poses
        double POSE3_X = 14.5;
        double POSE3_Y = 27;
        double POSE3_HEADING = Math.toRadians(90);

        double POSE4_X = 14.5;
        double POSE4_Y = 55;
        double POSE4_HEADING = Math.toRadians(90);

        double POSE5_X = 14.5;
        double POSE5_Y = 40;
        double POSE5_HEADING = Math.toRadians(90);

        double BACK_TO_SHOOT_X = SHOOTING_POSE_X;
        double BACK_TO_SHOOT_Y = SHOOTING_POSE_Y;
        double BACK_TO_SHOOT_HEADING = SHOOTING_HEADING;

        // Trajectories
        TrajectoryActionBuilder path2 =
                drive.actionBuilder(new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, SHOOTING_HEADING))
                        .strafeToLinearHeading(new Vector2d(POSE3_X, POSE3_Y), POSE3_HEADING);

        TrajectoryActionBuilder path3 =
                drive.actionBuilder(new Pose2d(POSE3_X, POSE3_Y, POSE3_HEADING))
                        .strafeToLinearHeading(new Vector2d(POSE4_X, POSE4_Y), POSE4_HEADING,
                                new TranslationalVelConstraint(INTAKE_VELOCITY));

        TrajectoryActionBuilder path4 =
                drive.actionBuilder(new Pose2d(POSE4_X, POSE4_Y, POSE4_HEADING))
                        .strafeToLinearHeading(new Vector2d(POSE5_X, POSE5_Y), POSE5_HEADING);

        TrajectoryActionBuilder path5 =
                drive.actionBuilder(new Pose2d(POSE5_X, POSE5_Y, POSE5_HEADING))
                        .strafeToLinearHeading(new Vector2d(BACK_TO_SHOOT_X, BACK_TO_SHOOT_Y), BACK_TO_SHOOT_HEADING);

        return new SequentialCommandGroup(
                new DriveActionCommand(path2),
                farminator.intake.activateIntakeCommand(),
                farminator.transfer.setFrontPowerCommand(1),
                farminator.transfer.setBackPowerCommand(-0.1),
                new DriveActionCommand(path3),
                farminator.intake.activateIntakeCommand(),
                farminator.transfer.setEntireTransferPowerCommand(0),
                new DriveActionCommand(path4),
                new ParallelRaceGroup(
                        farminator.shooter.runShooterBasedOnDistance(),
                        new SequentialCommandGroup(
                                new DriveActionCommand(path5),
                                new ParallelRaceGroup(
                                        farminator.drive.alignToTagCommandAuto(),
                                        new WaitCommand(500)
                                ),
                                farminator.drive.stop(),
                                farminator.transfer.setEntireTransferPowerCommand(1),
                                new WaitCommand(SCORE_TIME)
                        )
                ),
                farminator.transfer.setEntireTransferPowerCommand(0),
                farminator.intake.deactivateIntakeCommand(),
                farminator.shooter.turnOffInstant()
        );
    }

    public SequentialCommandGroup gpp() {
        // Trajectory poses
        double POSE3_X = 35;
        double POSE3_Y = 17;
        double POSE3_HEADING = Math.toRadians(90);

        double POSE4_X = 35;
        double POSE4_Y = 62;
        double POSE4_HEADING = Math.toRadians(90);

        double POSE5_X = 35;
        double POSE5_Y = 40;
        double POSE5_HEADING = Math.toRadians(90);

        double BACK_TO_SHOOT_X = SHOOTING_POSE_X;
        double BACK_TO_SHOOT_Y = SHOOTING_POSE_Y;
        double BACK_TO_SHOOT_HEADING = SHOOTING_HEADING;

        // Trajectories
        TrajectoryActionBuilder path2 =
                drive.actionBuilder(new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, SHOOTING_HEADING))
                        .strafeToLinearHeading(new Vector2d(POSE3_X, POSE3_Y), POSE3_HEADING);

        TrajectoryActionBuilder path3 =
                drive.actionBuilder(new Pose2d(POSE3_X, POSE3_Y, POSE3_HEADING))
                        .strafeToLinearHeading(new Vector2d(POSE4_X, POSE4_Y), POSE4_HEADING,
                                new TranslationalVelConstraint(INTAKE_VELOCITY));

        TrajectoryActionBuilder path4 =
                drive.actionBuilder(new Pose2d(POSE4_X, POSE4_Y, POSE4_HEADING))
                        .strafeToLinearHeading(new Vector2d(POSE5_X, POSE5_Y), POSE5_HEADING);

        TrajectoryActionBuilder path5 =
                drive.actionBuilder(new Pose2d(POSE5_X, POSE5_Y, POSE5_HEADING))
                        .strafeToLinearHeading(new Vector2d(BACK_TO_SHOOT_X, BACK_TO_SHOOT_Y), BACK_TO_SHOOT_HEADING);

        return new SequentialCommandGroup(
                new DriveActionCommand(path2),
                farminator.intake.activateIntakeCommand(),
                farminator.transfer.setFrontPowerCommand(1),
                farminator.transfer.setBackPowerCommand(-0.1),
                new DriveActionCommand(path3),
                farminator.intake.activateIntakeCommand(),
                farminator.transfer.setEntireTransferPowerCommand(0),
                new DriveActionCommand(path4),
                new ParallelRaceGroup(
                        farminator.shooter.runShooterBasedOnDistance(),
                        new SequentialCommandGroup(
                                new DriveActionCommand(path5),
                                new ParallelRaceGroup(
                                        farminator.drive.alignToTagCommandAuto(),
                                        new WaitCommand(500)
                                ),
                                farminator.drive.stop(),
                                farminator.transfer.setEntireTransferPowerCommand(1),
                                new WaitCommand(SCORE_TIME)
                        )
                ),
                farminator.transfer.setEntireTransferPowerCommand(0),
                farminator.intake.deactivateIntakeCommand(),
                farminator.shooter.turnOffInstant()
        );
    }

    @Override
    public void run() {
        super.run();
        farminator.periodic();
    }

    @Override
    public void end() {
        OpModeData.setAutoFinishPose(drive.localizer.getPose());
    }
}
