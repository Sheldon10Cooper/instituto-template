package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

@Config
@Autonomous(name = "AutoGael")
public class ParkForward extends OpMode {

    enum State { DRIVE, DONE }

    State state = State.DRIVE;
    ElapsedTime timer = new ElapsedTime();
    Drivetrain drivetrain;
    MultipleTelemetry telem;

    public static double TICKS_PER_WHEEL_REV = 537.7;
    public static double WHEEL_DIAMETER_IN = 3.78;

    public static double DRIVE_INCHES = 24;
    public static double DRIVE_POWER = 0.1;
    public static double MOVE_TIMEOUT = 4.0;

    @Override
    public void init(){
        telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        drivetrain = new Drivetrain(hardwareMap);
    }

    @Override
    public void start(){
        drivetrain.resetEncoders();
        timer.reset();
    }

    private double driveInches(){
        double avg = (drivetrain.getLeftPosition() + drivetrain.getRightPosition()) / 2.0;
        return Math.abs(avg / TICKS_PER_WHEEL_REV * Math.PI * WHEEL_DIAMETER_IN);
    }

    @Override
    public void loop(){
        // Emergency cancel: driver presses B
        if (gamepad1.b && state != State.DONE){
            drivetrain.stop();
            state = State.DONE;
        }

        switch (state){
            case DRIVE:
                drivetrain.arcade(DRIVE_POWER, 0);
                if (driveInches() >= DRIVE_INCHES || timer.seconds() > MOVE_TIMEOUT){
                    drivetrain.stop();
                    state = State.DONE;
                }
                break;
            case DONE:
            default:
                break;
        }

        telem.addData("State", state);
        telem.addData("Drive inches", driveInches());
        telem.addData("Left ticks", drivetrain.getLeftPosition());
        telem.addData("Right ticks", drivetrain.getRightPosition());
        telem.update();
    }
}