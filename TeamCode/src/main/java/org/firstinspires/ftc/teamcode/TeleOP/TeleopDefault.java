package org.firstinspires.ftc.teamcode.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.Commands.DriveCommand;
import org.firstinspires.ftc.teamcode.SubSystems.DriveTrain;

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


    private BarnRobot farminator;

    // Buttons


    @Override
    public void initialize() {
        farminator = BarnRobot.getInstance();
        farminator.initBarnRobotSystems(hardwareMap, gamepad1, gamepad2);
        // Initialize gamepads
        gamepadEx1 = BarnRobot.getInstance().gamepadEx1;
        gamepadEx2 = BarnRobot.getInstance().gamepadEx2;
        BarnRobot.getInstance().initDrivetrain();
        drive = BarnRobot.getInstance().drive;

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
                () -> BarnRobot.getInstance().gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.05).whenActive(
                    new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateSlowMode())
                ).whenInactive(
                        new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateFastMode())
                );


        BarnRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(() -> drive.resetHeading());

//        BarnRobot.getInstance().gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
//                .whenPressed(transferCommand);

//        BarnRobot.getInstance().initTransfer();

        drive.setDefaultCommand(driveCommand);
        register(drive);
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
