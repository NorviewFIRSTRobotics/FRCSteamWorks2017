package org.frc1793.robot.commands.agitator;

import org.frc1793.robot.components.HopperAgitators;
import org.strongback.command.Command;
import org.strongback.components.ui.ContinuousRange;

/**
 * Created by melvin on 2/8/2017.
 */
public class AgitateStartCommand extends Command {
    private HopperAgitators agitators;
    private ContinuousRange speed;
    public AgitateStartCommand(HopperAgitators agitators, ContinuousRange speed) {
        super(agitators);
        this.agitators = agitators;
        this.speed = speed;
    }
    @Override
    public boolean execute() {
        agitators.start(speed);
        return false;
    }

    @Override
    public void interrupted() {
        agitators.stop();
    }
}
