package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class ColorSensor {

    NormalizedColorSensor colorSensor;
    public posesIndexes posesIndex;

    public enum posesIndexes{SHOOTER, MIDDLE, INTAKE, SHOOTMID, SHOOTINTAKE, MIDINTAKE, ALL}

    public ColorSensor(NormalizedColorSensor colorSensor){
        this.colorSensor = colorSensor;
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

        if (timer.seconds() < 0.05){return true;}
        return getArtifactDistance() < 9;
    }

    public boolean isMidPosBusy(){
        if (hasTimerStarted){
            timer.reset();
            hasTimerStarted = true;
        }

        if (timer.seconds() < 0.05){return true;}
        return getArtifactDistance() < 5.5;
    }

    public boolean isIntakePosBusy(){
        if (hasTimerStarted){
            timer.reset();
            hasTimerStarted = true;
        }

        if (timer.seconds() < 0.05){return true;}
        return getArtifactDistance() < 6.5;
    }

    public posesIndexes getArtifactPoses() {
        if (isShootPosBusy() && isMidPosBusy() && isIntakePosBusy()){posesIndex = posesIndexes.ALL;}
        else if (isShootPosBusy() && isMidPosBusy()){posesIndex = posesIndexes.SHOOTMID;}
        else if (isShootPosBusy()) {posesIndex = posesIndexes.SHOOTER;}
        else if (isMidPosBusy()) {posesIndex = posesIndexes.MIDDLE;}
        else if (isIntakePosBusy()) {posesIndex = posesIndexes.INTAKE;}
        return posesIndex;
    }
}
