package org.frc1793.robot;

import org.strongback.command.Requirable;
import org.strongback.components.Motor;
import org.strongback.control.PIDController;
import org.strongback.control.TalonController;

/**
 * Created by melvin on 1/19/2017.
 */
public class Shooter implements Requirable {

    private Motor motor;
    private PIDController pidController;

    public Shooter(TalonController talon) {
        this(talon,talon);
    }

    public Shooter(Motor motor, PIDController pidController) {
        this.motor = motor;
        this.pidController = pidController;
    }

    public void shooter(double speed) {
        motor.setSpeed(speed);
    }

    public void stop() {
        motor.stop();
    }
}
