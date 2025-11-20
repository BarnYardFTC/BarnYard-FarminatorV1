package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.BarnRobot;

@Config
public class ShooterPIDFController {
    public static double kP, kI, kD, kF;
    private double dt = 0.1;
    private double integral = 0;
    private double lastError = 0;

    private final double NOMINAL_VOLTAGE = 13;

    public ShooterPIDFController(double kP, double kI, double kD, double kF) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
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
    }

    /**
     * Calculates PIDF output.
     * @param target desired value (setpoint)
     * @param current measured value
     * @return control output (e.g. motor power)
     */
    public double calculate(double target, double current) {
        double error = target - current;

        // Proportional
        double p = kP * error;

        // Integral
        integral += error * dt;
        double i = kI * integral;

        // Derivative
        double derivative = (error - lastError) / dt;
        double d = kD * derivative;

        // Feedforward adjusted by battery level
        // Higher voltage -> reduce feedforward; lower voltage -> increase
        double voltageCompensation = NOMINAL_VOLTAGE / Math.max(BarnRobot.getInstance().robotHardware.voltageSensor.getVoltage(), 1e-6);
        double f = kF * target * voltageCompensation;

        lastError = error;

        return p + i + d + f;
    }

}

