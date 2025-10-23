package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.ShootSequenceCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.roadrunner.MecanumDrive;

@Config
@Autonomous(name="3+0 close", group = "main")
public class ThreePlusZeroClose extends CommandOpMode {
    private BarnRobot farminator;
    private MecanumDrive drive;
    public static double POSE1_X = -37;
    public static double POSE1_Y = -53;
    public static double POSE1_HEADING = Math.toRadians(90);

    public static double POSE2_X = -25;
    public static double POSE2_Y = -15;
    public static double POSE2_HEADING = Math.toRadians(250);

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


        new SequentialCommandGroup(
                new DriveActionCommand(path1),
                BarnRobot.getInstance().shooter.shootAtRangeCommand(),
                new WaitCommand(2000),
                farminator.transfer.activateTransferCommand(),
                farminator.intake.activateIntakeCommand(),
                new WaitCommand(2000),
                farminator.intake.deactivateIntakeCommand(),
                farminator.transfer.deactivateTransferCommand()
        ).schedule();

    }

    @Override
    public void run() {
        super.run();
        BarnRobot.getInstance().limelight.periodic();
        BarnRobot.getInstance().limelight.findRange();
        if (farminator.limelight.isDataValid() && !farminator.limelight.isPatternFound()){
            farminator.limelight.findPattern();
        }
        farminator.limelight.displayTelemetry();
        farminator.periodic();
    }
}
