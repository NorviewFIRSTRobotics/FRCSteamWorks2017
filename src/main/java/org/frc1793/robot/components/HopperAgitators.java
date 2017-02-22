package org.frc1793.robot.components;

import org.strongback.command.Requirable;
import org.strongback.components.Motor;
import org.strongback.components.ui.ContinuousRange;

import java.util.function.DoubleSupplier;

/**
 * Created by melvin on 2/8/2017.
 */
public class HopperAgitators implements Requirable {
    private Motor left;
    public HopperAgitators(Motor left) {
        this.left = left;
    }

    public void agitate(ContinuousRange speed)  {
        left.setSpeed(speed.invert().read());
    }

    public void stop() {
        left.stop();
    }
}
