package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

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

    ElapsedTime timer = new ElapsedTime();
    boolean hasTimerStarted = false;

    public boolean isPosBusy(int distance){

        if (!hasTimerStarted) {
            timer.reset();
            hasTimerStarted = true;
        }

        if (timer.seconds() < 0.05) {
            return true;
        }

        return getArtifactDistance() < distance;
    }

    public boolean isShootPosBusy(){
        if (hasTimerStarted){
            timer.reset();
            hasTimerStarted = true;
        }

        if (timer.seconds() < 0.05){
            return true;
        }
        return getArtifactDistance() < 9;
    }

    public boolean isMidPosBusy(){
        if (hasTimerStarted){
            timer.reset();
            hasTimerStarted = true;
        }

        if (timer.seconds() < 0.05){
            return true;
        }
        return getArtifactDistance() < 5.5;
    }

    public boolean isIntakePosBusy(){
        if (hasTimerStarted){
            timer.reset();
            hasTimerStarted = true;
        }

        if (timer.seconds() < 0.05){
            return true;
        }
        return getArtifactDistance() < 6.5;
    }
}
