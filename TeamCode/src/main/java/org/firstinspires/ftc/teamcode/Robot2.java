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
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
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
    public Follower follower;
    public Gamepad g1;
    public Gamepad g2;

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
    private void initSubsystems(){
        follower = Constants.create(hMap);

        indexer = new Indexer(hMap);
        intake = new Intake(hMap);
        shooter = new Shooter(hMap);
        tank = new tank(hMap);

    }
    private void configureButtons(){
        new GamepadButton(OperatorGamepad, GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(new InstantCommand(indexer::index))
                .whenReleased(new InstantCommand(indexer::stop));
    }

    @Override
    public void run(){
        super.run();
        tank.drive(-g1.left_stick_y, g1.left_stick_x);

        if (OperatorGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.3) {
            intake.in();
        } else {
            intake.stop();
        }

        //TargetVelocity cambia con el getVelocity
        if (OperatorGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.3) {
            new InstantCommand(() -> shooter.setTargetVelocity(1500));
        } else {
            shooter.stop();
        }

        telemetry.addData("Shooter velocity", shooter.getVelocity());
        telemetry.addData("Shooter target", shooter.getTargetVelocity());
        telemetry.update();

    }
}
