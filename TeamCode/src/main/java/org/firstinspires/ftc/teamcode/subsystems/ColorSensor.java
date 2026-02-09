package org.firstinspires.ftc.teamcode.subsystems;

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
public class ColorSensor {

    /**
     * Minimum time (in seconds) between physical sensor reads.
     */
    public static double COLOR_SENSOR_WAIT_SECONDS = 0.5;

    /**
     * Distance thresholds (cm) for each robot position.
     */
    private static final double SHOOTER_DISTANCE_CM = 6.5;
    private static final double MIDDLE_DISTANCE_CM  = 3.0;
    private static final double INTAKE_DISTANCE_CM  = 6.2;

    /**
     * Hardware color sensor instance.
     */
    private final NormalizedColorSensor shooterSensor;
    private final NormalizedColorSensor midSensor;
    private final NormalizedColorSensor intakeSensor;

    /**
     * Timer used to rate-limit sensor reads.
     */
    private final ElapsedTime shooterTimer = new ElapsedTime();
    private final ElapsedTime midTimer = new ElapsedTime();
    private final ElapsedTime intakeTimer = new ElapsedTime();

    /**
     * Cached distance reading (cm).
     */
    private double cachedShooterDistanceCm = Double.POSITIVE_INFINITY;
    private double cachedMidDistanceCm = Double.POSITIVE_INFINITY;
    private double cachedIntakeDistanceCm = Double.POSITIVE_INFINITY;


    public ColorSensor() {
        this.shooterSensor = BarnRobot.getInstance().robotHardware.shooterColorSensor;
        this.midSensor = BarnRobot.getInstance().robotHardware.midColorSensor;
        this.intakeSensor = BarnRobot.getInstance().robotHardware.intakeColorSensor;

        if (this.shooterSensor != null && this.midSensor != null && this.intakeSensor != null) {
            this.shooterSensor.setGain(4);
            this.midSensor.setGain(4);
            this.intakeSensor.setGain(4);
        }

        shooterTimer.reset();
        midTimer.reset();
        intakeTimer.reset();
    }


    /**
     * Reads the physical distance sensor if the rate limit has expired.
     * Otherwise, returns the cached value.
     *  <p>
     *  This method takes number from 1 to 3 to select which color sensor use
     *  <p>
     *  1 - ShooterColorSensor
     *  <p>
     *  2 - MidColorSensor
     *  <p>
     *  3 - IntakeColorSensor
     *
     * @return cached distance to the nearest object (cm)
     */
    private double getArtifactDistanceTimed(int variant){
        switch (variant){
            case 1:
                if (shooterTimer.seconds() >= COLOR_SENSOR_WAIT_SECONDS) {
                    shooterTimer.reset();
                    cachedShooterDistanceCm = readDistanceSensor(this.shooterSensor);
                }
                return cachedShooterDistanceCm;

            case 2:
                if (midTimer.seconds() >= COLOR_SENSOR_WAIT_SECONDS) {
                    midTimer.reset();
                    cachedMidDistanceCm = readDistanceSensor(this.midSensor);
                }
                return cachedMidDistanceCm;

            case 3:
                if (intakeTimer.seconds() >= COLOR_SENSOR_WAIT_SECONDS) {
                    intakeTimer.reset();
                    cachedIntakeDistanceCm = readDistanceSensor(this.intakeSensor);
                }
                return cachedIntakeDistanceCm;
        }

        return -1;
    }


    /**
     * Reads the distance sensor directly.
     *
     * <p>
     * This method should NOT be called repeatedly in a loop.
     * Use {@link #getArtifactDistanceTimed(int)} instead.
     * </p>
     *
     *  This method takes number from 1 to 3 to select which color sensor use
     *  <p>
     *  1 - ShooterColorSensor
     *  <p>
     *  2 - MidColorSensor
     *  <p>
     *  3 - IntakeColorSensor
     *
     * @return distance in centimeters, or {@link Double#POSITIVE_INFINITY}
     *         if the sensor is unavailable
     */
    private double readDistanceSensor(NormalizedColorSensor sensor) {
        if (sensor instanceof DistanceSensor) {
            return ((DistanceSensor) sensor).getDistance(DistanceUnit.CM);
        }

        return Double.POSITIVE_INFINITY;
    }

    /**
     * Determines whether an artifact is present at the shooter position.
     *  <p>
     *
     *      For shooter sensor - 1
     *  </p>
     * @return {@code true} if an artifact is detected
     */
    public boolean isOnlyShootPosBusy() {
        return isShootPosBusy() && !isMidPosBusy() && !isIntakePosBusy();
    }

    public boolean isShootPosBusy(){
        return getArtifactDistanceTimed(1) < SHOOTER_DISTANCE_CM;
    }

    public boolean isOnlyMidPosBusy(){
        return isMidPosBusy() && ! isShootPosBusy() && !isIntakePosBusy();
    }

    public boolean isOnlyIntakePosBusy(){
        return isIntakePosBusy() && !isOnlyShootPosBusy() && !isMidPosBusy();
    }

    /**
     * Determines whether an artifact is present at the middle position.
     *<p>
     *     For mid sensor - 2
     *</p>
     * @return {@code true} if an artifact is detected
     */
    public boolean isMidPosBusy() {
        return getArtifactDistanceTimed(2) < MIDDLE_DISTANCE_CM;
    }

    /**
     * Determines whether an artifact is present at the intake position.
     * <p>
     *     For intake sensor - 3
     * </p>
     * @return {@code true} if an artifact is detected
     */
    public boolean isIntakePosBusy() {
        return getArtifactDistanceTimed(3) < INTAKE_DISTANCE_CM;
    }

    /**
     * Generic artifact presence check with a custom distance threshold.
     *
     * @param distanceCm distance threshold in centimeters
     * @return {@code true} if an artifact is detected
     */
    public boolean isPosBusy(double distanceCm , int pos) {
        return getArtifactDistanceTimed(pos) < distanceCm;
    }

    public boolean isRobotFull(){
        return isMidPosBusy() && isShootPosBusy() && isIntakePosBusy();
    }

    public boolean isOnlyShootAndMidBusy(){
        return isShootPosBusy() && isMidPosBusy() && !isIntakePosBusy();
    }

    /**
     * Returns the most recently cached distance measurement.
     *
     * @return cached distance in centimeters
     */
    public double getCachedDistanceCm(int variant) {
        switch (variant){
            case 1:
                return cachedShooterDistanceCm;
            case 2:
                return cachedMidDistanceCm;
            case 3:
                return cachedIntakeDistanceCm;
        }
        return Double.POSITIVE_INFINITY;
    }

    public void displayTelemetry(Telemetry telemetry){
        telemetry.addData("is shooter pos busy: ", isPosBusy(SHOOTER_DISTANCE_CM, 1));
        telemetry.addData("is mid pos busy: ", isPosBusy(MIDDLE_DISTANCE_CM, 2));
        telemetry.addData("is intake pos busy: ", isPosBusy(INTAKE_DISTANCE_CM, 3));
        telemetry.addData("shooter sensor: ", getArtifactDistanceTimed(1));
        telemetry.addData("mid sensor: ", getArtifactDistanceTimed(2));
        telemetry.addData("intake sensor: ", getArtifactDistanceTimed(3));

    }
}
