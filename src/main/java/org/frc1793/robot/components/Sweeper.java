package org.frc1793.robot.components;

import org.strongback.command.Requirable;
import org.strongback.components.Motor;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 2/3/17
 */
public class Sweeper extends BasicMotor implements Requirable {
    public Sweeper(Motor motor) {
        super(motor);
    }
}
