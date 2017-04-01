package org.frc1793.robot.core.commands.gear;

import org.frc1793.robot.core.components.Servo;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 4/1/17
 */
public class CloseGearCommand extends DualSolenoidCommand {

    public CloseGearCommand(Servo left, Servo right) {
        super(left, right);
    }

    @Override
    public boolean execute() {
        left.retract();
        right.retract();
        return false;
    }
}
