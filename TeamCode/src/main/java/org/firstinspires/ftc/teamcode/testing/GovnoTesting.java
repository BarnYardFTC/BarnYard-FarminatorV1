package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.commandGroups.RobotCommands;
import org.firstinspires.ftc.teamcode.util.OpModeData;

@TeleOp(name = "Cmd", group = "test")
public class GovnoTesting extends CommandOpMode {
    //WIP
    private BarnRobot farminator;
    @Override
    public void initialize() {
        OpModeData opModeData = new OpModeData(
                OpModeData.AllianceColor.BLUE,
                OpModeData.OpModeType.TELEOP,
                new Pose2d(0, 0, Math.toRadians(180)),
                180
        );
        farminator = BarnRobot.getInstance();
        farminator.init(
                this,
                opModeData
        );



        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.B)
                .toggleWhenActive(
                        RobotCommands.autoParkCommand()
                );
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
    }
}