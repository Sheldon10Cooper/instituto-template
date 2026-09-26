package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Commands.ArmCommand;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Arm;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

public class Robot extends com.arcrobotics.ftclib.command.Robot {
    public final HardwareMap hMap;
    public final Telemetry telemetry;
    public static GamepadEx DriverGamepad;
    public static GamepadEx OperatorGamepad;
    //public Intake intake;
    public Arm arm;
    public Follower follower;


    public Robot(HardwareMap hMap, Telemetry telemetry, GamepadEx driverGamepad, GamepadEx operatorGamepad) {
        this.hMap = hMap;
        this.telemetry = telemetry;
        DriverGamepad = driverGamepad;
        OperatorGamepad = operatorGamepad;

        initSubsystems();
        configureButtons();

    }
    private void initSubsystems(){
        //intake = new Intake(hMap, telemetry);
        arm = new Arm(hMap);
        follower = Constants.createFollower(hMap);
        //register(intake);
        register(arm);
    }
    private void configureButtons(){
       /* new Trigger(() -> DriverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.5)
                .whenActive(new InstantCommand(()-> intake.setPower(1)))
                .whenInactive(new InstantCommand(()->intake.setPower(0)));*/

        new Trigger(()->DriverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)>0.5)
                .whenActive(new ArmCommand(arm,100))
                .whenInactive(new ArmCommand(arm,0));
    }
    @Override
    public void run(){
       // telemetry.addData("Power",intake.getPower());
        follower.update();

        telemetry.addData("Chassis X", "%.4f", follower.getPose().getX());
        telemetry.addData("Chassis Y", "%.4f", follower.getPose().getY());
        telemetry.addData("Chassis Heading", "%.6f", follower.getPose().getHeading());
        telemetry.addData("Current Position Arm", arm.getPos());
        telemetry.addData("Target Position Arm", arm.getTargetPosition());
        telemetry.addData("Is at Target (ARM) ?", arm.isAtTarget());
        super.run();
    }
}
