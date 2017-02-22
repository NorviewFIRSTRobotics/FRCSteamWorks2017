package org.frc1793.robot.commands.sweeper;

import org.frc1793.robot.components.Sweeper;
import org.strongback.command.Command;
import org.strongback.components.ui.ContinuousRange;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 2/3/17
 */
public class SweeperStartCommand extends Command {

    private Sweeper sweeper;
    private ContinuousRange speed;

    public SweeperStartCommand(Sweeper sweeper, ContinuousRange speed) {
        super(sweeper);
        this.sweeper = sweeper;
        this.speed = speed;
    }

    public SweeperStartCommand(Sweeper sweeper) {
        this(sweeper, () -> 1);
    }

    @Override
    public boolean execute() {
        sweeper.sweep(speed);
        return false;
    }

    @Override
    public void interrupted() {
        sweeper.stop();
    }

    public void setSpeed(ContinuousRange speed) {
        this.speed = speed;
    }
}
