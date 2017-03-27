package org.frc1793.robot.core.utils.math;

/**
 * Created by melvin on 3/14/2017.
 * 3 Vector of Doubles
 */
public class DoubleVector extends Vector<Double> {
    public DoubleVector(Double x, Double y, Double z) {
        super(x, y, z);
    }
    public DoubleVector(Double s) {
        this(s,s,s);
    }
}
