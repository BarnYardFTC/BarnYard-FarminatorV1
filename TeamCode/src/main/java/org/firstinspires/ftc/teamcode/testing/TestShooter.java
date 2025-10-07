package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.OpModeData;

@TeleOp
public class TestShooter extends CommandOpMode {
    private BarnRobot farminator;

    @Override
    public void initialize() {

        // ------------------------
        // Initialize Robot Systems
        // ------------------------
        farminator = BarnRobot.getInstance();
        farminator.initBarnRobotSystems(this, new OpModeData());


    }

    @Override
    public void run() {
        super.run();
        telemetry.update();
    }
}
