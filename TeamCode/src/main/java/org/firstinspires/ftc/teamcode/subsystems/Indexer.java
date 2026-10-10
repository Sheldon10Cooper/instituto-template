package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Indexer {
    private CRServo indexer;
    public Indexer(HardwareMap hMap){
        indexer = hMap.get(CRServo.class,"indexer");
        indexer.setDirection(DcMotorSimple.Direction.FORWARD);
    }
    public void index(){
        indexer.setPower(1);
    }
    public void stop(){
        indexer.setPower(0);
    }
}
