package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class testshootermotor extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        shooter.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        waitForStart();

        while(opModeIsActive()){
            double velocity = shooter.getVelocity();

            if (gamepad1.a){
                shooter.setPower(1);
            } else {
                shooter.setPower(0);
            }

            telemetry.addData("Velocidad Maxima", velocity);
            telemetry.update();

        }

    }
}
