package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.OpModeData;

@TeleOp(name="HoodTunningOP", group = "test")
public class HoodTuningOpMode extends CommandOpMode {
    BarnRobot farminator;

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

        farminator.shooterHood.setDefaultCommand(farminator.shooterHood.setCustomDashboardPos());
        farminator.shooter.setDefaultCommand(farminator.shooter.turnOff());


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
                .toggleWhenPressed(
                        new ParallelCommandGroup(
                                farminator.shooter.runShooterBasedOnDistance(),
                                farminator.intake.activateIntakeCommand(),
                                farminator.gate.openCommand(),
                                farminator.transfer.activateTransfer()
                        ),
                        new ParallelCommandGroup(
                                farminator.shooter.turnOff(),
                                farminator.intake.deactivateIntakeCommand(),
                                farminator.gate.closeCommand(),
                                farminator.transfer.deactivateTransfer()
                                )
                        );
    }

    @Override
    public void run() {
        super.run();
        farminator.limelight.displayTelemetry();
        telemetry.addData("distance pinpoint", farminator.drive.getDistanceFromGoal());
        farminator.shooter.displayTelemetry();
        farminator.periodic();
    }
}
