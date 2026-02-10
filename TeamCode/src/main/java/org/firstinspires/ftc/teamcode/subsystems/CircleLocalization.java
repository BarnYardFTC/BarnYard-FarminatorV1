package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.roadrunner.Pose2d;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.sun.tools.javac.util.Position;

import org.firstinspires.ftc.teamcode.BarnRobot;

import java.sql.BatchUpdateException;
import java.sql.DatabaseMetaData;

public class CircleLocalization extends SubsystemBase {
    private static final double GOAL_X = -64.96;

    /** Goal Y positions depending on alliance (inches) */
    private static final double BLUE_GOAL_Y = -1.45;
    private static final double RED_GOAL_Y  =  1.45;


    public CircleLocalization(){

    }

    public Pose2d getPosition(){
        double lamlamDistance = BarnRobot.getInstance().limelight.getGoalDistance();
        double webcamDistance = 0;//temp

        return (new Pose2d(webcamDistance, lamlamDistance, 0));

    }
}
