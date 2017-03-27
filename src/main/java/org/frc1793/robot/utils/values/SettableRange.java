package org.frc1793.robot.utils.values;

import org.strongback.components.ui.ContinuousRange;
import org.strongback.util.Values;

/**
 * Created by melvin on 2/8/2017.
 */
public class SettableRange implements ContinuousRange {
    private double value;
    private double max, min;

    public SettableRange(double min, double max, double initial) {
        this.max = max;
        this.min = min;
        this.value = initial;
    }

    public void increment(double interval) {
        setValue(value+interval);
    }

    public void decrement(double interval) {
        setValue(value-interval);
    }

    public void setValue(double value) {
        this.value = Values.limit(min, value, max);

    }
    @Override
    public double read() {
        return value;
    }

}
