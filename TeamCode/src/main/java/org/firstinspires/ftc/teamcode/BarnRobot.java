package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import com.seattlesolvers.solverslib.command.Robot;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.RobotHardware;
import org.firstinspires.ftc.teamcode.util.roadrunner.MecanumDrive;

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
    public MecanumDrive autoDrive;

    // ------------------------------------------------------------
    // Gamepads
    // ------------------------------------------------------------
    public GamepadEx gamepadEx1;
    public GamepadEx gamepadEx2;

    // ------------------------------------------------------------
    // Telemetry
    // ------------------------------------------------------------
    public Telemetry telemetry;

    // ------------------------------------------------------------
    // Robot Hardware
    // ------------------------------------------------------------

    public RobotHardware farminatorHardware;

    // ------------------------------------------------------------
    // Robot Data
    // ------------------------------------------------------------

    public OpModeData opmodeData;

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
    public void initBarnRobotSystemsTeleop(OpMode opMode, OpModeData opModeData) {
        opmodeData = opModeData; //has to be first thing in this function

        farminatorHardware = new RobotHardware(opMode.hardwareMap);

        this.telemetry = opMode.telemetry;

        // Initialize Gamepads
        gamepadEx1 = new GamepadEx(opMode.gamepad1);
        gamepadEx2 = new GamepadEx(opMode.gamepad2);

        // Initialize Subsystems
        initTransfer();
        initDrivetrain();
        initLimeLight();
    }

    public void initBarnRobotSystemsAuto(OpMode opMode, OpModeData opModeData) {
        opmodeData = opModeData; //has to be first thing in this function

        farminatorHardware = new RobotHardware(opMode.hardwareMap);

        this.telemetry = opMode.telemetry;

        // Initialize Gamepads
        gamepadEx1 = new GamepadEx(opMode.gamepad1);
        gamepadEx2 = new GamepadEx(opMode.gamepad2);

        // Initialize Subsystems
        initTransfer();
        initAutoDrive(opModeData.startPose, opMode.hardwareMap);
        initLimeLight();
    }

    public void initDrivetrain() {
        drive = new DriveTrain();
        drive.setDefaultCommand(drive.driveCommand());
    }

    public void initAutoDrive(Pose2d startPose, HardwareMap hardwareMap){
        autoDrive = new MecanumDrive(hardwareMap, startPose);
    }

    public void initTransfer() {
        transfer = new Transfer();
    }

    public void initLimeLight() {
        limelight = new LimeLight();
    }

    public void periodic(){
        //code that always needs to run in the while loop
        telemetry.update();
    }
}
