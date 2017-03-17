package org.frc1793.robot.utils;

import java.util.function.BinaryOperator;

/**
 * Created by melvin on 3/14/2017.
 */
public class VectorIntegral<T> extends Integral<Vector<T>> {
    public VectorIntegral(BinaryOperator<T> add, BinaryOperator<T> multiply) {
        super( (a,b) -> a.add(b,add), (a,b) -> a.multiply(b,multiply));
    }

}
