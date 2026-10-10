package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.Robot;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Robot1 extends Robot {
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
    }
    private void configureButtons(){
        new Trigger(()->OperatorGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)>0.5)
                .whenActive(new InstantCommand(()-> ))
                .whenInactive(new InstantCommand(()-> ));

    }
    @Override
    public void run(){
        telemetry.update();
        super.run();

    }
}
