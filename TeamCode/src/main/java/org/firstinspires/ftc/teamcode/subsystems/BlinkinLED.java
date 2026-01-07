package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.components.pipelines.ArtifactPipeline;
import org.firstinspires.ftc.teamcode.util.OpModeData;


public class BlinkinLED extends SubsystemBase {
    private final RevBlinkinLedDriver blinkin;
    private ArtifactPipeline artifactPipeline;
    private RevBlinkinLedDriver.BlinkinPattern currentPattern = null;
    private long lastUpdateTime = 0;
    public BlinkinLED() {
        this.blinkin = BarnRobot.getInstance().robotHardware.blinkin;
        setNeutral();
    }
    /**WIP: func for periodic to update LEDs based on sensor input*/
    public void update(){
        if (BarnRobot.getInstance().webcam.getShooterX() == -1) {
            setNeutral();
        }
        else setGreen();
    }

    private void setRed(){
        setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
    }
    private void setBlue(){
        setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);
    }

    private void setGreen(){
        setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
    }
    private void setPurple(){
        setPattern(RevBlinkinLedDriver.BlinkinPattern.VIOLET);
    }

    public Command setRedCommand() {
        return new InstantCommand(() -> setRed(), this);
    }

    public Command setBlueCommand() {
        return new InstantCommand(() -> setBlue(), this);
    }

    public Command setGreenCommand() {
        return new InstantCommand(() -> setGreen(), this);
    }

    public Command setPurpleCommand() {
        return new InstantCommand(() -> setPurple(), this);
    }

    /**Sets LEDs to team color*/
    public void setNeutral(){
        if (BarnRobot.getInstance().opmodeData.allianceColor == OpModeData.AllianceColor.BLUE) {
            setBlue();
        }
        else {setRed();}
    }

    /**Sets LED pattern with optimisation*/
    private void setPattern(RevBlinkinLedDriver.BlinkinPattern newPattern){
        long now = System.currentTimeMillis();
        if(newPattern != currentPattern && now - lastUpdateTime > 250){
            blinkin.setPattern(newPattern);
            currentPattern = newPattern;
            lastUpdateTime = System.currentTimeMillis();
        }

    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

}
