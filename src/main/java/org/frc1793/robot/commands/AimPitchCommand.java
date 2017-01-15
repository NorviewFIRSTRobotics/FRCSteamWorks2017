package org.frc1793.robot.commands;

import org.strongback.command.Command;
import org.strongback.components.AngleSensor;
import org.strongback.components.Motor;

/**
 * Created by melvin on 1/15/2017.
 */
public class AimPitchCommand extends Command {

    public static final double HIGH_GOAL = 120;
    public static final double LOW_GOAL = 90;

    private double angle;
    private Motor motor;
    private double speed;
    private AngleSensor angleSensor;


    /**
     *  @param angle number in degrees to set the launcher to aim at.
     * @param motor supplied motor controller to change to the necessary angle.
     * @param speed speed for the motor.
     * @param angleSensor sensor to supply the current position of motor
     */
    public AimPitchCommand(double angle, Motor motor, double speed, AngleSensor angleSensor) {
        this.angle = angle;
        this.motor = motor;
        this.speed = speed;
        this.angleSensor = angleSensor;
    }
    @Override
    public boolean execute() {
        double dir = angleSensor.computeAngleChangeTo(angle,0.1);
        motor.setSpeed(dir == 0? 0: dir > 0? speed: -speed);
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
