package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Arm extends SubsystemBase {
    @Config
    public static class PIDConstants {
        public static double kP = 0.1;
        public static double kI = 0;
        public static double kD = 0;
    }
    private final PIDController pid = new PIDController(
            PIDConstants.kP, PIDConstants.kI, PIDConstants.kD);

    private DcMotorEx arm;

    private int targetPosition = 0;
    private double profiledPosition = 0;
    public static double vel = 20;

    public Arm(HardwareMap hMap){
        arm = hMap.get(DcMotorEx.class,"arm");
        arm.setDirection(DcMotorSimple.Direction.FORWARD);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        resetEncoders();
        pid.setTolerance(20);
        pid.setPID(PIDConstants.kP,PIDConstants.kI,PIDConstants.kD);


    }
    public void resetEncoders() {
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        targetPosition = 0;
    }
    public void setTargetPosition(int ticks) {
        targetPosition = ticks;
    }
    public int getTargetPosition() {
        return targetPosition;
    }
    public int getPos() {
        return arm.getCurrentPosition();
    }
    public boolean isAtTarget() {
        return pid.atSetPoint();
    }
    @Override
    public void periodic(){
        pid.setP(PIDConstants.kP);
        pid.setI(PIDConstants.kI);
        pid.setD(PIDConstants.kD);

        if (profiledPosition < targetPosition) {
            profiledPosition = Math.min(targetPosition, profiledPosition + vel);
        } else if (profiledPosition > targetPosition) {
            profiledPosition = Math.max(targetPosition, profiledPosition - vel);
        }
        double power = pid.calculate(getPos(), profiledPosition);
        arm.setPower(power);



    }



}
