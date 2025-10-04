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

    // ------------------------------------------------------------
    // Gamepads
    // ------------------------------------------------------------
    private GamepadEx gamepadEx1, gamepadEx2;

    // ------------------------------------------------------------
    // Subsystems
    // ------------------------------------------------------------
    private DriveTrain drive;
    // private Transfer transfer;

    // ------------------------------------------------------------
    // Commands
    // ------------------------------------------------------------
    private DriveCommand driveCommand;
    // private TransferArtifactCommand transferCommand;

    // ------------------------------------------------------------
    // Robot
    // ------------------------------------------------------------
    private BarnRobot farminator;

    // ------------------------------------------------------------
    // Buttons
    // ------------------------------------------------------------
    private static final double SLOW_MODE_TRIGGER_THRESHOLD = 0.05;


    @Override
    public void initialize() {

        // ------------------------
        // Initialize Robot Systems
        // ------------------------
        farminator = BarnRobot.getInstance();
        farminator.initBarnRobotSystems(hardwareMap, gamepad1, gamepad2);

        // ------------------------
        // Initialize Gamepads
        // ------------------------
        gamepadEx1 = farminator.gamepadEx1;
        gamepadEx2 = farminator.gamepadEx2;

        // ------------------------
        // Initialize Drivetrain
        // ------------------------
        farminator.initDrivetrain();
        drive = farminator.drive;

        // ------------------------
        // Initialize Drive Command
        // ------------------------
        driveCommand = new DriveCommand(
                drive,
                gamepadEx1::getLeftX,
                gamepadEx1::getLeftY,
                gamepadEx1::getRightX,
                telemetry
        );

        // ------------------------
        // Button Mappings
        // ------------------------

        // Left trigger toggles between slow and fast mode
        Trigger leftTriggerCondition = new Trigger(
                () -> gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > SLOW_MODE_TRIGGER_THRESHOLD
        )
                .whenActive(new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateSlowMode()))
                .whenInactive(new InstantCommand(() -> farminator.drive.mecanumDriveComponent.activateFastMode()));

        // Reset heading with X
        gamepadEx1.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(() -> drive.resetHeading());

        // Y for transfer (currently commented out)
        // farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
        //         .whenPressed(transferCommand);

        // ------------------------
        // Register Subsystems
        // ------------------------
        register(drive);

        // Set default drive command
        drive.setDefaultCommand(driveCommand);
    }

    @Override
    public void run() {
        super.run();
        telemetry.update();
    }
}
