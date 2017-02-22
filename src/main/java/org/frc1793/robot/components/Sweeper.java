package org.frc1793.robot.components;

import org.strongback.command.Requirable;
import org.strongback.components.Motor;
import org.strongback.components.ui.ContinuousRange;

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

    public void sweep(ContinuousRange speed) {
        this.motor.setSpeed(speed.read());
    }

    public void stop() {
        this.motor.stop();
    }

}
