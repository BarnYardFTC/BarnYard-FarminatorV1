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
import org.firstinspires.ftc.teamcode.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

@Config
@Autonomous (name = "3+3 Close test PPG", group = "main")
public class Blue_Close_PPG extends CommandOpMode {
    /* Robot and drive system instances */
    private BarnRobot farminator;
    private  RoadRunnerMecanumDrive drive;

    /* Initial pose */
    public static double POSE1_X = -37;
    public static double POSE1_Y = -48;
    public static double NORTH_HEADING = Math.toRadians(90);


    /* Shooter shooting pose */
    public static double POSE2_X = -37;
    public static double POSE2_Y = -37;
    public static double PERPENDICULAR_TO_DEPOT_HEADING = Math.toRadians(230);
    /* To artifacts pose */
    public static double POSE3_X = -10;
    public static double POSE3_Y = -17;
    public static double SOUTH_HEADING = Math.toRadians(270);

    /* Collect artifacts pose */
    public static double POSE4_X = -10;
    public static double POSE4_Y = -58;

    public static int SCORE_TIME = 4000;
    public static double INTAKE_VELOCITY = RoadRunnerMecanumDrive.PARAMS.maxWheelVel * 0.3;



    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.BLUE,
            OpModeData.OpModeType.AUTONOMOUS,
            LimeLight.BLUE_LOCALIZATION_PIPELINE,
            new Pose2d(POSE1_X,POSE1_Y, NORTH_HEADING));

    @Override
    public  void initialize() {
        /* initialize robot and drive system */
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        drive = new RoadRunnerMecanumDrive(hardwareMap,new Pose2d(POSE1_X,POSE1_Y, NORTH_HEADING));

        farminator.shooterHood.setDefaultCommand(farminator.shooterHood.autoHoodAlignment());
        farminator.shooter.setDefaultCommand(farminator.shooter.turnOff());


        /* Define trajectory to shooting pose */
        TrajectoryActionBuilder path1 = drive.actionBuilder(new Pose2d(POSE1_X,POSE1_Y,NORTH_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE2_X, POSE2_Y), PERPENDICULAR_TO_DEPOT_HEADING - Math.toRadians(20));

        /* Define trajectory to artifacts pose */
        TrajectoryActionBuilder path2 = drive.actionBuilder(new Pose2d(POSE2_X, POSE2_Y, PERPENDICULAR_TO_DEPOT_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE3_X, POSE3_Y), SOUTH_HEADING);

        /* Define trajectory to collect artifacts pose */
        TrajectoryActionBuilder path3 = drive.actionBuilder(new Pose2d(POSE3_X, POSE3_Y, SOUTH_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE4_X, POSE4_Y), SOUTH_HEADING, new TranslationalVelConstraint(INTAKE_VELOCITY));

        /* Define trajectory to returning to shooting pose */
        TrajectoryActionBuilder path4 = drive.actionBuilder(new Pose2d(POSE4_X, POSE4_Y, SOUTH_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE2_X, POSE2_Y), PERPENDICULAR_TO_DEPOT_HEADING - Math.toRadians(20));

        TrajectoryActionBuilder path5 = drive.actionBuilder(new Pose2d(POSE2_X, POSE2_Y, PERPENDICULAR_TO_DEPOT_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE2_X-20 , POSE2_Y+10), PERPENDICULAR_TO_DEPOT_HEADING);


        /* Schedule autonomous sequence */
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
                new ParallelRaceGroup(
                        farminator.shooter.runShooterBasedOnDistance(),
                        new SequentialCommandGroup(
                                new DriveActionCommand(path4),
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
                farminator.shooter.turnOffInstant(),
                new DriveActionCommand(path5)
        ).schedule();


    }
    @Override
    public void run() {
        super.run();
        telemetry.addData("heading", farminator.drive.getBotAbsoluteHeading());
        farminator.periodic();
    }

    /* runs when autonomous is finished */
    @Override
    public void end() {
        //store the finish heading of auto
        OpModeData.setAutoFinishPose(drive.localizer.getPose());
    }
}