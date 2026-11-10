package org.firstinspires.ftc.teamcode.subsystems;


import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad2;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class arcade {
    public DcMotor leftMotor;
    public DcMotor rightMotor;
    public Gamepad gamepad;

    public arcade(HardwareMap hMap){
        leftMotor = hMap.get(DcMotor.class, "leftMotor");
        rightMotor = hMap.get(DcMotor.class, "rightMotor");

        //leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        //rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void drive(double drive, double turn){
        double leftPower = drive + turn;
        double rightPower = drive - turn;

        double max = Math.max(Math.abs(leftPower), Math.abs(rightPower));
        if (max > 1){
            leftPower = leftPower / max;
            rightPower = rightPower / max;
        }

        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);
    }

}
