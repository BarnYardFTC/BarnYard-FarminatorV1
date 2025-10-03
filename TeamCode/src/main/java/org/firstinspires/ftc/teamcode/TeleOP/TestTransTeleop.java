package org.firstinspires.ftc.teamcode.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.button.Button;
import com.seattlesolvers.solverslib.command.button.GamepadButton;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.Commands.TransferArtifactCommand;
import org.firstinspires.ftc.teamcode.SubSystems.Transfer;

@TeleOp
public class TestTransTeleop extends CommandOpMode {
    private Transfer transfer;
    private GamepadEx gamepadEx1;

    private Button startTransferButton;

    private TransferArtifactCommand transferCommand;


    @Override
    public void initialize() {
        BarnRobot.getInstance().initBarnRobotSystems();
        gamepadEx1 = BarnRobot.getInstance().gamepadEx1;
        transfer = BarnRobot.getInstance().transfer;
        gamepadEx1.getGamepadButton(GamepadKeys.Button.Y).whenPressed(transferCommand);
        register(transfer);


    }

    @Override
    public void run() {
        super.run();
        telemetry.addData("Controller input : ", startTransferButton.get() ? 1 : 0);
        telemetry.update();
    }
}
