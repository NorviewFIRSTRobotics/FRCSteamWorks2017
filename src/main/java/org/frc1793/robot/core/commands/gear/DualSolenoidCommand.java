package org.frc1793.robot.core.commands.gear;

import org.frc1793.robot.core.components.Servo;
import org.strongback.command.Command;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 4/1/17
 */
public abstract class DualSolenoidCommand  extends Command{
    protected Servo left, right;

    public DualSolenoidCommand(Servo left, Servo right) {
        super(left,right);
        this.left = left;
        this.right = right;
    }

    @Override
    public void interrupted() {
        left.stop();
        right.stop();
    }
}
