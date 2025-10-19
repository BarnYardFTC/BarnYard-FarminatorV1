package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import org.firstinspires.ftc.teamcode.BarnRobot;

@Config
public class Shooter  extends SubsystemBase {
    private DcMotorEx shooter;
    public static  double DEFAULT_POWER = 1;

    public Shooter() {
        shooter = BarnRobot.getInstance().farminatorHardware.shooter;
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooter.setDirection(DcMotorSimple.Direction.FORWARD);
        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }


    //ToDo: Coordinate power with distance. Use limelight

    private void setPower(double power) {
        shooter.setPower(power);
    }

//    public void startShooter(){
//        setPower(1);
//    }
//
//    public void stopShooter(){
//        setPower(0);
//    }

    public Command customShooterCommand(double power) {
        return new InstantCommand(() -> setPower(power), this);
    }

    public Command activateShooterCommand() {
        return new InstantCommand(() -> setPower(DEFAULT_POWER), this);
    }

    public Command deactivateShooterCommand(){
        return new InstantCommand(() -> setPower(0), this);
    }



}
