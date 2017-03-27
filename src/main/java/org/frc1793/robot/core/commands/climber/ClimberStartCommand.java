package org.frc1793.robot.core.commands.climber;

import org.frc1793.robot.core.components.Climber;
import org.strongback.command.Command;
import org.strongback.components.ui.ContinuousRange;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 2/3/17
 */
public class ClimberStartCommand extends Command {

    private final Climber climber;
    private final ContinuousRange speed;

    public ClimberStartCommand(Climber climber, ContinuousRange speed) {
        super(climber);
        this.climber = climber;
        this.speed = speed;
    }

    public ClimberStartCommand(Climber climber) {
        this(climber, () -> 1);
    }

    @Override
    public boolean execute() {
        climber.start(speed);
        return false;
    }

    @Override
    public void interrupted() {
        climber.stop();
    }

}
