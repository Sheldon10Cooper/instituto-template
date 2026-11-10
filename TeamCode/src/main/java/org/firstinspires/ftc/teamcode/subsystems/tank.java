package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class tank {
    public DcMotor leftMotor;
    public DcMotor rightMotor;
    public Gamepad gamepad;

    public tank(HardwareMap hMap){
        leftMotor = hMap.get(DcMotor.class, "leftMotor");
        rightMotor = hMap.get(DcMotor.class, "rightMotor");

        //leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        //rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void drive(double drive, double turn){
        double leftPower = drive;
        double rightPower = turn;

        leftMotor.setPower((leftPower)+(rightPower));
        rightMotor.setPower((leftPower)-(rightPower));

    }
}

