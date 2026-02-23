package org.firstinspires.ftc.teamcode.opmodes.auto.red.close;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.CommandGroup;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

@Autonomous(name = "RC9", group = "red close")
@Disabled
public class RC9 extends CommandOpMode {

    // ================== FIELD / POSES ==================
    public static double SHOOT_TIME_SEC = 2.0; // (not used directly; kept like your template)

    public static double START_POSE_X = -53.333;
    public static double START_POSE_Y = 45.5;
    public static double START_HEADING = Math.toRadians(135);

    public static double SHOOT_POSE_X = -23;
    public static double SHOOT_POSE_Y = 22;
    public static double SHOOT_HEADING = Math.toRadians(133);

    public static double SOUTH_READY_POSE_Y = 30;
    public static double SOUTH_COLLECT_POSE_Y = 53;

    public static double SOUTH_HEADING = Math.toRadians(90);

    public static double LEFT_COLLECT_POSE_X = -11.5;
    public static double MID_COLLECT_POSE_X = 12;
    public static double RIGHT_COLLECT_POSE_X = 35;

    public static double GATE_POSE_X = 0;
    public static double GATE_POSE_Y = 44; // unused (kept)

    public static double ENDING_POSE_X = -40;
    public static double ENDING_POSE_Y = 22;

    // ================== PATH TUNING ==================
    private static final double START_Y_NUDGE = 5.0;
    private static final double MID_Y_OFFSET = 8.0;
    private static final double RIGHT_Y_OFFSET = 15.0;

    private static final double VEL_TO_SHOOT = 150.0;
    private static final double END_HEADING_OFFSET_RAD = Math.toRadians(15);

