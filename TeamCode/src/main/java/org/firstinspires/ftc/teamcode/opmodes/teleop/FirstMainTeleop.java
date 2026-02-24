package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.CommandGroup;
import org.firstinspires.ftc.teamcode.util.OpModeData;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

@Config
@TeleOp(name = "!!First TELEOP", group = "!")
public class FirstMainTeleop extends CommandOpMode{

    // ------------------------
    // Robot Instance
    // ------------------------
    private BarnRobot farminator;
    private TeleopTemplate template;
    public static double pos = 0.95;
    @Override
    public void initialize() {

        OpModeData opModeData = new OpModeData(
                OpModeData.AllianceColor.RED,
                OpModeData.OpModeType.TELEOP,
                new Pose2d(0, 0, Math.toRadians(90)),
                90
        );

        // ==========================================================
        // Robot Initialization
        // ==========================================================
        farminator = BarnRobot.getInstance();
        farminator.init(
                this,
                opModeData
        );
        template = new TeleopTemplate();
        template.initControls(90);
    }



    @Override
    public void run() {
        super.run();
        farminator.limelight.displayTelemetry();
        telemetry.addData("dis", BarnRobot.getInstance().limelight.getGoalDistance());
        telemetry.addData("val", BarnRobot.getInstance().shooter.getTargetVelocity());
        farminator.shooterHood.HoodRunCommand(() -> pos).schedule();
        telemetry.update();
    }
}