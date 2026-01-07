package org.firstinspires.ftc.teamcode.testing;


import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.subsystems.Webcam;
import org.firstinspires.ftc.teamcode.util.OpModeData;

@TeleOp (name="LimelightTestingOPMode")
@Config
public class LimelightOpMode extends CommandOpMode {
    private BarnRobot robot;
    @Override
    public void initialize(){
        Pose2d autoFinishPose = OpModeData.getAutoFinishPose();
        OpModeData  opModeData = new OpModeData(
                OpModeData.AllianceColor.BLUE,
                OpModeData.OpModeType.TELEOP,
                autoFinishPose
        );

        robot = BarnRobot.getInstance();
        robot.init(
                this,
                opModeData
        );

//        robot.limelight.switchPipeline(LimeLight.BLUE_LOCALIZATION_PIPELINE);
//        robot.gamepadEx1.getGamepadButton(GamepadKeys.Button.A)
//                .whenActive(robot.drive.alignToTagCommand());
    }

    @Override
    public void run(){
        super.run();

//        robot.telemetry.addData("Lime", BarnRobot.getInstance().limelight.getArtifactReadiness());
        robot.periodic();
    }

}
