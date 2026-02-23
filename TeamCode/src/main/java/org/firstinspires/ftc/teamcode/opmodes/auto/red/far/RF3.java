package org.firstinspires.ftc.teamcode.opmodes.auto.red.far;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.CommandGroup;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;
@Autonomous(name = "GV RF3", group = "red far")

public class RF3 extends CommandOpMode {

    public static double START_POSE_X = 60;
    public static double START_POSE_Y = 15;
    public static double START_HEADING = Math.toRadians(180);

    public static double SHOOTING_POSE_X = 55;
    public static double SHOOTING_POSE_Y = 10;
    public static double SHOOT_HEADING = Math.toRadians(156);

    public static double PRECOLLECT_Y = 33;
    public static int    SHOOTING_TIME_MS = 2000;

    public static double COLLECT_POSE_X = 48;
    public static double COLLECT_POSE2_X = 42;
    public static double COLLECT_POSE_Y = 59;
    public static double RIGHT_COLLECT_POSE_X = 21;

    public static double NORTH_READY_POSE_Y = 22;
    public static double NORTH_COLLECT_POSE_Y = 53;
    public static double COLLECT_HEADING = Math.toRadians(90);





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
                .strafeToLinearHeading(new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y), SHOOT_HEADING );


        TrajectoryActionBuilder angleCollect = drive.actionBuilder(new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, SHOOT_HEADING))
                .strafeToLinearHeading(new Vector2d(COLLECT_POSE2_X,PRECOLLECT_Y ),COLLECT_HEADING )
                .strafeToLinearHeading(new Vector2d(COLLECT_POSE2_X,COLLECT_POSE_Y ),COLLECT_HEADING )
                .strafeToLinearHeading(new Vector2d(COLLECT_POSE2_X,PRECOLLECT_Y ),COLLECT_HEADING )
                .strafeToLinearHeading(new Vector2d(COLLECT_POSE2_X,COLLECT_POSE_Y ),COLLECT_HEADING )
                .strafeToLinearHeading(new Vector2d(COLLECT_POSE2_X,PRECOLLECT_Y),COLLECT_HEADING )
                .strafeToLinearHeading(new Vector2d(COLLECT_POSE_X,COLLECT_POSE_Y ),COLLECT_HEADING );

        TrajectoryActionBuilder angleToShoot = drive.actionBuilder(
                        new Pose2d(COLLECT_POSE2_X, COLLECT_POSE_Y,COLLECT_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y), SHOOT_HEADING + Math.toRadians(4));

        TrajectoryActionBuilder rightCollect = drive.actionBuilder(
                        new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, SHOOT_HEADING + Math.toRadians(4)))
                .strafeToLinearHeading(new Vector2d(RIGHT_COLLECT_POSE_X, NORTH_READY_POSE_Y), COLLECT_HEADING )
                .strafeToLinearHeading(new Vector2d(RIGHT_COLLECT_POSE_X, NORTH_COLLECT_POSE_Y), COLLECT_HEADING, new TranslationalVelConstraint(30));

        TrajectoryActionBuilder rightToShoot   = drive.actionBuilder(
                        new Pose2d(RIGHT_COLLECT_POSE_X, NORTH_COLLECT_POSE_Y, COLLECT_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y), SHOOT_HEADING + Math.toRadians(4));

        TrajectoryActionBuilder finalPos   = drive.actionBuilder(
                        new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, SHOOT_HEADING + Math.toRadians(4)))
                .strafeToLinearHeading(new Vector2d(RIGHT_COLLECT_POSE_X, SHOOTING_POSE_Y), SHOOT_HEADING);





        new SequentialCommandGroup(
                new WaitUntilCommand(this::opModeIsActive),
                shootCommandPath(startToShoot),

                intakeCommandPath(angleCollect),

                shootCommandPath(angleToShoot),

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

    public Command shootCommandPathIntake(TrajectoryActionBuilder path){
        return new SequentialCommandGroup(
                new ParallelRaceGroup(
                        BarnRobot.getInstance().shooter.runShooterBasedOnDistance(),
                        new SequentialCommandGroup(
                                CommandGroup.intakeAndTransferActivateCommand(),
                                new DriveActionCommand(path),
                                CommandGroup.deactivateIntakeAndTransferCommand(),
                                new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                                CommandGroup.intakeAndTransferActivateCommand(),
                                BarnRobot.getInstance().gate.openCommand(),
                                new WaitCommand(SHOOTING_TIME_MS),
//                        new WaitUntilCommand(() -> !CommandGroup.robotContainsArtifacts()),
                                BarnRobot.getInstance().gate.closeCommand(),
                                CommandGroup.deactivateIntakeAndTransferCommand()
                        )
                ),
                BarnRobot.getInstance().shooter.turnOffInstant()
        );
    }

    private Command shootCommand() {
        return new SequentialCommandGroup(
                new ParallelRaceGroup(
                        BarnRobot.getInstance().shooter.runShooterBasedOnDistance(),
                        new SequentialCommandGroup(
                                new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                                BarnRobot.getInstance().gate.openCommand(),
                                CommandGroup.intakeAndTransferActivateCommand(),
                                new WaitCommand(SHOOTING_TIME_MS),
                                //new WaitUntilCommand(() -> !CommandGroup.robotContainsArtifacts()),
                                BarnRobot.getInstance().gate.closeCommand(),
                                CommandGroup.deactivateIntakeAndTransferCommand(),
                                BarnRobot.getInstance().shooterHood.setHoodPosition(0.85)
                        )
                ),
                BarnRobot.getInstance().shooter.turnOffInstant()
        );
    }

    public Command intakeCommandPath(TrajectoryActionBuilder path){
        return new SequentialCommandGroup(
                CommandGroup.intakeAndTransferActivateCommand(),
                new DriveActionCommand(path),
                new WaitCommand(500),
                CommandGroup.deactivateIntakeAndTransferCommand()
        );
    }



}

