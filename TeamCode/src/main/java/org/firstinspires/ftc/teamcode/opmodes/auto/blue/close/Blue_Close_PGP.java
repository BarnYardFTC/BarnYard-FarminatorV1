package org.firstinspires.ftc.teamcode.opmodes.auto.blue.close;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;

import org.firstinspires.ftc.onbotjava.handlers.objbuild.WaitForBuild;
import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
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
@Autonomous(name = "3+3 Close test PGP", group = "main")
public class Blue_Close_PGP extends CommandOpMode {

    /** Robot and drive system instances */
    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;

    public static final double POSE1_X = -37;
    public static final double POSE1_Y = -53;
    public static final double POSE1_HEADING = Math.toRadians(90);

    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.BLUE,
            OpModeData.OpModeType.AUTONOMOUS,
            LimeLight.BLUE_LOCALIZATION_PIPELINE,
            new Pose2d(1,1,1)
            );

    public static final double POSE2_X = -15.5;
    public static final double POSE2_Y = -15.5;
    public static final double POSE2_HEADING = Math.toRadians(225);

    public static final double POSE3_X = 11.5;
    public static final double POSE3_Y = -23.1;
    public static final double POSE3_HEADING = Math.toRadians(270);

    public static final double POSE4_X = 11.5;
    public static final double POSE4_Y = -60;
    public static final double POSE4_HEADING = Math.toRadians(270);

    public static final double POSE5_X =11.5;
    public static final double POSE5_Y = -40;
    public static final double POSE5_HEADING = Math.toRadians(270);
    @Override
    public void initialize() {

        /** Initialize robot and drive system */
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        drive = new RoadRunnerMecanumDrive(hardwareMap, new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING));

        /** Define trajectory to shooting pose */
        TrajectoryActionBuilder path1 = drive.actionBuilder(new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE2_X, POSE2_Y), POSE2_HEADING, new TranslationalVelConstraint(25));


        TrajectoryActionBuilder path2 = path1.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(POSE3_X, POSE3_Y), POSE3_HEADING, new TranslationalVelConstraint(25))
                .strafeToLinearHeading(new Vector2d(POSE4_X, POSE4_Y), POSE4_HEADING, new TranslationalVelConstraint(25));

        TrajectoryActionBuilder path3 = path2.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(POSE5_X, POSE5_Y), POSE5_HEADING, new TranslationalVelConstraint(25))
                .strafeToLinearHeading(new Vector2d(POSE2_X, POSE2_Y), POSE2_HEADING, new TranslationalVelConstraint(25));




        /** Schedule autonomous sequence */
        new SequentialCommandGroup(
                new DriveActionCommand(path1),
                new SequentialCommandGroup(farminator.shooter.runShooterClose()),
                new SequentialCommandGroup(farminator.transfer.setBackPowerCommand(1)),
                new WaitCommand(3000),
                new SequentialCommandGroup(farminator.intake.activateIntakeCommand()),
                new SequentialCommandGroup(farminator.shooter.runShooterClose()),


                //lehakot zman
                //lehafil et ha kol
                new DriveActionCommand(path2),
                new SequentialCommandGroup(farminator.transfer.setEntireTransferPowerCommand(0.2)),
                new SequentialCommandGroup(farminator.transfer.setFrontPowerCommand(0.2)),
                new SequentialCommandGroup(farminator.transfer.setBackPowerCommand(-1)),
                new SequentialCommandGroup(farminator.intake.deactivateIntakeCommand()),
                new DriveActionCommand(path3),
                new SequentialCommandGroup(farminator.shooter.runShooterClose()),
                new WaitCommand(4500),
                new SequentialCommandGroup(farminator.transfer.setBackPowerCommand(1)),
                new WaitCommand(3000),
                new SequentialCommandGroup(farminator.intake.activateIntakeCommand())
        ).schedule();

    }

    @Override
    public void run() {
        super.run();

//        /** Update limelight and shooter telemetry */
//        farminator.limelight.periodic();
//
//        if (farminator.limelight.isDataValid() && !farminator.limelight.isPatternFound()) {
//            farminator.limelight.findPattern();
//        } else if (farminator.limelight.isPatternFound()) {
//            if (farminator.limelight.currentPipeline == LimeLight.OBELISK_PIPELINE) {
//                farminator.limelight.switchPipeline(LimeLight.BLUE_LOCALIZATION_PIPELINE);
//            }
//            farminator.limelight.findRange(Math.toDegrees(drive.localizer.getPose().heading.real));
//        }
//
//        farminator.limelight.displayTelemetry();
//        farminator.shooter.displayTelemetry();
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
