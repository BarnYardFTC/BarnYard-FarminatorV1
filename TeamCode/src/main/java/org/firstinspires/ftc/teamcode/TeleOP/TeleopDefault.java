package org.firstinspires.ftc.teamcode.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.button.Button;
import com.seattlesolvers.solverslib.command.button.GamepadButton;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.Commands.DriveCommand;
import org.firstinspires.ftc.teamcode.Commands.TransferArtifactCommand;
import org.firstinspires.ftc.teamcode.SubSystems.DriveTrain;
import org.firstinspires.ftc.teamcode.SubSystems.Transfer;

@TeleOp
public class TeleopDefault extends CommandOpMode {

    // Gamepads
    private GamepadEx gamepadEx1, gamepadEx2;

    // Subsystems
    private DriveTrain drive;
    private Transfer transfer;

    // Commands
    private DriveCommand driveCommand;
    private TransferArtifactCommand transferCommand;

    // Buttons
    private Button speedModeButton,
            headingResetButton,
            startTransferButton;

    @Override
    public void initialize() {
        // Initialize gamepads
        gamepadEx1 = new GamepadEx(gamepad1);
        gamepadEx2 = new GamepadEx(gamepad2);

        // Initialize subsystems
        drive = new DriveTrain(hardwareMap);
        transfer = new Transfer(hardwareMap);

        // Initialize drive command (default teleop control)
        driveCommand = new DriveCommand(
                drive,
                gamepadEx1::getLeftX,
                gamepadEx1::getLeftY,
                gamepadEx1::getRightX,
                telemetry
        );

        // Button mappings
        speedModeButton = new GamepadButton(gamepadEx1, GamepadKeys.Button.B)
                .whenPressed(() -> drive.mecanumDriveComponent.toggleSpeedMode());

        headingResetButton = new GamepadButton(gamepadEx1, GamepadKeys.Button.X)
                .whenPressed(() -> drive.resetHeading());

        startTransferButton = new GamepadButton(gamepadEx1, GamepadKeys.Button.Y).whenPressed(transferCommand);

        // Register subsystems and commands
        register(drive);
        register(transfer);
        drive.setDefaultCommand(driveCommand);
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
