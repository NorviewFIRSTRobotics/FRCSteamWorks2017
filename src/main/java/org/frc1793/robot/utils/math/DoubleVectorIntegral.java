package org.frc1793.robot.utils.math;

import org.frc1793.robot.utils.math.VectorIntegral;

/**
 * Created by melvin on 3/14/2017.
 */
public class DoubleVectorIntegral extends VectorIntegral<Double> {
    public DoubleVectorIntegral() {
        super((a,b) -> a+b, (a,b) -> a*b);
    }
}
