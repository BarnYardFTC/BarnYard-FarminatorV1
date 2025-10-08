package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.OpModeData;

@TeleOp
public class TestingDefault extends CommandOpMode {
    private BarnRobot farminator;

    @Override
    public void initialize(){

        // ------------------------
        // Initialize Robot Systems
        // ------------------------
        farminator = BarnRobot.getInstance();
        farminator.initBarnRobotSystems(this, new OpModeData());


        /* ----------------------
              Gamepad Mapping
           ----------------------*/

        /*
         example:
         farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X).whenPressed(
                {RUN SOMETHING}
            );
         */

        Trigger rightTrigger = new Trigger(
                () -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.05
        )
                .whenActive(farminator.intake.activateIntake())
                .whenInactive(farminator.intake.deactivateIntake());


    }

    @Override
    public void run() {
        super.run();
        farminator.periodic();
    }
}
