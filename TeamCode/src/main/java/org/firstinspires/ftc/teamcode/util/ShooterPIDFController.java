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

    public double calculate(double target, double current) {

        // === SDK-style dt calculation ===
        double currentTime = System.nanoTime() * 1e-9;

        if (lastTime == 0) {
            lastTime = currentTime;
        }

        double dt = currentTime - lastTime;
        lastTime = currentTime;

        if (dt < 1e-6) dt = 1e-6;  // avoid division by zero

        // === PID calculations ===
        double error = target - current;

        double p = kP * error;

        integral += error * dt;
        double i = kI * integral;

        double derivative = (error - lastError) / dt;
        double d = kD * derivative;

        // === Feedforward with voltage comp ===
        double voltage = BarnRobot.getInstance().robotHardware.voltageSensor.getVoltage();
        double f = kF * target * (NOMINAL_VOLTAGE / Math.max(voltage, 1e-6));

        lastError = error;

        double output = p + i + d + f;

        // === clamp ===
        if (output > 1) output = 1;
        if (output < 0) output = 0;

        return output;
    }

}
