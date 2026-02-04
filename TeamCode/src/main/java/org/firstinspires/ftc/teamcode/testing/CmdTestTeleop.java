package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.BarnRobot;

@TeleOp(name = "Cmd", group = "test")
public class CmdTestTeleop extends CommandOpMode {
    //WIP
    private BarnRobot farminator;
    @Override
    public void initialize() {

    }
    @Override
    public void run() {
        super.run();
        farminator.periodic();
    }
}
