package org.firstinspires.ftc.teamcode.opmodes.auto.blue.close;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
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
        public static double POSE1_X = -50;
        public static double POSE1_Y = -50;
        public static double PERPENDICULAR_TO_DEPOT_HEADING = Math.toRadians(230);

        /* Shooter shooting pose */
        public static double POSE2_X = -23;
        public static double POSE2_Y = -23;

        /* To artifacts pose */
        public static double POSE3_X = -12;
        public static double POSE3_Y = -31;
        public static double SOUTH_HEADING = Math.toRadians(270);

        /* Collect artifacts pose */
        public static double POSE4_X = -12;
        public static double POSE4_Y = -58;



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

            farminator.shooterHood.setDefaultCommand(farminator.shooterHood.autoHoodAlignment());


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

            TrajectoryActionBuilder path5 = path2.endTrajectory().fresh()
                    .strafeToLinearHeading(new Vector2d(POSE2_X, POSE2_Y), PERPENDICULAR_TO_DEPOT_HEADING, new TranslationalVelConstraint(25))
                    .strafeToLinearHeading(new Vector2d(POSE2_X-20, POSE2_Y+10), PERPENDICULAR_TO_DEPOT_HEADING, new TranslationalVelConstraint(25));


            /* Schedule autonomous sequence */
            new SequentialCommandGroup(
                    new WaitUntilCommand(this::opModeIsActive),
                    new DriveActionCommand(path1),
                    farminator.shooter.runShooterBasedOnDistance(),
                    new DriveActionCommand(path2),
                    farminator.intake.activateIntakeCommand(),
                    new DriveActionCommand(path3),
                    farminator.intake.deactivateIntakeCommand(),
                    new DriveActionCommand(path4),
                    farminator.shooter.runShooterBasedOnDistance(),
                    new WaitCommand(3000),
                    farminator.shooter.turnOff(),
                    new DriveActionCommand(path5)


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


