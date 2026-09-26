package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake extends SubsystemBase {
    private DcMotorEx intake;
    private Telemetry telemetry;
    public Intake(HardwareMap hMap, Telemetry telemetry){
        this.telemetry = telemetry;
        intake = hMap.get(DcMotorEx.class,"intake");
    }
    public void setPower(double power){
        intake.setPower(power);
    }
    public double getPower(){
        return intake.getPower();
    }
}
