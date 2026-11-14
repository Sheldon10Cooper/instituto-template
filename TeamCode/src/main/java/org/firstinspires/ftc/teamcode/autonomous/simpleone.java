package org.firstinspires.ftc.teamcode.autonomous;

import static android.os.SystemClock.sleep;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.Robot1;

@Autonomous

public class simpleone extends OpMode {

    enum state {
        Shoot,
        Move,
        Done
    }

    state currentState = state.Shoot;
    ElapsedTime stateTimer = new ElapsedTime();
    Robot1 robot;

    static final double SHOOTER_VELOCITY = 1500;

    @Override
    public void init(){
        robot = new Robot1(
                hardwareMap,
                telemetry,
                new GamepadEx(gamepad1),
                new GamepadEx(gamepad2),
                gamepad1,
                gamepad2
        );
    }

    public void start() {
        stateTimer.reset();
    }

    public void loop(){
        switch (currentState){

            case Shoot:
                if(stateTimer.seconds() > 5.0){
                    robot.shooter.setTargetVelocity(SHOOTER_VELOCITY);
                    currentState = state.Move;
                }
                break;

            case Move:
                if(stateTimer.seconds() > 10.0){
                    robot.arcade.drive(-1,1);
                    sleep(500);
                    robot.arcade.drive(1,1);
                    sleep(500);
                    currentState = state.Done;
                }
                break;
            case Done:
            default:
                robot.arcade.drive(0, 0);
                break;
        }

        robot.shooter.update(); // must run every loop

        telemetry.addData("State", currentState);
        telemetry.addData("Timer", stateTimer.seconds());
        telemetry.addData("Shooter vel", robot.shooter.getVelocity());
        telemetry.update();



    }

}
