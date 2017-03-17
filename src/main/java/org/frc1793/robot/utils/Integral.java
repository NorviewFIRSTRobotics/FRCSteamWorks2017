package org.frc1793.robot.utils;

import java.util.function.BinaryOperator;

/**
 * Created by melvin on 3/14/2017.
 */
public class Integral<T> {
    T accumuation;
    BinaryOperator<T> addition, multiplier;

    protected Integral(BinaryOperator<T> addition, BinaryOperator<T> multipier) {
        this.addition = addition;
        this.multiplier = multipier;
    }

    public void integrate(T value, T time) {
        accumuation = addition.apply(multiplier.apply(value,time),accumuation);
    }

    public T getValue() {
        return accumuation;
    }
}
