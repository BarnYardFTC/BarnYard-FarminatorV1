package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.subsystems.BlinkinLED;

/**
 * RobotHardware handles all the low-level hardware setup for the robot.
 *
 * It connects each motor, servo, and sensor to the hardware map names configured in the FTC Driver Station.
 * This class is the single source of truth for:
 * - Hardware names and ports
 * - Device initialization
 * - Motor and servo directions
 * - IMU and vision configuration
 *
 * Every subsystem gets its devices from here.
 */


/*
Configuration:

    Exapnsion hub motors:
        0: intake
        1: leftFrontDrivetrain
        2: leftBackDrivetrain
        3: shoterLeft

    Control hub motors:
        0: rightBackDrivetrain
        1: shooterRight
        2: rightFrontDrivetrain
        3:

    Control hub i2c:
        1: pinpoint

    Servo Hub:
        0:  leftBackTransfer
        1: leftFrontTransfer
        2: shooterHood
        4: rightBackTransfer
        5:  rightFrontTransfer

    Webcam:
        Webcam 1

    Limelight
        limelight
 */
public class RobotHardware {

    public VoltageSensor voltageSensor;

    public RevBlinkinLedDriver blinkin;

    // ------------------------------------------------------------
    // Transfer Servos
    // ------------------------------------------------------------

    public CRServo leftFrontTransfer;
    public CRServo rightFrontTransfer;
    public CRServo leftBackTransfer;
    public CRServo rightBackTransfer;


    // ------------------------------------------------------------
    // Drivetrain Motors
    // ------------------------------------------------------------

    public DcMotorEx leftFrontDrivetrain;
    public DcMotorEx rightFrontDrivetrain;
    public DcMotorEx rightBackDrivetrain;
    public DcMotorEx leftBackDrivetrain;


    // ------------------------------------------------------------
    // Other Motors/Servos
    // ------------------------------------------------------------

    public DcMotorEx shooterRight;
    public DcMotorEx shooterLeft;
    public DcMotorEx intake;
    public Servo shooterHood;


    // ------------------------------------------------------------
    // Sensors
    // ------------------------------------------------------------

//    public IMU imu;
    public Limelight3A limelight;


    // ------------------------------------------------------------
    // HardwareMap Reference
    // ------------------------------------------------------------

    private HardwareMap hw;

    // ------------------------------------------------------------
    // Configuration Names (match those in the Control Hub config)
    // ------------------------------------------------------------

    private static final String LEFT_FRONT_TRANSFER_CONFIG_NAME =  "leftFrontTransfer";
    private static final String RIGHT_FRONT_TRANSFER_CONFIG_NAME = "rightFrontTransfer";
    private static final String LEFT_BACK_TRANSFER_CONFIG_NAME =   "leftBackTransfer";
    private static final String RIGHT_BACK_TRANSFER_CONFIG_NAME =  "rightBackTransfer";

    private static final String LEFT_FRONT_DRIVETRAIN_CONFIG_NAME =  "leftFrontDrivetrain";
    private static final String RIGHT_FRONT_DRIVETRAIN_CONFIG_NAME = "rightFrontDrivetrain";
    private static final String LEFT_BACK_DRIVETRAIN_CONFIG_NAME =   "leftBackDrivetrain";
    private static final String RIGHT_BACK_DRIVETRAIN_CONFIG_NAME =  "rightBackDrivetrain";

    private static final String SHOOTER_RIGHT_CONFIG_NAME = "shooterRight";
    private static final String INTAKE_CONFIG_NAME = "intake";
    private static final String SHOOTER_LEFT_CONFIG_NAME = "shooterLeft";

    private static final String SHOOTER_HOOD_CONFIG_NAME = "shooterHood";

    private static final String BLINKIN_CONFIG_NAME = "blinkin";



    // ------------------------------------------------------------
    // IMU Parameters
    // ------------------------------------------------------------

    /** Default IMU orientation settings for the control hub placement. */
//    public final IMU.Parameters IMU_PARAMETERS = new IMU.Parameters(
//            new RevHubOrientationOnRobot(
//                    RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
//                    RevHubOrientationOnRobot.UsbFacingDirection.UP
//            )
//    );


