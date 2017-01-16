package org.frc1793.robot.commands;

import org.strongback.command.Command;
import org.strongback.components.Motor;

/**
 * Created by melvin on 1/15/2017.
 */
public class FireFuelCommand extends Command {

    private Motor motor;
    private double speed;

    /**
     * Create a firing command
     * @param motor the launching mechanism
     * @param speed the speed at which to drive the motor; always positive
     * @param duration the duration of this command; should be positive
     *
     */
    public FireFuelCommand(Motor motor, double speed, double duration) {
        super(duration,motor);
        this.motor = motor;
        this.speed = Math.abs(speed);
    }

    @Override
    public boolean execute() {
        motor.setSpeed(speed);
        return false;
    }

    @Override
    public void interrupted() {
        motor.stop();
    }

    @Override
    public void end() {
        motor.stop();
    }
}
