package org.firstinspires.ftc.teamcode;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Robot;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

public class Robot1 extends Robot {
    public final HardwareMap hMap;
    public final Telemetry telemetry;
    public static GamepadEx DriverGamepad;
    public static GamepadEx OperatorGamepad;
    public Indexer indexer;
    public Intake intake;
    public Shooter shooter;
    public Follower follower;
    public Gamepad g1;
    public Gamepad g2;

    public Robot1(HardwareMap hMap, Telemetry telemetry, GamepadEx driverGamepad, GamepadEx operatorGamepad, Gamepad g1, Gamepad g2) {
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


    }
    private void configureButtons(){


    }
    @Override
    public void run(){
        DrivePowers powers = ManualDrive.fieldCentric(
                g1.left_stick_y,
                g1.left_stick_x,
                -g1.right_stick_x,
                follower.pose().heading()
        );
        follower.manual(powers);

        telemetry.update();
        follower.update();
        super.run();

    }
}
