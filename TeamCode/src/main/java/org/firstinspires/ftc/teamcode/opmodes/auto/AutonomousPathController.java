package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;

import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

import java.util.Map;

public class AutonomousPathController {
    /**
     * Variables(3 types of enum variable, map for positions, and last pose)
     */
    public AutoPars.side color;
    public AutoPars.posDistance distance;
    public AutoPars.positions position;
    public Map<AutoPars.positions, Pose2d> positions;
    public Pose2d lastPose;

    /**
     * Team constructor
     *
     * @param side
     * @param pos
     */
    public AutonomousPathController(AutoPars.side side, AutoPars.posDistance pos) {
        color = side;
        distance = pos;
        setupVariables(color);
    }

    /**
     * Variables constructor
     * @param colors
     */
    public void setupVariables(AutoPars.side colors) {
        switch (colors) {
            case BLUE:
                positions = Map.ofEntries(
                        Map.entry(AutoPars.positions.START_CLOSE, new Pose2d(60, -15, Math.toRadians(225))),
                        Map.entry(AutoPars.positions.START_FAR, new Pose2d( 60, -15, Math.toRadians(225))),
                        Map.entry(AutoPars.positions.SHOOT_CLOSE, new Pose2d(45, 0, Math.toRadians(227))),
                        Map.entry(AutoPars.positions.SHOOT_FAR, new Pose2d(45, 0, Math.toRadians(227))),
                        Map.entry(AutoPars.positions.LEFT_COLLECT, new Pose2d(-11.5, -25, Math.toRadians(270))),
                        Map.entry(AutoPars.positions.MID_COLLECT, new Pose2d(12, -25, Math.toRadians(270))),
                        Map.entry(AutoPars.positions.FAR_COLLECT, new Pose2d(34.5, -25, Math.toRadians(270))),
                        Map.entry(AutoPars.positions.LEFT_LOAD_COLLECT, new Pose2d(50, -65, Math.toRadians(270))),
                        Map.entry(AutoPars.positions.MID_LOAD_COLLECT, new Pose2d(54, -65, Math.toRadians(270))),
                        Map.entry(AutoPars.positions.FAR_LOAD_COLLECT, new Pose2d(52, -65, Math.toRadians(270))),
                        Map.entry(AutoPars.positions.GATE_OPEN, new Pose2d(0, -50, Math.toRadians(90))),
                        Map.entry(AutoPars.positions.GATE_COLLECT, new Pose2d(10, -57, Math.toRadians(227))),
                        Map.entry(AutoPars.positions.PARK, new Pose2d(-40, -22, Math.toRadians(227)))
                );
                break;

            case RED:
                positions = Map.ofEntries(
                        Map.entry(AutoPars.positions.START_CLOSE, new Pose2d(-53.333, 45.5, Math.toRadians(135))),
                        Map.entry(AutoPars.positions.START_FAR, new Pose2d( 60, 15, Math.toRadians(135))),
                        Map.entry(AutoPars.positions.SHOOT_CLOSE, new Pose2d(23, -22, Math.toRadians(137))),
                        Map.entry(AutoPars.positions.SHOOT_FAR, new Pose2d(45, 0, Math.toRadians(137))),
                        Map.entry(AutoPars.positions.LEFT_COLLECT, new Pose2d(-11.5, 63, Math.toRadians(90))),
                        Map.entry(AutoPars.positions.MID_COLLECT, new Pose2d(12, 63, Math.toRadians(90))),
                        Map.entry(AutoPars.positions.FAR_COLLECT, new Pose2d(34.5, 63, Math.toRadians(90))),
                        Map.entry(AutoPars.positions.LEFT_LOAD_COLLECT, new Pose2d(50, 65, Math.toRadians(90))),
                        Map.entry(AutoPars.positions.MID_LOAD_COLLECT, new Pose2d(52, 65, Math.toRadians(90))),
                        Map.entry(AutoPars.positions.FAR_LOAD_COLLECT, new Pose2d(54, 65, Math.toRadians(90))),
                        Map.entry(AutoPars.positions.GATE_OPEN, new Pose2d(0, 50, Math.toRadians(270))),
                        Map.entry(AutoPars.positions.GATE_COLLECT, new Pose2d(10, 57, Math.toRadians(127))),
                        Map.entry(AutoPars.positions.PARK, new Pose2d(-40, 22, Math.toRadians(127)))
                );
                break;

            default:
                positions = Map.ofEntries(
                        Map.entry(AutoPars.positions.START_CLOSE, new Pose2d(0, 0, Math.toRadians(180)))
                );
        }
    }

