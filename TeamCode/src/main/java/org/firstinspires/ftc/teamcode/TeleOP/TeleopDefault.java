package org.firstinspires.ftc.teamcode.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.button.Button;
import com.seattlesolvers.solverslib.command.button.GamepadButton;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.Commands.DriveCommand;
import org.firstinspires.ftc.teamcode.Commands.SpeedModeCommand;
import org.firstinspires.ftc.teamcode.Commands.TransferArtifactCommand;
import org.firstinspires.ftc.teamcode.SubSystems.DriveTrain;
import org.firstinspires.ftc.teamcode.SubSystems.Transfer;

@TeleOp
public class TeleopDefault extends CommandOpMode {

    // Gamepads
    private GamepadEx gamepadEx1, gamepadEx2;

    // Subsystems
    private DriveTrain drive;
//    private Transfer transfer;

    // Commands
    private DriveCommand driveCommand;
//    private TransferArtifactCommand transferCommand;
    private SpeedModeCommand speed;

    // Buttons


    @Override
    public void initialize() {
        // Initialize gamepads
        gamepadEx1 = BarnRobot.getInstance().gamepadEx1;
        gamepadEx2 = BarnRobot.getInstance().gamepadEx2;

        // Initialize subsystems
        drive = new DriveTrain();
//        transfer = new Transfer();

        // Initialize drive command (default teleop control)
        driveCommand = new DriveCommand(
                drive,
                gamepadEx1::getLeftX,
                gamepadEx1::getLeftY,
                gamepadEx1::getRightX,
                telemetry
        );

        // Button mappings
        Trigger leftTriggerCondition = new Trigger(
                () -> BarnRobot.getInstance().gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.05);
        leftTriggerCondition.whenActive(speed);


        BarnRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(() -> drive.resetHeading());

//        BarnRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
//                .whenPressed(transferCommand);

        BarnRobot.getInstance().initDrivetrain();
//        BarnRobot.getInstance().initTransfer();
    }

    @Override
    public void run() {
        super.run();

        // Telemetry for debugging joystick values
        telemetry.addData("Left Stick X", gamepadEx1.getLeftX());
        telemetry.addData("Left Stick Y", gamepadEx1.getLeftY());
        telemetry.addData("Right Stick X", gamepadEx1.getRightX());
        telemetry.update();
    }
}
