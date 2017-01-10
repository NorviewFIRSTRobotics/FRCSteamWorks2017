package org.frc1793.robot.commands;

import org.frc1793.robot.DistanceCamera;
import org.strongback.command.Command;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 1/9/17
 */
public class AimCommand extends Command {
    DistanceCamera camera;
    public AimCommand(DistanceCamera camera) {
        super(camera);
        this.camera = camera;
    }

    @Override
    public boolean execute() {

        double inches = camera.getDistanceInInches();



        return false;
    }
}
