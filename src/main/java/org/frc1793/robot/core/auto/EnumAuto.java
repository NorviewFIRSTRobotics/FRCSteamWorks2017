package org.frc1793.robot.core.auto;

import org.frc1793.robot.core.commands.drive.TimedDriveCommand;
import org.strongback.command.Command;
import org.strongback.command.CommandGroup;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static org.frc1793.robot.Robot.driveTime0;
import static org.frc1793.robot.Robot.driveTime1;
import static org.frc1793.robot.Robot.turnTime;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 3/27/17
 */
@SuppressWarnings("unused")

public enum EnumAuto {

    CENTER(drive(0.9, -0.75, 0)),
    RIGHT(drive(driveTime0, -0.75, 0),
            drive(turnTime, 0, 0.5),
            drive(driveTime1, -0.5, 0)),
    LEFT(drive(driveTime0, -0.75, 0),
            drive(turnTime, 0, -0.5),
            drive(driveTime1, -0.5, 0));

    private Supplier<Command> command;
    private List<Supplier<Command>> commands;

    EnumAuto(Supplier<Command> command) {
        this.command = command;
    }

    @SafeVarargs
    EnumAuto(Supplier<Command>... commands) {
        this.commands = Arrays.asList(commands);
    }

    public Command getCommand() {
        if (commands != null) {
            Command[] array = commands.stream().map(Supplier::get).toArray(Command[]::new);
            for (Command command : array) {
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

    public static Supplier<Command> drive(double time, double speed, double turn) {
        return () -> new TimedDriveCommand(Autonomous.drive, time, speed, turn);
    }

}
