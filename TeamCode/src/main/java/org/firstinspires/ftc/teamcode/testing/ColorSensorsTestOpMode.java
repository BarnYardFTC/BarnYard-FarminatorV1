package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.OpModeData;

@TeleOp(name="ColorSensor Test", group = "main")

public class ColorSensorsTestOpMode extends CommandOpMode {

    private BarnRobot farminator;

    @Override
    public void initialize() {
        OpModeData opModeData = new OpModeData(
                OpModeData.AllianceColor.RED,
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


        farminator.shooterColorSensor.displayTelemetry(telemetry, "shooter");
        farminator.midColorSensor.displayTelemetry(telemetry, "mid");
        farminator.intakeColorSensor.displayTelemetry(telemetry, "intake");

        farminator.periodic();
    }


}
