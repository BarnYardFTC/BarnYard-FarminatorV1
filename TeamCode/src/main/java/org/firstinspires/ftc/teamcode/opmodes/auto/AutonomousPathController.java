package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.libraries.roadrunner.RoadRunnerMecanumDrive;

import java.util.Map;

public class AutonomousPathController {
    /**
     * Variables(3 types of enum variable, map for positions, and last pose)
     */
    public AutoPars.side color;
    public AutoPars.posDistance distance;
    //    public AutoPars.positions position;
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
        if (distance == AutoPars.posDistance.CLOSE) {
            lastPose = positions.get(AutoPars.positions.START_CLOSE);
        }
        else {
            lastPose = positions.get(AutoPars.positions.START_FAR);
        }
    }

    /**
     * Variables constructor
     * @param colors
     */
    public void setupVariables(AutoPars.side colors) {
        switch (colors) {
            case BLUE:
                positions = Map.ofEntries(
                        Map.entry(AutoPars.positions.START_CLOSE, new Pose2d(-53.333, -45.5, Math.toRadians(225))),
                        Map.entry(AutoPars.positions.SHOOT_CLOSE, new Pose2d(-23, -22, Math.toRadians(227))),
                        Map.entry(AutoPars.positions.SHOOT_FAR, new Pose2d(45, 0, Math.toRadians(227))),
                        Map.entry(AutoPars.positions.LEFT_COLLECT, new Pose2d(-11.5, -53, Math.toRadians(270))),
                        Map.entry(AutoPars.positions.LEFT_READY_COLLECT, new Pose2d(-11.5, -26, Math.toRadians(270))),
                        Map.entry(AutoPars.positions.MID_COLLECT, new Pose2d(12, -53, Math.toRadians(270))),
                        Map.entry(AutoPars.positions.MID_READY_COLLECT, new Pose2d(12, -63, Math.toRadians(270))),
                        Map.entry(AutoPars.positions.FAR_COLLECT, new Pose2d(37, -53, Math.toRadians(270))),
                        Map.entry(AutoPars.positions.FAR_READY_COLLECT, new Pose2d(34.5, -63, Math.toRadians(270))),
                        Map.entry(AutoPars.positions.LEFT_LOAD_COLLECT, new Pose2d(50, -65, Math.toRadians(270))),
                        Map.entry(AutoPars.positions.MID_LOAD_COLLECT, new Pose2d(54, -65, Math.toRadians(270))),
                        Map.entry(AutoPars.positions.FAR_LOAD_COLLECT, new Pose2d(52, -65, Math.toRadians(270))),
                        Map.entry(AutoPars.positions.GATE_OPEN, new Pose2d(0, -50, Math.toRadians(90))),
                        Map.entry(AutoPars.positions.GATE_COLLECT, new Pose2d(10, -57, Math.toRadians(227))),
                        Map.entry(AutoPars.positions.PARK, new Pose2d(-40, -22, Math.toRadians(227))),
                        Map.entry(AutoPars.positions.LEFT_SHOOT, new Pose2d(-11.5, -26, Math.toRadians(210))),
                        Map.entry(AutoPars.positions.MID_SHOOT, new Pose2d(12, -26, Math.toRadians(205))),
                        Map.entry(AutoPars.positions.FAR_SHOOT, new Pose2d(35, -26, Math.toRadians(200))),
                        Map.entry(AutoPars.positions.SHOOT_NUDGE, new Pose2d(-53.333, -40.5, Math.toRadians(270)))
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
    TranslationalVelConstraint fastToShoot = new TranslationalVelConstraint(150);
    /**
     * Trajectory builder
     * @param position
     * @param drive
     * @return TrajectoryActionBuilder
     */
    public TrajectoryActionBuilder trajectories(AutoPars.positions position, RoadRunnerMecanumDrive drive, Telemetry telemetry) {
        TrajectoryActionBuilder path = drive.actionBuilder(lastPose);
        switch (color) {
            case BLUE:
                if (distance == AutoPars.posDistance.CLOSE) {
                    switch (position) {
                        case LEFT_SHOOT:
                            path = drive.actionBuilder(lastPose)
                                    .strafeToLinearHeading(positions.get(AutoPars.positions.LEFT_SHOOT).component1(), positions.get(AutoPars.positions.LEFT_SHOOT).component2(), fastToShoot);
                            break;
                        case MID_SHOOT:
                            path = drive.actionBuilder(lastPose)
                                    .strafeToLinearHeading(positions.get(AutoPars.positions.MID_SHOOT).component1(), positions.get(AutoPars.positions.MID_SHOOT).component2(), fastToShoot);
                            break;
                        case FAR_SHOOT:
                            path = drive.actionBuilder(lastPose)
                                    .strafeToLinearHeading(positions.get(AutoPars.positions.FAR_SHOOT).component1(),positions.get(AutoPars.positions.FAR_SHOOT).component2());
                            break;
                        case LEFT_COLLECT:
                            path = drive.actionBuilder(lastPose)
                                    .strafeToLinearHeading(positions.get(AutoPars.positions.SHOOT_NUDGE).component1(), positions.get(AutoPars.positions.SHOOT_NUDGE).component2())
                                    .splineToConstantHeading(positions.get(AutoPars.positions.LEFT_READY_COLLECT).component1(), positions.get(AutoPars.positions.LEFT_READY_COLLECT).component2())
                                    // keep Rotation2d hardcoded (as requested)
                                    .splineToConstantHeading(positions.get(AutoPars.positions.LEFT_COLLECT).component1(), new Rotation2d(0, 0));
                            break;
                        case MID_COLLECT:
                            path = drive.actionBuilder(lastPose)
                                    .splineToLinearHeading(positions.get(AutoPars.positions.MID_COLLECT), new Rotation2d(0, -5));
                            break;
                        case FAR_COLLECT:
                            path = drive.actionBuilder(lastPose)
                                    .splineToLinearHeading(positions.get(AutoPars.positions.FAR_COLLECT), new Rotation2d(0, -4));
                    }
                }
                break;
        }
        lastPose = positions.get(position);
        telemetry.addData("Position: ", position);
        telemetry.addData("Last Pose: ", lastPose.position);

        return path;
    }
}