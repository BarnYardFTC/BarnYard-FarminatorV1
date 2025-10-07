package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.button.Button;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

@TeleOp
public class TestTransTeleop extends CommandOpMode {
//    private Transfer transfer;
    private GamepadEx gamepadEx1;

    private Button startTransferButton;
//
//    private TransferArtifactCommand transferCommand;


    @Override
    public void initialize() {
//        BarnRobot.getInstance().initBarnRobotSystems(hardwareMap, gamepad1, gamepad2);
//        gamepadEx1 = BarnRobot.getInstance().gamepadEx1;
//        transfer = BarnRobot.getInstance().transfer;
//        gamepadEx1.getGamepadButton(GamepadKeys.Button.Y).whenPressed(transferCommand);
//        register(transfer);


    }

    @Override
    public void run() {
//        super.run();
//        telemetry.addData("Controller input : ", startTransferButton.get() ? 1 : 0);
//        telemetry.update();
    }
}
