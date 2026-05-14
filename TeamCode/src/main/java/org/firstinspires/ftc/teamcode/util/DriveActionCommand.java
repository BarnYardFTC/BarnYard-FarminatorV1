package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.seattlesolvers.solverslib.command.CommandBase;

/**
 * A CommandBase wrapper for executing Road Runner {@link Action} objects.
 *
 * This allows Road Runner actions to be scheduled and run within the command-based framework.
 * Each call to {@link #execute()} previews the action on the dashboard and updates telemetry.
 *
 * Example usage:
 * <pre>
 * DriveActionCommand cmd = new DriveActionCommand(trajectoryActionBuilder);
 * schedule(cmd);
 * </pre>
 */
public class DriveActionCommand extends CommandBase {

    /** The Road Runner action to execute. */
    private final Action action;

    /** Tracks whether the action has finished. */
    private boolean finished;

    /**
     * Creates a new DriveActionCommand from a {@link TrajectoryActionBuilder}.
     *
     * @param action the trajectory action builder
     */
    public DriveActionCommand(TrajectoryActionBuilder action) {
        this.action = action.build();
    }

    /**
     * Called once when the command is first scheduled.
     * Resets the finished flag.
     */
    @Override
    public void initialize() {
        finished = false;
    }

    /**
     * Called repeatedly while the command is active.
     * Previews the action on the FtcDashboard and runs it.
     */
    @Override
    public void execute() {
        TelemetryPacket packet = new TelemetryPacket();

        // Preview the action on the dashboard field overlay
        action.preview(packet.fieldOverlay());

        // Run the action and update the finished state
        finished = !action.run(packet);

        // Send telemetry to FtcDashboard
        FtcDashboard.getInstance().sendTelemetryPacket(packet);
    }

    /**
     * Returns true when the action has finished executing.
     *
     * @return true if the action is complete
     */
    @Override
    public boolean isFinished() {
        return finished;
    }
}