    /**
     * Trajectory builder
     * @param position
     * @param drive
     * @return TrajectoryActionBuilder
     */
    public TrajectoryActionBuilder trajectories(AutoPars.positions position, RoadRunnerMecanumDrive drive) {
        TrajectoryActionBuilder path;

        switch (color) {
            case BLUE:
                if (distance == AutoPars.posDistance.CLOSE) {
                    lastPose = positions.get(AutoPars.positions.START_CLOSE);
                    switch (position) {
                        case SHOOT_CLOSE:
                            path = drive.actionBuilder(lastPose)
                                    .splineToConstantHeading(positions.get(position).component1(), positions.get(position).component2());
                            lastPose = positions.get(position);
                            return path;

                        case LEFT_COLLECT: case MID_COLLECT: case FAR_COLLECT:
                            path = drive.actionBuilder(lastPose)
                                    .strafeToLinearHeading(positions.get(position).component1(), positions.get(position).component2());
                            lastPose = positions.get(position);
                            return path;
                    }
                } else {
                    lastPose = positions.get(AutoPars.positions.START_FAR);
                    switch (position) {
                        case SHOOT_FAR:
                            path = drive.actionBuilder(lastPose)
                                    .splineToConstantHeading(positions.get(position).component1(), positions.get(position).component2());
                            lastPose = positions.get(position);
                            return path;

                        case LOAD_COLLECT:
                            path = drive.actionBuilder(lastPose)
                                    .splineToLinearHeading(positions.get(AutoPars.positions.LEFT_LOAD_COLLECT), Math.toRadians(270))
                                    .splineToConstantHeading(positions.get(AutoPars.positions.MID_LOAD_COLLECT).component1(), positions.get(AutoPars.positions.MID_LOAD_COLLECT).component2())
                                    .splineToConstantHeading(positions.get(AutoPars.positions.FAR_LOAD_COLLECT).component1(), positions.get(AutoPars.positions.FAR_LOAD_COLLECT).component2());
                            lastPose = positions.get(AutoPars.positions.FAR_LOAD_COLLECT);
                            return path;

                        case FAR_COLLECT:
                            path = drive.actionBuilder(lastPose)
                                    .strafeToLinearHeading(positions.get(position).component1(), positions.get(position).component2());
                            lastPose = positions.get(position);
                            return path;
                    }
                }
                break;

            case RED:
                if (distance == AutoPars.posDistance.CLOSE) {
                    lastPose = positions.get(AutoPars.positions.START_CLOSE);
                    switch (position) {
                        case SHOOT_CLOSE:
                            path = drive.actionBuilder(lastPose)
                                    .splineToConstantHeading(positions.get(position).component1(), positions.get(position).component2());
                            lastPose = positions.get(position);
                            return path;

                        case LEFT_COLLECT: case MID_COLLECT: case FAR_COLLECT:
                            path = drive.actionBuilder(lastPose)
                                    .strafeToLinearHeading(positions.get(position).component1(), positions.get(position).component2());
                            lastPose = positions.get(position);
                            return path;
                    }
                } else {
                    lastPose = positions.get(AutoPars.positions.START_FAR);
                    switch (position) {
                        case SHOOT_FAR:
                            path = drive.actionBuilder(lastPose)
                                    .splineToConstantHeading(positions.get(position).component1(), positions.get(position).component2());
                            lastPose = positions.get(position);
                            return path;

                        case LOAD_COLLECT:
                            path = drive.actionBuilder(lastPose)
                                    .splineToLinearHeading(positions.get(AutoPars.positions.FAR_LOAD_COLLECT), Math.toRadians(90))
                                    .splineToConstantHeading(positions.get(AutoPars.positions.MID_LOAD_COLLECT).component1(), positions.get(AutoPars.positions.MID_LOAD_COLLECT).component2())
                                    .splineToConstantHeading(positions.get(AutoPars.positions.LEFT_LOAD_COLLECT).component1(), positions.get(AutoPars.positions.LEFT_LOAD_COLLECT).component2());
                            lastPose = positions.get(AutoPars.positions.LEFT_LOAD_COLLECT);
                            return path;
                    }
                }
                break;
        }
        return null;
    }
}
