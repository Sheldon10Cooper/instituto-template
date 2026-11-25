package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Indexer {
    private CRServo indexer;
    private double deadband(double value){
        return Math.abs(value) < 0.05 ? 0 : value;
    }
    public Indexer(HardwareMap hMap){
        indexer = hMap.get(CRServo.class,"indexer");
        indexer.setDirection(CRServo.Direction.REVERSE);
    }
    public void index(){
        indexer.setPower(1);
    }
    public void stop(){
        indexer.setPower(0);
    }
    public void reverse(){
        indexer.setPower(-1);
    }
}
