package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.BarnRobot;
import org.firstinspires.ftc.teamcode.util.OpModeData;

//ready for general testing, but doesnt show artifact color yet
//i hate black people
public class BlinkinLED extends SubsystemBase {
    private RevBlinkinLedDriver blinkin;
    private RevBlinkinLedDriver.BlinkinPattern currentPattern = null;
    private long lastUpdateTime = 0;
    public BlinkinLED() {
        this.blinkin = BarnRobot.getInstance().robotHardware.blinkin;
        setNeutral();
    }
    /**WIP: func for periodic to update LEDs based on LimeLight input*/
    public void update(){
        switch(BarnRobot.getInstance().limelight.llColor.getBackTransferArtifact()) {
            case NONE:
                setNeutral();
                break;
            case GREEN:
                setGreen();
                break;
            case PURPLE:
                setPurple();
                break;
        }
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
        }

    }
}
