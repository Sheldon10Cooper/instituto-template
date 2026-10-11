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
    public static final double P = 0;
    public static final double F = 0;
    public static final double S = 0;
    private final VoltageSensor battery;

    public Shooter(HardwareMap hMap){
        shooter = hMap.get(DcMotorEx.class,"shooter");
        shooter.setDirection(DcMotorSimple.Direction.FORWARD);
        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        battery = hMap.voltageSensor.iterator().next();
    }
    public void PFSControlVelocity(double targetVel){
        double error = targetVel - shooter.getVelocity();

        double voltageComp = 12.0 / Math.max(battery.getVoltage(), 1.0);


        shooter.setPower(((P*error)+(F*targetVel)+(S * Math.signum(targetVel))*(voltageComp)));
    }
}
