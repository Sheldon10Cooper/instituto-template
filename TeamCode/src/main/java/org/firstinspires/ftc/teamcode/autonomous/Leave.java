package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

@Config
@Autonomous(name = "Auto: Leave Only")
public class Leave extends OpMode {
    public static double START_DELAY = 0.0;
    public static double DRIVE_TIME = 1.0;
    public static double DRIVE_POWER = 0.5;

    private Drivetrain drivetrain;
    private final ElapsedTime timer = new ElapsedTime();

    @Override
    public void init() {
        drivetrain = new Drivetrain(hardwareMap);
    }

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void loop() {
        double t = timer.seconds();
        if (t >= START_DELAY && t < START_DELAY + DRIVE_TIME) {
            drivetrain.arcade(DRIVE_POWER, 0);
        } else {
            drivetrain.stop();
        }
        telemetry.addData("Time", t);
        telemetry.update();
    }
}