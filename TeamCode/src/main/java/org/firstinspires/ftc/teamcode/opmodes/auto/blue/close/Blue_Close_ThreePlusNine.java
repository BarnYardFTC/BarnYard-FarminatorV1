package org.firstinspires.ftc.teamcode.opmodes.auto.blue.close;

import static org.firstinspires.ftc.teamcode.opmodes.auto.blue.close.Blue_Close_PPG.INTAKE_VELOCITY;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

@Autonomous(name = "!BLUE CLOSE THREE + NINE", group = "!main")
public class Blue_Close_ThreePlusNine extends CommandOpMode {

    /** Robot and drive system instances */
    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;

    public static double START_X = -53.333;
    public static double START_Y = -45.5;
    public static final double START_HEADING = Math.toRadians(235);

    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.BLUE,
            OpModeData.OpModeType.AUTONOMOUS,
            LimeLight.BLUE_LOCALIZATION_PIPELINE,
            new Pose2d(START_X, START_Y, START_HEADING)
    );

    public static final double SHOOTING_POSE_X = -23;
    public static final double SHOOTING_POSE_Y = -22;
    public static final double SHOOTING_HEADING = Math.toRadians(227);

    double COLLECT_POS_X, COLLECT_POS_Y, COLLECTED_POS_X, COLLECTED_POS_Y;

    double COLLECTION_HEADING = Math.toRadians(270);
    double GATE_X = 0;
    double GATE_Y = 0;
    double GATE_POS_X = 0;
    double GATE_POS_Y = 0;


    public static int SCORE_TIME = 4000;

    @Override
    public void initialize() {

        /** Initialize robot and drive system */
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        drive = new RoadRunnerMecanumDrive(hardwareMap, new Pose2d(START_X, START_Y, START_HEADING));

        TrajectoryActionBuilder gateClose = drive.actionBuilder(new Pose2d(GATE_POS_X, GATE_POS_Y, COLLECTION_HEADING))
                .strafeToLinearHeading(new Vector2d(GATE_X, GATE_Y), COLLECTION_HEADING);

        TrajectoryActionBuilder gateFar = drive.actionBuilder(new Pose2d(GATE_X, GATE_Y, COLLECTION_HEADING))
                .strafeToLinearHeading(new Vector2d(GATE_POS_X, GATE_POS_Y), COLLECTION_HEADING);

        TrajectoryActionBuilder toGate = drive.actionBuilder(new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, SHOOTING_HEADING))
                .strafeToLinearHeading(new Vector2d(GATE_POS_X, GATE_POS_Y), COLLECTION_HEADING);

        TrajectoryActionBuilder fromGate = drive.actionBuilder(new Pose2d(GATE_POS_X, GATE_POS_Y, COLLECTION_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y), SHOOTING_HEADING);

        TrajectoryActionBuilder toShoot = drive.actionBuilder(new Pose2d(START_X, START_Y, SHOOTING_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y), SHOOTING_HEADING);



        /** Schedule autonomous sequence */
        new SequentialCommandGroup(
                new WaitUntilCommand(this::opModeIsActive),
                new ParallelRaceGroup(
                        farminator.shooter.runShooterBasedOnDistance(),
                        new SequentialCommandGroup(
                                farminator.transfer.setEntireTransferPowerCommand(1),
                                farminator.intake.activateIntakeCommand(),
                                new WaitCommand(SCORE_TIME)
                        )
                ),
                farminator.shooter.turnOffInstant(),
                farminator.intake.deactivateIntakeCommand(),
                farminator.transfer.setEntireTransferPowerCommand(0),
//                new DriveActionCommand(toGate),
//                new DriveActionCommand(gateClose),
//                new DriveActionCommand(gateFar),
//                new DriveActionCommand(fromGate),
                new DriveActionCommand(toShoot),
                ppg(),
                pgp(),
                gpp()
        ).schedule();


    }


    public SequentialCommandGroup ppg(){

        /* To artifacts pose */
        COLLECT_POS_X = -11.8;
        COLLECT_POS_Y = -22;

        /* Collect artifacts pose */
        COLLECTED_POS_X = -11.8;
        COLLECTED_POS_Y = -53;

        /* Define trajectory to artifacts pose */
        TrajectoryActionBuilder path2 = drive.actionBuilder(new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, COLLECTION_HEADING))
                .strafeToLinearHeading(new Vector2d(COLLECT_POS_X, COLLECT_POS_Y), COLLECTION_HEADING);

        /* Define trajectory to collect artifacts pose */
        TrajectoryActionBuilder path3 = drive.actionBuilder(new Pose2d(COLLECT_POS_X, COLLECT_POS_Y, COLLECTION_HEADING))
                .strafeToLinearHeading(new Vector2d(COLLECTED_POS_X, COLLECTED_POS_Y), COLLECTION_HEADING, new TranslationalVelConstraint(INTAKE_VELOCITY));

        /* Define trajectory to returning to shooting pose */
        TrajectoryActionBuilder path4 = drive.actionBuilder(new Pose2d(COLLECTED_POS_X, COLLECTED_POS_Y, COLLECTION_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y), SHOOTING_HEADING - Math.toRadians(20));


        return new SequentialCommandGroup(
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
                                farminator.transfer.setEntireTransferPowerCommand(1),
                                new WaitCommand(SCORE_TIME)
                        )
                ),
                farminator.transfer.setEntireTransferPowerCommand(0),
                farminator.intake.deactivateIntakeCommand(),
                farminator.shooter.turnOffInstant()
        );
    }

    public SequentialCommandGroup pgp() {

        COLLECT_POS_X = 10;
        COLLECT_POS_Y = -22;

        COLLECTED_POS_X = 10;
        COLLECTED_POS_Y = -53;

        TrajectoryActionBuilder path2 =
                drive.actionBuilder(new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, SHOOTING_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(COLLECT_POS_X, COLLECT_POS_Y),
                                COLLECTION_HEADING
                        );



        TrajectoryActionBuilder path3 =
                drive.actionBuilder(new Pose2d(COLLECT_POS_X, COLLECT_POS_Y, COLLECTION_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(COLLECTED_POS_X, COLLECTED_POS_Y),
                                COLLECTION_HEADING, new TranslationalVelConstraint(INTAKE_VELOCITY)
                        );
//
        TrajectoryActionBuilder path4 =
                drive.actionBuilder(new Pose2d(COLLECTED_POS_X, COLLECTED_POS_Y, COLLECTION_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(COLLECT_POS_X, COLLECT_POS_Y),
                                COLLECTION_HEADING
                        );
//
//

        TrajectoryActionBuilder path5 =
                drive.actionBuilder(new Pose2d(COLLECT_POS_X, COLLECT_POS_Y, COLLECTION_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y),
                                SHOOTING_HEADING

                        );


        return new SequentialCommandGroup(
                new DriveActionCommand(path2),
                farminator.intake.activateIntakeCommand(),
                farminator.transfer.setFrontPowerCommand(1),
                farminator.transfer.setBackPowerCommand(-0.1),
                new DriveActionCommand(path3),
                farminator.intake.activateIntakeCommand(),
                farminator.transfer.setEntireTransferPowerCommand(0),
                new DriveActionCommand(path4),
                new ParallelRaceGroup(
                        farminator.shooter.runShooterBasedOnDistance(),
                        new SequentialCommandGroup(
                                new DriveActionCommand(path5),
                                farminator.transfer.setEntireTransferPowerCommand(1),
                                new WaitCommand(SCORE_TIME)

                        )
                ),
                farminator.transfer.setEntireTransferPowerCommand(0),
                farminator.intake.deactivateIntakeCommand(),
                farminator.shooter.turnOffInstant()
        );
    }

    public SequentialCommandGroup gpp(){

        COLLECT_POS_X = 35;
        COLLECT_POS_Y = -22;

        COLLECTED_POS_X = 35;
        COLLECTED_POS_Y = -53;

        TrajectoryActionBuilder path2 =
                drive.actionBuilder(new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, SHOOTING_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(COLLECT_POS_X, COLLECT_POS_Y),
                                COLLECTION_HEADING
                        );



        TrajectoryActionBuilder path3 =
                drive.actionBuilder(new Pose2d(COLLECT_POS_X, COLLECT_POS_Y, COLLECTION_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(COLLECTED_POS_X, COLLECTED_POS_Y),
                                COLLECTION_HEADING, new TranslationalVelConstraint(INTAKE_VELOCITY)
                        );


        TrajectoryActionBuilder path4 =
                drive.actionBuilder(new Pose2d(COLLECTED_POS_X, COLLECTED_POS_Y, COLLECTION_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(COLLECT_POS_X, COLLECT_POS_Y),
                                COLLECTION_HEADING

                        );


        TrajectoryActionBuilder path5 =
                drive.actionBuilder(new Pose2d(COLLECT_POS_X, COLLECT_POS_Y, COLLECTION_HEADING))
                        .strafeToLinearHeading(
                                new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y),
                                SHOOTING_HEADING

                        );

        return new SequentialCommandGroup(
                new DriveActionCommand(path2),
                farminator.intake.activateIntakeCommand(),
                farminator.transfer.setFrontPowerCommand(1),
                farminator.transfer.setBackPowerCommand(-0.1),
                new DriveActionCommand(path3),
                farminator.intake.activateIntakeCommand(),
                farminator.transfer.setEntireTransferPowerCommand(0),
                new DriveActionCommand(path4),
                new ParallelRaceGroup(
                        farminator.shooter.runShooterBasedOnDistance(),
                        new SequentialCommandGroup(
                                new DriveActionCommand(path5),
                                farminator.transfer.setEntireTransferPowerCommand(1),
                                new WaitCommand(SCORE_TIME)

                        )
                ),
                farminator.transfer.setEntireTransferPowerCommand(0),
                farminator.intake.deactivateIntakeCommand(),
                farminator.shooter.turnOffInstant()
        );
    }

    @Override
    public void run() {
        super.run();
        farminator.shooterHood.displayTelemetry();
        farminator.drive.displayPinpointDataTelemetry();
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
