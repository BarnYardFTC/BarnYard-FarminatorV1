package org.firstinspires.ftc.teamcode.opmodes.auto.blue.close;

import static org.firstinspires.ftc.teamcode.opmodes.auto.blue.close.Blue_Close_PPG.INTAKE_VELOCITY;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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
@Disabled
@Autonomous(name = "!BLUE THREE PLUS THREE", group = "!main")
public class Blue_Close_ThreePlusThree extends CommandOpMode {

    /** Robot and drive system instances */
    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;

    public static double POSE1_X = -53.33;
    public static double POSE1_Y = -45.5;
    public static double POSE1_HEADING = Math.toRadians(230);

    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.BLUE,
            OpModeData.OpModeType.AUTONOMOUS,
            new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING)
    );

    public static final double SHOOTING_POSE_X = -57;
    public static final double SHOOTING_POSE_Y = -22;
    public static final double SHOOTING_HEADING = Math.toRadians(250);

    public static final double POSE3_X = 11.5;
    public static final double POSE3_Y = -17;
    public static final double POSE3_HEADING = Math.toRadians(270);

    public static final double POSE4_X = 11.5;
    public static final double POSE4_Y = -62;
    public static final double POSE4_HEADING = Math.toRadians(270);

    public static final double POSE5_X = 11.5;
    public static final double POSE5_Y = -40;
    public static final double POSE5_HEADING = Math.toRadians(270);

    public static final double OBELISK_POSE_X = -37.6;
    public static final double OBELISK_POSE_Y = -13.34;
    public static final double OBELISK_HEADING = Math.toRadians(163.48);




    public static int SCORE_TIME = 4000;

    @Override
    public void initialize() {

        /** Initialize robot and drive system */
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        drive = new RoadRunnerMecanumDrive(hardwareMap, new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING));


        TrajectoryActionBuilder path1 =
                drive.actionBuilder(new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y),
                                SHOOTING_HEADING
                        );

        TrajectoryActionBuilder path2 =
                drive.actionBuilder(new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, SHOOTING_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(OBELISK_POSE_X, OBELISK_POSE_Y), OBELISK_HEADING
                        );


        TrajectoryActionBuilder path3 =
                drive.actionBuilder(new Pose2d(OBELISK_POSE_X, OBELISK_POSE_Y, OBELISK_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(POSE3_X, POSE3_Y),
                                POSE3_HEADING
                        );



        TrajectoryActionBuilder path4 =
                drive.actionBuilder(new Pose2d(POSE3_X, POSE3_Y, POSE3_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(POSE4_X, POSE4_Y),
                                POSE4_HEADING, new TranslationalVelConstraint(INTAKE_VELOCITY)
                        );

        TrajectoryActionBuilder path5 =
                drive.actionBuilder(new Pose2d(POSE4_X, POSE4_Y, POSE4_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(POSE5_X, POSE5_Y),
                                POSE5_HEADING
                        );



        TrajectoryActionBuilder path6 =
                drive.actionBuilder(new Pose2d(POSE5_X, POSE5_Y, POSE5_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y),
                                SHOOTING_HEADING

                        );



        /** Schedule autonomous sequence */
        new SequentialCommandGroup(
                new WaitUntilCommand(this::opModeIsActive),
                new ParallelRaceGroup(
                        farminator.shooter.runShooterBasedOnDistance(),
                        new SequentialCommandGroup(
                                farminator.intake.activateIntakeCommand(),
                                new WaitCommand(SCORE_TIME)
                        )
                ),
                farminator.shooter.turnOffInstant(),
                new WaitCommand(500),

                ppg()
//                        new ConditionalCommand(
//                                pgp(),
//                                gpp(),
//                                () -> farminator.limelight.getGamePattern() == LimeLight.Pattern.PGP
//                        ),
//                        () -> farminator.limelight.getGamePattern() == null || farminator.limelight.getGamePattern() == LimeLight.Pattern.PPG
//                )
        ).schedule();

    }


    public SequentialCommandGroup ppg(){

        /* To artifacts pose */
        double POSE3_X = -10;
        double POSE3_Y = -17;

        /* Collect artifacts pose */
        double POSE4_X = -10;
        double POSE4_Y = -58;

        /* Define trajectory to artifacts pose */
        TrajectoryActionBuilder path2 = drive.actionBuilder(new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, SHOOTING_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE3_X, POSE3_Y), Math.toRadians(270));

        /* Define trajectory to collect artifacts pose */
        TrajectoryActionBuilder path3 = drive.actionBuilder(new Pose2d(POSE3_X, POSE3_Y, Math.toRadians(270)))
                .strafeToLinearHeading(new Vector2d(POSE4_X, POSE4_Y), Math.toRadians(270), new TranslationalVelConstraint(INTAKE_VELOCITY));

        /* Define trajectory to returning to shooting pose */
        TrajectoryActionBuilder path4 = drive.actionBuilder(new Pose2d(POSE4_X, POSE4_Y, Math.toRadians(270)))
                .strafeToLinearHeading(new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y), SHOOTING_HEADING - Math.toRadians(20));


        return new SequentialCommandGroup(
                new DriveActionCommand(path2),
                farminator.intake.activateIntakeCommand(),
                new DriveActionCommand(path3),
                farminator.intake.activateIntakeCommand(),
                new ParallelRaceGroup(
                        farminator.shooter.runShooterBasedOnDistance(),
                        new SequentialCommandGroup(
                                new DriveActionCommand(path4),
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
        );
    }

    public SequentialCommandGroup pgp() {

        double POSE3_X = 11.5;
        double POSE3_Y = -17;
        double POSE3_HEADING = Math.toRadians(270);

        double POSE4_X = 11.5;
        double POSE4_Y = -62;
        double POSE4_HEADING = Math.toRadians(270);

        double POSE5_X = 11.5;
        double POSE5_Y = -40;
        double POSE5_HEADING = Math.toRadians(270);

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


        return new SequentialCommandGroup(
                new DriveActionCommand(path2),
                farminator.intake.activateIntakeCommand(),
                new DriveActionCommand(path3),
                farminator.intake.activateIntakeCommand(),
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
        );
    }

    public SequentialCommandGroup gpp(){

        double POSE3_X = 35;
        double POSE3_Y = -17;
        double POSE3_HEADING = Math.toRadians(270);

        double POSE4_X = 35;
        double POSE4_Y = -62;
        double POSE4_HEADING = Math.toRadians(270);

        double POSE5_X = 35;
        double POSE5_Y = -40;
        double POSE5_HEADING = Math.toRadians(270);

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

        return new SequentialCommandGroup(
                new DriveActionCommand(path2),
                farminator.intake.activateIntakeCommand(),
                new DriveActionCommand(path3),
                farminator.intake.activateIntakeCommand(),
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
        );
    }

    @Override
    public void run() {
        super.run();
        telemetry.addData("x", drive.localizer.getPose().position.x);
        telemetry.addData("y", drive.localizer.getPose().position.y);
        telemetry.addData("heading", drive.localizer.getPose().heading.toDouble());
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
