package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.OpModeData;

public class AutoDefault extends CommandOpMode {
    private BarnRobot farminator;

    @Override
    public void initialize() {

        // ------------------------
        // Initialize Robot Systems
        // ------------------------
        farminator = BarnRobot.getInstance();
        farminator.initBarnRobotSystemsTeleop(this, new OpModeData());


        TrajectoryActionBuilder path1 = farminator.autoDrive.actionBuilder(farminator.opmodeData.startPose)
                .strafeToLinearHeading(new Vector2d(10,10), Math.toRadians(90));

        new SequentialCommandGroup(

        ).schedule();

    }

    @Override
    public void run() {
        super.run();
        farminator.periodic();
    }
}
