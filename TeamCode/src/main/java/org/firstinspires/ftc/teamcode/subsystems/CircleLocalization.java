package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.roadrunner.Pose2d;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.Point;

public class CircleLocalization extends SubsystemBase {

    // Goal coordinates in INCHES
    private static final Point GOAL_BLUE = new Point(-64.96, -57.09);
    private static final Point GOAL_RED = new Point(-64.96, 57.09);

    public CircleLocalization(){
    }

    public Pose2d getPosition(){
        Point goalA, goalB;
        double rA, rB; // Radii (distances)

        double METERS_TO_INCHES = 39.3701;

        // 1. Assign Goals and Distances based on Alliance
        if(BarnRobot.getInstance().opmodeData.allianceColor == OpModeData.AllianceColor.BLUE){
            goalA = GOAL_BLUE;
            goalB = GOAL_RED;

            // rA is distance to Blue (Limelight), rB is distance to Red (Webcam)
            rA = BarnRobot.getInstance().limelight.getGoalDistance() * METERS_TO_INCHES;
            rB = BarnRobot.getInstance().webcam.getDistanceToGoal() * METERS_TO_INCHES;
        }
        else {
            goalA = GOAL_RED;
            goalB = GOAL_BLUE;

            // rA is distance to Red (Limelight), rB is distance to Blue (Webcam)
            rA = BarnRobot.getInstance().limelight.getGoalDistance() * METERS_TO_INCHES;
            rB = BarnRobot.getInstance().webcam.getDistanceToGoal() * METERS_TO_INCHES;
        }

        // Distance between the two goals
        double d = Math.hypot(goalB.x - goalA.x, goalB.y - goalA.y);

        // --- TOLERANCE CHECK ---
        // If sensors are noisy, d might be slightly larger than rA + rB.
        // We clamp the values to prevent the math from returning null/NaN.

        // If circles are too far apart, assume they "touch" at the closest point
        if (d > rA + rB) {
            // Scale rA and rB to exactly meet
            double scale = d / (rA + rB);
            rA *= scale;
            rB *= scale;
        }

        // If one circle is inside the other (impossible), we can't solve.
        // We allow a small error (8 inches) before giving up.

        BarnRobot.getInstance().telemetry.addData("d", d);

        BarnRobot.getInstance().telemetry.addData("sum", rA + rB);

        BarnRobot.getInstance().telemetry.addData("diff", Math.abs(rA - rB));
        BarnRobot.getInstance().telemetry.addData("lamlam:", BarnRobot.getInstance().limelight.getGoalDistance());
        BarnRobot.getInstance().telemetry.addData("webc:", BarnRobot.getInstance().webcam.getDistanceToGoal());
        if (d < Math.abs(rA - rB) - 8.0 || d == 0) {
            return null;
        }

        // --- THE MATH ---

        // Calculate 'a' (distance from Goal A to the perpendicular line)
        double a = (Math.pow(rA, 2) - Math.pow(rB, 2) + Math.pow(d, 2)) / (2 * d);

        // Calculate 'h' (height of intersection).
        // Math.max(0, ...) ensures we never take sqrt of negative number.
        double h = Math.sqrt(Math.max(0, Math.pow(rA, 2) - Math.pow(a, 2)));

        // Find Anchor Point (P2)
        double x2 = goalA.x + a * (goalB.x - goalA.x) / d;
        double y2 = goalA.y + a * (goalB.y - goalA.y) / d;

        // Find Intersection Points
        double x3_1 = x2 + h * (goalB.y - goalA.y) / d;
        double y3_1 = y2 - h * (goalB.x - goalA.x) / d;

        double x3_2 = x2 - h * (goalB.y - goalA.y) / d;
        double y3_2 = y2 + h * (goalB.x - goalA.x) / d;

        Point p1 = new Point(x3_1, y3_1);
        Point p2 = new Point(x3_2, y3_2);

        // Select the valid point (inside the field)
        Point finalPos = isValidLocation(p1) ? p1 : p2;

        // Calculate heading using your requested Yaw method
        double heading = calculateHeading(finalPos, goalA, BarnRobot.getInstance().limelight.getGoalYaw());

        return new Pose2d(finalPos.x, finalPos.y, heading);
    }

    private static double calculateHeading(Point robotPos, Point goalPos, double cameraAngle) {
        double absoluteAngleToGoalRad = Math.atan2(goalPos.y - robotPos.y, goalPos.x - robotPos.x);
        double absoluteAngleToGoalDeg = Math.toDegrees(absoluteAngleToGoalRad);

        double robotHeading = absoluteAngleToGoalDeg - cameraAngle;

        return normalizeAngle(robotHeading);
    }

    private static double normalizeAngle(double degrees) {
        while (degrees > 180) degrees -= 360;
        while (degrees < -180) degrees += 360;
        return degrees;
    }

    private boolean isValidLocation(Point p){
        // Expanded slightly to 75 to account for robot being near wall
        return (Math.abs(p.x) <= 75.0 && Math.abs(p.y) <= 75.0);
    }

    public void displayTelemetry(){
        Pose2d currentPose = getPosition();
        if(currentPose != null){
            BarnRobot.getInstance().telemetry.addData("Robot Pose:", "(" + currentPose.position.x + ", " + currentPose.position.y + ")");
            BarnRobot.getInstance().telemetry.addData("Robot Heading:", currentPose.heading.toDouble());
        } else {
            BarnRobot.getInstance().telemetry.addData("Robot Pose:", "Calculating...");
        }
    }
}