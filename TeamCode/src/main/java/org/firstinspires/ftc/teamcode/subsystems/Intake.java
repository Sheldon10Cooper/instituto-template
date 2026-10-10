package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    private DcMotorEx intake;
    private CRServo roller1;
    private CRServo roller2;

    public Intake(HardwareMap hMap){
        intake = hMap.get(DcMotorEx.class, "intake");
        intake.setDirection(DcMotorSimple.Direction.FORWARD);

        roller1 = hMap.get(CRServo.class,"roller1");
        roller1.setDirection(DcMotorSimple.Direction.FORWARD);

        roller2 = hMap.get(CRServo.class,"roller2");
        roller2.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void in(){
        intake.setPower(1);
        roller1.setPower(1);
        roller2.setPower(1);
    }
    public void stop(){
        intake.setPower(0);
        roller1.setPower(0);
        roller2.setPower(0);
    }

}
