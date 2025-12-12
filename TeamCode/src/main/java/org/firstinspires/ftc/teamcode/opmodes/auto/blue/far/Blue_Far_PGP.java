package org.firstinspires.ftc.teamcode.opmodes.auto.blue.far;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

@Config
@Autonomous(name = "3+0 Far Blue PGP", group = "main")
public class Blue_Far_PGP extends CommandOpMode {

    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;

    public static double GOAL_HEADING = Math.toRadians(215);
    public static double NORTH_HEADING = Math.toRadians(270);
    public static double WEST_HEADING = Math.toRadians(215);

    public static double POSE1_X = 57.5;
    public static double POSE1_Y = 0;

    public static double POSE2_X = 50;
    public static double POSE2_Y = 0;

    public static double POSE3_X = 11;
    public static double POSE3_Y = -30;

    public static double POSE4_X = 11;
    public static double POSE4_Y = -50;







    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.BLUE,
            OpModeData.OpModeType.AUTONOMOUS,
            LimeLight.BLUE_LOCALIZATION_PIPELINE,
            new Pose2d(POSE1_X, POSE1_Y, WEST_HEADING));

    @Override
    public void initialize() {

        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        drive = new RoadRunnerMecanumDrive(hardwareMap, new Pose2d(POSE1_X, POSE1_Y, WEST_HEADING));

//        farminator.limelight.switchPipeline(LimeLight.OBELISK_PIPELINE);


        TrajectoryActionBuilder path1 = drive.actionBuilder(new Pose2d(POSE1_X, POSE1_Y, WEST_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE2_X, 0), GOAL_HEADING, new TranslationalVelConstraint(25));
        TrajectoryActionBuilder path2 = drive.actionBuilder(new Pose2d(POSE2_X, POSE2_Y, GOAL_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE3_X, POSE3_Y), NORTH_HEADING, new TranslationalVelConstraint(25) );
        TrajectoryActionBuilder path3 = drive.actionBuilder(new Pose2d(POSE3_X, POSE3_Y, GOAL_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE4_X , POSE4_Y), NORTH_HEADING, new TranslationalVelConstraint(25) );
        TrajectoryActionBuilder path4 = drive.actionBuilder(new Pose2d(POSE4_X , POSE4_Y, NORTH_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE2_X , POSE2_Y), GOAL_HEADING, new TranslationalVelConstraint(25) );
        TrajectoryActionBuilder path5 = drive.actionBuilder(new Pose2d(POSE2_X , POSE2_Y, NORTH_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE1_X , POSE1_Y+5), GOAL_HEADING, new TranslationalVelConstraint(25) );



        new SequentialCommandGroup(
                new WaitUntilCommand(this::opModeIsActive),
                farminator.shooterHood.setHoodPosition(1),
                new ParallelRaceGroup(
                        farminator.shooter.runShooterFar(),   // continuous, never finishes on its own
                        new SequentialCommandGroup(
                                new DriveActionCommand(path1),
                                farminator.shooter.runShooterBasedOnDistance(),
                                farminator.transfer.setEntireTransferPowerCommand(1),
//                                farminator.intake.activateIntakeCommand(),
//                                new WaitCommand(2000),
//                                farminator.transfer.setEntireTransferPowerCommand(0),
//                                farminator.intake.deactivateIntakeCommand(),
                                new DriveActionCommand(path2),
                                farminator.intake.activateIntakeCommand(),
                                new DriveActionCommand(path3),
                                new WaitCommand(1500),
                                new DriveActionCommand(path4),
                                farminator.intake.deactivateIntakeCommand(),
                                new DriveActionCommand(path5),
                                farminator.shooter.runShooterBasedOnDistance(),
                                farminator.transfer.setEntireTransferPowerCommand(1),
                                farminator.shooter.turnOff(),
                                farminator.transfer.setEntireTransferPowerCommand(0)





                        )
                ),
                farminator.shooter.turnOff()
        ).schedule();



    }

    @Override
    public void run() {
        super.run();
        farminator.periodic();
    }


    public void end() {
        OpModeData.setAutoFinishPose(drive.localizer.getPose());
    }
}