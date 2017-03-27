package org.frc1793.robot.core.utils.math;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 3/16/17
 */
public class DoubleIntegral extends Integral<Double> {
    public DoubleIntegral() {
        super((a,b)->a+b, (a,b)->a*b);
    }
}
