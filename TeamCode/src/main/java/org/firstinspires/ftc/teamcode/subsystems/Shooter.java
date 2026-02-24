package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.ShooterPIDFController;

@Config
public class Shooter  extends SubsystemBase {

    public static double p = 1, f = 0.0007;
    private ShooterPIDFController pidfController;
    private DcMotorEx shooterRight;
    private DcMotorEx shooterLeft;

    public boolean isAutoOperated;
    private double distance;

    public static double SHOOTER_VELOCITY_RANGE_4 = 1300; // only for far zone
    public static double SHOOTER_VELOCITY_RANGE_3 = 1150;
    public static double SHOOTER_VELOCITY_RANGE_2 = 1050;
    public static double SHOOTER_VELOCITY_RANGE_1 = 900;


    public static double SHOOTING_RANGE_1 = 1.26;
    public static double SHOOTING_RANGE_2 = 1.9;
    public static double SHOOTING_RANGE_3 = 2.8;

    public static double targetVelocity;


    public Shooter() {
        shooterRight = BarnRobot.getInstance().robotHardware.shooterRight;
        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterRight.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        shooterLeft = BarnRobot.getInstance().robotHardware.shooterLeft;
        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        isAutoOperated = true;

        pidfController = new ShooterPIDFController(p, 0, 0, f);

    }

    private void setPower(double power) {
        shooterRight.setPower(power);
        shooterLeft.setPower(power);
    }

    private void operateShooter(double velocity){
        pidfController.setPIDF(p, 0, 0, f);
        double power = pidfController.calculate(velocity, getVelocity());
        targetVelocity = velocity;
        setPower(power);
    }

    public void operateShooterDistanceBased(double distance) {
        if (!isAutoOperated) distance = this.distance;
        pidfController.setPIDF(p, 0, 0, f);
        if (distance < SHOOTING_RANGE_1) {
            targetVelocity = SHOOTER_VELOCITY_RANGE_1;
        }
        else if (distance > SHOOTING_RANGE_1 && distance < SHOOTING_RANGE_2){
            targetVelocity = SHOOTER_VELOCITY_RANGE_2;
        }
        else if (distance > SHOOTING_RANGE_2 && distance < SHOOTING_RANGE_3){
            targetVelocity = SHOOTER_VELOCITY_RANGE_3;
        }
        else if (distance > SHOOTING_RANGE_3) {
            targetVelocity = SHOOTER_VELOCITY_RANGE_4;
        }
        else {
            targetVelocity = 0;
        }
        setCustomVelocity();
    }
    private void setCustomVelocity(){
        double power = pidfController.calculate(targetVelocity, getVelocity());
        setPower(power);
    }

    private void operateShooterReverse(){
        setPower(-1);
    }


    public RunCommand runShooterFar(){
        return new RunCommand(() -> operateShooter(SHOOTER_VELOCITY_RANGE_4), this);
    }

    public RunCommand runShooterClose(){
        return new RunCommand(() -> operateShooter(SHOOTER_VELOCITY_RANGE_1), this);
    }

    public RunCommand runShooterCustomVelocityDashboard(){
        return new RunCommand(() -> setCustomVelocity(), this);
    }

    public RunCommand runShooterReversed(){
        return new RunCommand(() -> operateShooterReverse(), this);
    }

    public RunCommand turnOff(){
        return new RunCommand(() -> setPower(0), this);
    }

    public InstantCommand turnOffInstant(){
        return new InstantCommand(() -> setPower(0), this);
    }

    public RunCommand runShooter(double velocity){
        return new RunCommand(() -> operateShooter(velocity), this);
    }

    private void shooterSpeedOnDistance(){
        operateShooterDistanceBased(
                BarnRobot.getInstance().limelight.getGoalDistance()
        );
    }

    public boolean isAutoOperated(){
        return isAutoOperated;
    }

    public Command opearteShooter(){
        BarnRobot.getInstance().telemetry.addData("auto shoot", isAutoOperated);
        if(isAutoOperated)
            return runShooterBasedOnDistance();
        else return runShooterBasedOnConstantDistance();
    }

    public Command setShooterManualDistance(double d){
        return new InstantCommand(
                () -> distance = d
        );
    }

    public Command setManualDistance(){
        return new InstantCommand(
                () -> isAutoOperated = false
        );
    }

    public Command setAutoDistance(){
        return new InstantCommand(
                () -> isAutoOperated = true
        );
    }

    public Command runShooterBasedOnDistance(){
        return new RunCommand(() -> shooterSpeedOnDistance(), this);
    }


    public Command runShooterBasedOnConstantDistance(){
        return new RunCommand(() -> operateShooterDistanceBased(
                distance
        ), this);
    }



    public void displayTelemetry(){
        Telemetry telemetry = BarnRobot.getInstance().telemetry;
        telemetry.addData("shooter velocity", shooterRight.getVelocity());
        telemetry.addData("left shooter power", shooterLeft.getPower());
    }

    public double getVelocity() {
        return (shooterRight.getVelocity());
    }
    public double getPower(){
        return shooterRight.getPower();
    }



    public void testMotors(){
        shooterRight.setPower(1);
        shooterLeft.setPower(1);
    }

    public boolean isReady() {
        if (targetVelocity == 0) return false;
        return (getVelocity() > targetVelocity - 40 && getVelocity() < targetVelocity + 40);
    }

    public boolean isReadyCustom(double velocity){
        if (velocity == 0) return false;
        return (getVelocity() > velocity - 40 && getVelocity() < velocity + 40);
    }

    public boolean isShotDetected(double tgtRpm) {
        return getVelocity() < tgtRpm - 60;
    }


    public void setCustomVelocity(double velocity){
        operateShooter(velocity);
    }

    public double getTargetVelocity() {
        return targetVelocity;
    }

}
//