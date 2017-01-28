package org.frc1793.robot.commands;

import org.frc1793.robot.ShooterDrive;
import org.strongback.command.Command;
import org.strongback.components.ui.ContinuousRange;

/**
 *
 *
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 1/15/2017
 */
public class FireFuelCommand extends Command {

    private ShooterDrive drive;
    private final ContinuousRange speed;

    /**
     * Create a firing comm*and
     * @param drive the launching mechanism
     * @param speed the speed at which to drive the drive; always positive
     * @param duration the duration of this command; should be positive
     *
     */
    public FireFuelCommand(ShooterDrive drive, ContinuousRange speed, double duration) {
        super(duration,drive);
        this.drive = drive;
        this.speed = speed;
    }

    @Override
    public boolean execute() {
        drive.drive(speed.read());
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
