package org.frc1793.robot.commands.firing;

import org.frc1793.robot.components.Shooter;
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
        shooter.stop();
        Command.cancel(shooter);
        return true;
    }
}
