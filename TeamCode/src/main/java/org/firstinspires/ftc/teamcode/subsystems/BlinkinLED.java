
package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.BarnRobot;

/**
 * Subsystem responsible for controlling the REV Blinkin LED Driver.
 *
 * <p>This subsystem provides simple color state control for robot feedback.
 * It includes internal rate limiting and duplicate-pattern protection
 * to prevent unnecessary hardware updates.</p>
 *
 * <p>Typical use case: displaying robot state such as number of detected artifacts,
 * robot mode, or driver alerts.</p>
 */
public class BlinkinLED extends SubsystemBase {

    /** Hardware driver for LED control */
    private final RevBlinkinLedDriver blinkin;

    /** Last pattern that was sent to the Blinkin */
    public RevBlinkinLedDriver.BlinkinPattern currentPattern = null;

    /** Timestamp of the last successful LED update (ms) */
    private long lastUpdateTime = 0;

    /** Minimum delay between LED updates to prevent spam */
    private static final long UPDATE_COOLDOWN_MS = 250;


    /**
     * Creates the LED subsystem and initializes LEDs to black (off).
     */
    public BlinkinLED() {
        this.blinkin = BarnRobot.getInstance().robotHardware.blinkin;
        setBlack();
    }

    @Override
    public void periodic() {
        if (BarnRobot.getInstance().colorSensor.isShootPosBusy() && !BarnRobot.getInstance().colorSensor.isMidPosBusy() && !BarnRobot.getInstance().colorSensor.isIntakePosBusy()){
            setRed();
        }
        else if (BarnRobot.getInstance().colorSensor.isShootAndMidIn()
        ){
            setGreen();
        }

        else if (BarnRobot.getInstance().colorSensor.isRobotFull()){
            setPurple();
        }

        else setBlack();
    }

    // ----------------------------------------------------------------
    // Color Setters
    // ----------------------------------------------------------------

    /** Sets LEDs to black (off). Used when no artifacts are detected. */
    public void setBlack() {
        setPattern(RevBlinkinLedDriver.BlinkinPattern.BLACK);
    }

    /** Sets LEDs to red. Used to indicate low artifact count (1). */
    public void setRed() {
        setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
    }

    /** Sets LEDs to purple. Used to indicate medium artifact count (2). */
    public void setPurple() {
        setPattern(RevBlinkinLedDriver.BlinkinPattern.VIOLET);
    }

    /** Sets LEDs to green. Used to indicate maximum artifact count (3). */
    public void setGreen() {
        setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
    }

    // ----------------------------------------------------------------
    // Commands
    // ----------------------------------------------------------------

    /** @return Command that sets LEDs to black */
    public Command setBlackCommand() {
        return new InstantCommand(this::setBlack, this);
    }

    /** @return Command that sets LEDs to red */
    public Command setRedCommand() {
        return new InstantCommand(this::setRed, this);
    }

    /** @return Command that sets LEDs to yellow */
    public Command setPurpleCommand() {
        return new InstantCommand(this::setPurple, this);
    }

    /** @return Command that sets LEDs to green */
    public Command setGreenCommand() {
        return new InstantCommand(this::setGreen, this);
    }

    // ----------------------------------------------------------------
    // Internal Pattern Control
    // ----------------------------------------------------------------

    /**
     * Applies a new LED pattern if it differs from the current one and
     * enough time has passed since the last update.
     *
     * <p>This prevents excessive hardware calls and visual flickering.</p>
     *
     * @param newPattern The desired LED pattern
     */
    private void setPattern(RevBlinkinLedDriver.BlinkinPattern newPattern) {
        long now = System.currentTimeMillis();

        if (newPattern != currentPattern && now - lastUpdateTime > UPDATE_COOLDOWN_MS) {
            blinkin.setPattern(newPattern);
            currentPattern = newPattern;
            lastUpdateTime = now;
        }
    }

    /**
     * @return Time (ms) since the last LED pattern change
     */
    public long getLastUpdateTime() {
        return lastUpdateTime;
    }



}
