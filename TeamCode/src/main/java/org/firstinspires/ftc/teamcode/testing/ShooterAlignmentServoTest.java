package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.LimeLight;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.util.OpModeData;



@TeleOp(name = "ShooterAlignmentClassTest", group = "main")
@Config
public class ShooterAlignmentServoTest extends CommandOpMode {

        // ------------------------
        // Robot Instance
        // ------------------------
    private BarnRobot farminator;

    private static final double INITIAL_BOT_HEADING = 270;

    @Override
    public void initialize() {

        Pose2d autoFinishPose = OpModeData.getAutoFinishPose(); // use the pose in which the auto has ended
        OpModeData opModeData = new OpModeData(
                OpModeData.AllianceColor.BLUE,
                OpModeData.OpModeType.TELEOP,
                LimeLight.BLUE_LOCALIZATION_PIPELINE,
                autoFinishPose
        );

            // ==========================================================
            // Robot Initialization
            // ==========================================================
        farminator = BarnRobot.getInstance();
        farminator.init(
                this,
                opModeData
        );


            // ==========================================================
            // Gamepad 1 Controls
            // ==========================================================

            // ------------------------
            // Transfer System
            // ------------------------

            // Left Bumper → Run back transfer backward
//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
//                .whenActive(farminator.shooter.setShooterAlignment(-1))
//                .whenInactive(farminator.shooter.setShooterAlignment(0));

            // Right Bumper → Run all transfer motors forward
//        farminator.gamepadEx1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
//                .whenActive(farminator.shooter.setShooterAlignment(1))
//                .whenInactive(farminator.shooter.setShooterAlignment(0));


    }

    @Override
    public void run() {
        // ==========================================================
        // Periodic Updates
        // ==========================================================

        super.run();
        farminator.periodic();
    }
}
