package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad2;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.Robot;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.SubSystems.ArmTest;
import org.firstinspires.ftc.teamcode.SubSystems.ClawTest;
import org.firstinspires.ftc.teamcode.SubSystems.DriveTrain;
import org.firstinspires.ftc.teamcode.SubSystems.Transfer;

public class BarnRobot extends Robot {
    public static BarnRobot instance;
    public ClawTest claw;
    public ArmTest arm;
    public Transfer transfer;
    public DriveTrain drive;
    public GamepadEx gamepadEx1;
    public GamepadEx gamepadEx2;
    public Telemetry telemetry;

    public Command driveCommand;

    public static synchronized BarnRobot getInstance() {
        if (instance == null) {
            instance = new BarnRobot();
        }
        return instance;
    }

    public void initBarnRobotSystems() {
        gamepadEx1 = new GamepadEx(gamepad1);
        gamepadEx2 = new GamepadEx(gamepad2);

        claw = new ClawTest();
        arm = new ArmTest();
        transfer = new Transfer();
        drive = new DriveTrain();
    }

    public void initDrivetrain(){
        register(drive);
        drive.setDefaultCommand(driveCommand);
    }

    public void initTransfer(){
        register(transfer);

    }

}
