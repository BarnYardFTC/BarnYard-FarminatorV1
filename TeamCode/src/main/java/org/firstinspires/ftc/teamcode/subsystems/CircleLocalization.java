package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.roadrunner.Pose2d;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.OpModeData;
import org.firstinspires.ftc.teamcode.util.Point;

public class CircleLocalization extends SubsystemBase {

    // Check these coordinates! Are they really only 3 inches apart?
    private static final Point GOAL_BLUE = new Point(-64.96, -1.45);
    private static final Point GOAL_RED = new Point(-64.96, 1.45);

    public CircleLocalization(){
    }

    public Pose2d getPosition(){
        Point goalA, goalB;
        double rA, rB;

        if(BarnRobot.getInstance().opmodeData.allianceColor == OpModeData.AllianceColor.BLUE){
            goalA = GOAL_BLUE;
            goalB = GOAL_RED;

            rA = BarnRobot.getInstance().limelight.getGoalDistance();
            rB = BarnRobot.getInstance().webcam.getDistanceToGoal();
        }
        else {
            goalA = GOAL_RED;
            goalB = GOAL_BLUE;

            rA = BarnRobot.getInstance().limelight.getGoalDistance();
            rB = BarnRobot.getInstance().webcam.getDistanceToGoal();
        }

        double d = Math.hypot(goalB.x - goalA.x, goalB.y - goalA.y);

        if (d > rA + rB || d < Math.abs(rA - rB) || d == 0) {
            return null;
        }

        double a = (Math.pow(rA, 2) - Math.pow(rB, 2) + Math.pow(d, 2)) / (2 * d);

        double h = Math.sqrt(Math.max(0, Math.pow(rA, 2) - Math.pow(a, 2)));

        double x2 = goalA.x + a * (goalB.x - goalA.x) / d;
        double y2 = goalA.y + a * (goalB.y - goalA.y) / d;


        double x3_1 = x2 + h * (goalB.y - goalA.y) / d;
        double y3_1 = y2 - h * (goalB.x - goalA.x) / d;

        double x3_2 = x2 - h * (goalB.y - goalA.y) / d;
        double y3_2 = y2 + h * (goalB.x - goalA.x) / d;

        Point p1 = new Point(x3_1, y3_1);
        Point p2 = new Point(x3_2, y3_2);


        Point finalPos = isValidLocation(p1) ? p1 : p2;

        double heading = calculateHeading(finalPos, goalA, -BarnRobot.getInstance().limelight.getGoalYaw());

        return new Pose2d(finalPos.x, finalPos.y, heading);
    }

    public static double calculateHeading(Point robotPos, Point goalPos, double cameraBearingDegrees) {
        double absoluteAngleToGoalRad = Math.atan2(goalPos.y - robotPos.y, goalPos.x - robotPos.x);
        double absoluteAngleToGoalDeg = Math.toDegrees(absoluteAngleToGoalRad);

        double robotHeading = absoluteAngleToGoalDeg - cameraBearingDegrees;

        return normalizeAngle(robotHeading);
    }

    private static double normalizeAngle(double degrees) {
        while (degrees > 180) degrees -= 360;
        while (degrees < -180) degrees += 360;
        return degrees;
    }

    private boolean isValidLocation(Point p){
        double fieldLimit = 72.0;
        return (Math.abs(p.x) <= fieldLimit && Math.abs(p.y) <= fieldLimit);
    }
}