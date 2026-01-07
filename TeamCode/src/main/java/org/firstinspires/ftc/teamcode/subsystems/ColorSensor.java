package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class ColorSensor {

    NormalizedColorSensor colorSensor;

    public void init(HardwareMap hw){
        colorSensor = hw.get(NormalizedColorSensor.class, "colorSensor");
        colorSensor.setGain(4);
    }

    public double getArtifactDistance(Telemetry telemetry){
        if (colorSensor instanceof DistanceSensor){
            telemetry.addData("Distance (cm)", "%.3f", ((DistanceSensor) colorSensor).getDistance(DistanceUnit.CM));
            return ((DistanceSensor) colorSensor).getDistance(DistanceUnit.CM);
        }
        return -1;
    }
}
