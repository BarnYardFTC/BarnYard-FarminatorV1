package org.firstinspires.ftc.teamcode.Commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.SubSystems.DriveSubsystem;

import java.util.function.DoubleSupplier;

/**
 * A command to drive the robot with joystick input (passed in as {@link DoubleSupplier}s). Written
 * explicitly for pedagogical purposes.
 */
public class DriveCommand extends CommandBase {

    private final DriveSubsystem m_drive;
    private final double y;
    private final double x;
    private final double turn;


    public DriveCommand(DriveSubsystem subsystem, double x, double y, double turn) {
        m_drive = subsystem;
        this.x = x;
        this.y = y;
        this.turn = turn;
        addRequirements(m_drive);
    }

    @Override
    public void execute() {
        m_drive.drive(x,y,turn);
    }

}