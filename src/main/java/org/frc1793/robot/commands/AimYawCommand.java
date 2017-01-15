package org.frc1793.robot.commands;

import org.strongback.command.Command;
import org.strongback.components.AngleSensor;
import org.strongback.drive.TankDrive;
import org.strongback.util.Values;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 1/15/17
 */
public class AimYawCommand extends Command {

    private final TankDrive drive;
    private AngleSensor angleSensor;
    private final double turnSpeed;
    private final boolean squareInputs;
    private double angle;

    /**
     * Create a new autonomous command.
     *
     * @param drive        the chassis
     * @param turnSpeed    the speed at which to turn; should be [-1.0, 1.0]
     * @param squareInputs whether to increase sensitivity at low speeds
     */
    public AimYawCommand(TankDrive drive, AngleSensor angleSensor,double turnSpeed, boolean squareInputs, double angle) {
        super(drive);
        this.drive = drive;
        this.angleSensor = angleSensor;
        this.turnSpeed = turnSpeed;
        this.squareInputs = squareInputs;
        this.angle = angle;
    }

    @Override
    public boolean execute() {
        double dir = Values.limit(-1, angleSensor.computeAngleChangeTo(angle, 0.1), 1);
        drive.arcade(0,turnSpeed*dir,squareInputs);
        return false;
    }

    @Override
    public void interrupted() {
        drive.stop();
    }

    @Override
    public void end() {
        drive.stop();
    }
}
