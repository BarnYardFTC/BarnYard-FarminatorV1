package org.firstinspires.ftc.teamcode.testing;
import static org.firstinspires.ftc.teamcode.opmodes.auto.blue.close.BlueCloseTemp.*;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.AutoController;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoPars;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutonomousPathController;
import org.firstinspires.ftc.teamcode.opmodes.auto.blue.close.BlueCloseTemp;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

@Autonomous(name="BC9 PSA", group="test")
public class PowerSavingAuto extends CommandOpMode {

    /** Robot and drive system instances */
    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;
    private final AutonomousPathController autoHub = new AutonomousPathController(AutoPars.side.BLUE, AutoPars.posDistance.CLOSE);

    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.BLUE,
            OpModeData.OpModeType.AUTONOMOUS,
            autoHub.positions.get(AutoPars.positions.START_CLOSE)
    );

    public static int SCORE_TIME = 2200;

    @Override
    public void initialize() {

        /** Initialize robot and drive system */
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        drive = new RoadRunnerMecanumDrive(hardwareMap, autoHub.positions.get(AutoPars.positions.START_CLOSE));
        BlueCloseTemp.createPath(drive);

        farminator.shooterHood.setDefaultCommand(farminator.shooterHood.autoHoodAlignment());

/**
 * robotPathCommands(boolean intake, boolean shoot, TrajectoryActionBuilder path)
 */


        new SequentialCommandGroup(
                new WaitUntilCommand(this::opModeIsActive),
                AutoController.robotPathCommandsPSM(false, true, goShootPre),

                AutoController.robotPathCommandsPSM(true, false, goCollectLeftGate),

                AutoController.robotPathCommandsPSM(true, true, goShootLeftGate),

                AutoController.robotPathCommandsPSM(true, false, goCollectMid),

                AutoController.robotPathCommandsPSM(true, true, goShootMid),

                AutoController.robotPathCommandsPSM(true, false, goCollectRight),

                AutoController.robotPathCommandsPSM(true, true, goShootLastLeave)
        ).schedule();
    }
    @Override
    public void run() {
        super.run();
        telemetry.addData("shooter command: ", farminator.shooter.getDefaultCommand().getName());
        telemetry.addData("shooter command: ", farminator.shooter.getCurrentCommand().getName());
        farminator.periodic();
    }

    /**
     * runs when the autonomous is finished
     */
    @Override
    public void end(){
        // store the finish heading of the auto
        OpModeData.setAutoFinishPose(drive.localizer.getPose());
    }

}