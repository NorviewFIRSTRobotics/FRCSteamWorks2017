package org.frc1793.robot.core.auto;

import org.frc1793.robot.core.config.Config;
import org.strongback.Strongback;
import org.strongback.drive.TankDrive;


/**
 * Created by melvin on 3/13/2017.
 * Provides TankDrive to EnumAuto
 */
@SuppressWarnings("unused")
public class Autonomous {


    public static TankDrive drive;

    public Autonomous(TankDrive drive) {
        Autonomous.drive = drive;
    }


    public void init() {
        Strongback.submit(EnumAuto.fromString(Config.autonomous.get()).getCommand());
    }

}

