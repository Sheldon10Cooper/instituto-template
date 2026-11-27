package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.teleop.kitbot;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

@Config
@Autonomous(name = "AutoB: Carried")
public class ShootPark extends OpMode {

    enum State { SPIN_UP, FEED, DRIVE_1, TURN, DRIVE_2, DONE }

    State state = State.SPIN_UP;
    ElapsedTime timer = new ElapsedTime();
    kitbot robot;

    public static double SPIN_UP_TIMEOUT = 4.0;
    public static double FEED_TIME = 5.0;
    public static double TICKS_PER_WHEEL_REV = 537.7;
    public static double WHEEL_DIAMETER_IN = 3.78;

    public static double DRIVE_1_INCHES = 12;
    public static double TURN_WHEEL_INCHES = 12;
    public static double DRIVE_2_INCHES = 35;
    public static double DRIVE_POWER = 0.5;
    public static double TURN_POWER = 0.5;
    public static double MOVE_TIMEOUT = 4.0;

    @Override
    public void init(){
        MultipleTelemetry telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        robot = new kitbot(hardwareMap, telem, gamepad1, gamepad2, kitbot.DriveStyle.ONE_STICK);
    }

    @Override
    public void start(){
        robot.shooter.setTargetVelocity(Shooter.NEAR_VELOCITY);
        timer.reset();
    }

    private double ticksToInches(double ticks){
        return ticks / TICKS_PER_WHEEL_REV * Math.PI * WHEEL_DIAMETER_IN;
    }

    private double driveInches(){
        double avg = (robot.drivetrain.getLeftPosition() + robot.drivetrain.getRightPosition()) / 2.0;
        return Math.abs(ticksToInches(avg));
    }

    private double turnInches(){
        double diff = (robot.drivetrain.getRightPosition() - robot.drivetrain.getLeftPosition()) / 2.0;
        return Math.abs(ticksToInches(diff));
    }

    private void next(State s){
        state = s;
        timer.reset();
        if (s == State.DRIVE_1 || s == State.TURN || s == State.DRIVE_2){
            robot.drivetrain.resetEncoders(); // each move measures from zero
        }
    }

    @Override
    public void loop(){
        if (gamepad1.b && state != State.DONE){
            robot.indexer.stop();
            robot.intake.stop();
            robot.shooter.stop();
            robot.drivetrain.stop();
            state = State.DONE;
        }

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
                if (driveInches() >= DRIVE_1_INCHES || timer.seconds() > MOVE_TIMEOUT){
                    robot.drivetrain.stop();
                    next(State.TURN);
                }
                break;
            case TURN:
                robot.drivetrain.arcade(0, TURN_POWER);
                if (turnInches() >= TURN_WHEEL_INCHES || timer.seconds() > MOVE_TIMEOUT){
                    robot.drivetrain.stop();
                    next(State.DRIVE_2);
                }
                break;
            case DRIVE_2:
                robot.drivetrain.arcade(-DRIVE_POWER, 0);
                if (driveInches() >= DRIVE_2_INCHES || timer.seconds() > MOVE_TIMEOUT){
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
        telemetry.addData("Drive inches", driveInches());
        telemetry.addData("Turn inches", turnInches());
        telemetry.addData("Left ticks", robot.drivetrain.getLeftPosition());
        telemetry.addData("Right ticks", robot.drivetrain.getRightPosition());
        telemetry.update();
    }
}