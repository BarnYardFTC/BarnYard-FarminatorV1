package org.firstinspires.ftc.teamcode.opmodes.auto;

import static org.firstinspires.ftc.teamcode.subsystems.Transfer.TRANSFER_ONE_DURATION;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.ShootSequenceCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.roadrunner.MecanumDrive;

@Config
@Autonomous(name="DeBug", group = "main")
public class DeBug extends CommandOpMode {
    private BarnRobot farminator;
    private MecanumDrive drive;
    public static double POSE1_X = -37;
    public static double POSE1_Y = -53;
    public static double POSE1_HEADING = Math.toRadians(90);

    public static double POSE2_X = 0;
    public static double POSE2_Y = 0;
    public static double POSE2_HEADING = Math.toRadians(230);

    @Override
    public void initialize() {

        // ------------------------
        // Initialize Robot Systems
        // ------------------------
        farminator = BarnRobot.getInstance();
        farminator.init(this, new OpModeData(OpModeData.AllianceColor.BLUE, 0, 0, OpModeData.OpModeType.AUTONOMOUS));
        drive = new MecanumDrive(hardwareMap, new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING));
        farminator.limelight.switchPipeline(LimeLight.OBELISK_PIPELINE);

        TrajectoryActionBuilder path1 = drive.actionBuilder(new Pose2d(POSE1_X, POSE1_Y, POSE1_HEADING))
                .strafeToLinearHeading(new Vector2d(POSE2_X, POSE2_Y), POSE2_HEADING);

//        TrajectoryActionBuilder path2 = path1.endTrajectory().fresh()
//                .strafeToLinearHeading(new Vector2d(POSE2_X, POSE2_Y), POSE2_HEADING);


        new SequentialCommandGroup(
                new DriveActionCommand(path1)
        ).schedule();

    }

    @Override
    public void run() {
        super.run();
        BarnRobot.getInstance().limelight.periodic();
//        if (farminator.limelight.isDataValid() && !farminator.limelight.isPatternFound()){
//            farminator.limelight.findPattern();
//        }
//        else if (farminator.limelight.isPatternFound()){
//            if (farminator.limelight.currentPipeline == LimeLight.OBELISK_PIPELINE) farminator.limelight.switchPipeline(LimeLight.BLUE_PIPELINE);
//            BarnRobot.getInstance().limelight.findRange(Math.toDegrees(drive.localizer.getPose().heading.real));
//        }

        if (farminator.limelight.currentPipeline != LimeLight.BLUE_PIPELINE) farminator.limelight.switchPipeline(LimeLight.BLUE_PIPELINE);
        BarnRobot.getInstance().limelight.findRange(Math.toDegrees(drive.localizer.getPose().heading.real));

        farminator.periodic();
        farminator.shooter.displayTelemetry();
    }
}
