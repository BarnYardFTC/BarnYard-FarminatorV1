package org.firstinspires.ftc.teamcode;




import com.qualcomm.robotcore.hardware.HardwareMap;
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

    public void initBarnRobotSystems(HardwareMap hw, GamepadEx gamepad1, GamepadEx gamepad2) {
        gamepadEx1 = gamepad1;
        gamepadEx2 = gamepad2;

//        claw = new ClawTest();
//        arm = new ArmTest();
//        transfer = new Transfer();
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
