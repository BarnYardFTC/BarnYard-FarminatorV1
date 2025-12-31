package org.firstinspires.ftc.teamcode.opmodes.auto.red.close;

import static org.firstinspires.ftc.teamcode.commandGroups.ShootSequenceCommandGroup.shootWhenReady;

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
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

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
@Autonomous(name = "3+0 Close Red", group = "main")
public class Red_Close_ThreePlusZero extends CommandOpMode {

    /** Robot and drive system instances */
    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;


    public static final double POSE1_X = -37;
    public static final double POSE1_Y = 43;
    public static final double POSE1_HEADING = Math.toRadians(270);
    public static final double POSE2_HEADING = Math.toRadians(110);

    /** Shooter shooting pose */
    public static double POSE2_X = -30;
    public static double POSE2_Y = 30;
    public static double PERPENDICULAR_TO_DEPOT_HEADING = Math.toRadians(225);
    public static boolean IsFinished = false;

    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.BLUE,
            OpModeData.OpModeType.AUTONOMOUS,
            Webcam.BLUE_LOCALIZATION_PIPELINE,
            new Pose2d(POSE1_X, POSE1_Y, PERPENDICULAR_TO_DEPOT_HEADING));

    @Override
    public void initialize() {

        /** Initialize robot and drive system */
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        drive = new RoadRunnerMecanumDrive(hardwareMap, new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING));



        /** Define trajectory to shooting pose */
        TrajectoryActionBuilder path1 = drive.actionBuilder(new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE2_X, POSE2_Y), POSE2_HEADING, new TranslationalVelConstraint(25) );

        TrajectoryActionBuilder path2 = drive.actionBuilder(new Pose2d(POSE2_X, POSE2_Y, POSE2_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE1_X +15, POSE1_Y), POSE1_HEADING, new TranslationalVelConstraint(25) );

        /** Schedule autonomous sequence */
//        new SequentialCommandGroup(
//                new WaitUntilCommand(this::opModeIsActive),
//                new DriveActionCommand(path1),
//                new SequentialCommandGroup(farminator.shooter.runShooter()),
//                new SequentialCommandGroup(farminator.transfer.setBackPowerCommand(1)),
//                farminator.shooter.turnOff()
//        ).schedule();

        //TODO This auto just can move robot to right place and start shooter and i didnt found the solution yet
        new SequentialCommandGroup(
                new WaitUntilCommand(this::opModeIsActive),
                farminator.shooterHood.setHoodPosition(0.1),
                new ParallelCommandGroup(
                        new DriveActionCommand(path1),
                        new ParallelRaceGroup(
                                farminator.shooter.runShooterClose(),   // continuous, never finishes on its own
                                new SequentialCommandGroup(
                                        shootWhenReady(),
                                        shootWhenReady(),
                                        shootWhenReady(),
                                        new DriveActionCommand(path2)
                                )
                        )
                )
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
