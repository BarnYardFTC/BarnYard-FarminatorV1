package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.subsystems.ColorSensor;

@TeleOp(name = "Color Sensor Test", group = "test")
public class ColorSensorTest extends OpMode {
    ColorSensor colorSensor = new ColorSensor();

    @Override
    public void init(){
        colorSensor.init(hardwareMap);
    }

    @Override
    public void loop(){
        colorSensor.getArtifactDistance(telemetry);
    }
}
