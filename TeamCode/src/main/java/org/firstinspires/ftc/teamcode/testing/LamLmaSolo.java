package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.OpModeData;

@TeleOp(name = "LamLamSolo", group = "test")
@Config
public class LamLmaSolo extends CommandOpMode {

    // ------------------------
    // Robot Instance
    // ------------------------
    private BarnRobot farminator = BarnRobot.getInstance();

    @Override
    public void initialize() {
        OpModeData opModeData = new OpModeData(
                OpModeData.AllianceColor.BLUE,
                OpModeData.OpModeType.TELEOP,
                new Pose2d(0, 0, Math.toRadians(180)),
                180
        );
        farminator.init(this, opModeData);

//        farminator.limelight.setDefaultCommand(()-> farminator);
    }


    @Override
    public void run() {
        super.run();

        // ==========================================================
        // Telemetry
        // ==========================================================
        farminator.limelight.displayTelemetry();


        farminator.periodic();
    }

}
