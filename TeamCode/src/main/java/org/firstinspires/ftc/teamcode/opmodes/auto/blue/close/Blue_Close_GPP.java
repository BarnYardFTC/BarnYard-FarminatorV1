package org.firstinspires.ftc.teamcode.opmodes.auto.blue.close;

import static org.firstinspires.ftc.teamcode.opmodes.auto.blue.close.Blue_Close_PPG.INTAKE_VELOCITY;

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
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.subsystems.Webcam;
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
@Autonomous(name = "3+3 Close test gpp", group = "main")
public class Blue_Close_GPP extends CommandOpMode {

    /** Robot and drive system instances */
    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;

    public static final double POSE1_X = -37;
    public static final double POSE1_Y = -48;
    public static final double POSE1_HEADING = Math.toRadians(90);

    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.BLUE,
            OpModeData.OpModeType.AUTONOMOUS,
            LimeLight.BLUE_LOCALIZATION_PIPELINE,
            new Pose2d(POSE1_X,POSE1_Y,POSE1_HEADING)
    );


    public static final double POSE3_X = 35;
    public static final double POSE3_Y = -17;
    public static final double POSE3_HEADING = Math.toRadians(270);

    public static final double POSE4_X = 35;
    public static final double POSE4_Y = -62;
    public static final double POSE4_HEADING = Math.toRadians(270);

    public static final double POSE5_X = 35;
    public static final double POSE5_Y = -40;
    public static final double POSE5_HEADING = Math.toRadians(270);

    public static final double SHOOTING_POSE_X = -57;
    public static final double SHOOTING_POSE_Y = -22;
    public static final double SHOOTING_HEADING = Math.toRadians(250);



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
