package org.frc1793.robot.utils.math;

import org.frc1793.robot.utils.math.Vector;

/**
 * Created by melvin on 3/14/2017.
 */
public class DoubleVector extends Vector<Double> {
    public DoubleVector(Double x, Double y, Double z) {
        super(x, y, z);
    }
    public DoubleVector(Double s) {
        this(s,s,s);
    }
}
