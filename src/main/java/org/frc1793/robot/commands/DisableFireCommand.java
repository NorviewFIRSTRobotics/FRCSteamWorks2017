package org.frc1793.robot.commands;

import org.frc1793.robot.Shooter;
import org.strongback.command.Command;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 2/3/17
 */
public class DisableFireCommand extends Command {
    private Shooter shooter;

    public DisableFireCommand(Shooter shooter) {
        super(shooter);
        this.shooter = shooter;
    }

    @Override
    public boolean execute() {
        Command.cancel(shooter);
        return false;
    }
}
