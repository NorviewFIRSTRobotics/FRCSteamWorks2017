package org.frc1793.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc1793.robot.ShooterDrive;
import org.strongback.command.Command;
import org.strongback.components.Motor;
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
    private final ContinuousRange left;
    private final ContinuousRange right;

    /**
     * Create a firing comm*and
     * @param drive the launching mechanism
     * @param left the speed at which to drive the drive; always positive
     * @param right the speed at which to drive the drive; always positive*
     * @param duration the duration of this command; should be positive
     *
     */
    public FireFuelCommand(ShooterDrive drive, ContinuousRange left, ContinuousRange right, double duration) {
        super(duration,drive);
        this.drive = drive;
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean execute() {
        drive.tank(left.read(),-right.read());
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
