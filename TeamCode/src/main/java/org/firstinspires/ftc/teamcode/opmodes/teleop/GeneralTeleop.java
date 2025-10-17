package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.Subsystem;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.ShootSequenceCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.util.OpModeData;

@TeleOp(name="General Teleop", group = "main")
public class GeneralTeleop extends CommandOpMode {

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





         farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X).whenPressed(
                 ShootSequenceCommandGroup.shootAllCommand()
            );

         farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B).whenPressed(
                farminator.transfer.activateFrontTransferCommand()
         );

         farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
                 farminator.transfer.activateBackTransferCommand()
         );
         new Trigger(
                () -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0
        )
                .whenActive(farminator.transfer.activateBackTransferCommand(-farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)))
                .whenInactive(farminator.transfer.deactivateBackTransferCommand())
                ;
        new  Trigger(
                () -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0
        )
                .whenActive(farminator.intake.customIntakeCommand(farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)))
                .whenInactive(farminator.intake.deactivateIntake())
                ;

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.A).toggleWhenActive(
                new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateSlowMode()),
                new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateFastMode())
        );




    }

    @Override
    public void run() {
        super.run();
        farminator.periodic();
    }
}
