package org.firstinspires.ftc.teamcode;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.Robot;
import com.seattlesolvers.solverslib.command.button.GamepadButton;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.arcade;
import org.firstinspires.ftc.teamcode.subsystems.tank;

public class Robot2 extends Robot {
    public final HardwareMap hMap;
    public final Telemetry telemetry;
    public static GamepadEx DriverGamepad;
    public static GamepadEx OperatorGamepad;

    public tank tank;
    public Indexer indexer;
    public Intake intake;
    public Shooter shooter;
    public Gamepad g1;
    public Gamepad g2;
    private double deadband(double value){
        return Math.abs(value) < 0.05 ? 0 : value;
    }

    public Robot2(HardwareMap hMap, Telemetry telemetry, GamepadEx driverGamepad, GamepadEx operatorGamepad, Gamepad g1, Gamepad g2) {
        this.hMap = hMap;
        this.telemetry = telemetry;
        this.g1 = g1;
        this.g2 = g2;

        DriverGamepad = driverGamepad;
        OperatorGamepad = operatorGamepad;

        initSubsystems();
        configureButtons();

    }

    private void initSubsystems() {

        indexer = new Indexer(hMap);
        intake = new Intake(hMap);
        shooter = new Shooter(hMap);
        tank = new tank(hMap);

    }

    private void configureButtons() {

    }

    @Override
    public void run() {
        tank.drive(deadband(-g1.left_stick_y), deadband(g1.right_stick_x));

        if (OperatorGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.3) {
            intake.in();
        } else if (OperatorGamepad.getButton(GamepadKeys.Button.A)) {
            intake.out();
        } else {
            intake.stop();
        }

        if (OperatorGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.3) {
            shooter.setTargetVelocity(1500  );
        } else if (OperatorGamepad.getButton(GamepadKeys.Button.B)) {
            shooter.setTargetVelocity(-500); // slow reverse to clear jams
        } else {
            shooter.stop();
        }
        shooter.update();

        boolean feed = OperatorGamepad.getButton(GamepadKeys.Button.LEFT_BUMPER)
                && shooter.getTargetVelocity() > 0 && shooter.atTargetVelocity();
        boolean override = OperatorGamepad.getButton(GamepadKeys.Button.RIGHT_BUMPER);
        if (feed || override) {
            indexer.index();
        } else if (OperatorGamepad.getButton(GamepadKeys.Button.X)) {
            indexer.reverse();
        } else {
            indexer.stop();
        }

        telemetry.addData("Shooter velocity", shooter.getVelocity());
        telemetry.addData("Shooter target", shooter.getTargetVelocity());
        telemetry.addData("Shooter ready", shooter.atTargetVelocity());
        telemetry.update();
    }
}