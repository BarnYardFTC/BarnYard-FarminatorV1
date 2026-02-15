package org.firstinspires.ftc.teamcode.opmodes.auto.red.close.threePlusThree;

//import static org.firstinspires.ftc.teamcode.opmodes.auto.blue.close.Blue_Close_PPG.SOUTH_HEADING;

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
import org.firstinspires.ftc.teamcode.subsystems.Webcam;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;
import org.opencv.core.Mat;

@Config
@Autonomous (name = "3+3 Close test PPG RED", group = "main")
public class Red_Close_PPG extends CommandOpMode {
    /* Robot and drive system instances */
    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;

    /* Initial pose */
    public static double POSE1_X = -37;
    public static double POSE1_Y = 48;
    public static double SOUTH_HEADING = Math.toRadians(270);


    /* Shooter shooting pose */
    public static double SHOOTING_POSE_X = -41;
    public static double SHOOTING_POSE_Y = 25;

    public static double SHOOTING_HEADING = Math.toRadians(145);
    /* To artifacts pose */
    public static double POSE3_X = -8;
    public static double POSE3_Y = 20;


    /* Collect artifacts pose */
    public static double POSE4_X = -8;
    public static double POSE4_Y = 50;

    public static int SCORE_TIME = 4000;
    public static double INTAKE_VELOCITY = RoadRunnerMecanumDrive.PARAMS.maxWheelVel * 0.3;

    public static double HEADING_90 = Math.toRadians(90);


    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.RED,
            OpModeData.OpModeType.AUTONOMOUS,
            new Pose2d(POSE1_X, POSE1_Y, SOUTH_HEADING));

    @Override
    public void initialize() {
        /* initialize robot and drive system */
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        drive = new RoadRunnerMecanumDrive(hardwareMap, new Pose2d(POSE1_X, POSE1_Y, SOUTH_HEADING));

        farminator.shooterHood.setDefaultCommand(farminator.shooterHood.autoHoodAlignment());
        farminator.shooter.setDefaultCommand(farminator.shooter.turnOff());


        /* Define trajectory to shooting pose */
        TrajectoryActionBuilder path1 = drive.actionBuilder(new Pose2d(POSE1_X, POSE1_Y, SOUTH_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y), SHOOTING_HEADING);

        /* Define trajectory to artifacts pose */
        TrajectoryActionBuilder path2 = drive.actionBuilder(new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, SHOOTING_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE3_X, POSE3_Y),  HEADING_90);

        /* Define trajectory to collect artifacts pose */
        TrajectoryActionBuilder path3 = drive.actionBuilder(new Pose2d(POSE3_X, POSE3_Y,  HEADING_90))
                .strafeToLinearHeading(new Vector2d(POSE4_X, POSE4_Y),  HEADING_90, new TranslationalVelConstraint(INTAKE_VELOCITY));


        /* Define trajectory to returning to shooting pose */
        TrajectoryActionBuilder path4 = drive.actionBuilder(new Pose2d(POSE4_X, POSE4_Y,  HEADING_90))
                .strafeToLinearHeading(new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y), SHOOTING_HEADING);


        /* Schedule autonomous sequence */
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
        ).schedule();


    }

    @Override
    public void run() {
        super.run();
        farminator.periodic();
    }

    /* runs when autonomous is finished */
    @Override
    public void end() {
        //store the finish heading of auto
        OpModeData.setAutoFinishPose(drive.localizer.getPose());
    }
}
