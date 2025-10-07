package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Robot;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.util.GlobalData;

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
    public LimeLight limelight;

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

    public GlobalData.Alliance teamColor;
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
    public void initBarnRobotSystems(HardwareMap hw, Gamepad gamepad1, Gamepad gamepad2, GlobalData.Alliance teamColor) {
        this.hardwareMap = hw;
        this.teamColor = teamColor;

        // Initialize Gamepads
        gamepadEx1 = new GamepadEx(gamepad1);
        gamepadEx2 = new GamepadEx(gamepad2);

        // Initialize Subsystems
        initTransfer();
        initDrivetrain();
        initLimeLight();
    }

    public void initDrivetrain() {
        drive = new DriveTrain();
        drive.setDefaultCommand(drive.driveCommand());
    }

    public void initTransfer() {
        transfer = new Transfer();
    }

    public void initLimeLight() {
        limelight = new LimeLight();
        limelight.start();
    }
}
