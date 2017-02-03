package org.frc1793.robot;

import org.strongback.command.Requirable;
import org.strongback.components.Motor;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 2/3/17
 */
public class Sweeper implements Requirable {
    private Motor motor;

    public Sweeper(Motor motor) {
        this.motor = motor;
    }

    public void sweep(double speed) {
        this.motor.setSpeed(speed);
    }

    public void stop() {
        this.motor.stop();
    }
}
