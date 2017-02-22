package org.frc1793.robot.commands.sweeper;

import org.frc1793.robot.components.Sweeper;
import org.strongback.command.Command;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 2/3/17
 */
public class SweeperStopCommand extends Command {
    private Sweeper sweeper;

    public SweeperStopCommand(Sweeper sweeper) {
        super(sweeper);
        this.sweeper = sweeper;
    }

    @Override
    public boolean execute() {
        sweeper.stop();
        Command.cancel(sweeper);
        return true;
    }
}
