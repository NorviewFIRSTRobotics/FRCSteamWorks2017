package org.frc1793.robot.commands.drive;

import org.strongback.command.Command;
import org.strongback.drive.TankDrive;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 3/16/17
 */
public class DriveUtils {
    public TankDrive drive;
    public static DriveUtils INSTANCE;

    public static void init(TankDrive drive) {
        INSTANCE = new DriveUtils(drive);
    }

    private DriveUtils(TankDrive drive) {
        this.drive = drive;
    }

    public static DriveUtils getUtils() {
        return INSTANCE;
    }

    public static Command drive(double time, double driveSpeed, double turnSpeed) {
        assert INSTANCE != null;
        return new TimedDriveCommand(getUtils().drive, driveSpeed, turnSpeed, false, time);
    }

}
