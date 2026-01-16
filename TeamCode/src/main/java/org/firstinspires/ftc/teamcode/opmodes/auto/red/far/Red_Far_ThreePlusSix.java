package org.firstinspires.ftc.teamcode.opmodes.auto.red.far;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
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
@Autonomous(name = "!RED THREE PLUS Six FAR", group = "!main")

public class Red_Far_ThreePlusSix  extends CommandOpMode {


    public static double START_POSE_X = 60;
    public static double START_POSE_Y = 15;
    public static double START_HEADING = Math.toRadians(180);

    public static double SHOOTING_POSE_X = 55;
    public static double SHOOTING_POSE_Y = 10;
    public static double SHOOT_HEADING = Math.toRadians(175);

    public static double PRECOLLECT_Y = 40;
    public static int SHOOTING_TIME_MS = 2000;

    public static double COLLECT_POSE_X = 50;
    public static double COLLECT_POSE2_X =55 ;
    public static double COLLECT_POSE_Y = 58;

    public static double RIGHT_COLLECT_POSE_X = 30;
    public static double NORTH_READY_POSE_Y = 22;
    public static double NORTH_COLLECT_POSE_Y = 53;
    public static double COLLECT_HEADING = Math.toRadians(90);



    /** Robot and drive system instances */
    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;



    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.BLUE,
            OpModeData.OpModeType.AUTONOMOUS,
            new Pose2d(START_POSE_X, START_POSE_Y, START_HEADING)
    );

    public static int SCORE_TIME = 2200;

    @Override
    public void initialize() {

        /** Initialize robot and drive system */
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        farminator.shooter.setDefaultCommand(farminator.shooter.turnOff());
        drive = new RoadRunnerMecanumDrive(hardwareMap, new Pose2d(START_POSE_X, START_POSE_Y, START_HEADING));

        TrajectoryActionBuilder path1 = drive.actionBuilder(
                        new Pose2d(START_POSE_X, START_POSE_Y, START_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y), SHOOT_HEADING);


        TrajectoryActionBuilder path2 = drive.actionBuilder(new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, SHOOT_HEADING))
                .strafeToLinearHeading(new Vector2d(COLLECT_POSE_X,COLLECT_POSE_Y ),COLLECT_HEADING )
                .strafeToLinearHeading(new Vector2d(COLLECT_POSE_X,PRECOLLECT_Y ),COLLECT_HEADING )
                .strafeToLinearHeading(new Vector2d(COLLECT_POSE2_X,COLLECT_POSE_Y ),COLLECT_HEADING );

        TrajectoryActionBuilder path3 = drive.actionBuilder(
                        new Pose2d(COLLECT_POSE2_X, COLLECT_POSE_Y,COLLECT_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y), SHOOT_HEADING);

        TrajectoryActionBuilder path4 = drive.actionBuilder(
                        new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, SHOOT_HEADING))
                .strafeToLinearHeading(new Vector2d(RIGHT_COLLECT_POSE_X, NORTH_READY_POSE_Y), COLLECT_HEADING )
                .strafeToLinearHeading(new Vector2d(RIGHT_COLLECT_POSE_X, NORTH_COLLECT_POSE_Y), COLLECT_HEADING);

        TrajectoryActionBuilder path5 = drive.actionBuilder(
                        new Pose2d(RIGHT_COLLECT_POSE_X, NORTH_COLLECT_POSE_Y, COLLECT_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y), SHOOT_HEADING);





        new SequentialCommandGroup(
                new WaitUntilCommand(this::opModeIsActive),
                shootCommandPath(path1),

                intakeCommandPath(path2),

                shootCommandPath(path3),

                intakeCommandPath(path4),

                shootCommandPath(path5)
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

    public Command shootCommandPath(TrajectoryActionBuilder shootingPath){
        return new ParallelRaceGroup(
                BarnRobot.getInstance().shooter.runShooterBasedOnDistance(),
                new SequentialCommandGroup(
                        new DriveActionCommand(shootingPath),
                        new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                        BarnRobot.getInstance().gate.openCommand(),
                        BarnRobot.getInstance().transfer.activateTransfer(),
                        BarnRobot.getInstance().intake.activateIntakeCommand(),
                        new ParallelRaceGroup(
                                new WaitUntilCommand(() -> !BarnRobot.getInstance().shooter.isReady()),
                                new WaitCommand(SHOOTING_TIME_MS)
                        ),
                        CommandGroup.deactivateIntakeAndTransferCommand(),
                        new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                        BarnRobot.getInstance().transfer.activateTransfer(),
                        BarnRobot.getInstance().intake.activateIntakeCommand(),
                        new ParallelRaceGroup(
                                new WaitUntilCommand(() -> !BarnRobot.getInstance().shooter.isReady()),
                                new WaitCommand(SHOOTING_TIME_MS)
                        ),
                        CommandGroup.deactivateIntakeAndTransferCommand(),
                        new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                        BarnRobot.getInstance().transfer.activateTransfer(),
                        BarnRobot.getInstance().intake.activateIntakeCommand(),
                        new ParallelRaceGroup(
                                new WaitUntilCommand(() -> !BarnRobot.getInstance().shooter.isReady()),
                                new WaitCommand(SHOOTING_TIME_MS)
                        ),
                        BarnRobot.getInstance().gate.closeCommand(),
                        CommandGroup.deactivateIntakeAndTransferCommand()
                )
        );
    }

    public Command shootCommand(){
        return new SequentialCommandGroup(
                new ParallelRaceGroup(
                        farminator.shooter.runShooterBasedOnDistance(),
                        new SequentialCommandGroup(
                                new WaitCommand(500),
                                farminator.intake.activateIntakeCommand(),
                                new WaitCommand(SCORE_TIME),
                                farminator.intake.deactivateIntakeCommand()
                        )
                ),
                farminator.shooter.turnOffInstant()
        );
    }

    public Command intakeCommandPath(TrajectoryActionBuilder path){
        return new SequentialCommandGroup(
                CommandGroup.intakeAndTransferCommand(),
                new DriveActionCommand(path),
                deactivateIntakeAndTransferCommand()
        );
    }
    public static Command deactivateIntakeAndTransferCommand(){
        return new ParallelCommandGroup(BarnRobot.getInstance().intake.deactivateIntakeCommand(), BarnRobot.getInstance().transfer.deactivateTransfer());
    }


}

