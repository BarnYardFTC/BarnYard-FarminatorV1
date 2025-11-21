package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.BarnRobot;

@Config
public class ShooterPIDFController {
    public double kP, kI, kD, kF;

    private double integral = 0;
    private double lastError = 0;
    private double lastTime = 0;

    private final double NOMINAL_VOLTAGE = 13;
    private final ElapsedTime timer = new ElapsedTime();

    public ShooterPIDFController(double kP, double kI, double kD, double kF) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
        timer.reset();
    }

    public void setPIDF(double kP, double kI, double kD, double kF) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
    }

    public void reset() {
        integral = 0;
        lastError = 0;
        timer.reset();
    }

    public double calculate(double target, double current) {

        double dt = timer.seconds();
        if (dt < 0.0001) dt = 0.0001;
        timer.reset();

        double error = target - current;

        // Proportional
        double p = kP * error;

        // Integral (usually keep kI = 0 for flywheels)
        integral += error * dt;
        double i = kI * integral;

        // Derivative
        double derivative = (error - lastError) / dt;
        double d = kD * derivative;

        // Feedforward with voltage compensation
        double voltage = BarnRobot.getInstance().robotHardware.voltageSensor.getVoltage();
        double voltageCompensation = NOMINAL_VOLTAGE / Math.max(voltage, 1e-6);

        double f = kF * target * voltageCompensation;

        lastError = error;

        // Clamp output
        double output = p + i + d + f;

        if (output > 1.0) output = 1.0;
        if (output < 0.0) output = 0.0;

        return output;
    }
}
