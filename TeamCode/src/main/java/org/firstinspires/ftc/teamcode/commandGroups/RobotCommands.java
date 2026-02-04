package org.firstinspires.ftc.teamcode.commandGroups;

import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.command.Command;

/**
 * A class that contains complex command groups with commands from different classes.
 * It's here to make the code more object oriented and something :)
 */
@Config
public class RobotCommands {
    //WIP: currently only represents approximate class structure
    /** Timings for intake and shooter in milliseconds. */
    public static int TRANSFER_ONE_DURATION = 1500;
    public static int TRANSFER_ALL_DURATION = TRANSFER_ONE_DURATION * 3;
    /** Commands */
    public static Command shootCommand(){
        return null;
    }
}
