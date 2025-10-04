package org.firstinspires.ftc.teamcode.Commands;

import com.seattlesolvers.solverslib.command.CommandBase;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.SubSystems.DriveTrain;

import java.util.function.DoubleSupplier;

public class DriveCommand extends CommandBase {

    // Subsystem
    private final DriveTrain drive;

    // Inputs from gamepad
    private final DoubleSupplier x;      // Strafe
    private final DoubleSupplier y;      // Forward/back
    private final DoubleSupplier turn;   // Rotation

    // Telemetry for debugging
    private final Telemetry telemetry;

    // Constructor
    public DriveCommand(DoubleSupplier x, DoubleSupplier y, DoubleSupplier turn, Telemetry telemetry) {
        this.drive = BarnRobot.getInstance().drive;
        this.x = x;
        this.y = y;
        this.turn = turn;
        this.telemetry = telemetry;

        // Declare subsystem requirement
        addRequirements(drive);
    }

    @Override
    public void execute() {
        // Debug telemetry
        telemetry.addData("X input", x.getAsDouble());
        telemetry.addData("Y input", y.getAsDouble());
        telemetry.addData("Turn input", turn.getAsDouble());
        telemetry.addData("Heading (rad)", drive.getHeadingRadians());
        telemetry.update();

        // Drive the robot
        drive.drive(x.getAsDouble(), y.getAsDouble(), turn.getAsDouble());
    }
}
