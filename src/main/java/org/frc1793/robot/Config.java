package org.frc1793.robot;

import edu.wpi.first.wpilibj.Preferences;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 2/22/17
 */
public class Config {



    public static DoubleSupplier proportional, integral, differential;
    public static DoubleSupplier autonomousDriveTime;
    public static BooleanSupplier isControllerDrive;

    public static DoubleSupplier rightShooterInitialSpeed, leftShooterInitialSpeed;
    public static Supplier<String> autonomous;

    public static void init() {
        autonomous = config("autonomous", Autonomous.EnumAuto.BACKWARD.getName());
        isControllerDrive = config("isControllerDrive", false);
        autonomousDriveTime = config("autonomousDriveTime", 0.5);

        proportional = config("p", 0.0);
        integral = config("i", 0.0);
        differential = config("d", 0.0);

        rightShooterInitialSpeed = config("rightShooterInitialSpeed", 0.77);
        leftShooterInitialSpeed = config("leftShooterInitialSpeed", 1.0);


    }

    public static void update() {

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

    public static Supplier<String> config(String key, String defaultVal) {
        if(!Preferences.getInstance().containsKey(key)) {
            Preferences.getInstance().putString(key,defaultVal);
        }
        return () -> Preferences.getInstance().getString(key,defaultVal);
    }
}

