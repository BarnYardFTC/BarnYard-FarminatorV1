package org.firstinspires.ftc.teamcode.opmodes.auto.red.close;

import static org.firstinspires.ftc.teamcode.opmodes.auto.blue.close.Blue_Close_PPG.INTAKE_VELOCITY;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
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

public class Red_Close_ThreePlusNine extends CommandOpMode {
    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;

    public static double START_POSE_X = 0;

    public static double START_POSE_Y = 0;
    public static final double START_POSE_HEADING = Math.toRadians(270);

    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.RED,
            OpModeData.OpModeType.AUTONOMOUS,
            new Pose2d(START_POSE_X, START_POSE_Y, START_POSE_HEADING)
    );

    public static int SCORE_TIME = 4000;

    @Override
    public void initialize() {
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);




    }

    @Override
    public void run() {
        super.run();
        farminator.periodic();
    }

    @Override
    public void end() {
        OpModeData.setAutoFinishPose(drive.localizer.getPose());
    }
}
