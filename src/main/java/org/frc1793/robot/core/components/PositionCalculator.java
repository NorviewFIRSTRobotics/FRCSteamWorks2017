package org.frc1793.robot.core.components;

import org.frc1793.robot.core.utils.math.DoubleVector;
import org.frc1793.robot.core.utils.math.DoubleVectorIntegral;
import org.strongback.Strongback;
import org.strongback.components.Clock;
import org.strongback.components.DistanceSensor;
import org.strongback.components.ThreeAxisAcceleration;
import org.strongback.components.ThreeAxisAccelerometer;

/**
 * Created by melvin on 3/14/2017.
 * Class to handle integration of acceleration via an accelerometer
 * to get change in distance.
 */
@SuppressWarnings("unused")
public class PositionCalculator implements DistanceSensor {
    private final ThreeAxisAccelerometer accelerometer;

    private final Clock clock;
    private long prev, current;
    private final DoubleVectorIntegral velocity = new DoubleVectorIntegral();
    private final DoubleVectorIntegral position = new DoubleVectorIntegral();

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
        long delta = current - prev;
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
