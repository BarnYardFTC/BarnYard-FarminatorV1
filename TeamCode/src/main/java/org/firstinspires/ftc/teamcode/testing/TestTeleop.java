package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.ShootSequenceCommandGroup;
import org.firstinspires.ftc.teamcode.util.OpModeData;

@TeleOp(name="Test Teleop", group = "main")
public class TestTeleop extends CommandOpMode {


    /*
    TODO
    - A toggle button to activate/deactivate limelight yaw alignment v
    - Display to the telemetry bot's position on the field (based on limelight) v
    - Pattern Recognition implementation in init v
    - A button to activate shootAllCommand v
    */

    private BarnRobot farminator;

    @Override
    public void initialize() {

        // ------------------------
        // Initialize Robot Systems
        // ------------------------
        farminator = BarnRobot.getInstance();
        farminator.init(this, new OpModeData(OpModeData.AllianceColor.BLUE, 270, 270));

        /* ----------------------
              Gamepad Mapping
           ----------------------*/

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(farminator.shooter.activateShooterCommand())
                .whenInactive(farminator.shooter.deactivateShooterCommand())
        ;

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B).whenPressed(
                farminator.transfer.activateFrontTransferCommand()
        ).whenInactive(farminator.transfer.deactivateFrontTransferCommand());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
                farminator.transfer.activateBackTransferCommand()
        ).whenInactive(farminator.transfer.deactivateBackTransferCommand());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
                farminator.transfer.activateBackTransferCommand(-1)
        ).whenInactive(farminator.transfer.activateBackTransferCommand(0));

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.A)
                .whenActive(farminator.intake.activateIntake())
                .whenInactive(farminator.intake.deactivateIntakeCommand());

        new Trigger(
                () -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0
        )
                .whenActive(farminator.intake.customIntakeCommand(farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)))
                .whenInactive(farminator.intake.deactivateIntakeCommand());

        new Trigger(
                () -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0
        )
                .whenActive(new InstantCommand(() -> farminator.telemetry.addLine("right trigger pressed")))
                .whenInactive(new InstantCommand(() -> farminator.telemetry.addLine("right trigger not pressed")));

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_UP).toggleWhenActive(
                new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateSlowMode()),
                new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateFastMode())
        );
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whileHeld(
                farminator.drive.alignToTagCommand()
        );
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whileHeld(
                ShootSequenceCommandGroup.shootAllCommand()
        );
    }

    @Override
    public void initialize_loop(){
        farminator.limelight.findPattern();
        BarnRobot.getInstance().limelight.periodic();
        BarnRobot.getInstance().limelight.displayTelemetry();
        farminator.periodic();
    }

    @Override
    public void run() {
        super.run();
        farminator.periodic();
    }
}
