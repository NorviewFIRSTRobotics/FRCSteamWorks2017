package org.frc1793.robot.core.utils.math;

import java.util.function.BinaryOperator;

/**
 * Created by melvin on 3/14/2017.
 * Riemann sum any object with a denoted addition and multiplication operator
 */
public class Integral<T> {
    T accumulation;
    final BinaryOperator<T> addition;
    final BinaryOperator<T> multiplier;

    protected Integral(BinaryOperator<T> addition, BinaryOperator<T> multiplier) {
        this.addition = addition;
        this.multiplier = multiplier;
    }

    public void integrate(T value, T time) {
        T scaled = multiplier.apply(value, time);
        if (accumulation == null)
            accumulation = scaled;
        else
            accumulation = addition.apply(scaled, accumulation);
    }

    public T getValue() {
        return accumulation;
    }
}
