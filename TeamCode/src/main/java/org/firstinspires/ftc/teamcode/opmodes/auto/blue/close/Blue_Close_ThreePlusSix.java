package org.firstinspires.ftc.teamcode.opmodes.auto.blue.close;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
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

@Autonomous(name = "!!3+6Final", group = "!main")
public class Blue_Close_ThreePlusSix extends CommandOpMode {

    public static double START_POSE_X = -53.333;
    public static double START_POSE_Y = -45.5;
    public static double START_HEADING = Math.toRadians(225);

    public static double SHOOT_POSE_X = -23;
    public static double SHOOT_POSE_Y = -22;
    public static double SHOOT_HEADING = Math.toRadians(227);

    public static double SOUTH_READY_POSE_Y = -25;
    public static double SOUTH_COLLECT_POSE_Y = -63;

    public static double SOUTH_HEADING = Math.toRadians(270);

    public static double LEFT_COLLECT_POSE_X = -11.5;
    public static double MID_COLLECT_POSE_X = 12;
    public static double GATE_POSE_X = 0;
    public static double GATE_POSE_Y = -44; // currently unused in your paths

    public static double ENDING_POSE_X = -40;
    public static double ENDING_POSE_Y = -22;

    // ================== BOT / SIM CONFIG ==================
    private static final int WINDOW_SIZE = 800;

    private static final double MAX_VEL = 60;
    private static final double MAX_ACCEL = 60;
    private static final double MAX_ANG_VEL = Math.toRadians(180);
    private static final double MAX_ANG_ACCEL = Math.toRadians(180);
    private static final double TRACK_WIDTH = 15;

    private static final double BOT_WIDTH = 15.07;
    private static final double BOT_HEIGHT = 16.961;

    // ================== PATH TUNING (no Rotation2d here) ==================
    private static final double START_Y_NUDGE = 5.0;
    private static final double MID_Y_OFFSET = 8.0;
    private static final double RIGHT_Y_OFFSET = 15.0;

    private static final double VEL_TO_SHOOT = 120.0;

    private static final double END_HEADING_OFFSET_RAD = Math.toRadians(15);


