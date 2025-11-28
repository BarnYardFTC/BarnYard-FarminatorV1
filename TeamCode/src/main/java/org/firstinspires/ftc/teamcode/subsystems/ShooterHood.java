package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.BarnRobot;

public class ShooterHood extends SubsystemBase {
    private Servo servo;
    private final double MIN = 0;
    private final double MAX = 1;

    private final double A = 0.1;
    private final double B = 0.5;
    private final double C = 10;


    public ShooterHood(){
        servo = BarnRobot.getInstance().robotHardware.shooterHood;
        servo.setDirection(Servo.Direction.REVERSE);    // Change if needed
        servo.scaleRange(MIN,MAX);
        servo.setPosition(MAX);
    }

    public double rangeDependentAngle(double range) {
        return Math.sqrt(A*range) + B*range + C;
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
