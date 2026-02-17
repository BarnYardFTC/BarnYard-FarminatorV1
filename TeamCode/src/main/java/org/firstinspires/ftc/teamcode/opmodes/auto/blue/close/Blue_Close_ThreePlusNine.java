package org.firstinspires.ftc.teamcode.opmodes.auto.blue.close;

import static org.firstinspires.ftc.teamcode.opmodes.auto.blue.close.blueCloseTemp.goCollectLeft;
import static org.firstinspires.ftc.teamcode.opmodes.auto.blue.close.blueCloseTemp.goCollectMid;
import static org.firstinspires.ftc.teamcode.opmodes.auto.blue.close.blueCloseTemp.goCollectRight;
import static org.firstinspires.ftc.teamcode.opmodes.auto.blue.close.blueCloseTemp.goShootLeft;
import static org.firstinspires.ftc.teamcode.opmodes.auto.blue.close.blueCloseTemp.goShootMid;
import static org.firstinspires.ftc.teamcode.opmodes.auto.blue.close.blueCloseTemp.goShootPre;
import static org.firstinspires.ftc.teamcode.opmodes.auto.blue.close.blueCloseTemp.goShootRight;
import static org.firstinspires.ftc.teamcode.opmodes.auto.blue.close.blueCloseTemp.parkPose;
import static org.firstinspires.ftc.teamcode.opmodes.auto.blue.close.blueCloseTemp.startPose;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.AutoController;
import org.firstinspires.ftc.teamcode.commandGroups.CommandGroup;
import org.firstinspires.ftc.teamcode.util.DriveActionCommand;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;
import org.firstinspires.ftc.teamcode.opmodes.auto.blue.close.blueCloseTemp.*;

@Disabled
@Autonomous(name = "!BLUE THREE PLUS NINE", group = "!main")
public class Blue_Close_ThreePlusNine extends CommandOpMode {



    /** Robot and drive system instances */
    private BarnRobot farminator;
    private RoadRunnerMecanumDrive drive;

    private final OpModeData opModeData = new OpModeData(
            OpModeData.AllianceColor.BLUE,
            OpModeData.OpModeType.AUTONOMOUS,
            startPose
    );

    public static int SCORE_TIME = 2200;

    @Override
    public void initialize() {

        /** Initialize robot and drive system */
        farminator = BarnRobot.getInstance();
        farminator.init(this, opModeData);

        farminator.shooter.setDefaultCommand(farminator.shooter.turnOff());

        drive = new RoadRunnerMecanumDrive(hardwareMap, startPose);

        blueCloseTemp.createPath(drive);

        new SequentialCommandGroup(
                new WaitUntilCommand(this::opModeIsActive),
                AutoController.shootCommandPath(goShootPre),
                AutoController.intakeCommandPath(goCollectLeft),
                AutoController.shootCommandPath(goShootLeft),
                AutoController.intakeCommandPath(goCollectMid),
                AutoController.shootCommandPathIntake(goShootMid),
                AutoController.intakeCommandPath(goCollectRight),
                AutoController.shootCommandPathIntake(goShootRight),
                new DriveActionCommand(new TrajectoryActionBuilder(parkPose))




//                AutoController.shootCommand(),
//                new WaitCommand(500),
//                AutoController.intakeCommandPath(path1),
//
//                AutoController.shootCommandPath(path2),
//
//                AutoController.intakeCommandPath(path3),
//
//                AutoController.shootCommandPathIntake(path4),
//
//                AutoController.intakeCommandPath(path5),
//
//                AutoController.shootCommandPathIntake(path7)

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


}
