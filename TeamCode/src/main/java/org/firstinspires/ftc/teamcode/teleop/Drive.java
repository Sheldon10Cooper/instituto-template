package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot;

@TeleOp(name = "Drive", group = "TeleOp")
public class Drive extends CommandOpMode {
    private Robot bot;
    private boolean hasStarted = false;


    @Override
    public void initialize() {
        CommandScheduler.getInstance().reset();

        MultipleTelemetry telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        GamepadEx driver = new GamepadEx(gamepad1);
        GamepadEx operator = new GamepadEx(gamepad2);

        bot = new org.firstinspires.ftc.teamcode.Robot(hardwareMap, telem, driver, operator);
        bot.follower.setStartingPose(new Pose(72,72,Math.toRadians(0)));



    }
    @Override
    public void run(){
        if (!hasStarted) {
            bot.follower.startTeleopDrive(true);
            hasStarted = true;
        }

        bot.follower.setTeleOpDrive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x, false);


        super.run();
        bot.run();

    }
}
