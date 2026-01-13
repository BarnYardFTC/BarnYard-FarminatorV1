package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.BarnRobot;

public class ColorSensor {

    NormalizedColorSensor colorSensor;


    public ColorSensor(){
        colorSensor = BarnRobot.getInstance().robotHardware.shooterColorSensor;
        colorSensor.setGain(4);
    }

    public void printArtifactDistance(Telemetry telemetry){
        if (colorSensor instanceof DistanceSensor){
            telemetry.addData("Distance (cm)", "%.3f", ((DistanceSensor) colorSensor).getDistance(DistanceUnit.CM));
        }
    }

    public double getArtifactDistance(){
        if (colorSensor instanceof DistanceSensor){
            return ((DistanceSensor) colorSensor).getDistance(DistanceUnit.CM);
        }
        else{
            return -1;
        }
    }

    public boolean isPosBusy(){
        return getArtifactDistance() < 6;
    }
}
