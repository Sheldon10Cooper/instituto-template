package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

@Config
public class Shooter {
    private final DcMotorEx shooter;

    //PFS needs to be tuned
    public static double P = 0;
    public static double F = 0;
    public static double S = 0;
    private final VoltageSensor battery;
    private double targetVel = 0;


    public Shooter(HardwareMap hMap){
        shooter = hMap.get(DcMotorEx.class,"shooter");
        shooter.setDirection(DcMotorSimple.Direction.FORWARD);
        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        battery = hMap.voltageSensor.iterator().next();
    }

    public void setTargetVelocity(double vel){
        targetVel = vel;
    }

    public void stop(){
        targetVel = 0;
    }

    public void update(){
        if (targetVel == 0){
            shooter.setPower(0);
            return;
        }
        double error = targetVel - shooter.getVelocity();
        double voltageComp = 12.0 / Math.max(battery.getVoltage(), 1.0);

        double power = (P * error) + (F * targetVel) + (S * targetVel);
        shooter.setPower(power * voltageComp);
    }

    public double getVelocity(){
        return shooter.getVelocity();
    }

    public double getTargetVelocity(){
        return targetVel;
    }
}
