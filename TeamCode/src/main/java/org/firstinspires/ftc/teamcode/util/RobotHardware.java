package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

public class RobotHardware {

    public CRServo leftFrontTransfer;
    public CRServo rightFrontTransfer;
    public CRServo leftBackTransfer;
    public CRServo rightBackTransfer;

    public IMU imu;

//    private RevCuttleHub ctrlHub;
//    private RevCuttleHub expHub;

    public DcMotorEx leftFrontDrivetrain;
    public DcMotorEx rightFrontDrivetrain;
    public DcMotorEx rightBackDrivetrain;
    public DcMotorEx leftBackDrivetrain;

    public DcMotorEx shooter;

    public DcMotorEx intake;

    public Limelight3A limelight;

    private HardwareMap hw;

    //TODO: set the correct port numbers when implementing cuttleFTCBridge
    private static final int LEFT_FRONT_DRIVETRAIN_PORT = 3;
    private static final int LEFT_BACK_DRIVETRAIN_PORT = 2;

    private static final int RIGHT_FRONT_DRIVETRAIN_PORT = 0;
    private static final int RIGHT_BACK_DRIVETRAIN_PORT = 1;

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

    private static final String LEFT_FRONT_DRIVETRAIN_CONFIG_NAME = "leftFrontDrivetrain";
    private static final String RIGHT_FRONT_DRIVETRAIN_CONFIG_NAME = "rightFrontDrivetrain";
    private static final String LEFT_BACK_DRIVETRAIN_CONFIG_NAME = "leftBackDrivetrain";
    private static final String RIGHT_BACK_DRIVETRAIN_CONFIG_NAME = "rightBackDrivetrain";

    private static final String SHOOTER_CONFIG_NAME = "shooter";
    private static final String INTAKE_CONFIG_NAME = "intake";

    public final IMU.Parameters IMU_PARAMETERS = new IMU.Parameters(
            new RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                    RevHubOrientationOnRobot.UsbFacingDirection.UP
            )
    );

    public RobotHardware(HardwareMap hw){
        this.hw = hw;
        initMotors();
        initServos();
        initSensors();
    }


    private void periodic(){
//        ctrlHub.pullBackData();
//        expHub.pullBackData();
    }

    private void initHubs(){
//        ctrlHub = new CuttleRevHub(hw,CuttleRevHub.HubTypes.CONTROL_HUB);
//        expHub = new CuttleRevHub(hw,"Expansion Hub 1");
    }

    private void initMotors(){
        leftFrontDrivetrain  = hw.get(DcMotorEx.class, LEFT_FRONT_DRIVETRAIN_CONFIG_NAME);
        leftBackDrivetrain = hw.get(DcMotorEx.class, LEFT_BACK_DRIVETRAIN_CONFIG_NAME);
        rightFrontDrivetrain = hw.get(DcMotorEx.class, RIGHT_FRONT_DRIVETRAIN_CONFIG_NAME);
        rightBackDrivetrain = hw.get(DcMotorEx.class, RIGHT_BACK_DRIVETRAIN_CONFIG_NAME);

        shooter = hw.get(DcMotorEx.class, SHOOTER_CONFIG_NAME);

        intake = hw.get(DcMotorEx.class, INTAKE_CONFIG_NAME);
    }

    private void initServos(){
        leftFrontTransfer = hw.get(CRServo.class, LEFT_FRONT_TRANSFER_CONFIG_NAME);
        rightFrontTransfer = hw.get(CRServo.class, RIGHT_FRONT_TRANSFER_CONFIG_NAME);
        leftBackTransfer = hw.get(CRServo.class, LEFT_BACK_TRANSFER_CONFIG_NAME);
        rightBackTransfer = hw.get(CRServo.class, RIGHT_BACK_TRANSFER_CONFIG_NAME);
    }

    private void initSensors(){
        imu = hw.get(IMU.class, "imu");
        limelight = hw.get(Limelight3A.class, "limelight");
    }

}