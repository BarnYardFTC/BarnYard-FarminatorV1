package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.OpModeData;

@TeleOp(name = "Junction Test", group = "Test")
public class JunctionTest extends CommandOpMode {
    private BarnRobot farminator;
    @Override
    public void initialize() {

        OpModeData opModeData = new OpModeData(
                OpModeData.AllianceColor.BLUE,
                OpModeData.OpModeType.TELEOP,
                new Pose2d(0, 0, Math.toRadians(180)),
                180
        );

        farminator = BarnRobot.getInstance();

        farminator.init(
                this,
                opModeData
        );
    }

    @Override
    public void run() {
        super.run();

        farminator.periodic();
        
        farminator.webcam.displayTelemetry();
        telemetry.addData("pinpoint x: ", farminator.pinpointLocalizer.getPose().position.x);
        telemetry.addData("pinpoint y:", farminator.pinpointLocalizer.getPose().position.y);
        telemetry.addData("pinpoint heading: ", farminator.pinpointLocalizer.getPose().heading.toDouble());
    }
}
