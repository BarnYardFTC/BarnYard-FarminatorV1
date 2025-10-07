package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.roboctopi.cuttlefishftcbridge.devices.CuttleMotor;
import com.roboctopi.cuttlefishftcbridge.devices.CuttleRevHub;

public class RobotHardware {

    public CRServo leftFrontTransfer;
    public CRServo rightFrontTransfer;
    public CRServo leftBackTransfer;
    public CRServo rightBackTransfer;

    public IMU imu;

    public CuttleRevHub ctrlHub;
    public CuttleRevHub expHub;

    public CuttleMotor leftFrontDrivetrain;
    public CuttleMotor rightFrontDrivetrain;
    public CuttleMotor rightBackDrivetrain;
    public CuttleMotor leftBackDrivetrain;

    public CuttleMotor shooter;

    public CuttleMotor intake;

    private HardwareMap hw;

    private static final int LEFT_FRONT_DRIVETRAIN_PORT = 3;
    private static final int LEFT_BACK_DRIVETRAIN_PORT = 2;

    private static final int RIGHT_FRONT_DRIVETRAIN_PORT = 0;
    private static final int RIGHT_BACK_DRIVETRAIN_PORT = 1;

    //TODO: set the port numbers for the hardware devices with -1
    private static final int SHOOTER_PORT = -1;

    private static final int INTAKE_PORT = -1;

    /*
    Written in here just so we can remember the ports. No usage.
            LEFT_FRONT_TRANSFER_PORT = -1;
            RIGHT_FRONT_TRANSFER_PORT = -1;
            LEFT_BACK_TRANSFER_PORT = -1;
            RIGHT_BACK_TRANSFER_PORT = -1;
     */

    private static final String LEFT_FRONT_TRANSFER_CONFIG_NAME = "leftFrontTransfer";
    private static final String RIGHT_FRONT_TRANSFER_CONFIG_NAME = "rightFrontTransfer";
    private static final String LEFT_BACK_TRANSFER_CONFIG_NAME = "leftBackTransfer";
    private static final String RIGHT_BACK_TRANSFER_CONFIG_NAME = "rightBackTransfer";

    public static final IMU.Parameters IMU_PARAMETERS = new IMU.Parameters(
            new RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                    RevHubOrientationOnRobot.UsbFacingDirection.UP
            )
    );

    public RobotHardware(HardwareMap hw){
        this.hw = hw;
        initHubs();
        initMotors();
        initServos();
        initSensors();
    }

    public void periodic(){
        expHub.pullBulkData();
        ctrlHub.pullBulkData();
    }


    private void initHubs(){
        ctrlHub = new CuttleRevHub(hw,CuttleRevHub.HubTypes.CONTROL_HUB);
        expHub = new CuttleRevHub(hw,"Expansion Hub 1");
    }

    private void initMotors(){
        leftFrontDrivetrain  = expHub.getMotor(LEFT_FRONT_DRIVETRAIN_PORT);
        leftBackDrivetrain = expHub.getMotor(LEFT_BACK_DRIVETRAIN_PORT);

        rightFrontDrivetrain = ctrlHub.getMotor(RIGHT_FRONT_DRIVETRAIN_PORT);
        rightBackDrivetrain = ctrlHub.getMotor(RIGHT_BACK_DRIVETRAIN_PORT);

        // TODO: change if connected to expansion hub
        shooter = ctrlHub.getMotor(SHOOTER_PORT);

        // TODO: change if connected to expansion hub
        intake = ctrlHub.getMotor(INTAKE_PORT);
    }

    private void initServos(){
        leftFrontTransfer = hw.get(CRServo.class, LEFT_FRONT_TRANSFER_CONFIG_NAME);
        rightFrontTransfer = hw.get(CRServo.class, RIGHT_FRONT_TRANSFER_CONFIG_NAME);
        leftBackTransfer = hw.get(CRServo.class, LEFT_BACK_TRANSFER_CONFIG_NAME);
        rightBackTransfer = hw.get(CRServo.class, RIGHT_BACK_TRANSFER_CONFIG_NAME);
    }

    private void initSensors(){
        imu = hw.get(IMU.class, "imu");
    }

}