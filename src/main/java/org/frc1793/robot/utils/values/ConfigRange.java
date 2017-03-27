package org.frc1793.robot.utils.values;

import org.frc1793.robot.Config;

/**
 * Created by melvin on 3/25/2017.
 */
public class ConfigRange extends SettableRange {
    private Config.ConfigOption<Double> config;
    public ConfigRange(Config.ConfigOption<Double> config, double min, double max, double initial) {
        super(min, max, initial);
        this.config = config;
    }

    @Override
    public void setValue(double value) {
        config.put(value);
        setValue(config.get());
    }
}
