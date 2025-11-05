package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.util.OpModeData;

@TeleOp(name = "DeadwheelLocalizationTest", group = "testing")
public class TestDeadWheelLocalizationTeleop extends CommandOpMode {

    private double START_POSE_X = 0;
    private double START_POSE_Y = 0;
    private double START_POSE_HEADING = 0;

    private BarnRobot farminator;

    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.RED,
            new Pose2d(START_POSE_X, START_POSE_Y, START_POSE_HEADING),
            0,
            OpModeData.OpModeType.TELEOP,
            LimeLight.BLUE_LOCALIZATION_PIPELINE
    );

    @Override
    public void initialize() {
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

    }

    @Override
    public void run(){
        farminator.telemetry.addData("X", farminator.pinpointLocalizer.getPose().position.x);
        farminator.telemetry.addData("Y", farminator.pinpointLocalizer.getPose().position.y);
        farminator.telemetry.addData("heading", Math.toDegrees(farminator.pinpointLocalizer.getPose().heading.toDouble()));
        farminator.pinpointLocalizer.update();
        farminator.periodic();
    }
}


