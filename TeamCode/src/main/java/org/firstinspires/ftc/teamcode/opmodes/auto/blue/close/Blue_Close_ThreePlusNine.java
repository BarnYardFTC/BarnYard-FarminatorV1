package org.firstinspires.ftc.teamcode.opmodes.auto.blue.close;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

@Autonomous(name = "!BLUE THREE PLUS NINE", group = "!main")
public class Blue_Close_ThreePlusNine extends CommandOpMode {

    public static double SHOOTING_PROG_POSE_X = -35;
    public static double SHOOTING_PROG_POSE_Y = -30;

    public static long SHOOT_TIME = 2;

    public static double START_POSE_X = -53.333;
    public static double START_POSE_Y = -45.5;
    public static double START_HEADING = Math.toRadians(225);

    public static double SHOOT_POSE_X = -23;
    public static double SHOOT_POSE_Y = -22;
    public static double SHOOT_HEADING = Math.toRadians(227);

    public static double SOUTH_READY_POSE_Y = -31.5;
    public static double SOUTH_COLLECT_POSE_Y = -53;

    public static double SOUTH_HEADING = Math.toRadians(270);

    public static double LEFT_COLLECT_POSE_X = -11.5;
    public static double MID_COLLECT_POSE_X = 12;
    public static double RIGHT_COLLECT_POSE_X = 35;

    public static double GATE_POSE_X = 0;
    public static double GATE_POSE_Y = -44;

    public static double ENDING_POSE_X = -40;
    public static double ENDING_POSE_Y = -22;
    public static double defaultVel = 50;

    public boolean shootPosBusyness = false;
    public boolean midPosBusyness = false;


    /** Robot and drive system instances */
    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;

    public static double POSE1_X = -53.33;
    public static double POSE1_Y = -45.5;
    public static double POSE1_HEADING = Math.toRadians(230);

    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.BLUE,
            OpModeData.OpModeType.AUTONOMOUS,
            new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING)
    );

    public static int SCORE_TIME = 2200;

    @Override
    public void initialize() {

        /** Initialize robot and drive system */
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        farminator.shooter.setDefaultCommand(farminator.shooter.turnOff());
        drive = new RoadRunnerMecanumDrive(hardwareMap, new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING));

        TrajectoryActionBuilder path1 = drive.actionBuilder(
                        new Pose2d(START_POSE_X, START_POSE_Y, START_HEADING))
