package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Robot;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.*;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.RobotHardware;
import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.PinpointLocalizer;
import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

/**
 * BarnRobot is the central robot class.
 *
 * It stores and initializes everything related to the robot:
 * - Subsystems (drivetrain, shooter, intake, transfer, limelight)
 * - Gamepads
 * - Telemetry
 * - Hardware and configuration data
 *
 * This class should only have one instance (singleton).
 * Every OpMode gets access to it using {@link #getInstance()} and calls {@link #init(OpMode, OpModeData)}.
 */
public class BarnRobot extends Robot {

    // ------------------------------------------------------------
    // Singleton Instance
    // ------------------------------------------------------------

    private static BarnRobot instance;


    // ------------------------------------------------------------
    // Subsystems
    // ------------------------------------------------------------

    public Transfer transfer;
    public DriveTrain drive; // used in teleop
    public RoadRunnerMecanumDrive roadRunnerMecanumDrive; // used in auto
    public LimeLight limelight;
    public Shooter shooter;
    public ShooterHood shooterHood;
    public Intake intake;
    public Webcam webcam;
    public ColorSensor colorSensor;
    public BlinkinLED blinkin;
    public PinpointLocalizer pinpointLocalizer;
    public Arms arms;




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

    public RobotHardware robotHardware;


    // ------------------------------------------------------------
    // Robot Data
    // ------------------------------------------------------------

    public OpModeData opmodeData;
    public static boolean isRobotInitialized = false;


    // ------------------------------------------------------------
    // Singleton Accessor
    // ------------------------------------------------------------

    /**
     * Returns the single BarnRobot instance.
     * If it doesn’t exist yet, creates it.
     */
    public static synchronized BarnRobot getInstance() {
        if (instance == null) {
            instance = new BarnRobot();
            isRobotInitialized = true;
        }
        return instance;
    }


    // ------------------------------------------------------------
    // Initialization
    // ------------------------------------------------------------

    /**
     * Initializes all robot systems, including:
     * - Hardware
     * - Gamepads
     * - Telemetry
     * - Subsystems
     *
     * This method should be called once at the start of each OpMode.
     *
     * @param opMode     the active OpMode
     * @param opModeData contains config info like alliance color and heading offset
     */
    public void init(OpMode opMode, OpModeData opModeData) {
        // Store mode data (must be first)
        this.opmodeData = opModeData;

        // Hardware and telemetry setup
        this.robotHardware = new RobotHardware(opMode.hardwareMap);
        this.telemetry = opMode.telemetry;

        // Gamepad setup
        gamepadEx1 = new GamepadEx(opMode.gamepad1);
        gamepadEx2 = new GamepadEx(opMode.gamepad2);

        // Subsystem initialization
        initTransfer();
        initLimeLight();
        initShooter();
        initIntake();
        initDrivetrain(opMode.hardwareMap);
        initShooterHood();
        initWebcam(opMode.hardwareMap);
        initPinpointLocalizer(opMode.hardwareMap);
        initBlinkin();
        initArms();
    }


    // ------------------------------------------------------------
    // Subsystem Initializers
    // ------------------------------------------------------------

    /** Sets up the webcam. */
    public void initWebcam(HardwareMap hw){
//        webcam = new Webcam(hw);
    }

    /** Sets up the shooter system. */
    public void initShooter() {
        shooter = new Shooter();
        shooter.setDefaultCommand(shooter.turnOff());
    }

    /**
     * Sets up the drivetrain, registers it in the command framework,
     * and sets its default driving command.
     */
    public void initDrivetrain(HardwareMap hw) {
         initDrivetrainTeleop();
         initDrivetrainAutonomous(hw);
    }

    /**
     * Init drivetrain teleop
     */
    private void initDrivetrainTeleop(){
        drive = new DriveTrain();
    }

    /**
     * init
     * @param hw hardwareMap
     */
    private void initDrivetrainAutonomous(HardwareMap hw){
        roadRunnerMecanumDrive = new RoadRunnerMecanumDrive(hw, opmodeData.initialPose2d);
        drive = new DriveTrain();
    }

    private void initPinpointLocalizer(HardwareMap hw){
        pinpointLocalizer = new PinpointLocalizer(hw, RoadRunnerMecanumDrive.PARAMS.inPerTick, opmodeData.initialPose2d);
    }

    public void initShooterHood(){
        shooterHood = new ShooterHood();
        shooterHood.setDefaultCommand(shooterHood.autoHoodAlignment());
    }

    public void initArms(){
        arms = new Arms();
    }

    /** Sets up the transfer system. */
    public void initTransfer() {
        transfer = new Transfer();
    }

    /** Sets up the LimeLight vision system. */
    public void initLimeLight() {
        limelight = new LimeLight();
    }

    /** Sets up the intake system. */
    public void initIntake() {
        intake = new Intake();
    }

    public void initBlinkin(){
        blinkin = new BlinkinLED();
    }
    public void initColorSensor(){colorSensor = new ColorSensor();}


    // ------------------------------------------------------------
    // Periodic Loop
    // ------------------------------------------------------------

    /**
     * Runs code that should update every loop, across all modes.
     * For now, it just updates telemetry, but more shared logic can go here.
     */
    public void periodic() {
        pinpointLocalizer.update();
        telemetry.update();
    }
}
