package org.firstinspires.ftc.teamcode.Commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Arm;

public class ArmCommand extends CommandBase {
    private final Arm arm;
    private final int targetTicks;

    public ArmCommand(Arm arm, int targetTicks) {
        this.arm = arm;
        this.targetTicks = targetTicks;
        addRequirements(arm);
    }
    @Override
    public void initialize() {
        arm.setTargetPosition(targetTicks);
    }
    @Override
    public boolean isFinished() {
        return arm.isAtTarget();
    }
}
