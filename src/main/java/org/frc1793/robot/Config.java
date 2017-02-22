package org.frc1793.robot;

import edu.wpi.first.wpilibj.Preferences;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

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

    public static void init() {
        isControllerDrive = config("isControllerDrive",false);
        autonomousDriveTime = config("autonomousDriveTime",0.5);

        proportional = config("p",0);
        integral = config("i",0);
        differential = config("d",0);

        rightShooterInitialSpeed = config("rightShooterInitialSpeed", 0.77);
        leftShooterInitialSpeed = config("leftShooterInitialSpeed", 1);
    }
    public static void update() {
        isControllerDrive = config("isControllerDrive",false);
        proportional = config("p",0);
        integral = config("i",0);
        differential = config("d",0);
    }

    public static BooleanSupplier config(String key, boolean defaultVal) {
        if(!Preferences.getInstance().containsKey(key))
            Preferences.getInstance().putBoolean(key,defaultVal);
        return () -> Preferences.getInstance().getBoolean(key, defaultVal);
    }

    public static DoubleSupplier config(String key, double defaultVal) {
        if(!Preferences.getInstance().containsKey(key))
            Preferences.getInstance().putDouble(key,defaultVal);
        return () -> Preferences.getInstance().getDouble(key, defaultVal);
    }
}

