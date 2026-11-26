package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.teleop.kitbot;

@Config
@Autonomous(name = "ShootR")
public class simpleone extends OpMode {

    enum State { SPIN_UP, FEED, DRIVE_1, TURN, DRIVE_2, TURN2, DRIVE_3, DRIVE_4, FEED2, DONE }

    State state = State.SPIN_UP;
    ElapsedTime timer = new ElapsedTime();

    kitbot robot;

    public static double SHOOTER_VELOCITY = 1310;
    public static double SPIN_UP_TIMEOUT = 4.0;
    public static double FEED_TIME = 5.0;
    public static double DRIVE_1_TIME = 0.7;
    public static double DRIVE_2_TIME = 1.6;
    public static double DRIVE_POWER = 0.5;
    public static double TURN_TIME = 0.8;
    public static double TURN2_TIME = 0.8;
    public static double TURN_POWER = 0.4;
    public static double DRIVE_3_TIME = 0.8;
    public static double DRIVE_4_TIME = 0.8;
    public static double FEED2_TIME = 5.0;

    @Override
    public void init(){
        MultipleTelemetry telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        robot = new kitbot(hardwareMap, telem, gamepad1, gamepad1, kitbot.DriveStyle.TWO_STICK);
    }

    @Override
    public void start(){
        robot.shooter.setTargetVelocity(SHOOTER_VELOCITY);
        timer.reset();
    }

    private void next(State s){
        state = s;
        timer.reset();
    }

    @Override
    public void loop(){
        switch (state){
            case SPIN_UP:
                if (robot.shooter.atTargetVelocity() || timer.seconds() > SPIN_UP_TIMEOUT){
                    next(State.FEED);
                }
                break;
            case FEED:
                robot.indexer.index();
                robot.intake.in();
                if (timer.seconds() > FEED_TIME){
                    robot.indexer.stop();
                    robot.intake.stop();
                    robot.shooter.stop();
                    next(State.DRIVE_1);
                }
                break;
            case DRIVE_1:
                robot.drivetrain.arcade(-DRIVE_POWER, 0);
                if (timer.seconds() > DRIVE_1_TIME){
                    robot.drivetrain.stop();
                    next(State.TURN);
                }
                break;
            case TURN:
                robot.drivetrain.arcade(0, TURN_POWER);
                if (timer.seconds() > TURN_TIME){
                    robot.drivetrain.stop();
                    next(State.DRIVE_2);
                }
                break;
            case DRIVE_2:
                robot.drivetrain.arcade(DRIVE_POWER, 0);
                if (timer.seconds() > DRIVE_2_TIME){
                    robot.drivetrain.stop();
                    next(State.TURN2);
                }
                break;
            case TURN2:
                robot.drivetrain.arcade(0, -TURN_POWER);
                if (timer.seconds() > TURN2_TIME){
                    robot.drivetrain.stop();
                    next(State.DRIVE_3);
                }
                break;
            case DRIVE_3:
                robot.drivetrain.arcade(DRIVE_POWER, 0);
                if (timer.seconds() > DRIVE_3_TIME){
                    robot.drivetrain.stop();
                    next(State.FEED2);
                }
                break;
            case FEED2:
                robot.intake.in();
                if (timer.seconds() > FEED2_TIME){
                    robot.intake.stop();
                    next(State.DRIVE_4);
                }
                break;
            case DRIVE_4:
                robot.drivetrain.arcade(-DRIVE_POWER, 0);
                if (timer.seconds() > DRIVE_4_TIME){
                    robot.drivetrain.stop();
                    next(State.DONE);
                }
                break;
            case DONE:
            default:
                break;
        }

        robot.shooter.update();

        telemetry.addData("State", state);
        telemetry.addData("Shooter vel", robot.shooter.getVelocity());
        telemetry.addData("Shooter ready", robot.shooter.atTargetVelocity());
        telemetry.update();
    }
}