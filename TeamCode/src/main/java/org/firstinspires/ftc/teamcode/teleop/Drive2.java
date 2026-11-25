package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.kitbot;

@TeleOp(name = "Drive2 (two sticks)")
public class Drive2 extends OpMode {
    private kitbot robot;

    @Override
    public void init() {
        MultipleTelemetry telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        robot = new kitbot(hardwareMap, telem, gamepad1, gamepad2, kitbot.DriveStyle.TWO_STICK);
    }

    @Override
    public void loop() {
        robot.run();
    }
}