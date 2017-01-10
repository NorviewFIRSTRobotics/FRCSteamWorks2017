package org.frc1793.robot;

import edu.wpi.first.wpilibj.vision.USBCamera;
import org.strongback.command.Requirable;
import org.strongback.components.DistanceSensor;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 1/9/17
 */
public class DistanceCamera extends USBCamera implements DistanceSensor, Requirable {
    //TODO don't know how to implement this
    @Override
    public double getDistanceInInches() {
        return 0;
    }

    @Override
    public double getDistanceInFeet() {
        return 0;
    }

    @Override
    public DistanceSensor zero() {
        return this;
    }
}
