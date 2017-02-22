package org.frc1793.robot.components;

import org.strongback.command.Requirable;
import org.strongback.components.Motor;
import org.strongback.components.ui.ContinuousRange;

/**
 * Created by melvin on 2/8/2017.
 */
public class HopperAgitators extends BasicMotor implements Requirable {

    public HopperAgitators(Motor motor) {
        super(motor);
    }

    @Override
    public void start(ContinuousRange speed) {
        motor.setSpeed(speed.invert().read());
    }

}
