package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Kitbot Solo Driver", group = "TeleOp")
public class SoloDriver extends LinearOpMode {

    @Override
    public void runOpMode() {
        kitbot robot = new kitbot(
                hardwareMap,
                telemetry,
                gamepad1,
                gamepad1,
                kitbot.DriveStyle.TWO_STICK
        );

        telemetry.addData("Status", "Inicializado - Listo para jugar");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            robot.run();
        }
    }
}