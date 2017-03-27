package org.frc1793.robot.mocks;

import org.strongback.components.ui.ContinuousRange;

/**
 * Created by melvin on 2/8/2017.
 * Mock for Continuous Range
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
