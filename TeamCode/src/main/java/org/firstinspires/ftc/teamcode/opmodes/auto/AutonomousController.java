package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;

import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

import java.util.Map;
import java.util.function.BooleanSupplier;

public class AutonomousController {
    public AutoPars.side color;
    public AutoPars.posDistance distance;
    public AutoPars.positions position;
    public Map<AutoPars.positions, Pose2d> positions;

    public void setTeamPars(AutoPars.side side, AutoPars.posDistance pos) {
        color = side;
        distance = pos;
        setupVariables(color);
    }

    public void setupVariables(AutoPars.side colors) {
        switch (colors) {
            case BLUE:
                positions = Map.ofEntries(
                        Map.entry(AutoPars.positions.START, new Pose2d(60, -15, 225)),
                        Map.entry(AutoPars.positions.SHOOT, new Pose2d(45, 0, 227)),
                        Map.entry(AutoPars.positions.LEFT_COLLECT, new Pose2d(-11.5, -25, 270)),
                        Map.entry(AutoPars.positions.MID_COLLECT, new Pose2d(12, -25, 270)),
                        Map.entry(AutoPars.positions.FAR_COLLECT, new Pose2d(34.5, -25, 270)),
                        Map.entry(AutoPars.positions.LEFT_LOAD_COLLECT, new Pose2d(50, -65, 270)),
                        Map.entry(AutoPars.positions.MID_LOAD_COLLECT, new Pose2d(54, -65, 270)),
                        Map.entry(AutoPars.positions.FAR_LOAD_COLLECT, new Pose2d(52, -65, 270)),
                        Map.entry(AutoPars.positions.GATE_OPEN, new Pose2d(0, -50, 90)),
                        Map.entry(AutoPars.positions.GATE_COLLECT, new Pose2d(10, -57, 227)),
                        Map.entry(AutoPars.positions.PARK, new Pose2d(-40, -22, 227))
                );
                break;

            case RED:
                positions = Map.ofEntries(
                        Map.entry(AutoPars.positions.START, new Pose2d(-53.333, 45.5, 135)),
                        Map.entry(AutoPars.positions.SHOOT, new Pose2d(23, -22, 137)),
                        Map.entry(AutoPars.positions.LEFT_COLLECT, new Pose2d(-11.5, 63, 90)),
                        Map.entry(AutoPars.positions.MID_COLLECT, new Pose2d(12, 63, 90)),
                        Map.entry(AutoPars.positions.FAR_COLLECT, new Pose2d(34.5, 63, 90)),
                        Map.entry(AutoPars.positions.LEFT_LOAD_COLLECT, new Pose2d(50, 65, 90)),
                        Map.entry(AutoPars.positions.MID_LOAD_COLLECT, new Pose2d(52, 65, 90)),
                        Map.entry(AutoPars.positions.FAR_LOAD_COLLECT, new Pose2d(54, 65, 90)),
                        Map.entry(AutoPars.positions.GATE_OPEN, new Pose2d(0, 50, 270)),
                        Map.entry(AutoPars.positions.GATE_COLLECT, new Pose2d(10, 57, 127)),
                        Map.entry(AutoPars.positions.PARK, new Pose2d(-40, 22, 127))
                );
                break;

            default:
                positions = Map.ofEntries(
                        Map.entry(AutoPars.positions.START, new Pose2d(0, 0, 180))
                );
        }
    }

//    public static void driveTo(AutoPars.side colors, AutoPars.posDistance pos, RoadRunnerMecanumDrive drive, Pose2d lastPose, Map<AutoPars.positions, Pose2d> positions){
//        switch (colors){
//            case BLUE:
//                switch (pos){
//                    case CLOSE:
//                        AutoPars.positions.SHOOT
//                        drive.actionBuilder(lastPose)
//                                .strafeToLinearHeading(positions.get(AutoPars.positions.SHOOT).component1(), positions.get(AutoPars.positions.SHOOT).component2());
//
//
//                    case FAR:
//
//                }
//
//            case RED:
//                switch(pos){
//                    case CLOSE:
//
//                    case FAR:
//
//                }
//            default:
//
//        }
//    }

    Pose2d lastPose = positions.get(AutoPars.positions.START);

//    public TrajectoryActionBuilder path(AutoPars.positions pos) {
//
//    }

}
