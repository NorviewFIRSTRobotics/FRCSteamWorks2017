package org.frc1793.robot;

import org.frc1793.robot.commands.drive.DriveUtils;
import org.strongback.Strongback;
import org.strongback.command.Command;
import org.strongback.command.CommandGroup;
import org.strongback.drive.TankDrive;


/**
 * Created by melvin on 3/13/2017.
 */
public class Autonomous {

    public TankDrive drive;

    public Autonomous(TankDrive drive) {
        this.drive = drive;
    }

    @SuppressWarnings("unused")
    public enum EnumAuto {
        FORWARD(DriveUtils.drive(2.0, 1, 0)),
        FORWARD_LEFT(),
        FORWARD_RIGHT(),
        BACKWARD(DriveUtils.drive(1.1, -0.5, 0)),
        BACKWARD_LEFT(
                DriveUtils.drive(0.25, -1, 0),
                DriveUtils.drive(0.25, 0, 0.6),
                DriveUtils.drive(0.80, -1, 0),
                DriveUtils.drive(0.25, 0, -0.6)
        ),
        BACKWARD_RIGHT(
                DriveUtils.drive(0.25, -1, 0),
                DriveUtils.drive(0.25, 0, -0.6),
                DriveUtils.drive(0.80, -1, 0),
                DriveUtils.drive(0.25, 0, 0.6)
        ),
        FALLBACK(Command.create(() -> Strongback.logger().error("AUTONOMOUS MODE NOT SELECTED, FAILING")));
        public static final EnumAuto[] VALUES = values();
        private Command command;

        EnumAuto() {
        }

        EnumAuto(Command command) {
            this.command = command;
        }

        EnumAuto(Command... sequentially) {
            this.command = CommandGroup.runSequentially(sequentially);
        }

        public Command getCommand() {
            return command;
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
        Strongback.submit(EnumAuto.fromName(Config.autonomous.get()).getCommand());
    }

}

