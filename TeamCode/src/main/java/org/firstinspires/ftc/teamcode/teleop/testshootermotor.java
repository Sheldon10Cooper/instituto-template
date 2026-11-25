package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class testshootermotor extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        shooter.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        double maxVelocity = 0;
        waitForStart();

        while (opModeIsActive()) {
            double velocity = shooter.getVelocity();

            if (gamepad1.a) {
                shooter.setPower(1);
                maxVelocity = Math.max(maxVelocity, Math.abs(velocity));
            } else {
                shooter.setPower(0);
            }
            if (gamepad1.b) maxVelocity = 0; // reset

            telemetry.addData("Velocity", velocity);
            telemetry.addData("Max velocity", maxVelocity);
            telemetry.addData("Suggested F", maxVelocity > 0 ? 1.0 / maxVelocity : 0);
            telemetry.update();
        }
    }
}