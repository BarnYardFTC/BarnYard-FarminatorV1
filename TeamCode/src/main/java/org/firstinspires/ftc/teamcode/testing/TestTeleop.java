package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.IntakeCommandGroup;
import org.firstinspires.ftc.teamcode.commandGroups.ShootSequenceCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
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
        farminator.limelight.switchPipeline(LimeLight.BLUE_PIPELINE);

        /* ----------------------
              Gamepad Mapping
           ----------------------*/


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .toggleWhenPressed(farminator.shooter.shootAtRangeCommand(), farminator.shooter.deactivateShooterCommand());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
                        .whenActive(farminator.drive.resetHeadingCommand());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
                .whenActive(IntakeCommandGroup.activateIntakeCommand())
                .whenInactive(IntakeCommandGroup.deactivateIntakeCommand());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X)
                .whenActive(farminator.transfer.unloadTransferCommand())
                .whenInactive(farminator.transfer.deactivateTransferCommand());

//        new Trigger(
//                () -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0
//        )
//                .whenActive(new InstantCommand(() -> farminator.telemetry.addLine("right trigger pressed")))
//                .whenInactive(new InstantCommand(() -> farminator.telemetry.addLine("right trigger not pressed")));

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.A).toggleWhenActive(
                new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateSlowMode()),
                new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateFastMode())
        );

        new Trigger(
                () -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.05
        )
                .whenActive(farminator.drive.alignToTagCommand())
                .whenInactive(farminator.drive.driveCommand());


        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(ShootSequenceCommandGroup.activateTransferToShooter())
                .whenInactive(ShootSequenceCommandGroup.deactivateTransferToShooter());
    }

    @Override
    public void initialize_loop(){
        BarnRobot.getInstance().limelight.periodic();
        BarnRobot.getInstance().limelight.displayTelemetry();
        farminator.periodic();
    }

    @Override
    public void run() {
        super.run();
        farminator.limelight.findDyaw();
        farminator.limelight.findRange(farminator.drive.getHeading());
        farminator.periodic();
    }
}
