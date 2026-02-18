package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.CommandGroup;
import org.firstinspires.ftc.teamcode.util.OpModeData;

import java.util.function.BooleanSupplier;


@TeleOp(name = "!Local TELEOP", group = "!")
public class LocalizationTest extends CommandOpMode{

    // ------------------------
    // Robot Instance
    // ------------------------
    private BarnRobot farminator;

    @Override
    public void initialize() {

        OpModeData opModeData = new OpModeData(
                OpModeData.AllianceColor.BLUE,
                OpModeData.OpModeType.TELEOP,
                new Pose2d(0, 0, Math.toRadians(180)),
                180
        );

        // ==========================================================
        // Robot Initialization
        // ==========================================================
        farminator = BarnRobot.getInstance();
        farminator.init(
                this,
                opModeData
        );
        farminator.drive.setDefaultCommand(farminator.drive.driveOneDriverCommand());
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
                .toggleWhenPressed(
                        farminator.shooter.turnOff(),
                        farminator.shooter.runShooterBasedOnDistance()
                );







//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_STICK_BUTTON)
//                .toggleWhenActive(
//                        new InstantCommand(() -> farminator.drive.setDefaultCommand(farminator.drive.maintainPosCommand())),
//                        new InstantCommand(() -> farminator.drive.setDefaultCommand(farminator.drive.driveNonFieldOrientedCommand()))
//                );


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X).whenPressed(
                new InstantCommand(() -> farminator.pinpointLocalizer.setPose(new Pose2d(0,0,Math.toRadians(180)))));

    }

    @Override
    public void run() {
        super.run();
        farminator.drive.displayPinpointDataTelemetry();
        farminator.webcam.updatePose();
        farminator.periodic();
    }

    private InstantCommand rumbleCommand() {
        return new InstantCommand(() -> gamepad1.rumble(200));
    }
}