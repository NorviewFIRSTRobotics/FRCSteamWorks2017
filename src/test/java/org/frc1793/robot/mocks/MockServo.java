package org.frc1793.robot.mocks;

import org.frc1793.robot.core.components.Servo;
import org.strongback.components.Solenoid;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 4/1/17
 */
public class MockServo implements Servo {
    private Direction direction;

    public MockServo() {
        this.direction = Direction.STOPPED;
    }

    @Override
    public Servo stop() {
        this.direction = Direction.STOPPED;
        return this;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public Solenoid extend() {
        this.direction = Direction.EXTENDING;
        return this;
    }

    @Override
    public Solenoid retract() {
        this.direction = Direction.RETRACTING;
        return this;
    }
}
