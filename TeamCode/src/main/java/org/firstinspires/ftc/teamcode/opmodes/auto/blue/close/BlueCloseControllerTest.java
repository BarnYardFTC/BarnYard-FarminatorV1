package org.firstinspires.ftc.teamcode.opmodes.auto.blue.close;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
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

@Autonomous(name = "Auto Controller Test", group = "test")
public class BlueCloseControllerTest extends CommandOpMode {

    /** Robot and drive system instances */
    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;
    private final AutonomousPathController autoControl = new AutonomousPathController(AutoPars.side.BLUE, AutoPars.posDistance.CLOSE);



    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.BLUE,
            OpModeData.OpModeType.AUTONOMOUS,
            autoControl.positions.get(AutoPars.positions.START_CLOSE)
    );

    public static int SCORE_TIME = 2200;

    @Override
    public void initialize() {

        /** Initialize robot and drive system */
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);
        farminator.shooter.setDefaultCommand(farminator.shooter.turnOff());

        drive = new RoadRunnerMecanumDrive(hardwareMap, autoControl.positions.get(AutoPars.positions.START_CLOSE));

        //         ===== Commands =====
        new SequentialCommandGroup(
                new WaitUntilCommand(this::opModeIsActive),

                BarnRobot.getInstance().shooterHood.setHoodPosition(0.85),

                AutoController.shootCommandPath(autoControl.trajectories(AutoPars.positions.SHOOT_CLOSE, drive, telemetry)),

                AutoController.intakeCommandPath(autoControl.trajectories(AutoPars.positions.LEFT_COLLECT, drive, telemetry)),

                AutoController.shootCommandPath(autoControl.trajectories(AutoPars.positions.SHOOT_CLOSE, drive, telemetry)),

                AutoController.intakeCommandPath(autoControl.trajectories(AutoPars.positions.MID_COLLECT, drive, telemetry)),

                AutoController.shootCommandPathIntake(autoControl.trajectories(AutoPars.positions.SHOOT_CLOSE, drive, telemetry))

        ).schedule();
    }

    @Override
    public void run() {
        super.run();
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
    public static int SHOOTING_TIME_MS = 2000;


}
