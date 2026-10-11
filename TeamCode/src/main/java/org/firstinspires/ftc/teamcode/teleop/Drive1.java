package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.Robot1;

@TeleOp
public class Drive1 extends CommandOpMode {
    public Robot1 robot1;

    @Override
    public void initialize() {
        CommandScheduler.getInstance().reset();

        MultipleTelemetry telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        GamepadEx driver = new GamepadEx(gamepad1);
        GamepadEx operator = new GamepadEx(gamepad2);

        robot1 = new Robot1(hardwareMap, telem, driver,operator,gamepad1,gamepad2);
    }
    @Override
    public void run(){
        super.run();
        robot1.run();
    }
}
