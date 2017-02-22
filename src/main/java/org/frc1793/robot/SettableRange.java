package org.frc1793.robot;

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

    public void setValue(double value) {
        this.value = value;
    }

    public void increment(double interval) {
        value = Values.limit(min,value+interval, max);
    }
    public void decrement(double interval) {
        value = Values.limit(min,value-interval, max);
    }
    @Override
    public double read() {
        return value;
    }

}
