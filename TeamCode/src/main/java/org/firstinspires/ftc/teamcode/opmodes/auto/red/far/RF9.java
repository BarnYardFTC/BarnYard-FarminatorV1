package org.firstinspires.ftc.teamcode.opmodes.auto.red.far;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.AutoController;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoPars;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutonomousPathController;
import org.firstinspires.ftc.teamcode.opmodes.auto.red.close.RedCloseTrajs;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;


@Autonomous(name="RF9", group="test")
public class RF9 extends CommandOpMode {

    /** Robot and drive system instances */
    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;
    private final AutonomousPathController autoHub = new AutonomousPathController(AutoPars.side.RED, AutoPars.posDistance.FAR);

    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.RED,
            OpModeData.OpModeType.AUTONOMOUS,
            autoHub.positions.get(AutoPars.positions.START_FAR)
    );

    public static int SCORE_TIME = 2200;

    @Override
    public void initialize() {

        /** Initialize robot and drive system */
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        drive = new RoadRunnerMecanumDrive(hardwareMap, autoHub.positions.get(AutoPars.positions.START_FAR));
        RedCloseTrajs.createPath(drive);

        farminator.shooter.setDefaultCommand(farminator.shooter.runShooterBasedOnDistance());
        farminator.shooterHood.setDefaultCommand(farminator.shooterHood.autoHoodAlignment());

/**
 * robotPathCommands(boolean intake, boolean shoot, TrajectoryActionBuilder path)
 */
//test

        new SequentialCommandGroup(
                new WaitUntilCommand(this::opModeIsActive),
                AutoController.robotPathCommands(false, true, RedCloseTrajs.goShootPre),

                AutoController.robotPathCommands(true, false, RedCloseTrajs.goCollectLeft),

                AutoController.robotPathCommands(true, true, RedCloseTrajs.goShootLeft),

                AutoController.robotPathCommands(true, false, RedCloseTrajs.goCollectMid),

                AutoController.robotPathCommands(true, true, RedCloseTrajs.goShootMid),

                AutoController.robotPathCommands(true, false, RedCloseTrajs.goCollectRight),

                AutoController.robotPathCommands(true, true, RedCloseTrajs.goShootLast)


        ).schedule();
    }
    @Override
    public void run() {
        super.run();
        telemetry.addData("aligned: ", farminator.limelight.isAlignedToGoal());
        telemetry.addData("yaw: ", farminator.limelight.getGoalYaw());
        telemetry.addData("tag detected: ", farminator.limelight.isGoalTagDetected());
        if (farminator.limelight.isGoalTagDetected())
            telemetry.addData("seen", true);
        farminator.limelight.displayTelemetry();

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
