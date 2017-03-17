package org.frc1793.robot.utils;

/**
 * Created by melvin on 3/14/2017.
 */
public class DoubleVectorIntegral extends VectorIntegral<Double> {
    public DoubleVectorIntegral() {
        super((a,b) -> a+b, (a,b) -> a*b);
    }
}
