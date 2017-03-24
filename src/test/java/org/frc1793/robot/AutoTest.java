package org.frc1793.robot;

import org.fest.assertions.Delta;
import org.frc1793.robot.commands.drive.TimedDriveCommand;
import org.junit.Before;
import org.junit.Test;
import org.strongback.command.CommandGroup;
import org.strongback.command.CommandTester;
import org.strongback.components.Motor;
import org.strongback.drive.TankDrive;
import org.strongback.mock.Mock;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 3/21/17
 */
public class AutoTest {
    private final Delta TOLERANCE = Delta.delta(0.001);
    private final long START_TIME_MS = 1000;

    private Motor leftMotor;
    private Motor rightMotor;
    private TankDrive drive;
    private Autonomous autonomous;


    private CommandTester tester;

    @Before
    public void beforeEach() {
        leftMotor = Mock.stoppedMotor();
        rightMotor = Mock.stoppedMotor();
        drive = new TankDrive(leftMotor, rightMotor);
        autonomous = new Autonomous(drive);
    }

    @Test
    public void shouldGoBackwards() {
        tester = new CommandTester(Autonomous.EnumAuto.CENTER.getCommand());
        assertThat(leftMotor.getSpeed()).isEqualTo(0.0, TOLERANCE);
        assertThat(rightMotor.getSpeed()).isEqualTo(0.0, TOLERANCE);

        // Start the command with the given artificial start duration ...
        tester.step(START_TIME_MS);

        // Start the command and simulate duration advancing almost 2 seconds ...
        tester.step(START_TIME_MS + 1);
        assertThat(leftMotor.getSpeed()).isEqualTo(-0.75, TOLERANCE);
        assertThat(rightMotor.getSpeed()).isEqualTo(-0.75, TOLERANCE);

        // Advance duration past the 2 seconds ...
        tester.step(START_TIME_MS + 1000);
        assertThat(leftMotor.getSpeed()).isEqualTo(0.0, TOLERANCE);
        assertThat(rightMotor.getSpeed()).isEqualTo(0.0, TOLERANCE);
    }

//    @Test
    public void shouldDoLeftGear() {
        tester = new CommandTester(CommandGroup.runSequentially(new TimedDriveCommand(drive, 1.0,1.0,0)));
        assertThat(leftMotor.getSpeed()).isEqualTo(0.0, TOLERANCE);
        assertThat(rightMotor.getSpeed()).isEqualTo(0.0, TOLERANCE);

        // Start the command with the given artificial start duration ...
        tester.step(START_TIME_MS);
        // Start the command and simulate duration advancing almost 2 seconds ...
        tester.step(START_TIME_MS+1);
        assertThat(leftMotor.getSpeed()).isEqualTo(1, TOLERANCE);
        assertThat(rightMotor.getSpeed()).isEqualTo(1, TOLERANCE);

        // Advance duration past the 2 seconds ...
        tester.step(START_TIME_MS + 1000);
        assertThat(leftMotor.getSpeed()).isEqualTo(0.0, TOLERANCE);
        assertThat(rightMotor.getSpeed()).isEqualTo(0.0, TOLERANCE);
    }
}
