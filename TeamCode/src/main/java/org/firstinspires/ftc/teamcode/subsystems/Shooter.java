package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

@Config
public class Shooter {
    private final DcMotorEx shooter;
    private final VoltageSensor battery;

    public static double P = 0;
    public static double F = 0.00042;
    public static double S = 0;
    public static double TOLERANCE = 50;

    private double targetVel = 0;
    private double currentVel = 0;

    public Shooter(HardwareMap hMap){
        shooter = hMap.get(DcMotorEx.class, "shooter");
        shooter.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER); // only our controller runs
        shooter.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        shooter.setDirection(DcMotorEx.Direction.FORWARD);
        battery = hMap.voltageSensor.iterator().next();
    }

    public void setTargetVelocity(double vel){ targetVel = vel; }
    public double getVelocity(){ return currentVel; }
    public double getTargetVelocity(){ return targetVel; }

    public boolean atTargetVelocity(){
        return targetVel != 0 && Math.abs(targetVel - currentVel) <= TOLERANCE;
    }

    public void stop(){
        targetVel = 0;
        shooter.setPower(0);
    }

    public void update(){
        currentVel = shooter.getVelocity();
        if (targetVel == 0){
            shooter.setPower(0);
            return;
        }
        double error = targetVel - currentVel;
        double voltageComp = 12.0 / Math.max(battery.getVoltage(), 1.0);
        double feedforward = (F * targetVel) + (Math.signum(targetVel) * S);
        double power = (P * error) + (feedforward * voltageComp);
        shooter.setPower(Math.max(-1.0, Math.min(1.0, power)));
    }
}