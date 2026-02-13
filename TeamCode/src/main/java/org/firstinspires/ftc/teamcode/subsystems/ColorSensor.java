package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.BarnRobot;



/**
 * ColorSensor subsystem wrapper.
 *
 * <p>
 * This class provides a clean and efficient interface for determining whether
 * an artifact is present at various robot positions using a REV Color Sensor.
 * </p>
 *
 * <p>
 * To reduce CPU load and avoid excessive I2C traffic, distance readings are
 * rate-limited and cached. The physical sensor is queried only once every
 * {@link #COLOR_SENSOR_WAIT_SECONDS} seconds, while callers can safely poll
 * the {@code is*Busy()} methods every loop.
 * </p>
 *
 * <p>
 * If the sensor is not present or does not support distance measurement,
 * this subsystem fails gracefully by reporting no artifact present.
 * </p>
 */
@Config
public class ColorSensor {

    /**
     * Minimum time (in seconds) between physical sensor reads.
     */
    public static double COLOR_SENSOR_WAIT_SECONDS = 0.5;

    /**
     * Distance thresholds (cm) for each robot position.
     */
    public static double SHOOTER_DISTANCE_CM = 7;
    public static double MIDDLE_DISTANCE_CM  = 3.0;
    public static double INTAKE_DISTANCE_CM  = 7.1;

    /**
     * Hardware color sensor instance.
     */
    private final NormalizedColorSensor colorSensor;

    /**
     * Timer used to rate-limit sensor reads.
     */
    private final ElapsedTime sensorTimer = new ElapsedTime();

    /**
     * Cached distance reading (cm).
     */
    private double cachedDistanceCm = Double.POSITIVE_INFINITY;

    /**
     * Creates a new ColorSensor subsystem.
     *
     * @param colorSensor the REV color sensor from the hardware map
     */
    public ColorSensor(NormalizedColorSensor colorSensor) {
        this.colorSensor = colorSensor;

        if (this.colorSensor != null) {
            this.colorSensor.setGain(4);
        }

        sensorTimer.reset();
    }

    /**
     * Reads the physical distance sensor if the rate limit has expired.
     * Otherwise, returns the cached value.
     *
     * @return cached distance to the nearest object (cm)
     */
    private double getArtifactDistanceTimed() {
        if (sensorTimer.seconds() >= COLOR_SENSOR_WAIT_SECONDS) {
            sensorTimer.reset();
            cachedDistanceCm = readDistanceSensor();
        }
        return cachedDistanceCm;
    }

    /**
     * Reads the distance sensor directly.
     *
     * <p>
     * This method should NOT be called repeatedly in a loop.
     * Use {@link #getArtifactDistanceTimed()} instead.
     * </p>
     *
     * @return distance in centimeters, or {@link Double#POSITIVE_INFINITY}
     *         if the sensor is unavailable
     */
    private double readDistanceSensor() {
        if (colorSensor instanceof DistanceSensor) {
            return ((DistanceSensor) colorSensor).getDistance(DistanceUnit.CM);
        }
        return Double.POSITIVE_INFINITY;
    }

    /**
     * Determines whether an artifact is present at the shooter position.
     *
     * @return {@code true} if an artifact is detected
     */
    public boolean isShootPoseBusy() {
        return getArtifactDistanceTimed() < SHOOTER_DISTANCE_CM;
    }public boolean isMidPoseBusy() {
        return getArtifactDistanceTimed() < MIDDLE_DISTANCE_CM;
    }public boolean isIntakePoseBusy() {
        return getArtifactDistanceTimed() < INTAKE_DISTANCE_CM;
    }

    /**
     * Generic artifact presence check with a custom distance threshold.
     *
     * @param distanceCm distance threshold in centimeters
     * @return {@code true} if an artifact is detected
     */
    public boolean isPosBusy(double distanceCm) {
        return getArtifactDistanceTimed() < distanceCm;
    }

    /**
     * Returns the most recently cached distance measurement.
     *
     * @return cached distance in centimeters
     */
    public double getCachedDistanceCm() {
        return cachedDistanceCm;
    }

    public void displayTelemetry(String name){
        if(name.equals("shoot")){
            BarnRobot.getInstance().telemetry.addData("shoot", isShootPoseBusy());

        } else if (name.equals("mid")) {
            BarnRobot.getInstance().telemetry.addData("mid", isMidPoseBusy());

        } else if (name.equals("intake")) {
            BarnRobot.getInstance().telemetry.addData("intake", isIntakePoseBusy());

        }
    }
}
