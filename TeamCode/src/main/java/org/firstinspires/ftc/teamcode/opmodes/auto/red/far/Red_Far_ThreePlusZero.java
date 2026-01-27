package org.firstinspires.ftc.teamcode.opmodes.auto.red.far;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
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
@Autonomous(name = "!3+0 RED FAR", group = "!main")

public class Red_Far_ThreePlusZero  extends CommandOpMode {

    public static double START_POSE_X = 60;
    public static double START_POSE_Y = 15;
    public static double START_HEADING = Math.toRadians(180);

    public static double SHOOTING_POSE_X = 55;
    public static double SHOOTING_POSE_Y = 10;
    public static double SHOOT_HEADING = Math.toRadians(160);

    public static double PRECOLLECT_Y = 40;
    public static int    SHOOTING_TIME_MS = 2000;

    public static double FINISH_X = 30;
    public static double FINISH_Y = 9;

    public static double RIGHT_COLLECT_POSE_X = 21;

    /** Robot and drive system instances */
    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;



    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.RED,
            OpModeData.OpModeType.AUTONOMOUS,
            new Pose2d(START_POSE_X, START_POSE_Y, START_HEADING)
    );



    @Override
    public void initialize() {

        /** Initialize robot and drive system */
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        farminator.shooter.setDefaultCommand(farminator.shooter.turnOff());
        drive = new RoadRunnerMecanumDrive(hardwareMap, new Pose2d(START_POSE_X, START_POSE_Y, START_HEADING));

        TrajectoryActionBuilder startToShoot = drive.actionBuilder(
                        new Pose2d(START_POSE_X, START_POSE_Y, START_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y), SHOOT_HEADING - Math.toRadians(3));

        TrajectoryActionBuilder finalPos   = drive.actionBuilder(
                        new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, SHOOT_HEADING + Math.toRadians(4)))
                .strafeToLinearHeading(new Vector2d(FINISH_X, FINISH_Y), SHOOT_HEADING);





        new SequentialCommandGroup(
                new WaitUntilCommand(this::opModeIsActive),
                shootCommandPath(startToShoot),

                intakeCommandPath(finalPos)
        ).schedule();
    }


    @Override
    public void end(){
        // store the finish heading of the auto
        OpModeData.setAutoFinishPose(drive.localizer.getPose());
    }

    //       =========== Intake and Shoot CommandPaths ===========
    public Command shootCommandPath(TrajectoryActionBuilder shootingPath){
        return new SequentialCommandGroup(
                new ParallelRaceGroup(
                        BarnRobot.getInstance().shooter.runShooterFar(),
                        new SequentialCommandGroup(
                                new DriveActionCommand(shootingPath),
                                new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReadyCustom(1350)),
                                BarnRobot.getInstance().gate.openCommand(),
                                CommandGroup.intakeAndTransferActivateCommand(),
                                new WaitCommand(SHOOTING_TIME_MS),
//                            new WaitUntilCommand(() -> !CommandGroup.robotContainsArtifacts()),
                                BarnRobot.getInstance().gate.closeCommand(),
                                CommandGroup.deactivateIntakeAndTransferCommand()
                        )
                ),
                BarnRobot.getInstance().shooter.turnOffInstant()
        );
    }

    public Command intakeCommandPath(TrajectoryActionBuilder path){
        return new SequentialCommandGroup(
                CommandGroup.smartIntakeAndTransferCommand(),
                new DriveActionCommand(path),
                CommandGroup.deactivateIntakeAndTransferCommand()
        );
    }



}

