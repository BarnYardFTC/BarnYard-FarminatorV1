package org.firstinspires.ftc.teamcode.util.pedroPathingTuners;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {

    private static final double ROBOT_MASS_KG = 5;
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(ROBOT_MASS_KG);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("rightFrontDrivetrain")
            .rightRearMotorName("rightBackDrivetrain")
            .leftRearMotorName("leftBackDrivetrain")
            .leftFrontMotorName("leftFrontDrivetrain")
            .leftFrontMotorDirection(DcMotorEx.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorEx.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorEx.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorEx.Direction.FORWARD);

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .build();
    }
}
