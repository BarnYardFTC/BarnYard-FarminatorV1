package org.firstinspires.ftc.teamcode.opmodes.auto.blue.close;

import android.widget.AutoCompleteTextView;

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
import org.firstinspires.ftc.teamcode.commandGroups.AutoController;
import org.firstinspires.ftc.teamcode.commandGroups.CommandGroup;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoPars;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutonomousPathController;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;
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

@Autonomous(name="first auto with adjustment", group="test")
public class BlueCloseThreeNineAutoAjust extends CommandOpMode {
    // SleepAction is in seconds (double). Keep as seconds.
    // ===== Common objects (cleaner, no magic numbers) =====
    public static double SHOOTING_PROG_POSE_X = -35;
    public static double SHOOTING_PROG_POSE_Y = -30;

    // SleepAction is in seconds (double). Keep as seconds.
    public static double SHOOT_TIME_SEC = 2.0;

    public static double START_POSE_X = -53.333;
    public static double START_POSE_Y = -45.5;
    public static double START_HEADING = Math.toRadians(225);

    public static double SHOOT_POSE_X = -23;
    public static double SHOOT_POSE_Y = -22;
    public static double SHOOT_HEADING = Math.toRadians(227);
    public static double LEFT_SHOOT_HEADING = Math.toRadians(210);
    public static double MID_SHOOT_HEADING = Math.toRadians(205);
    public static double FAR_SHOOT_HEADING = Math.toRadians(200);

    public static double SOUTH_READY_POSE_Y = -26;
    public static double SOUTH_COLLECT_POSE_Y = -53;

    public static double SOUTH_HEADING = Math.toRadians(270);

    public static double LEFT_COLLECT_POSE_X = -11.5;
    public static double MID_COLLECT_POSE_X = 12;
    public static double RIGHT_COLLECT_POSE_X = 35;

    public static double GATE_POSE_X = -5;
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

    private static final double VEL_TO_SHOOT = 150.0;

    private static final double END_HEADING_OFFSET_RAD = Math.toRadians(15);


