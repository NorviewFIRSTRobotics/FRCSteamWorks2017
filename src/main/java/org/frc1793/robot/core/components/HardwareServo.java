package org.frc1793.robot.core.components;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 4/1/17
 */
public class HardwareServo implements Servo {
    private edu.wpi.first.wpilibj.Servo servo;
    private double extendedAngle, retractedAngle;
    private Direction direction;
    public HardwareServo(int channel, double extendedAngle, double retractedAngle) {
        this.direction = Direction.STOPPED;
        this.servo = new edu.wpi.first.wpilibj.Servo(channel);
        this.extendedAngle = extendedAngle;
        this.retractedAngle = retractedAngle;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    public Servo stop() {
        this.direction = Direction.STOPPED;
        return this;
    }

    @Override
    public Servo extend() {
        this.direction = Direction.EXTENDING;
        servo.setAngle(extendedAngle);
        return this;
    }

    @Override
    public Servo retract() {
        this.direction = Direction.RETRACTING;
        servo.setAngle(retractedAngle);
        return this;
    }
}
