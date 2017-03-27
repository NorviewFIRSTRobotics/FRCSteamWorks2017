package org.frc1793.robot.core.commands.climber;

import org.frc1793.robot.core.components.Climber;
import org.strongback.command.Command;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 2/3/17
 */
public class ClimberStopCommand extends Command {
    private final Climber climber;

    public ClimberStopCommand(Climber climber) {
        super(climber);
        this.climber = climber;
    }

    @Override
    public boolean execute() {
        climber.stop();
        Command.cancel(climber);
        return true;
    }
}
