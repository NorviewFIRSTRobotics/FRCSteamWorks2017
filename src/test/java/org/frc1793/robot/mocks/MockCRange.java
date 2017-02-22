package org.frc1793.robot.mocks;

import org.strongback.components.ui.ContinuousRange;
import org.strongback.function.DoubleToDoubleFunction;

import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

/**
 * Created by melvin on 2/8/2017.
 */
public class MockCRange implements ContinuousRange {
    private double value;

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public double read() {
        return value;
    }

}
