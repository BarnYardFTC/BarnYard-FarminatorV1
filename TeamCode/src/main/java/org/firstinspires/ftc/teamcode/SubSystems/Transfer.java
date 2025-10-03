package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.Subsystem;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import java.util.Set;

public class Transfer extends SubsystemBase {

    private CRServo leftFrontTrans;
    private CRServo leftBackTrans;
    private CRServo rightFrontTrans;
    private CRServo rightBackTrans;

    public Transfer(HardwareMap hardwareMap){
        leftFrontTrans = hardwareMap.get(CRServo.class, "leftFrontTrans");
        leftBackTrans = hardwareMap.get(CRServo.class, "leftBackTrans");
        rightFrontTrans = hardwareMap.get(CRServo.class, "rightFrontTrans");
        rightBackTrans = hardwareMap.get(CRServo.class, "rightBackTrans");
    }

    public void transfer(){
        leftFrontTrans.setPower(1);
        leftBackTrans.setPower(1);
        rightFrontTrans.setPower(1);
        rightBackTrans.setPower(1);
    }

    public void stop(){
        leftFrontTrans.setPower(0);
        leftBackTrans.setPower(0);
        rightFrontTrans.setPower(0);
        rightBackTrans.setPower(0);
    }


}
