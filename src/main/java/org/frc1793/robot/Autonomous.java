package org.frc1793.robot;

import org.frc1793.robot.commands.drive.TimedDriveCommand;
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

    public Command drive(double time, double driveSpeed, double turnSpeed) {
        return new TimedDriveCommand(drive, driveSpeed, turnSpeed, false, time);
    }

    public void init() {

        Command command = null;
        switch (Config.EnumAuto.fromName(Config.autonomous.get())) {

            case FORWARD:
                command = drive(2.0, 1, 0);
                break;
            case FORWARD_LEFT:
                command = CommandGroup.runSequentially(
                        drive(0.5, 0.5, 0),
                        drive(0.5, 0.5, -1),
                        drive(0.5, 0.5, 0),
                        drive(0.5, 0.5, 1),
                        drive(0.5, 0.5, 0)
                );

                break;
            case FORWARD_RIGHT:
                CommandGroup.runSequentially(
                        drive(0.5, 0.5, 0),
                        drive(0.5, 0.5, 1),
                        drive(0.5, 0.5, 0),
                        drive(0.5, 0.5, -1),
                        drive(0.5, 0.5, 0)
                );
                break;
            case BACKWARD:
                command = drive(1.1, -0.5, 0);
                break;
            case BACKWARD_LEFT:
                command = CommandGroup.runSequentially(
                        drive(0.25, -1, 0),
                        Command.pause(1),
                        drive(0.25, 0, 0.6),
                        Command.pause(1),
                        drive(0.80, -1, 0),
                        drive(0.25, 0, 0.6)
                );
                break;
            case BACKWARD_RIGHT:
                break;
            case FALLBACK:
                command = Command.create(() -> Strongback.logger().error("AUTONOMOUS MODE NOT SELECTED, FAILING"));
                break;
        }
        Strongback.submit(command);
    }

}