    /** Robot and drive system instances */
    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;
    private final AutonomousPathController autoHub = new AutonomousPathController(AutoPars.side.BLUE, AutoPars.posDistance.CLOSE);

    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.BLUE,
            OpModeData.OpModeType.AUTONOMOUS,
            autoHub.positions.get(AutoPars.positions.START_CLOSE)
    );

    public static int SCORE_TIME = 2200;

    @Override
    public void initialize() {

        /** Initialize robot and drive system */
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        farminator.shooter.setDefaultCommand(farminator.shooter.runShooterBasedOnDistance());
        farminator.shooterHood.setDefaultCommand(farminator.shooterHood.autoHoodAlignment());

        drive = new RoadRunnerMecanumDrive(hardwareMap, autoHub.positions.get(AutoPars.positions.START_CLOSE));




        // ===== Common objects (cleaner, no magic numbers) =====
        Pose2d startPose = new Pose2d(START_POSE_X, START_POSE_Y, START_HEADING);

        Vector2d startNudge = new Vector2d(START_POSE_X, START_POSE_Y + START_Y_NUDGE);

        Vector2d leftReady = new Vector2d(LEFT_COLLECT_POSE_X, SOUTH_READY_POSE_Y);
        Vector2d leftCollect = new Vector2d(LEFT_COLLECT_POSE_X, SOUTH_COLLECT_POSE_Y);
        Vector2d gateAtCollectY = new Vector2d(GATE_POSE_X, SOUTH_COLLECT_POSE_Y);

        Vector2d shootVec = new Vector2d(SHOOT_POSE_X, SHOOT_POSE_Y);
        Pose2d shootPose = new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING);
        Vector2d midShoot = new Vector2d(MID_COLLECT_POSE_X, SOUTH_READY_POSE_Y);
        Vector2d farShoot = new Vector2d(RIGHT_COLLECT_POSE_X, SOUTH_READY_POSE_Y);
        Vector2d leftShoot = new Vector2d(LEFT_COLLECT_POSE_X, SOUTH_READY_POSE_Y);


        Pose2d leftCollectPose = new Pose2d(
                LEFT_COLLECT_POSE_X,
                SOUTH_COLLECT_POSE_Y,
                SOUTH_HEADING
        );

        Pose2d leftShootPose = new Pose2d(
                LEFT_COLLECT_POSE_X,
                SOUTH_READY_POSE_Y,
                LEFT_SHOOT_HEADING
        );

        Pose2d midCollectPose = new Pose2d(
                MID_COLLECT_POSE_X,
                SOUTH_COLLECT_POSE_Y + MID_Y_OFFSET,
                SOUTH_HEADING
        );

        Pose2d midShootPose = new Pose2d(
                MID_COLLECT_POSE_X,
                SOUTH_READY_POSE_Y,
                MID_SHOOT_HEADING
        );

        Pose2d farShootHeading = new Pose2d(
                RIGHT_COLLECT_POSE_X,
                SOUTH_READY_POSE_Y,
                FAR_SHOOT_HEADING
        );

        Pose2d rightCollectPose = new Pose2d(
                RIGHT_COLLECT_POSE_X+2,
                SOUTH_COLLECT_POSE_Y ,
                SOUTH_HEADING
        );

        Pose2d endPose = new Pose2d(
                ENDING_POSE_X,
                ENDING_POSE_Y,
                SHOOT_HEADING + END_HEADING_OFFSET_RAD
        );

        TranslationalVelConstraint fastToShoot = new TranslationalVelConstraint(VEL_TO_SHOOT);

        TrajectoryActionBuilder path1 = drive.actionBuilder(startPose)
                .strafeToLinearHeading(startNudge, SOUTH_HEADING)
                .splineToConstantHeading(leftReady, SOUTH_HEADING)
                // keep Rotation2d hardcoded (as requested)
                .splineToConstantHeading(leftCollect, new Rotation2d(0, 0));

        TrajectoryActionBuilder path2 = drive.actionBuilder(leftCollectPose)
                .strafeToLinearHeading(leftShoot, LEFT_SHOOT_HEADING, fastToShoot);

        TrajectoryActionBuilder path3 = drive.actionBuilder(leftShootPose)
                .splineToLinearHeading(midCollectPose, new Rotation2d(0, -3));


        TrajectoryActionBuilder path4 = drive.actionBuilder(midCollectPose)
                .strafeToLinearHeading(midShoot, MID_SHOOT_HEADING);

        TrajectoryActionBuilder path5 = drive.actionBuilder(midShootPose)
                .splineToLinearHeading(rightCollectPose, new Rotation2d(0, -4));

        TrajectoryActionBuilder path7 = drive.actionBuilder(rightCollectPose)
                .strafeToLinearHeading(farShoot, FAR_SHOOT_HEADING);


        new SequentialCommandGroup(
                new WaitUntilCommand(this::opModeIsActive),
                AutoController.shootCommand(),
                new WaitCommand(500),

                AutoController.intakeCommandPath(autoHub.trajectories(AutoPars.positions.LEFT_COLLECT, drive, telemetry)),

                AutoController.shootCommandPath(autoHub.trajectories(AutoPars.positions.LEFT_SHOOT, drive, telemetry)),

                AutoController.intakeCommandPath(autoHub.trajectories(AutoPars.positions.MID_COLLECT, drive, telemetry)),

                farminator.drive.alignToTagLamLamCommand(), //Aligning exactly to the goal

                AutoController.shootCommandPathIntake(autoHub.trajectories(AutoPars.positions.MID_SHOOT, drive, telemetry)),

                AutoController.intakeCommandPath(autoHub.trajectories(AutoPars.positions.FAR_COLLECT, drive, telemetry)),

                farminator.drive.alignToTagLamLamCommand(), //Aligning exactly to the goal

                AutoController.shootCommandPathIntake(autoHub.trajectories(AutoPars.positions.FAR_SHOOT, drive, telemetry))

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
}
