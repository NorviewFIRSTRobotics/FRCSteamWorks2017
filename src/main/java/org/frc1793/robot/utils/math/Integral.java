package org.frc1793.robot.utils.math;

import java.util.function.BinaryOperator;

/**
 * Created by melvin on 3/14/2017.
 */
public class Integral<T> {
    T accumuation;
    BinaryOperator<T> addition, multiplier;

    protected Integral(BinaryOperator<T> addition, BinaryOperator<T> multiplier) {
        this.addition = addition;
        this.multiplier = multiplier;
    }

    public void integrate(T value, T time) {
        T scaled = multiplier.apply(value, time);
        if (accumuation == null)
            accumuation = scaled;
        else
            accumuation = addition.apply(scaled, accumuation);
    }

    public T getValue() {
        return accumuation;
    }
}
