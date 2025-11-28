package org.firstinspires.ftc.teamcode.opmodes.auto.blue.far;

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
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.ShootSequenceCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.roadrunner.RoadRunnerMecanumDrive;

@Config
@Autonomous(name = "3+0 Far Blue", group = "main")
public class Blue_Far_ThreePlusZero extends CommandOpMode {

    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;

    public static double POSE1_X = 60;
    public static double POSE1_Y = -10;
    public static double POSE1_HEADING = Math.toRadians(180);

    public static double POSE2_X = 55;
    public static double POSE2_Y = -11;
    public static double POSE2_HEADING = Math.toRadians(215);


    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.BLUE,
            OpModeData.OpModeType.AUTONOMOUS,
            LimeLight.BLUE_LOCALIZATION_PIPELINE,
            new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING));

    @Override
    public void initialize() {

        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        drive = new RoadRunnerMecanumDrive(hardwareMap, new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING));

//        farminator.limelight.switchPipeline(LimeLight.OBELISK_PIPELINE);


        TrajectoryActionBuilder path1 = drive.actionBuilder(new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE2_X, POSE2_Y), POSE2_HEADING, new TranslationalVelConstraint(25));
        TrajectoryActionBuilder path2 = drive.actionBuilder(new Pose2d(POSE2_X, POSE2_Y, POSE2_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE2_X + 15, POSE2_Y), POSE2_HEADING, new TranslationalVelConstraint(25) );

        new SequentialCommandGroup(
                new WaitUntilCommand(this::opModeIsActive),
                farminator.shooterHood.setHoodPosition(1),
                new ParallelRaceGroup(
                        farminator.shooter.runShooterFar(),   // continuous, never finishes on its own
                        new SequentialCommandGroup(
                                new DriveActionCommand(path1),
                                new WaitCommand(2000),
                                farminator.transfer.setEntireTransferPowerCommand(1),
                                new WaitCommand(2000),
                                farminator.intake.activateIntakeCommand(),
                                new WaitCommand(2000),
                                farminator.transfer.setEntireTransferPowerCommand(0),
                                farminator.intake.deactivateIntakeCommand()

                        )
                ),
                farminator.shooter.turnOff(),
                new DriveActionCommand(path2)
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