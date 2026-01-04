package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.subsystems.Webcam;
import org.firstinspires.ftc.teamcode.util.OpModeData;

@TeleOp(name = "DeadwheelLocalizationTest", group = "testing")
public class TestDeadWheelLocalizationTeleop extends CommandOpMode {

    private BarnRobot farminator;

    private OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.BLUE,
            OpModeData.OpModeType.TELEOP,
            LimeLight.BLUE_LOCALIZATION_PIPELINE,
            new Pose2d(0,0,0)
    );

    @Override
    public void initialize() {
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);
        farminator.drive.setDefaultCommand(farminator.drive.driveOneDriverCommand());
    }

    @Override
    public void run(){
        farminator.telemetry.addData("X", farminator.pinpointLocalizer.getPose().position.x);
        farminator.telemetry.addData("Y", farminator.pinpointLocalizer.getPose().position.y);
        farminator.telemetry.addData("heading", Math.toDegrees(farminator.pinpointLocalizer.getPose().heading.toDouble()));
        farminator.pinpointLocalizer.update();
        farminator.periodic();
        super.run();
    }
}


