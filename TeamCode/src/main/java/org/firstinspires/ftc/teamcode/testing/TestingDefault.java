package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.IntakeCommandGroup;
import org.firstinspires.ftc.teamcode.commandGroups.ShootSequenceCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TeleOp
public class TestingDefault extends CommandOpMode {
    private static final Logger log = LoggerFactory.getLogger(TestingDefault.class);
    private BarnRobot farminator;

    private ShootSequenceCommandGroup shootCommand;
    private IntakeCommandGroup intakeCommand;

    @Override
    public void initialize(){

        // ------------------------
        // Initialize Robot Systems
        // ------------------------
        farminator = BarnRobot.getInstance();
        farminator.initBarnRobotSystems(this, new OpModeData());

        intakeCommand = new IntakeCommandGroup();
        shootCommand = new ShootSequenceCommandGroup();
        /* ----------------------
              Gamepad Mapping
           ----------------------*/
        /*

         example:
         farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X).whenPressed(
                {RUN SOMETHING}
            );
         */

//        Trigger rightTrigger = new Trigger(
//                () -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.05)
//                .whenActive(() -> farminator.drive.mecanumDriveComponent.activateSlowMode())
//                .whenInactive(() -> farminator.drive.mecanumDriveComponent.activateFastMode());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_STICK_BUTTON)
                .toggleWhenPressed(() -> farminator.drive.mecanumDriveComponent.activateSlowMode(), () -> farminator.drive.mecanumDriveComponent.activateFastMode());




        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(() -> farminator.drive.resetHeading());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(shootCommand.shootAllCommand());
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(shootCommand.shootAllCommand());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.A)
                .whenActive(intakeCommand.autoIntakeCommand())
                .whenInactive(intakeCommand.deactivateIntakeCommand());


    }

    @Override
    public void run() {
        super.run();
        farminator.periodic();
    }
}
