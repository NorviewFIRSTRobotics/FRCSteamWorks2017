package org.frc1793.robot;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import java.util.function.*;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 2/22/17
 */
public class Config {

    public static ConfigOption<Boolean> isControllerDrive;
    public static ConfigOption<Double> shooterInitialSpeed;
    public static ConfigOption<String> autonomous;

    public static ConfigOption<Double> agitatorSpeed;

    //    public static ConfigOption<Double> autonomousDriveTime;
    //    public static ConfigOption<Double> turnTime, turnSpeed;
    //    public static ConfigOption<Boolean> isCameraEnabled;
    public static SendableChooser<Autonomous.EnumAuto> autoSender;

    public static void init() {

        autoSender = new SendableChooser<Autonomous.EnumAuto>();
        autoSender.addDefault("default", Autonomous.EnumAuto.CENTER);
        for(Autonomous.EnumAuto auto: Autonomous.EnumAuto.VALUES) {
            autoSender.addObject(auto.name(),auto);
        }
        autonomous = config("autonomous", "CENTER");
        isControllerDrive = config("isControllerDrive", false);
        shooterInitialSpeed = config("shooterInitialSpeed", 0.77);

        agitatorSpeed = config("agitatorSpeed",0.5);
//        isCameraEnabled = config("isCameraEnabled", false);
//        autonomousDriveTime = config("autonomousDriveTime", 0.5);
//        turnTime = config("turnTime", 1.0);
//        turnSpeed = config("turnSpeed", 1.0);
    }

    public static void update() {
        isControllerDrive = config("isControllerDrive", false);
        shooterInitialSpeed = config("shooterInitialSpeed", 0.77);
    }

    public static Preferences p = Preferences.getInstance();

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


    public static class ConfigOption<T> implements Supplier<T> {
        private String key;
        private T defaultVal;

        private BiFunction<String, T, T> getDashboard;
        private BiConsumer<String, T> setDashboard;

        public ConfigOption(String key, T defaultVal, Predicate<String> contains, BiConsumer<String, T> setDashboard, BiFunction<String, T, T> getDashboard) {
            this.key = key;
            this.defaultVal = defaultVal;
            this.getDashboard = getDashboard;
            this.setDashboard = setDashboard;
            if (!contains.test(key))
                setDashboard.accept(key, this.defaultVal);
        }

        @Override
        public T get() {
            return this.getDashboard.apply(key, defaultVal);
        }

        public void put(T u) {
            this.setDashboard.accept(key, u);
        }
    }
}

