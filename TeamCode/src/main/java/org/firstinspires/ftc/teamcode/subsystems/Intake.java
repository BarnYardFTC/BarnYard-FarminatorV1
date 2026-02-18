package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BarnRobot;

/**
 * Subsystem for controlling the intake mechanism.
 *
 * Handles power control, activation, and deactivation commands.
 */
@Config
public class Intake extends SubsystemBase {

    /** Intake motor hardware object. */
    private final DcMotorEx intake;/** Default power to run the intake. */
    public static double DEFAULT_POWER = 1;

    /**
     * Constructs the Intake subsystem and initializes motor settings.
     */
    public Intake() {
        this.intake = BarnRobot.getInstance().robotHardware.intake;

        intake.setDirection(DcMotorSimple.Direction.FORWARD);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    /**
     * Sets the intake motor power.
     *
     * @param power motor power [-1, 1]
     */
    public void setPower(double power) {
        intake.setPower(power);
    }

    /**
     * Returns a command that runs the intake at a custom power.
     *
     * @param power desired motor power
     * @return command to run intake at specified power
     */
    public Command customIntakeCommand(double power) {
        return new InstantCommand(() -> setPower(power), this);
    }

    /**
     * \
     * Returns a command that activates the intake at default power.
     *
     * @return command to activate intake
     */
    public Command activateIntakeCommand() {
        return new InstantCommand(() -> setPower(DEFAULT_POWER), this);
    }

    public Command smartIntakeCommand(){
        return new ConditionalCommand(
                deactivateIntakeCommand(),
                activateIntakeCommand(),
                () -> BarnRobot.getInstance().colorSensor.isRobotFull()
        );
    }


    /**
     * Returns a command that deactivates the intake.
     *
     * @return command to stop the intake
     */
    public Command deactivateIntakeCommand() {
        return new InstantCommand(() -> setPower(0), this);
    }

    public void displayTelemetry(Telemetry telemetry){
        telemetry.addData("Is Robot FULL TRANSFER: ", BarnRobot.getInstance().colorSensor.isRobotFull());
    }
}
