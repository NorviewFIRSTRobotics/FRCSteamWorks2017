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
    private Motor left, right;

    public Sweeper(Motor left, Motor right) {
        this.left = left;
        this.right = right;
    }

    public void start(ContinuousRange speed) {
        this.left.setSpeed(speed.read());
        this.right.setSpeed(-speed.read());
    }

    public void stop() {
        this.right.stop();
        this.left.stop();
    }
}
