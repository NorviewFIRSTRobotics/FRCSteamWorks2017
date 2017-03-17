package org.frc1793.robot.commands.drive;

import org.strongback.drive.TankDrive;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 3/16/17
 */
public class SlopedDriveCommand extends TimedDriveCommand {
    /**
     * Create a new autonomous command.
     *
     * @param drive        the chassis
     * @param maxDriveSpeed starting speed of descending drive [-1.0, 0) ∪ (0,1.0]
     * @param turnSpeed    the speed at which to turn; should be [-1.0, 1.0]
     * @param squareInputs whether to increase sensitivity at low speeds
     * @param duration     the duration of this command; should be positive
     */
    private double maxDriveSpeed;
    private double slope;
    public SlopedDriveCommand(TankDrive drive, double maxDriveSpeed, double turnSpeed, boolean squareInputs, double duration) {
        super(drive, maxDriveSpeed, turnSpeed, squareInputs, duration);
        this.maxDriveSpeed = maxDriveSpeed;
        this.slope = maxDriveSpeed/duration;
    }


    @Override
    public boolean execute() {
        drive.arcade(driveSpeed,turnSpeed,squareInputs);
        driveSpeed-=slope;
        return false;
    }
}
