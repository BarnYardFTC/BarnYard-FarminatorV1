package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.ColorSensor;

@TeleOp(name = "Color Sensor Test", group = "test")
public class ColorSensorTest extends OpMode {
    ColorSensor colorSensor;

    @Override
    public void init(){
        colorSensor = new ColorSensor();
    }

    @Override
    public void loop(){
        colorSensor.printArtifactDistance(telemetry);
    }
}
