package org.firstinspires.ftc.teamcode.Commands;

import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.button.Trigger;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.SubSystems.MecanumDriveComponent;


public class SpeedModeCommand extends CommandBase {
    public SpeedModeCommand(Trigger trigger){
        BarnRobot.getInstance().drive.setSpeedMode(MecanumDriveComponent.SpeedMode.SLOW);
    }

    @Override
    public void end(boolean interrupted){
        BarnRobot.getInstance().drive.setSpeedMode(MecanumDriveComponent.SpeedMode.FAST);
    }

}
