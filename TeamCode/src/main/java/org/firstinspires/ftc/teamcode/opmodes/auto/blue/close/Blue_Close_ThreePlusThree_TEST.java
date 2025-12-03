package org.firstinspires.ftc.teamcode.opmodes.auto.blue.close;

import static org.firstinspires.ftc.teamcode.commandGroups.ShootSequenceCommandGroup.shootWhenReady;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.robocol.Command;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.ShootSequenceCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.roadrunner.RoadRunnerMecanumDrive;

    @Config
    @Autonomous (name = "3+3 Close test eden", group = "main")
    public class Blue_Close_ThreePlusThree_TEST extends CommandOpMode {
        /* Robot and drive system instances */
        private BarnRobot farminator;
        private  RoadRunnerMecanumDrive drive;

        /* Initial pose */
        public static double POSE1_X = -50;
        public static double POSE1_Y = -50;
        public static double PERPENDICULAR_TO_DEPOT_HEADING = Math.toRadians(230);

        /* Shooter shooting pose */
        public static double POSE2_X = -23;
        public static double POSE2_Y = -23;

        /* To artifacts pose */
        public static double POSE3_X = -11;
        public static double POSE3_Y = -31;
        public static double SOUTH_HEADING = Math.toRadians(270);

        /* Collect artifacts pose */
        public static double POSE4_X = -11;
        public static double POSE4_Y = -55;



        private final OpModeData opModeData = new OpModeData(
                OpModeData.AllianceColor.BLUE,
                OpModeData.OpModeType.AUTONOMOUS,
                LimeLight.BLUE_LOCALIZATION_PIPELINE,
                new Pose2d(POSE1_X,POSE1_Y, PERPENDICULAR_TO_DEPOT_HEADING));

        @Override
        public  void initialize() {
            /* initialize robot and drive system */
            farminator = BarnRobot.getInstance();
            farminator.init(this, opModeData);

            drive = new RoadRunnerMecanumDrive(hardwareMap,new Pose2d(POSE1_X,POSE1_Y, PERPENDICULAR_TO_DEPOT_HEADING));


            /* Define trajectory to shooting pose */
            TrajectoryActionBuilder path1 = drive.actionBuilder(new Pose2d(POSE1_X,POSE1_Y,PERPENDICULAR_TO_DEPOT_HEADING))
                    .strafeToLinearHeading(new Vector2d(POSE2_X, POSE2_Y), PERPENDICULAR_TO_DEPOT_HEADING);

            /* Define trajectory to artifacts pose */
            TrajectoryActionBuilder path2 = drive.actionBuilder(new Pose2d(POSE2_X, POSE2_Y, PERPENDICULAR_TO_DEPOT_HEADING))
                    .strafeToLinearHeading(new Vector2d(POSE3_X, POSE3_Y), SOUTH_HEADING);

            /* Define trajectory to collect artifacts pose */
            TrajectoryActionBuilder path3 = drive.actionBuilder(new Pose2d(POSE3_X, POSE3_Y, SOUTH_HEADING))
                    .strafeToLinearHeading(new Vector2d(POSE4_X, POSE4_Y), SOUTH_HEADING);

            /* Define trajectory to returning to shooting pose */
            TrajectoryActionBuilder path4 = drive.actionBuilder(new Pose2d(POSE4_X, POSE4_Y, SOUTH_HEADING))
                    .strafeToLinearHeading(new Vector2d(POSE2_X, POSE2_Y), PERPENDICULAR_TO_DEPOT_HEADING);

            /* Schedule autonomous sequence */
            new SequentialCommandGroup(
                    new WaitUntilCommand(this::opModeIsActive),
//                farminator.shooter.customShooterCommand(farminator.shooter.rangeDependentVelocity(1.8)),
                    new DriveActionCommand(path1),
                    farminator.intake.activateIntakeCommand(),
//                ShootSequenceCommandGroup.shootWhenReady(1.8),
//                ShootSequenceCommandGroup.shootWhenReady(1.8),
//                ShootSequenceCommandGroup.shootWhenReady(1.8),
//                farminator.shooter.customShooterCommand(farminator.shooter.rangeDependentVelocity(1.8)),
                    farminator.intake.activateIntakeCommand(),
                    new DriveActionCommand(path2),
                    new DriveActionCommand(path3),
                    new DriveActionCommand(path4),
//                ShootSequenceCommandGroup.shootWhenReady(1.8),
//                ShootSequenceCommandGroup.shootWhenReady(1.8),
//                ShootSequenceCommandGroup.shootWhenReady(1.8),
//                farminator.shooter.deactivateShooterCommand()
                    farminator.intake.deactivateIntakeCommand()
            ).schedule();

            new SequentialCommandGroup(
                    new WaitCommand(10000)
            );

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
            opModeData.setAutoFinishPose(drive.localizer.getPose());
        }

    }


