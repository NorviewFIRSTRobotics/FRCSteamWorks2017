package org.frc1793.robot;

import org.strongback.command.Requirable;
import org.strongback.components.Motor;
import org.strongback.drive.TankDrive;

/**
 * Created by melvin on 1/19/2017.
 */
public class ShooterDrive extends TankDrive {
    public ShooterDrive(Motor left, Motor right) {
        super(left,right);
    }
    public void tank(double speed) {
        super.tank(speed, -speed);
    }
}
