package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class ServoWheel extends SubsystemBase {
        private Servo wheel;

        private final double MAX = 1;
        private final double MIN = -1;
        public double pos = 0;

        public ServoWheel(HardwareMap hw){
            wheel = hw.get(Servo.class, "servo_wheel");
            wheel.setDirection(Servo.Direction.FORWARD);
        }

        public void turnRight(){
            pos += 0.05;
            wheel.setPosition(pos);
        }

        public void turnLeft(){
            pos -= 0.05;
            wheel.setPosition(pos);
        }

        public void setServoPos(double a){
            wheel.setPosition(a);
        }

        public void turn(double angle){
            if (angle > MAX) angle = MAX;
            if (angle < MIN) angle = MIN;
            wheel.setPosition(angle);
        }

        public Command turnCommand(double ang) {
            return new InstantCommand(() -> turn(ang), this);
        }
}
