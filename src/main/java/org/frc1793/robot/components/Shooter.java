package org.frc1793.robot.components;

import org.frc1793.robot.Config;
import org.strongback.command.Requirable;
import org.strongback.components.Motor;
import org.strongback.components.TalonSRX;
import org.strongback.components.ui.ContinuousRange;
import org.strongback.control.PIDController;
import org.strongback.control.TalonController;
import org.strongback.hardware.Hardware;

/**
 * Created by melvin on 1/19/2017.
 * <p>
 * Controller for the fuel shooter
 */
public class Shooter extends BasicMotor implements Requirable {

    public Shooter(Motor motor) {
        super(motor);
    }

    @Override
    public void start(ContinuousRange speed) {
        super.start(speed);
    }

}
