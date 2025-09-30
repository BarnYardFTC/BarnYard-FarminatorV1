package org.firstinspires.ftc.teamcode.Commands;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RepeatCommand;

public class TelemetryCommandGroup {

    public static Command displayTelemetry() {
        return new RepeatCommand(
                new InstantCommand(() -> telemetry.addLine("My first solverslib program!"))
        );
    }

}