//              .splineToConstantHeading(new Vector2d(LEFT_COLLECT_POSE_X, SOUTH_READY_POSE_Y), SOUTH_HEADING)
//                .splineToConstantHeading(new Vector2d(LEFT_COLLECT_POSE_X, SOUTH_COLLECT_POSE_Y), new Rotation2d(2,-3))
                .strafeToLinearHeading(new Vector2d(LEFT_COLLECT_POSE_X,SOUTH_READY_POSE_Y+2.5), SOUTH_HEADING)
                .strafeToLinearHeading(new Vector2d(LEFT_COLLECT_POSE_X, SOUTH_COLLECT_POSE_Y), new Rotation2d(0,-3))
                .splineToConstantHeading(
                        new Vector2d(GATE_POSE_X, SOUTH_COLLECT_POSE_Y),
                        new Rotation2d(3, -8)
                )

                .strafeToLinearHeading(
                        new Vector2d(SHOOT_POSE_X, SHOOT_POSE_Y),
                        SHOOT_HEADING);



        ;

        TrajectoryActionBuilder path3 = drive.actionBuilder(
                        new Pose2d(LEFT_COLLECT_POSE_X, SOUTH_COLLECT_POSE_Y, SOUTH_HEADING))
                .splineToConstantHeading(
                        new Vector2d(GATE_POSE_X, SOUTH_COLLECT_POSE_Y),
                        new Rotation2d(3, -8)
                )

                .strafeToLinearHeading(
                        new Vector2d(SHOOT_POSE_X, SHOOT_POSE_Y),
                        SHOOT_HEADING);

        TrajectoryActionBuilder path4 = drive.actionBuilder(
                        new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING))
                .strafeToLinearHeading(
                        new Vector2d(MID_COLLECT_POSE_X, SOUTH_READY_POSE_Y),
                        SOUTH_HEADING)

                .strafeToLinearHeading(
                        new Vector2d(MID_COLLECT_POSE_X, SOUTH_COLLECT_POSE_Y),
                        SOUTH_HEADING)
                .splineToLinearHeading(
                        new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING),
                        new Rotation2d(-2, -1));
        ;

        TrajectoryActionBuilder path6 = drive.actionBuilder(
                        new Pose2d(MID_COLLECT_POSE_X, SOUTH_COLLECT_POSE_Y, SOUTH_HEADING))
                .splineToLinearHeading(
                        new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING),
                        new Rotation2d(-2, -1));

        TrajectoryActionBuilder path7 = drive.actionBuilder(
                                new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(RIGHT_COLLECT_POSE_X, SOUTH_READY_POSE_Y),
                                SOUTH_HEADING)

                        .strafeToLinearHeading(
                                new Vector2d(RIGHT_COLLECT_POSE_X, SOUTH_COLLECT_POSE_Y),
                                SOUTH_HEADING)
                        .splineToLinearHeading(
                                new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING)
                                , new Rotation2d(-2, -1))
                ;

        TrajectoryActionBuilder path9 = drive.actionBuilder(
                        new Pose2d(RIGHT_COLLECT_POSE_X, SOUTH_COLLECT_POSE_Y, SOUTH_HEADING))
                .splineToLinearHeading(
                        new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING)
                        , new Rotation2d(-1, -1));

        TrajectoryActionBuilder path10 = drive.actionBuilder(
                                new Pose2d(SHOOT_POSE_X, SHOOT_POSE_Y, SHOOT_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(ENDING_POSE_X, ENDING_POSE_Y),
                                SHOOT_HEADING);


        new SequentialCommandGroup(
                new WaitUntilCommand(this::opModeIsActive),
                shootCommand(),

                intakeCommandPath(path1),

                shootCommandPath(path3),

                intakeCommandPath(path4),

                shootCommandPath(path6),

                intakeCommandPath(path7),

                shootCommandPath(path9),
                new DriveActionCommand(path10)


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
        return new SequentialCommandGroup(
                new ParallelRaceGroup(
                        farminator.shooter.runShooterBasedOnDistance(),
                        farminator.gate.openCommand(),
                        new SequentialCommandGroup(
                                new DriveActionCommand(shootingPath),
                                new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                                farminator.intake.activateIntakeCommand(),
                                new WaitUntilCommand(() -> !farminator.shooterColorSensor.isPosBusy() && !farminator.midColorSensor.isPosBusy()),
                                farminator.intake.deactivateIntakeCommand()
                        )
                ),
                farminator.gate.closeCommand(),
                farminator.shooter.turnOffInstant()
        );
    }

    public Command shootCommand(){
        return new SequentialCommandGroup(
            new ParallelRaceGroup(
                    farminator.shooter.runShooterBasedOnDistance(),
                    farminator.gate.openCommand(),
                    new SequentialCommandGroup(
                            new WaitUntilCommand(() -> BarnRobot.getInstance().shooter.isReady()),
                            farminator.intake.activateIntakeCommand(),
                            new WaitUntilCommand(() -> !farminator.shooterColorSensor.isPosBusy() && !farminator.midColorSensor.isPosBusy()),
                            farminator.intake.deactivateIntakeCommand()
                    )
            ),
                farminator.gate.closeCommand(),
                farminator.shooter.turnOffInstant()
        );

    }

    public Command intakeCommandPath(TrajectoryActionBuilder path){
        return new SequentialCommandGroup(
                farminator.intake.activateIntakeCommand(),
                new DriveActionCommand(path),
                farminator.intake.deactivateIntakeCommand()
        );
    }


}
