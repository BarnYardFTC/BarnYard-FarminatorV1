package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.subsystems.Shooter.CLOSE_SHOOTING_RANGE;
import static org.firstinspires.ftc.teamcode.subsystems.Shooter.FAR_SHOOTING_RANGE;
import static org.firstinspires.ftc.teamcode.subsystems.Shooter.SHOOTER_VELOCITY_CLOSE;

import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.BarnRobot;

public class ShooterHood extends SubsystemBase {
    private Servo servo;
    private final double MIN = 0;
    private final double MAX = 1;

    private final double A_CLOSE = 0.1;
    private final double B_CLOSE = 0.5;
    private final double C_CLOSE = 10;

    private final double A_MID = 0.1;
    private final double B_MID = 0.5;
    private final double C_MID = 10;


    public ShooterHood(){
        servo = BarnRobot.getInstance().robotHardware.shooterHood;
        servo.setDirection(Servo.Direction.REVERSE);    // Change if needed
        servo.scaleRange(MIN,MAX);
        servo.setPosition(MAX);
    }

    public void distanceDependentAngleClose(double distance) {
        double position = -17.196*Math.pow(distance,6) + 97.619*Math.pow(distance,5) - 220.89*Math.pow(distance,4)
                + 253.55*Math.pow(distance,3) - 154.62*Math.pow(distance,2) + 47.505*distance - 5.4667;

        BarnRobot.getInstance().telemetry.addData("position", position);
        servo.setPosition(position);
    }

    public double distanceDependentAngleMid(double distance) {
        return Math.sqrt(A_MID*distance) + B_MID*distance + C_MID;
    }

    public double distanceDependentAngleFar(double distance) {
        return Math.sqrt(A_MID*distance) + B_MID*distance + C_MID;
    }

    public void tempTestRangeDependent() {
//        servo.setPosition(Math.min(BarnRobot.getInstance().limelight.getGoalRange()/4, 1));
    }

//    public Command autoAdjust(){
//        return new InstantCommand(() -> {
//            double newPos = tempTestFormula(BarnRobot.getInstance().limelight.getGoalRange());
//            servo.setPosition(newPos);
//        }, this);
////        servo.setPosition(tempTestFormula(BarnRobot.getInstance().limelight.getGoalRange()));
////        return new InstantCommand(()  -> servo.setPosition(rangeDependentAngle(BarnRobot.getInstance().limelight.getGoalRange())));
//    }

//    public Command moveWithFormula(double position){
//        return new InstantCommand(()  -> servo.setPosition(tempTestFormula(position)));
//    }

    public Command autoHoodAlignment(double distance){
        if (distance < CLOSE_SHOOTING_RANGE) {
            return new RunCommand(() -> BarnRobot.getInstance().telemetry.addLine("as"), this);
        }
        else if (distance > CLOSE_SHOOTING_RANGE && distance < FAR_SHOOTING_RANGE){
            return new RunCommand(() -> distanceDependentAngleMid(distance), this);
        }
        else {
            return new RunCommand(() -> distanceDependentAngleClose(distance), this);
        }
    }

    public Command lower() {
        return new InstantCommand(() -> {
            double newPos = servo.getPosition() + 0.1;
            if (newPos <= MAX) {
                servo.setPosition(newPos);
            }
        }, this);
    }

    public Command raise() {
        return new InstantCommand(() -> {
            double newPos = servo.getPosition() - 0.1;
            servo.setPosition(newPos);
            if (servo.getPosition() > MIN && servo.getPosition() < 0.1) {
                servo.setPosition(MIN);
            }
        }, this);
    }

    public Command setHoodPosition(double position){
        return new InstantCommand(() ->
            servo.setPosition(position), this);
    }

    public Command returnToBase(){
        return new InstantCommand(() -> {
            servo.setPosition(MIN);
            },this
        );
    }

    public void displayTelemetry(){
        BarnRobot robot = BarnRobot.getInstance();
        robot.telemetry.addData("Position", servo.getPosition());
    }
}
