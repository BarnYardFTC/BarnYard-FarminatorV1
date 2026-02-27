package org.firstinspires.ftc.teamcode.subsystems;

public class Ballistics {
    private static final double G = 981.0;            // gravity in cm/s^2
    private static final double WHEEL_DIAMETER = 9.6; // cm

    public static double[] optimalShot(double xCm, double yCm) {

        double R = Math.sqrt(xCm * xCm + yCm * yCm);

        double phi = Math.atan((yCm + R) / xCm);

        double v = Math.sqrt(G * (R + yCm));

        double rpm = (60.0 * v) / (Math.PI * WHEEL_DIAMETER);

        return new double[]{Math.toDegrees(phi), rpm};

    }
}
