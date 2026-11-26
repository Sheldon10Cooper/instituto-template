package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.autonomous.simpleone;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

public class kitbot {
    private final Gamepad driver;
    private final Gamepad operator;
    private double deadband(double value) {
        return Math.abs(value) < 0.05 ? 0 : value;
    }

    /**
     * TeleOp only. Call once per loop.
     */
    public void run() {
        double drive = deadband(-driver.left_stick_y);
        double turn = deadband(driveStyle == DriveStyle.ONE_STICK ? driver.left_stick_x : driver.right_stick_x);
        drivetrain.arcade(drive, turn);

        boolean intakeBtn = operator.right_trigger > 0.3;
        if (intakeBtn && !lastIntakeBtn) intakeOn = !intakeOn;
        lastIntakeBtn = intakeBtn;

        boolean shooterBtn = operator.left_trigger > 0.3;
        if (shooterBtn && !lastShooterBtn) shooterOn = !shooterOn;
        lastShooterBtn = shooterBtn;

        if (operator.dpad_up) farShot = true;
        if (operator.dpad_down) farShot = false;

        if (operator.a) {
            intake.out();
        } else if (intakeOn) {
            intake.in();
        } else {
            intake.stop();
        }

        if (operator.b) {
            shooter.setTargetVelocity(-500);
        } else if (shooterOn) {
            shooter.setTargetVelocity(farShot ? Shooter.FAR_VELOCITY : Shooter.NEAR_VELOCITY);
        } else {
            shooter.stop();
        }
        shooter.update();

        boolean ready = shooter.getTargetVelocity() > 0 && shooter.atTargetVelocity();
        if (ready && !lastReady) {
            operator.rumble(250);
            driver.rumble(250);
        }
        lastReady = ready;

        if ((operator.left_bumper && ready) || operator.right_bumper) {
            indexer.index();
        } else if (operator.x) {
            indexer.reverse();
        } else {
            indexer.stop();
        }

        telemetry.addData("Intake on", intakeOn);
        telemetry.addData("Shooter on", shooterOn);
        telemetry.addData("Preset", farShot ? "FAR" : "NEAR");
        telemetry.addData("Shooter velocity", shooter.getVelocity());
        telemetry.addData("Shooter target", shooter.getTargetVelocity());
        telemetry.addData("Shooter ready", ready);
        telemetry.update();
    }

    public enum DriveStyle {ONE_STICK, TWO_STICK}

    public final Drivetrain drivetrain;
    public final Indexer indexer;
    public final Intake intake;
    public final Shooter shooter;

    private final Telemetry telemetry;
    private final DriveStyle driveStyle;

    private boolean intakeOn = false;
    private boolean shooterOn = false;
    private boolean farShot = false;
    private boolean lastIntakeBtn = false;
    private boolean lastShooterBtn = false;
    private boolean lastReady = false;

    public kitbot(HardwareMap hMap, Telemetry telemetry, Gamepad operator, Gamepad driver, DriveStyle driveStyle) {
        this.telemetry = telemetry;
        this.driver = driver;
        this.operator = operator;
        this.driveStyle = driveStyle;

        drivetrain = new Drivetrain(hMap);
        indexer = new Indexer(hMap);
        intake = new Intake(hMap);
        shooter = new Shooter(hMap);
    }
}
