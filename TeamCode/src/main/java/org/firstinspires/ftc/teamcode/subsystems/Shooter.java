package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Shooter {
    public DcMotorEx shooter;
    public HardwareMap hardwareMap;
    public Shooter(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;

        shooter = hardwareMap.get(DcMotorEx.class,"shooter");
        shooter.setDirection(DcMotorSimple.Direction.FORWARD);
    }
    public void shoot(){
        shooter.setPower(1);
    }

    public void stop(){
        shooter.setPower(0);
    }
    public double getPower(){
        return shooter.getPower();
    }
}
