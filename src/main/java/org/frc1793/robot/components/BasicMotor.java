package org.frc1793.robot.components;

import org.strongback.components.Motor;
import org.strongback.components.ui.ContinuousRange;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 2/22/17
 */
public class BasicMotor {
    protected Motor motor;

    public BasicMotor(Motor motor) {
        this.motor = motor;
    }

    public void start(ContinuousRange speed) {
        this.motor.setSpeed(speed.read());

    }
    public void stop() {
        this.motor.stop();
    }
}
