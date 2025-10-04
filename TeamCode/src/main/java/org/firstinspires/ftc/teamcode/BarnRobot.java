package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.Robot;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.SubSystems.DriveTrain;
import org.firstinspires.ftc.teamcode.SubSystems.Transfer;

public class BarnRobot extends Robot {
    public static BarnRobot instance;
    public Transfer transfer;
    public DriveTrain drive;
    public GamepadEx gamepadEx1;
    public GamepadEx gamepadEx2;
    public Telemetry telemetry;
    public HardwareMap hardwareMap;

    public Command driveCommand;

    public static synchronized BarnRobot getInstance() {
        if (instance == null) {
            instance = new BarnRobot();
        }
        return instance;
    }

    public void initBarnRobotSystems(HardwareMap hw, Gamepad gamepad1, Gamepad gamepad2) {
        this.hardwareMap = hw;
        gamepadEx1 = new GamepadEx(gamepad1);
        gamepadEx2 = new GamepadEx(gamepad2);

        initTransfer();
        initDrivetrain();
    }

    public void initDrivetrain(){
        drive = new DriveTrain(hardwareMap);

    }

    public void initTransfer(){
        transfer = new Transfer(hardwareMap);
        register(transfer);

    }

}