    /** Robot and drive system instances */
    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;

    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.BLUE,
            OpModeData.OpModeType.AUTONOMOUS,
            new Pose2d(START_POSE_X, START_POSE_Y, START_HEADING)
    );

    public static int SCORE_TIME = 2200;

    @Override
    public void initialize() {

        /** Initialize robot and drive system */
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        farminator.shooter.setDefaultCommand(farminator.shooter.turnOff());

        drive = new RoadRunnerMecanumDrive(hardwareMap, new Pose2d(START_POSE_X, START_POSE_Y, START_HEADING));

        // ===== Common objects (cleaner, no magic numbers) =====
        Pose2d startPose = new Pose2d(START_POSE_X, START_POSE_Y, START_HEADING);

        Vector2d startNudge = new Vector2d(START_POSE_X, START_POSE_Y + START_Y_NUDGE);

        Vector2d leftReady = new Vector2d(LEFT_COLLECT_POSE_X, SOUTH_READY_POSE_Y);
        Vector2d leftCollect = new Vector2d(LEFT_COLLECT_POSE_X, SOUTH_COLLECT_POSE_Y);
        Vector2d gateAtCollectY = new Vector2d(GATE_POSE_X, SOUTH_COLLECT_POSE_Y);

        Vector2d shootVec = new Vector2d(SHOOT_POSE_X, SHOOT_POSE_Y);
        Pose2d shootPose = new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING);

        Pose2d midCollectPose = new Pose2d(
                MID_COLLECT_POSE_X,
                SOUTH_COLLECT_POSE_Y + MID_Y_OFFSET,
                SOUTH_HEADING
        );

        Pose2d endPose = new Pose2d(
                ENDING_POSE_X,
                ENDING_POSE_Y,
                SHOOT_HEADING + END_HEADING_OFFSET_RAD
        );


        //      ===== Trajectory constraints =====
        TranslationalVelConstraint fastToShoot = new TranslationalVelConstraint(VEL_TO_SHOOT);


        //         ===== Paths =====
        TrajectoryActionBuilder collectLeftArts = drive.actionBuilder(startPose)
                .strafeToLinearHeading(startNudge, SOUTH_HEADING)
                .splineToConstantHeading(leftReady, SOUTH_HEADING)
                // keep Rotation2d hardcoded (as requested)
                .splineToConstantHeading(leftCollect, new Rotation2d(0, 0))
                .splineToConstantHeading(gateAtCollectY, new Rotation2d(3, -8));

        TrajectoryActionBuilder gateToShoot = drive.actionBuilder(new Pose2d(gateAtCollectY.x, gateAtCollectY.y, SOUTH_HEADING))
                .strafeToLinearHeading(shootVec, SHOOT_HEADING, fastToShoot);

        TrajectoryActionBuilder collectMidArts = drive.actionBuilder(shootPose)
                .splineToLinearHeading(midCollectPose, new Rotation2d(0, -3));

        TrajectoryActionBuilder midToShoot = drive.actionBuilder(midCollectPose)
                .splineToLinearHeading(endPose, new Rotation2d(-1.8, -1), fastToShoot);


        //         ===== Commands =====
        new SequentialCommandGroup(
                new WaitUntilCommand(this::opModeIsActive),
                shootCommand(),

                intakeCommandPath(collectLeftArts),

                shootCommandPath(gateToShoot),

                intakeCommandPath(collectMidArts),

                shootCommandPathIntake(midToShoot)

        ).schedule();
    }

    @Override
    public void run() {
        super.run();
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
    public static int SHOOTING_TIME_MS = 2000;


    //       =========== Intake and Shoot CommandPaths ===========
    public Command shootCommandPath(TrajectoryActionBuilder shootingPath){
        return new SequentialCommandGroup(
                new ParallelRaceGroup(
                        BarnRobot.getInstance().shooter.runShooterBasedOnDistance(),
                        new SequentialCommandGroup(
                                new DriveActionCommand(shootingPath),
                                new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                                BarnRobot.getInstance().gate.openCommand(),
                                CommandGroup.intakeAndTransferActivateCommand(),
                                new WaitCommand(SHOOTING_TIME_MS),
//                            new WaitUntilCommand(() -> !CommandGroup.robotContainsArtifacts()),
                                BarnRobot.getInstance().gate.closeCommand(),
                                CommandGroup.deactivateIntakeAndTransferCommand()
                        )
                ),
                BarnRobot.getInstance().shooter.turnOffInstant()
        );
    }

    public Command shootCommandPathIntake(TrajectoryActionBuilder path){
        return new SequentialCommandGroup(
                new ParallelRaceGroup(
                        BarnRobot.getInstance().shooter.runShooterBasedOnDistance(),
                        new SequentialCommandGroup(
                                CommandGroup.intakeAndTransferActivateCommand(),
                                new DriveActionCommand(path),
                                CommandGroup.deactivateIntakeAndTransferCommand(),
                                new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                                CommandGroup.intakeAndTransferActivateCommand(),
                                BarnRobot.getInstance().gate.openCommand(),
                                new WaitCommand(SHOOTING_TIME_MS),
//                        new WaitUntilCommand(() -> !CommandGroup.robotContainsArtifacts()),
                                BarnRobot.getInstance().gate.closeCommand(),
                                CommandGroup.deactivateIntakeAndTransferCommand()
                        )
                ),
                BarnRobot.getInstance().shooter.turnOffInstant()
        );
    }

    private Command shootCommand() {
        return new SequentialCommandGroup(
                new ParallelRaceGroup(
                        BarnRobot.getInstance().shooter.runShooterBasedOnDistance(),
                        new SequentialCommandGroup(
                                new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                                BarnRobot.getInstance().gate.openCommand(),
                                CommandGroup.intakeAndTransferActivateCommand(),
                                new WaitCommand(SHOOTING_TIME_MS),
                                //new WaitUntilCommand(() -> !CommandGroup.robotContainsArtifacts()),
                                BarnRobot.getInstance().gate.closeCommand(),
                                CommandGroup.deactivateIntakeAndTransferCommand()
                        )
                ),
                BarnRobot.getInstance().shooter.turnOffInstant()
        );
    }

    public Command intakeCommandPath(TrajectoryActionBuilder path){
        return new SequentialCommandGroup(
                CommandGroup.smartIntakeAndTransferCommand(),
                new DriveActionCommand(path),
                CommandGroup.deactivateIntakeAndTransferCommand()
        );
    }


}
