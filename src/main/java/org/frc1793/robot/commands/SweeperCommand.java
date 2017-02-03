package org.frc1793.robot.commands;

import org.frc1793.robot.Sweeper;
import org.strongback.command.Command;
import org.strongback.components.ui.ContinuousRange;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 2/3/17
 */
public class SweeperCommand extends Command {
    private Sweeper sweeper;
    private ContinuousRange speed;


    public SweeperCommand(Sweeper sweeper, ContinuousRange speed) {
        super(sweeper);
        this.sweeper = sweeper;
        this.speed = speed;
    }

    @Override
    public boolean execute() {
        sweeper.sweep(speed.read());
        return false;
    }

    @Override
    public void interrupted() {
        sweeper.stop();
    }

}
