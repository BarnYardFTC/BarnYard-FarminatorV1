package org.firstinspires.ftc.teamcode.testing;

import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.OpModeData;

public class TestingDefault extends CommandOpMode {
    private BarnRobot farminator;

    @Override
    public void initialize() {

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
        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(farminator.transfer.transferCommand());

//        Trigger leftTrigger = new Trigger(
//                () -> farminator.gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.05
//
//                        .whenActive(farminator.transfer.activateTransfer())
//                        .whenInactive(farminator.transfer.activateTransfer());
//
    }

    @Override
    public void run() {
        super.run();
        farminator.periodic();
    }
}
