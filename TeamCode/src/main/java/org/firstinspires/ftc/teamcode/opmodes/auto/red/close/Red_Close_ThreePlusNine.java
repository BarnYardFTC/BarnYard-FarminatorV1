package org.firstinspires.ftc.teamcode.opmodes.auto.red.close;

import static org.firstinspires.ftc.teamcode.opmodes.auto.blue.close.Blue_Close_PPG.INTAKE_VELOCITY;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

public class Red_Close_ThreePlusNine extends CommandOpMode {
    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;

    public static double START_POSE_X = 0;

    public static double START_POSE_Y = 0;
    public static final double START_POSE_HEADING = Math.toRadians(270);

    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.RED,
            OpModeData.OpModeType.AUTONOMOUS,
            LimeLight.RED_LOCALIZATION_PIPELINE,
            new Pose2d(START_POSE_X, START_POSE_Y, START_POSE_HEADING)
    );

    public static double SHOOTING_POSE_X = 0;
    public static double SHOOTING_POSE_Y = 0;
    public static final double SHOOTING_HEADING = Math.toRadians(0);

    public static int SCORE_TIME = 4000;

    @Override
    public void initialize() {
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        drive = new RoadRunnerMecanumDrive(hardwareMap, new Pose2d(START_POSE_X, START_POSE_Y, START_POSE_HEADING));

        TrajectoryActionBuilder path1 =
                drive.actionBuilder(new Pose2d(START_POSE_X, START_POSE_Y, START_POSE_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y),
                                SHOOTING_HEADING
                        );


        new SequentialCommandGroup(
                new WaitUntilCommand(this::opModeIsActive),
                new ParallelRaceGroup(
                        farminator.shooter.runShooterBasedOnDistance(),
                        new SequentialCommandGroup(
                                new DriveActionCommand(path1),
                                farminator.transfer.setEntireTransferPowerCommand(1),
                                farminator.intake.activateIntakeCommand(),
                                new WaitCommand(SCORE_TIME)
                        )
                ),
                farminator.shooter.turnOffInstant(),
                new WaitCommand(500),
                new SequentialCommandGroup(
                        ppg(),
                        pgp(),
                        gpp()
                )
        ).schedule();
    }

    public SequentialCommandGroup ppg() {
        // Define trajectory poses
        double ARTIFACTS_POSE_X = 0;
        double ARTIFACTS_POSE_Y = 0;
        double ARTIFACTS_POSE_HEADING = Math.toRadians(0);

        double COLLECT_POSE_X = 0;
        double COLLECT_POSE_Y = 0;
        double COLLECT_POSE_HEADING = Math.toRadians(0);

        double OPEN_GATE_X = 0;
        double OPEN_GATE_Y = 0;
        double OPEN_GATE_HEADING = Math.toRadians(0);

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

        TrajectoryActionBuilder openGate =
                drive.actionBuilder(new Pose2d(COLLECT_POSE_X, COLLECT_POSE_Y, COLLECT_POSE_HEADING))
                        .strafeToLinearHeading(new Vector2d(OPEN_GATE_X, OPEN_GATE_Y), OPEN_GATE_HEADING);

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
                                farminator.transfer.setEntireTransferPowerCommand(1),
                                new WaitCommand(SCORE_TIME)
                        )
                ),
                farminator.transfer.setEntireTransferPowerCommand(0),
                farminator.intake.deactivateIntakeCommand(),
                farminator.shooter.turnOffInstant(),
                new WaitCommand(500)
        );
    }

    public SequentialCommandGroup pgp() {
        // Trajectory poses
        double ARTIFACTS_POSE_X = 0;
        double ARTIFACTS_POSE_Y = 0;
        double ARTIFACTS_POSE_HEADING = Math.toRadians(0);

        double COLLECT_POSE_X = 0;
        double COLLECT_POSE_Y = 0;
        double COLLECT_POSE_HEADING = Math.toRadians(0);


        // Trajectories
        TrajectoryActionBuilder toArtifacts =
                drive.actionBuilder(new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, SHOOTING_HEADING))
                        .strafeToLinearHeading(new Vector2d(ARTIFACTS_POSE_X, ARTIFACTS_POSE_Y), ARTIFACTS_POSE_HEADING);

        TrajectoryActionBuilder collect =
                drive.actionBuilder(new Pose2d(ARTIFACTS_POSE_X, ARTIFACTS_POSE_Y, ARTIFACTS_POSE_HEADING))
                        .strafeToLinearHeading(new Vector2d(COLLECT_POSE_X, COLLECT_POSE_Y), COLLECT_POSE_HEADING,
                                new TranslationalVelConstraint(INTAKE_VELOCITY));

        TrajectoryActionBuilder backToShoot =
                drive.actionBuilder(new Pose2d(COLLECT_POSE_X, COLLECT_POSE_Y, COLLECT_POSE_HEADING))
                        .strafeToLinearHeading(new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y), SHOOTING_HEADING);

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
                                farminator.transfer.setEntireTransferPowerCommand(1),
                                new WaitCommand(SCORE_TIME)
                        )
                ),
                farminator.transfer.setEntireTransferPowerCommand(0),
                farminator.intake.deactivateIntakeCommand(),
                farminator.shooter.turnOffInstant(),
                new WaitCommand(500)
        );
    }

    public SequentialCommandGroup gpp() {
        // Trajectory poses
        double ARTIFACTS_POSE_X = 0;
        double ARTIFACTS_POSE_Y = 0;
        double ARTIFACTS_POSE_HEADING = Math.toRadians(0);

        double COLLECT_POSE_X = 0;
        double COLLECT_POSE_Y = 0;
        double COLLECT_POSE_HEADING = Math.toRadians(0);


        // Trajectories
        TrajectoryActionBuilder toArtifacts =
                drive.actionBuilder(new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, SHOOTING_HEADING))
                        .strafeToLinearHeading(new Vector2d(ARTIFACTS_POSE_X, ARTIFACTS_POSE_Y), ARTIFACTS_POSE_HEADING);

        TrajectoryActionBuilder collect =
                drive.actionBuilder(new Pose2d(ARTIFACTS_POSE_X, ARTIFACTS_POSE_Y, ARTIFACTS_POSE_HEADING))
                        .strafeToLinearHeading(new Vector2d(COLLECT_POSE_X, COLLECT_POSE_Y), COLLECT_POSE_HEADING,
                                new TranslationalVelConstraint(INTAKE_VELOCITY));

        TrajectoryActionBuilder backToShoot =
                drive.actionBuilder(new Pose2d(COLLECT_POSE_X, COLLECT_POSE_Y, COLLECT_POSE_HEADING))
                        .strafeToLinearHeading(new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y), SHOOTING_HEADING);

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
                                farminator.transfer.setEntireTransferPowerCommand(1),
                                new WaitCommand(SCORE_TIME)
                        )
                ),
                farminator.transfer.setEntireTransferPowerCommand(0),
                farminator.intake.deactivateIntakeCommand(),
                farminator.shooter.turnOffInstant(),
                new WaitCommand(500)
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
