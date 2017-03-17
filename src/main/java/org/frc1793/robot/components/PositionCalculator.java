package org.frc1793.robot.components;

import org.frc1793.robot.utils.DoubleVector;
import org.frc1793.robot.utils.DoubleVectorIntegral;
import org.strongback.Strongback;
import org.strongback.components.Clock;
import org.strongback.components.DistanceSensor;
import org.strongback.components.ThreeAxisAcceleration;
import org.strongback.components.ThreeAxisAccelerometer;

/**
 * Created by melvin on 3/14/2017.
 */
public class PositionCalculator implements DistanceSensor {
    private ThreeAxisAccelerometer accelerometer;

    private Clock clock;
    private long delta, prev, current;
    private DoubleVectorIntegral velocity = new DoubleVectorIntegral(), position = new DoubleVectorIntegral();

    public PositionCalculator(ThreeAxisAccelerometer accelerometer) {
        this.clock = Strongback.timeSystem();
        this.accelerometer = accelerometer;

    }

    public void calculatePosition() {
        current = clock.currentTimeInMillis();
        delta = current - prev;
        prev = current;
        DoubleVector deltaVector = new DoubleVector((double)delta);
        ThreeAxisAcceleration a = accelerometer.getAcceleration();
        velocity.integrate(new DoubleVector(a.getX(),a.getY(),a.getZ()), deltaVector);
        position.integrate(velocity.getValue(), deltaVector);
    }

    @Override
    public double getDistanceInInches() {
        return 0;
    }

    @Override
    public double getDistanceInFeet() {
        return 0;
    }

    @Override
    public DistanceSensor zero() {
        return this;
    }


}
