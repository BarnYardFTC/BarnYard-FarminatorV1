package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.RobotCommands;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoPars;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutonomousPathController;
import org.firstinspires.ftc.teamcode.opmodes.auto.blue.far.BlueFarTemp;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

@TeleOp(name = "Cmd", group = "test")
public class GovnoTesting extends CommandOpMode {
    //WIP
    private BarnRobot farminator;
    private final AutonomousPathController autoHub = new AutonomousPathController(AutoPars.side.BLUE, AutoPars.posDistance.FAR);

    private final RoadRunnerMecanumDrive drive = new RoadRunnerMecanumDrive(hardwareMap, autoHub.positions.get(AutoPars.positions.START_FAR));

    @Override
    public void initialize() {
        OpModeData opModeData = new OpModeData(
                OpModeData.AllianceColor.BLUE,
                OpModeData.OpModeType.TELEOP,
                new Pose2d(23, 0, Math.toRadians(180)),
                180
        );
        farminator = BarnRobot.getInstance();
        farminator.init(
                this,
                opModeData
        );
        BlueFarTemp.createPath(drive);

        farminator.shooterHood.setDefaultCommand(farminator.shooterHood.setCustomDashboardPos());
        farminator.drive.setDefaultCommand(farminator.drive.driveOneDriverCommand());
        farminator.shooter.setDefaultCommand(farminator.shooter.turnOff());

        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
                .toggleWhenPressed(
                        RobotCommands.rrDrive(0, 0, drive) //.alongWith(farminator.drive.stop())
                );

//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X)
//                .toggleWhenPressed(
//                        RobotCommands.parkRun().alongWith(farminator.drive.stop())
//                );

//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.A)
//                .toggleWhenActive(
//                        RobotCommands.park().alongWith(farminator.drive.stop())
//                );

//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y)
//                .toggleWhenActive(
//                        RobotCommands.parkRun().alongWith(farminator.drive.stop())
//                );

//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
//                .toggleWhenActive(
//                        farminator.shooter.runShooter(1000)
//                );
//
//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.A).whenPressed(
//                RobotCommands.collectCommand()
//        );
//
//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
//                RobotCommands.collectStopCommand()
//        );
//
////        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.X).whenPressed(
////                RobotCommands.shootAllCommand()
////        );
//
//
//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(
//                RobotCommands.smartCollectCommand()
//        );
//
//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(
//                RobotCommands.smartShootCommand()
//        );
    }
    @Override
    public void run() {
        super.run();
        farminator.periodic();
        RobotCommands.displayTelemetry();
        telemetry.addData("x: ", farminator.pinpointLocalizer.getPose().position.x);
        telemetry.addData("y: ", farminator.pinpointLocalizer.getPose().position.y);
        telemetry.addData("heading: ", farminator.pinpointLocalizer.getPose().heading.toDouble());

    }
}