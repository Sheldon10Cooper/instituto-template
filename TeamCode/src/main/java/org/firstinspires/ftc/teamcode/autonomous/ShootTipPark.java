package org.firstinspires.ftc.teamcode.autonomous;

import static android.os.SystemClock.sleep;

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

    // Headings below use the field picture: north = up, robot starts facing south.
    enum State {
        SPIN_UP, FEED,
        BACK_OFF,        // 1. straight backward off the start wall
        TURN_1,          //    right 90 -> face west
        DRIVE_WEST,      // 2. forward to the left wall
        TURN_2,          //    left 90 -> face south
        DRIVE_CORNER,
        BACK_OFF_2,// 3. forward into the bottom-left corner, intaking
        TURN_AROUND,     //    180 -> face north (intake still on)
        DRIVE_NORTH_1,   // 4. forward along the left wall (intake still on)
        TURN_3,          //    right 90 -> face east
        DRIVE_EAST,      // 5. forward
        TURN_4,          //    left 90 -> face north
        DRIVE_LONG,      // 6. forward a lot
        TURN_5,          //    left 90 -> face west
        PARK,            // 7. forward to the left wall
        DONE
    }

    State state = State.SPIN_UP;
    ElapsedTime timer = new ElapsedTime();
    kitbot robot;

    // Encoder readings captured at the start of each step (replaces resetEncoders()).
    private double leftStart = 0;
    private double rightStart = 0;

    public static double SPIN_UP_TIMEOUT = 4.0;
    public static double FEED_TIME = 5.0;

    public static double TICKS_PER_WHEEL_REV = 537.7;
    public static double WHEEL_DIAMETER_IN = 3.78;

    //  TUNE on the field.
    public static double BACK_OFF_INCHES = 15;
    public static  double BACK_OFF_INCHES_2 = 10;
    public static double WEST_INCHES = 42;
    public static double CORNER_INCHES = 29;
    public static double NORTH_1_INCHES = 23;
    public static double EAST_INCHES = 16.5;
    public static double LONG_INCHES = 66;
    public static double PARK_INCHES = 22;

    // Wheel travel (per side) needed for a 90 degree turn.
    // Starting guess: (pi * trackWidthInches) / 4. TUNE THIS on the field.
    public static double TURN_90_INCHES = 11;

    public static double DRIVE_POWER = 0.5;
    public static double TURN_POWER = 0.4;
    public static double MOVE_TIMEOUT = 5.0;
    public static double LONG_TIMEOUT = 5.0;
    public static long intake_time = 2000;

    @Override
    public void init(){
        MultipleTelemetry telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        robot = new kitbot(hardwareMap, telem, gamepad1, gamepad2, kitbot.DriveStyle.ONE_STICK);
    }

    @Override
    public void start(){
        robot.shooter.setTargetVelocity(Shooter.NEAR_VELOCITY);
        markEncoders();
        timer.reset();
    }

    @Override
    public void stop(){
        stopAll();
    }

    private void stopAll(){
        robot.indexer.stop();
        robot.intake.stop();
        robot.shooter.stop();
        robot.drivetrain.stop();
    }

    private void markEncoders(){
        leftStart = robot.drivetrain.getLeftPosition();
        rightStart = robot.drivetrain.getRightPosition();
    }

    private double ticksToInches(double ticks){
        return ticks / TICKS_PER_WHEEL_REV * Math.PI * WHEEL_DIAMETER_IN;
    }

    private double driveInches(){
        double l = robot.drivetrain.getLeftPosition() - leftStart;
        double r = robot.drivetrain.getRightPosition() - rightStart;
        return Math.abs(ticksToInches((l + r) / 2.0));
    }

    private double turnInches(){
        double l = robot.drivetrain.getLeftPosition() - leftStart;
        double r = robot.drivetrain.getRightPosition() - rightStart;
        return Math.abs(ticksToInches((r - l) / 2.0));
    }

    // forward = true drives forward (positive arcade power), false drives backward
    private boolean driveLeg(double inches, boolean forward, double timeout){
        robot.drivetrain.arcade(forward ? DRIVE_POWER : -DRIVE_POWER, 0);
        if (driveInches() >= inches || timer.seconds() > timeout){
            robot.drivetrain.stop();
            return true;
        }
        return false;
    }

    // "left" is positive turn power, "right" is negative. Wheel travel scales with the angle.
    private boolean turn(boolean left, double degrees){
        robot.drivetrain.arcade(0, left ? TURN_POWER : -TURN_POWER);
        if (turnInches() >= TURN_90_INCHES * degrees / 90.0 || timer.seconds() > MOVE_TIMEOUT){
            robot.drivetrain.stop();
            return true;
        }
        return false;
    }

    private void next(State s){
        state = s;
        timer.reset();
        markEncoders();
    }

    @Override
    public void loop(){
        // Emergency stop
        if (gamepad1.b && state != State.DONE){
            stopAll();
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
                if (timer.seconds() > FEED_TIME){
                    robot.indexer.stop();
                    robot.shooter.stop();
                    next(State.BACK_OFF);
                }
                break;

            case BACK_OFF:
                if (driveLeg(BACK_OFF_INCHES, false, MOVE_TIMEOUT)) next(State.TURN_1);
                break;

            case TURN_1:
                if (turn(false, 90)) next(State.DRIVE_WEST);
                break;

            case DRIVE_WEST:
                if (driveLeg(WEST_INCHES, false, MOVE_TIMEOUT)) next(State.TURN_2);
                break;

            case TURN_2:
                if (turn(true, 90)) next(State.DRIVE_CORNER);
                break;

            case DRIVE_CORNER:
                robot.intake.in();
                if (driveLeg(CORNER_INCHES, true, MOVE_TIMEOUT)) next(State.BACK_OFF_2);
                sleep(intake_time);
                break;

            case BACK_OFF_2:
                if (driveLeg(BACK_OFF_INCHES_2, false, MOVE_TIMEOUT)) next(State.TURN_3);
                robot.intake.stop();
                break;


            case TURN_3:
                if (turn(false, 90)) next(State.DRIVE_EAST);
                break;

            case DRIVE_EAST:
                if (driveLeg(EAST_INCHES, true, MOVE_TIMEOUT)) next(State.TURN_AROUND);
                break;

            case TURN_AROUND:
                robot.intake.in();
                if (turn(false, 180)) next(State.DRIVE_LONG);
                break;

            case DRIVE_LONG:
                if (driveLeg(LONG_INCHES, true, LONG_TIMEOUT)) next(State.TURN_5);
                break;

            case TURN_5:
                if (turn(true, 90)) next(State.PARK);
                break;

            case PARK:
                if (driveLeg(PARK_INCHES, true, MOVE_TIMEOUT)){
                    stopAll();
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
        telemetry.update();
    }
}
