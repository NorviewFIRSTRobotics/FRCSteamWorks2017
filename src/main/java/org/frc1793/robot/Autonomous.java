package org.frc1793.robot;

import org.frc1793.robot.commands.drive.TimedDriveCommand;
import org.strongback.Strongback;
import org.strongback.command.Command;
import org.strongback.command.CommandGroup;
import org.strongback.drive.TankDrive;

import java.util.function.Supplier;


/**
 * Created by melvin on 3/13/2017.
 */
public class Autonomous {
    public static Supplier<TankDrive> driveSupplier;

    public static TimedDriveCommand drive(double time, double speed, double turn) {
        return new TimedDriveCommand(driveSupplier.get(), speed, turn, false, time);
    }

    public TankDrive drive;

    public Autonomous(TankDrive drive) {
        this.drive = drive;
        driveSupplier = () -> drive;
    }

    @SuppressWarnings("unused")
    public enum EnumAuto {
        BACKWARD(drive(1.1, -0.5, 0)),
        BACKWARD_LEFT(
                drive(0.25, -1, 0),
                drive(0.25, 0, 0.6),
                drive(0.80, -1, 0),
                drive(0.25, 0, -0.6)
        ),
        BACKWARD_RIGHT(
                drive(0.25, -1, 0),
                drive(0.25, 0, -0.6),
                drive(0.80, -1, 0),
                drive(0.25, 0, 0.6)
        ),
        FALLBACK();
        public static final EnumAuto[] VALUES = values();

        private Command command;

        EnumAuto(Command command) {
            this.command = command;
        }
        EnumAuto(Command... commands) {
            CommandGroup.runSequentially(commands);
        }
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

    public void init() {

        System.out.println(EnumAuto.BACKWARD.command);
    }

}

