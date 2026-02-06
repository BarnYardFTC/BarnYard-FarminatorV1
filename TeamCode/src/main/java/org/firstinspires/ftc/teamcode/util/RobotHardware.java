package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

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

    leftFrontDrivetrain - expansion hub 0 - yellow
    rightFrontDrivetrain - control hub 0 - yellow
    leftBackDrivetrain - expansion hub 1 - blue
    rightBackDrivetrain - control hub 1 - blue
    shooterLeft- expansion hub 2 - white
    shooterRight - control hub 2 - white
    Intake - control hub 3 - white big
    rightGate - servo hub 1
    leftGate - servo hub 2

 */
public class RobotHardware {

    public VoltageSensor voltageSensor;

    public RevBlinkinLedDriver blinkin;


    // ------------------------------------------------------------
    // Arms Servos
    // ------------------------------------------------------------

    public Servo rightGate;
    public Servo leftGate;

    // ------------------------------------------------------------
    // Drivetrain Motors
    // ------------------------------------------------------------

    public DcMotorEx leftFrontDrivetrain;
    public DcMotorEx rightFrontDrivetrain;
    public DcMotorEx rightBackDrivetrain;
    public DcMotorEx leftBackDrivetrain;


    public DcMotorEx transfer;


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
    public NormalizedColorSensor shooterColorSensor;
    public NormalizedColorSensor intakeColorSensor;
    public NormalizedColorSensor midColorSensor;


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

    private static final String RIGHT_GATE_CONFIG_NAME = "rightGate";
    private static final String LEFT_GATE_CONFIG_NAME = "leftGate";

    private static final String TRANSFER_CONFIG_NAME = "transfer";





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

        transfer = hw.get(DcMotorEx.class, "transfer");
    }


    // ------------------------------------------------------------
    // Initialization - Servos
    // ------------------------------------------------------------

    /**
     * Initializes all continuous rotation servos and sets their direction.
     */
    private void initServos() {

        shooterHood = hw.get(Servo.class, SHOOTER_HOOD_CONFIG_NAME);

        rightGate = hw.get(Servo.class, RIGHT_GATE_CONFIG_NAME);
        leftGate =hw.get(Servo.class, LEFT_GATE_CONFIG_NAME);
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
//        limelight = hw.get(Limelight3A.class, "limelight");
//        shooterColorSensor = hw.get(NormalizedColorSensor.class, "colorSensorShooter");
//        intakeColorSensor = hw.get(NormalizedColorSensor.class, "colorSensorIntake");
//        midColorSensor = hw.get(NormalizedColorSensor.class, "colorSensorMid");

    }

    private void initBlinkinLed() {
//        blinkin = hw.get(RevBlinkinLedDriver.class, BLINKIN_CONFIG_NAME);
    }

    private void initVoltageSensor(){
        voltageSensor = hw.get(VoltageSensor.class, "Control Hub");
    }
}
