package org.frc1793.robot.commands.climber;

import org.frc1793.robot.components.Climber;
import org.strongback.command.Command;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 2/3/17
 */
public class ClimberStopCommand extends Command {
    private Climber sweeper;

    public ClimberStopCommand(Climber sweeper) {
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
