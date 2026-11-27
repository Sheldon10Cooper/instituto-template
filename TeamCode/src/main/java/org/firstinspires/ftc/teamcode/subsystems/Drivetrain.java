package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.acmerobotics.dashboard.config.Config;
@Config
public class Drivetrain {
    private final DcMotor leftMotor;
    private final DcMotor rightMotor;
    public static double DRIVE_CURVE = 0.6;
    public static double TURN_CURVE = 0.4;

    private static double curve(double x, double k){
        return (1 - k) * x + k * x * x * x;
    }

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
    public void arcadeCurved(double drive, double turn){
        arcade(curve(drive, DRIVE_CURVE), curve(turn, TURN_CURVE));
    }

    public void stop(){
        arcade(0, 0);
    }
    public void resetEncoders(){
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public int getLeftPosition(){
        return leftMotor.getCurrentPosition();
    }

    public int getRightPosition(){
        return rightMotor.getCurrentPosition();
    }
}