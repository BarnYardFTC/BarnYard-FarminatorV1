package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.util.OpModeData;

@TeleOp(name="pattern recognition", group="test")
public class TestPatternRecognition extends CommandOpMode {
    private BarnRobot farminator;

    @Override
    public void initialize() {

        // ------------------------
        // Initialize Robot Systems
        // ------------------------
        farminator = BarnRobot.getInstance();
        farminator.init(this, new OpModeData());
        farminator.limelight.switchPipeline(LimeLight.OBELISK_PIPELINE);


        /* ----------------------
              Gamepad Mapping
           ----------------------*/

        /*
         example:
         farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X).whenPressed(
                {RUN SOMETHING}
            );
         */


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