    // ------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------

    /**
     * Creates a new RobotHardware instance and initializes all devices.
     *
     * @param hw the hardware map provided by the OpMode
     */
    public RobotHardware(HardwareMap hw) {
        this.hw = hw;
        initMotors();
        initServos();
        initSensors();
        initVoltageSensor();
        initBlinkinLed();
        shooterHood = hw.get(Servo.class, "shooterHood");
    }


    // ------------------------------------------------------------
    // Periodic (optional, for hub data polling)
    // ------------------------------------------------------------

    /**
     * Runs periodically if we ever need to manually pull data from hubs.
     * Currently not used.
     */
    private void periodic() {
        // ctrlHub.pullBackData();
        // expHub.pullBackData();
    }


    // ------------------------------------------------------------
    // (Optional) Hub Initialization
    // ------------------------------------------------------------

    /**
     * Example method for initializing Control/Expansion hubs, kept for future expansion.
     */
    private void initHubs() {

        // ctrlHub = new CuttleRevHub(hw, CuttleRevHub.HubTypes.CONTROL_HUB);
        // expHub = new CuttleRevHub(hw, "Expansion Hub 1");
    }


    // ------------------------------------------------------------
    // Initialization - Motors
    // ------------------------------------------------------------

    /**
     * Initializes all DC motors, sets their directions where needed,
     * and gets them from the hardware map.
     */
    private void initMotors() {
        leftFrontDrivetrain  = hw.get(DcMotorEx.class, LEFT_FRONT_DRIVETRAIN_CONFIG_NAME);
        leftBackDrivetrain   = hw.get(DcMotorEx.class, LEFT_BACK_DRIVETRAIN_CONFIG_NAME);
        rightFrontDrivetrain = hw.get(DcMotorEx.class, RIGHT_FRONT_DRIVETRAIN_CONFIG_NAME);
        rightBackDrivetrain  = hw.get(DcMotorEx.class, RIGHT_BACK_DRIVETRAIN_CONFIG_NAME);

        shooterRight = hw.get(DcMotorEx.class, SHOOTER_RIGHT_CONFIG_NAME);
        shooterLeft = hw.get(DcMotorEx.class, SHOOTER_LEFT_CONFIG_NAME);

        intake = hw.get(DcMotorEx.class, INTAKE_CONFIG_NAME);
    }


    // ------------------------------------------------------------
    // Initialization - Servos
    // ------------------------------------------------------------

    /**
     * Initializes all continuous rotation servos and sets their direction.
     */
    private void initServos() {
        leftFrontTransfer  = hw.get(CRServo.class, LEFT_FRONT_TRANSFER_CONFIG_NAME);
        rightFrontTransfer = hw.get(CRServo.class, RIGHT_FRONT_TRANSFER_CONFIG_NAME);
        leftBackTransfer   = hw.get(CRServo.class, LEFT_BACK_TRANSFER_CONFIG_NAME);
        rightBackTransfer  = hw.get(CRServo.class, RIGHT_BACK_TRANSFER_CONFIG_NAME);

        leftFrontTransfer.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBackTransfer.setDirection(DcMotorSimple.Direction.REVERSE);

        shooterHood = hw.get(Servo.class, SHOOTER_HOOD_CONFIG_NAME);
    }


    // ------------------------------------------------------------
    // Initialization - Sensors
    // ------------------------------------------------------------

    /**
     * Initializes the IMU and Limelight sensors.
     */
    private void initSensors() {
//        imu.initialize(IMU_PARAMETERS);
//        imu.resetYaw();
        limelight = hw.get(Limelight3A.class, "limelight");

    }

    private void initBlinkinLed() {
        blinkin = hw.get(RevBlinkinLedDriver.class, BLINKIN_CONFIG_NAME);
    }

    private void initVoltageSensor(){
        voltageSensor = hw.get(VoltageSensor.class, "Control Hub");
    }
}