    /** Robot and drive system instances */
    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;

    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.RED,
            OpModeData.OpModeType.AUTONOMOUS,
            new Pose2d(START_POSE_X, START_POSE_Y, START_HEADING)
    );

    public static int SHOOTING_TIME_MS = 2000;

    @Override
    public void initialize() {

        // ===== Init robot =====
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        farminator.shooter.setDefaultCommand(farminator.shooter.turnOff());
        drive = new RoadRunnerMecanumDrive(hardwareMap, new Pose2d(START_POSE_X, START_POSE_Y, START_HEADING));

        // ===== Common objects =====
        Pose2d startPose = new Pose2d(START_POSE_X, START_POSE_Y, START_HEADING);

        // In Red mirror: start nudge is Y - 5 (as in your Red MeepMeep)
        Vector2d startNudge = new Vector2d(START_POSE_X, START_POSE_Y - START_Y_NUDGE);

        Vector2d leftReady = new Vector2d(LEFT_COLLECT_POSE_X, SOUTH_READY_POSE_Y);
        Vector2d leftCollect = new Vector2d(LEFT_COLLECT_POSE_X, SOUTH_COLLECT_POSE_Y);
        Vector2d gateAtCollectY = new Vector2d(GATE_POSE_X, SOUTH_COLLECT_POSE_Y);

        Vector2d shootVec = new Vector2d(SHOOT_POSE_X, SHOOT_POSE_Y);
        Pose2d shootPose = new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING);

        Pose2d midCollectPose = new Pose2d(
                MID_COLLECT_POSE_X,
                SOUTH_COLLECT_POSE_Y - MID_Y_OFFSET,
                SOUTH_HEADING
        );

        Pose2d rightCollectPose = new Pose2d(
                RIGHT_COLLECT_POSE_X,
                SOUTH_COLLECT_POSE_Y - RIGHT_Y_OFFSET,
                SOUTH_HEADING
        );

        Pose2d endPose = new Pose2d(
                ENDING_POSE_X,
                ENDING_POSE_Y,
                SHOOT_HEADING - END_HEADING_OFFSET_RAD
        );

        TranslationalVelConstraint fastToShoot = new TranslationalVelConstraint(VEL_TO_SHOOT);

        // ================== PATHS (matching your Red MeepMeep) ==================

        // Start -> Left ready -> Left collect -> Gate
        TrajectoryActionBuilder path1 = drive.actionBuilder(startPose)
                .strafeToLinearHeading(startNudge, SOUTH_HEADING)
                .splineToConstantHeading(leftReady, SOUTH_HEADING)
                .splineToConstantHeading(leftCollect, new Rotation2d(0, 0))
                .splineToConstantHeading(gateAtCollectY, new Rotation2d(-3, -8));

        // Gate -> Shoot (strafe fast)
        TrajectoryActionBuilder path2 = drive.actionBuilder(new Pose2d(gateAtCollectY.x, gateAtCollectY.y, SOUTH_HEADING))
                .strafeToLinearHeading(shootVec, SHOOT_HEADING, fastToShoot);

        // Shoot -> Mid collect (approach)
        TrajectoryActionBuilder path3 = drive.actionBuilder(shootPose)
                .splineToLinearHeading(midCollectPose, new Rotation2d(0, 3));

        // Mid collect -> Shoot (return fast)
        TrajectoryActionBuilder path4 = drive.actionBuilder(midCollectPose)
                .splineToLinearHeading(shootPose, new Rotation2d(-2, 1), fastToShoot);

        // Shoot -> Right collect (approach)
        TrajectoryActionBuilder path5 = drive.actionBuilder(shootPose)
                .splineToLinearHeading(rightCollectPose, new Rotation2d(-1, -2));

        // Right collect -> End (return/park)
        TrajectoryActionBuilder path6 = drive.actionBuilder(rightCollectPose)
                .splineToLinearHeading(endPose, new Rotation2d(-1.8, 1), fastToShoot);

        // ================== SCHEDULE ==================
        new SequentialCommandGroup(
                new WaitUntilCommand(this::opModeIsActive),

                shootCommand(),

                intakeCommandPath(path1),

                shootCommandPath(path2),

                intakeCommandPath(path3),

                new ParallelCommandGroup(
                        shootCommandPath(path4),
                        new SequentialCommandGroup(
                                new ParallelRaceGroup(
                                        BarnRobot.getInstance().intake.activateIntakeCommand(),
                                        new WaitCommand(1000)
                                ),
                                BarnRobot.getInstance().intake.deactivateIntakeCommand()
                        )
                ),

                intakeCommandPath(path5),

                new ParallelCommandGroup(
                        shootCommandPath(path6),
                        new SequentialCommandGroup(
                                new ParallelRaceGroup(
                                        BarnRobot.getInstance().intake.activateIntakeCommand(),
                                        new WaitCommand(1000)
                                ),
                                BarnRobot.getInstance().intake.deactivateIntakeCommand()
                        )
                )
        ).schedule();
    }

    @Override
    public void run() {
        super.run();
        farminator.periodic();
    }

    @Override
    public void end() {
        // store the finish heading of the auto
        OpModeData.setAutoFinishPose(drive.localizer.getPose());
    }

    // ================== COMMAND HELPERS (copied style) ==================

    public Command shootCommandPath(TrajectoryActionBuilder shootingPath) {
        return new ParallelRaceGroup(
                BarnRobot.getInstance().shooter.runShooterBasedOnDistance(),
                new SequentialCommandGroup(
                        new DriveActionCommand(shootingPath),

                        new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                        BarnRobot.getInstance().gate.openCommand(),
                        BarnRobot.getInstance().transfer.activateTransfer(),
                        BarnRobot.getInstance().intake.activateIntakeCommand(),
                        new ParallelRaceGroup(
                                new WaitUntilCommand(() -> !BarnRobot.getInstance().shooter.isReady()),
                                new WaitCommand(SHOOTING_TIME_MS)
                        ),
                        CommandGroup.deactivateIntakeAndTransferCommand(),

                        new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                        BarnRobot.getInstance().transfer.activateTransfer(),
                        BarnRobot.getInstance().intake.activateIntakeCommand(),
                        new ParallelRaceGroup(
                                new WaitUntilCommand(() -> !BarnRobot.getInstance().shooter.isReady()),
                                new WaitCommand(SHOOTING_TIME_MS)
                        ),
                        CommandGroup.deactivateIntakeAndTransferCommand(),

                        new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                        BarnRobot.getInstance().transfer.activateTransfer(),
                        BarnRobot.getInstance().intake.activateIntakeCommand(),
                        new ParallelRaceGroup(
                                new WaitUntilCommand(() -> !BarnRobot.getInstance().shooter.isReady()),
                                new WaitCommand(SHOOTING_TIME_MS)
                        ),

                        BarnRobot.getInstance().gate.closeCommand(),
                        CommandGroup.deactivateIntakeAndTransferCommand()
                )
        );
    }

    private Command shootCommand() {
        return new ParallelRaceGroup(
                BarnRobot.getInstance().shooter.runShooterBasedOnDistance(),
                new SequentialCommandGroup(
                        new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                        BarnRobot.getInstance().gate.openCommand(),
                        BarnRobot.getInstance().transfer.activateTransfer(),
                        BarnRobot.getInstance().intake.activateIntakeCommand(),
                        new ParallelRaceGroup(
                                new WaitUntilCommand(() -> !BarnRobot.getInstance().shooter.isReady()),
                                new WaitCommand(SHOOTING_TIME_MS)
                        ),
                        CommandGroup.deactivateIntakeAndTransferCommand(),

                        new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                        BarnRobot.getInstance().transfer.activateTransfer(),
                        BarnRobot.getInstance().intake.activateIntakeCommand(),
                        new ParallelRaceGroup(
                                new WaitUntilCommand(() -> !BarnRobot.getInstance().shooter.isReady()),
                                new WaitCommand(SHOOTING_TIME_MS)
                        ),
                        CommandGroup.deactivateIntakeAndTransferCommand(),

                        new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                        BarnRobot.getInstance().transfer.activateTransfer(),
                        BarnRobot.getInstance().intake.activateIntakeCommand(),
                        new ParallelRaceGroup(
                                new WaitUntilCommand(() -> !BarnRobot.getInstance().shooter.isReady()),
                                new WaitCommand(SHOOTING_TIME_MS)
                        ),

                        BarnRobot.getInstance().gate.closeCommand(),
                        CommandGroup.deactivateIntakeAndTransferCommand()
                )
        );
    }

    public Command intakeCommandPath(TrajectoryActionBuilder path) {
        return new SequentialCommandGroup(
                CommandGroup.intakeAndTransferGateCommand(),
                new DriveActionCommand(path),
                deactivateIntakeAndTransferCommand()
        );
    }

    public static Command deactivateIntakeAndTransferCommand() {
        return new ParallelCommandGroup(
                BarnRobot.getInstance().intake.deactivateIntakeCommand(),
                BarnRobot.getInstance().transfer.deactivateTransfer()
        );
    }
}
