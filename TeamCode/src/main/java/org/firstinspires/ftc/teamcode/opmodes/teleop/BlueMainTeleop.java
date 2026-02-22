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


@TeleOp(name = "!!BLUE TELEOP", group = "!")
public class BlueMainTeleop extends CommandOpMode{

    // ------------------------
    // Robot Instance
    // ------------------------
    private BarnRobot farminator;
    private TeleopTemplate template;

    @Override
    public void initialize() {

        OpModeData opModeData = new OpModeData(
                OpModeData.AllianceColor.BLUE,
                OpModeData.OpModeType.TELEOP,
                new Pose2d(0, 0, Math.toRadians(270)),
                270
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
        template.initControls();

    }



    @Override
    public void run() {
        super.run();
        farminator.shooterHood.displayTelemetry();
        farminator.drive.displayPinpointDataTelemetry();
        farminator.shooter.displayTelemetry();
//        telemetry.addData("ang", farminator.pinpointLocalizer.getPose().heading.toDouble());
        telemetry.addData("ang", Math.toDegrees(farminator.pinpointLocalizer.getPose().heading.toDouble()));
//        telemetry.addData("Loop Time (ms)", getRuntime() * 1000);
//        telemetry.addData("default drive command: ", farminator.drive.getDefaultCommand());
        telemetry.addData("Is smart functions ON? ", farminator.colorSensor.getIntakeMode());
        farminator.colorSensor.displayTelemetry(telemetry);
        telemetry.addData("is in zone", farminator.drive.isInsideLaunchZone());

        telemetry.addData("gate left pos", farminator.gate.getLeftPos());
        farminator.limelight.displayTelemetry();
        farminator.periodic();
    }


    private InstantCommand rumbleCommand() {
        return new InstantCommand(() -> gamepad1.rumble(200));
    }
}