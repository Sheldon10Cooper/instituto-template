package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.teleop.kitbot;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

@Config
@Autonomous(name = "AutoT: Carry")
public class ShootTipPark extends OpMode {

    enum State { SPIN_UP, FEED, DRIVE_1, TURN_1, DRIVE_2, TURN_2, DRIVE_3, TURN_3, PARK, DONE }

    State state = State.SPIN_UP;
    ElapsedTime timer = new ElapsedTime();
    kitbot robot;

    public static double SPIN_UP_TIMEOUT = 4.0;
    public static double FEED_TIME = 5.0;

    public static double TICKS_PER_WHEEL_REV = 537.7;
    public static double WHEEL_DIAMETER_IN = 3.78;

    public static double LEG_1_INCHES = 12;
    public static double LEG_2_INCHES = 26;
    public static double LEG_3_INCHES = 70;
    public static double PARK_INCHES = 18;
    public static double TURN_WHEEL_INCHES = 9;
    public static double DRIVE_POWER = 0.5;
    public static double TURN_POWER = 0.4;
    public static double MOVE_TIMEOUT = 3.0;

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

    private boolean driveLeg(double inches){
        robot.drivetrain.arcade(-DRIVE_POWER, 0);
        if (driveInches() >= inches || timer.seconds() > MOVE_TIMEOUT){
            robot.drivetrain.stop();
            return true;
        }
        return false;
    }

    private boolean turnLeg(double power){
        robot.drivetrain.arcade(0, power);
        if (turnInches() >= TURN_WHEEL_INCHES || timer.seconds() > MOVE_TIMEOUT){
            robot.drivetrain.stop();
            return true;
        }
        return false;
    }

    private void next(State s){
        state = s;
        timer.reset();
        robot.drivetrain.resetEncoders();
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
                if (driveLeg(LEG_1_INCHES)) next(State.TURN_1);
                break;
            case TURN_1:
                if (turnLeg(-TURN_POWER)) next(State.DRIVE_2);
                break;
            case DRIVE_2:
                if (driveLeg(LEG_2_INCHES)) next(State.TURN_2);
                break;
            case TURN_2:
                if (turnLeg(TURN_POWER)) next(State.DRIVE_3);
                break;
            case DRIVE_3:
                if (driveLeg(LEG_3_INCHES)) next(State.TURN_3);
                break;
            case TURN_3:                                   // left
                if (turnLeg(-TURN_POWER)) next(State.PARK);
                break;
            case PARK:
                if (driveLeg(PARK_INCHES)) next(State.DONE);
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
        telemetry.update();
    }
}