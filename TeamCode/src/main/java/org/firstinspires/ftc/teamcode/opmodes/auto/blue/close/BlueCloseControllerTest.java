package org.firstinspires.ftc.teamcode.opmodes.auto.blue.close;

import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.AutoController;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoPars;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutonomousPathController;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

@Autonomous(name="Auto Controller Test", group="test")
public class BlueCloseControllerTest extends CommandOpMode {
    BarnRobot farminator;

    //Initializing autoPathController with team parameters
    private final AutonomousPathController autoHub = new AutonomousPathController(AutoPars.side.BLUE, AutoPars.posDistance.CLOSE);

    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.BLUE,
            OpModeData.OpModeType.AUTONOMOUS,
            autoHub.positions.get(AutoPars.positions.START_CLOSE)
    );

    @Override
    public void initialize() {
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);
        farminator.shooter.setDefaultCommand(farminator.shooter.turnOff());

        RoadRunnerMecanumDrive drive = new RoadRunnerMecanumDrive(hardwareMap, autoHub.positions.get(AutoPars.positions.START_CLOSE));

//       ========== COMMANDS =============
        new SequentialCommandGroup(
                new WaitUntilCommand(this::opModeIsActive),

                AutoController.shootCommandPath(autoHub.trajectories(AutoPars.positions.SHOOT_CLOSE, drive)),

                AutoController.intakeCommandPath(autoHub.trajectories(AutoPars.positions.LEFT_COLLECT, drive)),

                AutoController.shootCommandPath(autoHub.trajectories(AutoPars.positions.SHOOT_CLOSE, drive)),

                AutoController.intakeCommandPath(autoHub.trajectories(AutoPars.positions.PARK, drive))


        ).schedule();
    }
}

