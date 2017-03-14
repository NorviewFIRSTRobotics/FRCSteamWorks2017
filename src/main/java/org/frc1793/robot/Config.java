package org.frc1793.robot;

import edu.wpi.first.wpilibj.Preferences;

import java.util.StringJoiner;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 2/22/17
 */
public class Config {

    public enum EnumAuto {
        FORWARD,
        FORWARD_LEFT,
        FORWARD_RIGHT,
        BACKWARD,
        BACKWARD_LEFT,
        BACKWARD_RIGHT,
        FALLBACK;
        public static final EnumAuto[] VALUES = values();

        public String getName() {
            return this.name().toLowerCase();
        }

        public static EnumAuto fromName(String name) {
            for (EnumAuto e : VALUES) {
                if (name.equalsIgnoreCase(e.getName()))
                    return e;
            }
            return FALLBACK;
        }
    }

    public static DoubleSupplier proportional, integral, differential;
    public static DoubleSupplier autonomousDriveTime;
    public static BooleanSupplier isControllerDrive;

    public static DoubleSupplier rightShooterInitialSpeed, leftShooterInitialSpeed;

    public static Supplier<EnumAuto> autonomous;
    public static void init() {

        autonomous = autoConfig();
        isControllerDrive = config("isControllerDrive", false);
        autonomousDriveTime = config("autonomousDriveTime", 0.5);

        proportional = config("p", 0.0);
        integral = config("i", 0.0);
        differential = config("d", 0.0);

        rightShooterInitialSpeed = config("rightShooterInitialSpeed", 0.77);
        leftShooterInitialSpeed = config("leftShooterInitialSpeed", 1.0);


    }

    public static void update() {
        autonomous = autoConfig();
        isControllerDrive = config("isControllerDrive", false);
        proportional = config("p", 0.0);
        integral = config("i", 0.0);
        differential = config("d", 0.0);
    }

    public static BooleanSupplier config(String key, boolean defaultVal) {
        if (!Preferences.getInstance().containsKey(key))
            Preferences.getInstance().putBoolean(key, defaultVal);
        return () -> Preferences.getInstance().getBoolean(key, defaultVal);
    }

    public static DoubleSupplier config(String key, double defaultVal) {
        if (!Preferences.getInstance().containsKey(key))
            Preferences.getInstance().putDouble(key, defaultVal);
        return () -> Preferences.getInstance().getDouble(key, defaultVal);
    }

    public static IntSupplier config(String key, int defaultVal) {
        if (!Preferences.getInstance().containsKey(key))
            Preferences.getInstance().putInt(key, defaultVal);
        return () -> Preferences.getInstance().getInt(key, defaultVal);
    }

    public static Supplier<String> config(String key, String defaultVal) {
        if (!Preferences.getInstance().containsKey(key))
            Preferences.getInstance().putString(key, defaultVal);
        return () -> Preferences.getInstance().getString(key, defaultVal);
    }


    public static Supplier<EnumAuto> autoConfig() {
        return () -> EnumAuto.fromName(config("autonomous",EnumAuto.BACKWARD.getName()).get());
    }
}

