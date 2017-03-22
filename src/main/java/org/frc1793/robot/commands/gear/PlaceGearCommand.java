package org.frc1793.robot.commands.gear;

import org.strongback.command.Command;
import org.strongback.drive.TankDrive;

/**
 * Purpose: Moves into place on the peg from baseline starting position
 *
 * @author Tyler Marshall
 * @version 1/8/17
 */
public class PlaceGearCommand extends Command {
    private TankDrive drive;
    public PlaceGearCommand(TankDrive drive) {
        this.drive = drive;
    }

    @Override
    public boolean execute() {
        
        return false;
    }
}
