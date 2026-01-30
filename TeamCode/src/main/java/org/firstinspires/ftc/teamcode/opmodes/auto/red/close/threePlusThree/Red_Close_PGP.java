package org.firstinspires.ftc.teamcode.opmodes.auto.red.close.threePlusThree;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

/**
 * Autonomous routine "3+0 Close":
 * - Starts at a defined pose
 * - Prepares shooter and shoots three preloaded elements
 * - Does not collect additional elements after shooting
 */
@Config
@Autonomous(name = "3+3 Close test PGP Red", group = "main")
public class Red_Close_PGP extends CommandOpMode {

    /** Robot and drive system instances */
    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;



    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.RED,
            OpModeData.OpModeType.AUTONOMOUS,
            new Pose2d(POSE1_X, POSE1_Y, NORTH_HEADING)
    );


    /* Initial pose */
    public static double POSE1_X = -37;
    public static double POSE1_Y = 48;
    public static double NORTH_HEADING = Math.toRadians(270);


    /* Shooter shooting pose */
    public static double SHOOTING_POSE_X = -41;
    public static double SHOOTING_POSE_Y = 25;

    public static double SHOOTING_HEADING = Math.toRadians(145);


    public static final double POSE3_X = 14.5;
    public static final double POSE3_Y = 27;
    public static final double POSE3_HEADING = Math.toRadians(90);


    public static final double POSE4_X = 14.5;
    public static final double POSE4_Y = 55;
    public static final double POSE4_HEADING = Math.toRadians(90);

    public static final double POSE5_X = 14.5;
    public static final double POSE5_Y = 40;
    public static final double POSE5_HEADING = Math.toRadians(90);

    public static double INTAKE_VELOCITY = RoadRunnerMecanumDrive.PARAMS.maxWheelVel * 0.3;

    public static int SCORE_TIME = 4000;

    @Override
    public void initialize() {

        /** Initialize robot and drive system */
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        drive = new RoadRunnerMecanumDrive(hardwareMap, new Pose2d(POSE1_X, POSE1_Y, NORTH_HEADING));


        TrajectoryActionBuilder path1 =
                drive.actionBuilder(new Pose2d(POSE1_X, POSE1_Y, NORTH_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y),
                                SHOOTING_HEADING
                        );


        TrajectoryActionBuilder path2 =
                drive.actionBuilder(new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, SHOOTING_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(POSE3_X, POSE3_Y),
                                POSE3_HEADING
                        );



        TrajectoryActionBuilder path3 =
                drive.actionBuilder(new Pose2d(POSE3_X, POSE3_Y, POSE3_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(POSE4_X, POSE4_Y),
                                POSE4_HEADING, new TranslationalVelConstraint(INTAKE_VELOCITY)
                        );

        TrajectoryActionBuilder path4 =
                drive.actionBuilder(new Pose2d(POSE4_X, POSE4_Y, POSE4_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(POSE5_X, POSE5_Y),
                                POSE5_HEADING
                        );

        TrajectoryActionBuilder path5 =
                drive.actionBuilder(new Pose2d(POSE5_X, POSE5_Y, POSE5_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y),
                                SHOOTING_HEADING
                        );






        /** Schedule autonomous sequence */
        new SequentialCommandGroup(
                new WaitUntilCommand(this::opModeIsActive),
                farminator.intake.activateIntakeCommand(),
                new ParallelRaceGroup(
                        farminator.shooter.runShooterBasedOnDistance(),
                        new SequentialCommandGroup(
                                new DriveActionCommand(path1),
                                new ParallelRaceGroup(
                                        farminator.drive.alignToTagCommandAuto(),
                                        new WaitCommand(500)
                                ),
                                farminator.drive.stop(),
                                farminator.intake.activateIntakeCommand(),
                                new WaitCommand(SCORE_TIME)
                        )
                ),
                farminator.intake.deactivateIntakeCommand(),
                farminator.shooter.turnOffInstant(),
                new DriveActionCommand(path2),
                farminator.intake.activateIntakeCommand(),
                new DriveActionCommand(path3),
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
                                new WaitCommand(SCORE_TIME)

                        )
                ),
                farminator.intake.deactivateIntakeCommand(),
                farminator.shooter.turnOffInstant()
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