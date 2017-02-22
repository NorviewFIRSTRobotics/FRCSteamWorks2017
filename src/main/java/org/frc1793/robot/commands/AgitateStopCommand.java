package org.frc1793.robot.commands;

import org.frc1793.robot.components.HopperAgitators;
import org.strongback.command.Command;

/**
 * Created by melvin on 2/8/2017.
 */
public class AgitateStopCommand extends Command {
    private HopperAgitators agitators;
    public AgitateStopCommand(HopperAgitators agitators) {
        super(agitators);
        this.agitators = agitators;
    }
    @Override
    public boolean execute() {
        agitators.stop();
        Command.cancel(agitators);
        return true;
    }

    @Override
    public void interrupted() {
        agitators.stop();
    }
}
