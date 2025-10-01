package org.firstinspires.ftc.teamcode.Commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.SubSystems.DriveSubsystem;

public class DriveCommand extends CommandBase {

    private final DriveSubsystem drive;
    private final double y;
    private final double x;
    private final double turn;


    public DriveCommand(DriveSubsystem subsystem, double x, double y, double turn) {
        drive = subsystem;
        this.x = x;
        this.y = y;
        this.turn = turn;
        addRequirements(drive);
    }

    @Override
    public void execute() {
        drive.drive(x,y,turn);
    }

}