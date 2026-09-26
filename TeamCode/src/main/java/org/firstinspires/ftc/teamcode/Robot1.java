package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.Robot;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Commands.ArmCommand;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

public class Robot1 extends Robot {
    public Shooter shooter;
    public final HardwareMap hMap;
    public final Telemetry telemetry;
    public static GamepadEx DriverGamepad;
    public static GamepadEx OperatorGamepad;
    public Robot1(HardwareMap hMap, Telemetry telemetry, GamepadEx driverGamepad, GamepadEx operatorGamepad) {
        this.hMap = hMap;
        this.telemetry = telemetry;

        DriverGamepad = driverGamepad;
        OperatorGamepad = operatorGamepad;

        initSubsystems();
        configureButtons();

    }
    private void initSubsystems(){
        shooter = new Shooter(hMap);
    }
    private void configureButtons(){
        new Trigger(()->OperatorGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)>0.5)
                .whenActive(new InstantCommand(()-> shooter.shoot()))
                .whenInactive(new InstantCommand(()-> shooter.stop()));

    }
    @Override
    public void run(){
        telemetry.addData("Shooter Power", shooter.getPower());
        telemetry.update();
        super.run();

    }
}
