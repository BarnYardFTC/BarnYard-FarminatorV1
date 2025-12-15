package org.firstinspires.ftc.teamcode.opmodes.auto.blue.close;

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
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
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
@Autonomous(name = "3+0 Close Blue", group = "main")
public class Blue_Close_ThreePlusZero extends CommandOpMode {

    /** Robot and drive system instances */
    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;

    /** Initial pose */
//    public static double POSE1_X = -45;
//    public static double POSE1_Y = -45;
    public static final double POSE1_X = -37;
    public static final double POSE1_Y = -48;
    public static final double POSE1_HEADING = Math.toRadians(90);
    public static final double POSE2_HEADING = Math.toRadians(225);

    /** Shooter shooting pose */
    public static double SHOOTING_POSE_X = -57;
    public static double SHOOTING_POSE_Y = -32;
    public static double PERPENDICULAR_TO_DEPOT_HEADING = Math.toRadians(135);
    public static boolean IsFinished = false;

    public static int SCORE_TIME = 4000;


    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.BLUE,
            OpModeData.OpModeType.AUTONOMOUS,
            LimeLight.BLUE_LOCALIZATION_PIPELINE,
            new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING));

    @Override
    public void initialize() {

        /** Initialize robot and drive system */
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        drive = new RoadRunnerMecanumDrive(hardwareMap, new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING));

        farminator.shooterHood.setDefaultCommand(farminator.shooterHood.autoHoodAlignment());

        /** Define trajectory to shooting pose */
        TrajectoryActionBuilder path1 = drive.actionBuilder(new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y), POSE2_HEADING, new TranslationalVelConstraint(25) );

        TrajectoryActionBuilder path2 = drive.actionBuilder(new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, POSE2_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y), POSE2_HEADING, new TranslationalVelConstraint(25) );


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
    public void end() {
        // store the finish heading of the auto
        OpModeData.setAutoFinishPose(drive.localizer.getPose());
    }
}
