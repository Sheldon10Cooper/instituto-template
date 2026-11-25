package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.Robot1;

@Autonomous(name = "Simple Shoot and Park")
public class simpleone extends OpMode {

    enum State { SPIN_UP, FEED, DRIVE, DONE }

    State state = State.SPIN_UP;
    ElapsedTime timer = new ElapsedTime();
    Robot1 robot;

    static final double SHOOTER_VELOCITY = 1500;
    static final double SPIN_UP_TIMEOUT = 4.0; // s, give up waiting and feed anyway
    static final double FEED_TIME = 5.0;       // s
    static final double DRIVE_TIME = 1.0;      // s
    static final double DRIVE_POWER = 0.5;

    @Override
    public void init(){
        robot = new Robot1(hardwareMap, telemetry,
                new GamepadEx(gamepad1), new GamepadEx(gamepad2), gamepad1, gamepad2);
    }

    @Override
    public void start(){
        robot.shooter.setTargetVelocity(SHOOTER_VELOCITY);
        timer.reset();
    }

    @Override
    public void loop(){
        switch (state){
            case SPIN_UP:
                if (robot.shooter.atTargetVelocity() || timer.seconds() > SPIN_UP_TIMEOUT){
                    state = State.FEED;
                    timer.reset();
                }
                break;
            case FEED:
                robot.indexer.index();
                robot.intake.in();
                if (timer.seconds() > FEED_TIME){
                    robot.indexer.stop();
                    robot.intake.stop();
                    robot.shooter.stop();
                    state = State.DRIVE;
                    timer.reset();
                }
                break;
            case DRIVE:
                robot.arcade.drive(DRIVE_POWER, 0); // straight ahead
                if (timer.seconds() > DRIVE_TIME){
                    robot.arcade.drive(0, 0);
                    state = State.DONE;
                }
                break;
            case DONE:
            default:
                break;
        }

        robot.shooter.update(); // must run every loop

        telemetry.addData("State", state);
        telemetry.addData("Shooter vel", robot.shooter.getVelocity());
        telemetry.addData("Shooter ready", robot.shooter.atTargetVelocity());
        telemetry.update();
    }
}