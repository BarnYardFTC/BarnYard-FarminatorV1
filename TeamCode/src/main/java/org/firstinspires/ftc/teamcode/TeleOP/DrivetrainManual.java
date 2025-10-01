package org.firstinspires.ftc.teamcode.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.Commands.DriveCommand;
import org.firstinspires.ftc.teamcode.SubSystems.DriveSubsystem;

@TeleOp
public class DrivetrainManual extends CommandOpMode {
    private GamepadEx gamepadEx1, gamepadEx2;
    private DriveSubsystem drive;
    private DriveCommand driveCommand;

    @Override
    public void initialize() {
        gamepadEx1 = new GamepadEx(gamepad1);
        gamepadEx2 = new GamepadEx(gamepad2);

        drive = new DriveSubsystem(hardwareMap);

        driveCommand = new DriveCommand(drive, gamepadEx1.getLeftX(), gamepadEx1.getLeftY(), gamepadEx1.getRightX());

        register(drive);
        drive.setDefaultCommand(driveCommand);
    }
}
