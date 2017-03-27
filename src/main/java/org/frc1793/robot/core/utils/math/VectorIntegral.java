package org.frc1793.robot.core.utils.math;

import java.util.function.BinaryOperator;

/**
 * Created by melvin on 3/14/2017.
 * Abstract object to allow integrating {@link Vector}s
 */
@SuppressWarnings("unchecked")
public class VectorIntegral<T> extends Integral<Vector<T>> {
    public VectorIntegral(BinaryOperator<T> add, BinaryOperator<T> multiply) {
        super( (Vector<T> a, Vector<T> b) -> a.add(b,add), (Vector<T> a, Vector<T> b) -> a.multiply(b,multiply));
    }

}
