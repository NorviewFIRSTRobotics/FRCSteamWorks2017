package org.frc1793.robot.commands.climber;

import org.frc1793.robot.components.Climber;
import org.strongback.command.Command;
import org.strongback.components.ui.ContinuousRange;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 2/3/17
 */
public class ClimberStartCommand extends Command {

    private Climber sweeper;
    private ContinuousRange speed;

    public ClimberStartCommand(Climber sweeper, ContinuousRange speed) {
        super(sweeper);
        this.sweeper = sweeper;
        this.speed = speed;
    }

    public ClimberStartCommand(Climber sweeper) {
        this(sweeper, () -> 1);
    }

    @Override
    public boolean execute() {
        sweeper.start(speed);
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
