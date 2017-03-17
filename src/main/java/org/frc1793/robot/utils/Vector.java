package org.frc1793.robot.utils;

import java.util.function.BinaryOperator;

/**
 * Created by melvin on 3/14/2017.
 */
public class Vector<T> {
    private T x, y, z;

    public Vector(T x, T y, T z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector(T a) {
        x = y = z = a;
    }

    public Vector add(Vector<T> vector, BinaryOperator<T> add) {
        return this.add(vector.x, vector.y, vector.z, add);
    }

    public Vector add(T x, T y, T z, BinaryOperator<T> add) {
        this.x = add.apply(this.x, x);
        this.y = add.apply(this.y, y);
        this.z = add.apply(this.z, z);
        return this;
    }

    public Vector multiply(Vector<T> vector, BinaryOperator<T> multiply) {
        return new Vector(multiply.apply(this.x, vector.x), multiply.apply(this.y, vector.y), multiply.apply(this.z, vector.z));
    }

    @Override
    public String toString() {
        return String.format("<%s,%s,%s>", x, y, z);
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }

    public T getZ() {
        return z;
    }
}
