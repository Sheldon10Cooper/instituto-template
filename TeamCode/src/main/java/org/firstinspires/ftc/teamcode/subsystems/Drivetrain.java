package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Drivetrain {
    private final DcMotor leftMotor;
    private final DcMotor rightMotor;

    public Drivetrain(HardwareMap hMap){
        leftMotor = hMap.get(DcMotor.class, "leftMotor");
        rightMotor = hMap.get(DcMotor.class, "rightMotor");
        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void arcade(double drive, double turn){
        double left = drive - turn;
        double right = drive + turn;
        double max = Math.max(Math.abs(left), Math.abs(right));
        if (max > 1){
            left /= max;
            right /= max;
        }
        leftMotor.setPower(left);
        rightMotor.setPower(right);
    }

    public void stop(){
        arcade(0, 0);
    }
}