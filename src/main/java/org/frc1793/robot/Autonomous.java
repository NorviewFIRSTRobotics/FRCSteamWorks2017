package org.frc1793.robot;

import org.frc1793.robot.commands.drive.TimedDriveCommand;
import org.strongback.Strongback;
import org.strongback.command.Command;
import org.strongback.command.CommandGroup;
import org.strongback.drive.TankDrive;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;


/**
 * Created by melvin on 3/13/2017.
 */
public class Autonomous {
    public static TankDrive drive;

    public static Supplier<Command> drive(double time, double speed, double turn) {
        return () -> new TimedDriveCommand(drive, time, speed, turn);
    }

    public Autonomous(TankDrive drive) {
        this.drive = drive;
    }
    private static double turnTime = 0.42, driveTime0 = 0.88 , driveTime1 = 0.65;
    public void init() {
        Strongback.submit(EnumAuto.fromString(Config.autonomous.get()).getCommand());
    }

    public enum EnumAuto {
        CENTER(drive(0.9, -0.75, 0)),
        RIGHT(drive(driveTime0, -0.75, 0),
                drive(turnTime , 0, 0.5),
                drive(driveTime1, -0.5, 0)),
        LEFT(drive(driveTime0, -0.75, 0),
                drive(turnTime, 0, -0.5),
                drive( driveTime1, -0.5, 0)),
        SHOOTING();

        public static final EnumAuto[] VALUES = values();
        private Supplier<Command> command;
        private List<Supplier<Command>> commands;
        EnumAuto() {
            this.command = null;
        }

        EnumAuto(Supplier<Command> command) {
            this.command = command;
        }
        EnumAuto(Supplier<Command>... commands) {
            this.commands = Arrays.asList(commands);
        }

        public Command getCommand() {
            if(commands != null) {
                Command[] array = commands.stream().map(Supplier::get).toArray(Command[]::new);
                for(Command command: array) {
                    System.out.println(command);
                }
                return CommandGroup.runSequentially(array);
            } else {
                return command.get();
            }
        }
        public static EnumAuto fromString(String str) {
            return valueOf(str.toUpperCase());
        }

    }

}

