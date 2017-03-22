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

    public PositionCalculator(Clock clock,ThreeAxisAccelerometer accelerometer) {
        this.clock = clock;
        this.accelerometer = accelerometer;
        this.current = prev = clock.currentTimeInMicros();
    }
    public PositionCalculator(ThreeAxisAccelerometer accelerometer) {
        this(Strongback.timeSystem(),accelerometer);
    }

    public void calculatePosition() {
        current = clock.currentTimeInMicros();
        delta = current - prev;
        prev = current;
        DoubleVector deltaVector = new DoubleVector((double)delta/1000000);
        ThreeAxisAcceleration a = accelerometer.getAcceleration();
        velocity.integrate(new DoubleVector(a.getX(),a.getY(),a.getZ()), deltaVector);

        position.integrate(velocity.getValue(), deltaVector);
    }


    public double getSquareDistance() {
        double x = position.getValue().getX();
        double y = position.getValue().getY();
        double z = position.getValue().getZ();
        return x*x + y*y + z*z;
    }
    @Override
    public double getDistanceInInches() {
        return Math.sqrt(getSquareDistance());
    }

    @Override
    public double getDistanceInFeet() {
        return getDistanceInInches()/12;
    }

    @Override
    public DistanceSensor zero() {
        return this;
    }


}
