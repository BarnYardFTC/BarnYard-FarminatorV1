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

    // ------------------------------------------------------------
    // Singleton Instance
    // ------------------------------------------------------------
    private static BarnRobot instance;

    // ------------------------------------------------------------
    // Subsystems
    // ------------------------------------------------------------
    public Transfer transfer;
    public DriveTrain drive;

    // ------------------------------------------------------------
    // Gamepads
    // ------------------------------------------------------------
    public GamepadEx gamepadEx1;
    public GamepadEx gamepadEx2;

    // ------------------------------------------------------------
    // FTC SDK References
    // ------------------------------------------------------------
    public Telemetry telemetry;
    public HardwareMap hardwareMap;

    // ------------------------------------------------------------
    // Commands
    // ------------------------------------------------------------
    public Command driveCommand;

    // ------------------------------------------------------------
    // Singleton Accessor
    // ------------------------------------------------------------
    public static synchronized BarnRobot getInstance() {
        if (instance == null) {
            instance = new BarnRobot();
        }
        return instance;
    }

    // ------------------------------------------------------------
    // Initialization
    // ------------------------------------------------------------
    public void initBarnRobotSystems(HardwareMap hw, Gamepad gamepad1, Gamepad gamepad2) {
        this.hardwareMap = hw;

        // Initialize Gamepads
        gamepadEx1 = new GamepadEx(gamepad1);
        gamepadEx2 = new GamepadEx(gamepad2);

        // Initialize Subsystems
        transfer = new Transfer();
        drive = new DriveTrain();
    }

    public void initDrivetrain() {
        // This method can be expanded if future drivetrain setup is needed
    }

    public void initTransfer() {
        register(transfer);
    }
}
