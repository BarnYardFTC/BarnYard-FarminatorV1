package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad2;

import com.seattlesolvers.solverslib.command.Robot;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.SubSystems.ArmSubSystem;
import org.firstinspires.ftc.teamcode.SubSystems.ClawSubSystem;

public class BarnRobot extends Robot {
    public static BarnRobot instance;
    public ClawSubSystem claw;
    public ArmSubSystem arm;
    public GamepadEx gamepadEx1;
    public GamepadEx gamepadEx2;
    public Telemetry telemetry;

    public static synchronized BarnRobot getInstance() {
        if (instance == null) {
            instance = new BarnRobot();
        }
        return instance;
    }

    public void initBarnRobotSystems() {
        gamepadEx1 = new GamepadEx(gamepad1);
        gamepadEx2 = new GamepadEx(gamepad2);

        claw = new ClawSubSystem();
        arm = new ArmSubSystem();
    }
}
