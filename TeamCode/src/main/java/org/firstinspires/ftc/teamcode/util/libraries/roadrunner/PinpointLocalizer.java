package org.firstinspires.ftc.teamcode.util.libraries.roadrunner;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;

import java.util.Objects;

@Config
public final class PinpointLocalizer implements Localizer {

    private Pose2d lastPose = null;
    private long lastTimeNs = 0;
    public double dt = 0;

    private Pose2d poseVelocity = new Pose2d(0, 0, 0);

    public static class Params {
        public double parYTicks = 907.239352427659; // y position of the parallel encoder (in tick units)
        public double perpXTicks = -3234.0914110711988; // x position of the perpendicular encoder (in tick units)
    }

    public static Params PARAMS = new Params();
//    public static int xOffset = 110; //was 100
//    public static int yOffset = -110; //was -90

    public final GoBildaPinpointDriver driver;
    public final GoBildaPinpointDriver.EncoderDirection initialParDirection, initialPerpDirection;

    private Pose2d txWorldPinpoint;
    private Pose2d txPinpointRobot = new Pose2d(0, 0, 0);

    public PinpointLocalizer(HardwareMap hardwareMap, double inPerTick, Pose2d initialPose) {
        driver = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        double mmPerTick = inPerTick * 25.4;

        double xOffset = mmPerTick * PARAMS.perpXTicks, yOffset = mmPerTick * PARAMS.parYTicks;

        driver.setEncoderResolution(1 / mmPerTick, DistanceUnit.MM);
        driver.setOffsets(xOffset, yOffset, DistanceUnit.MM);
//        driver.setOffsets(mmPerTick * PARAMS.parYTicks, mmPerTick * PARAMS.perpXTicks, DistanceUnit.MM);

        initialParDirection = GoBildaPinpointDriver.EncoderDirection.FORWARD;
        initialPerpDirection = GoBildaPinpointDriver.EncoderDirection.FORWARD;

        driver.setEncoderDirections(initialParDirection, initialPerpDirection);

        driver.resetPosAndIMU();

        txWorldPinpoint = initialPose;
    }

    @Override
    public void setPose(Pose2d pose) {
        txWorldPinpoint = pose.times(txPinpointRobot.inverse());
    }

    @Override
    public Pose2d getPose() {
        return txWorldPinpoint.times(txPinpointRobot);
    }

    @Override
    public PoseVelocity2d update() {
        driver.update();

        Pose2d currentPose = getPose(); // however you already compute it
        long now = System.nanoTime();

        if (lastPose != null) {
            dt = (now - lastTimeNs) * 1e-9; // seconds

            if (dt > 1e-4) { // avoid divide-by-zero
                double vx = (currentPose.position.x - lastPose.position.x);
                double vy = (currentPose.position.y - lastPose.position.y);
                double omega = (currentPose.heading.toDouble()
                        - lastPose.heading.toDouble());

                poseVelocity = new Pose2d(vx, vy, omega);
            }
        }

        lastPose = currentPose;
        lastTimeNs = now;

        if (Objects.requireNonNull(driver.getDeviceStatus()) == GoBildaPinpointDriver.DeviceStatus.READY) {
            txPinpointRobot = new Pose2d(driver.getPosX(DistanceUnit.INCH), driver.getPosY(DistanceUnit.INCH), driver.getHeading(UnnormalizedAngleUnit.RADIANS));
            Vector2d worldVelocity = new Vector2d(driver.getVelX(DistanceUnit.INCH), driver.getVelY(DistanceUnit.INCH));
            Vector2d robotVelocity = Rotation2d.fromDouble(-txPinpointRobot.heading.log()).times(worldVelocity);

            return new PoseVelocity2d(robotVelocity, driver.getHeadingVelocity(UnnormalizedAngleUnit.RADIANS));
        }
        return new PoseVelocity2d(new Vector2d(0, 0), 0);
    }

    public Pose2d getPoseVelocity() {
        return poseVelocity;
    }
}
