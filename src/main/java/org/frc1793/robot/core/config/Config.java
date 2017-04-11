package org.frc1793.robot.core.config;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 2/22/17
 */
@SuppressWarnings("unused")
public class Config {

    public static ConfigOption<Boolean> isControllerDrive;
    public static ConfigOption<String> autonomous;

    public static void init() {
        autonomous = config("autonomous", "CENTER");
        isControllerDrive = config("driveMode", false);
    }

    public static void update() {
        isControllerDrive = config("isControllerDrive", false);
    }

    public static final Preferences p = Preferences.getInstance();

    public static ConfigOption<Boolean> config(String key, Boolean defaultVal) {
        return new ConfigOption<>(key, defaultVal, p::containsKey, p::putBoolean, p::getBoolean);
    }

    public static ConfigOption<Double> config(String key, Double defaultVal) {
        return new ConfigOption<>(key, defaultVal, p::containsKey, p::putDouble, p::getDouble);
    }

    public static ConfigOption<String> config(String key, String defaultVal) {
        Preferences p = Preferences.getInstance();
        return new ConfigOption<>(key, defaultVal, p::containsKey, p::putString, p::getString);
    }

}

