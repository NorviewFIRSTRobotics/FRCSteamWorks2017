package org.frc1793.robot.core.utils.math;

/**
 * Created by melvin on 3/14/2017.
 * Riemann sum values of a 3 vector of doubles
 */
public class DoubleVectorIntegral extends VectorIntegral<Double> {
    public DoubleVectorIntegral() {
        super((a,b) -> a+b, (a,b) -> a*b);
    }
}
