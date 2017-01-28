package org.frc1793.robot;

import org.strongback.command.Requirable;
import org.strongback.components.Motor;
import org.strongback.control.PIDController;
import org.strongback.drive.TankDrive;
import org.strongback.hardware.Hardware;

/**
 * Created by melvin on 1/19/2017.
 */
public class ShooterDrive implements Requirable {

    private Motor motor;
    private PIDController controller;

    public ShooterDrive(Motor motor) {
        this.motor = motor;
    }
    public void drive(double speed) {
        motor.setSpeed(speed);
    }
    public void stop() {
        motor.stop();
    }
}
