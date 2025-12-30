package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class BlinkinLED extends SubsystemBase {
    RevBlinkinLedDriver blinkin;
    RevBlinkinLedDriver.BlinkinPattern pattern;
    public BlinkinLED() {

    }
}
