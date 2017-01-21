package org.frc1793.robot;

import org.fest.assertions.Delta;
import org.frc1793.robot.commands.FireFuelCommand;
import org.junit.Before;
import org.junit.Test;
import org.strongback.command.CommandTester;
import org.strongback.components.Motor;
import org.strongback.mock.Mock;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 1/8/17
 */
public class FiringCommandTest {

    private final Delta TOLERANCE = Delta.delta(0.001);
    private final long START_TIME_MS = 1000;

    private ShooterDrive launcher;
    private Motor left, right;
    private CommandTester tester;

    @Before
    public void beforeEach() {
        left = Mock.stoppedMotor();
        right = Mock.stoppedMotor();
        launcher = new ShooterDrive(left,right);
    }
    @Test
    public void shouldFireAfterDuration() {
        tester = new CommandTester(new FireFuelCommand(launcher,() -> 1, () -> 1,2));
        assertThat(left.getSpeed()).isEqualTo(0.0, TOLERANCE);
        assertThat(right.getSpeed()).isEqualTo(0.0, TOLERANCE);

        // Start the command with the given artificial start time ...
        tester.step(START_TIME_MS);

        // Start the command and simulate time advancing almost 2 seconds ...
        tester.step(START_TIME_MS + 1999);
        assertThat(left.getSpeed()).isEqualTo(1, TOLERANCE);
        assertThat(right.getSpeed()).isEqualTo(-1, TOLERANCE);

        // Advance time past the 2 seconds ...
        tester.step(START_TIME_MS + 2001);
        assertThat(left.getSpeed()).isEqualTo(0.0, TOLERANCE);
        assertThat(right.getSpeed()).isEqualTo(0.0, TOLERANCE);

    }

    @Test
    public void shouldStopWhenCancelled() {
        tester = new CommandTester(new FireFuelCommand(launcher, () -> 1, () -> 1,2));
        assertThat(left.getSpeed()).isEqualTo(0.0, TOLERANCE);
        assertThat(right.getSpeed()).isEqualTo(0.0, TOLERANCE);

        // Start the command with the given artificial start time ...
        tester.step(START_TIME_MS);

        // Start the command and simulate time advancing almost 2 seconds ...
        tester.step(START_TIME_MS + 1999);
        assertThat(left.getSpeed()).isEqualTo(1, TOLERANCE);
        assertThat(right.getSpeed()).isEqualTo(-1, TOLERANCE);

        // Cancel the command, which should interrupt the command and advance the time ...
        tester.cancel();
        tester.step(START_TIME_MS + 1);
        assertThat(left.getSpeed()).isEqualTo(0.0, TOLERANCE);
        assertThat(right.getSpeed()).isEqualTo(0.0, TOLERANCE);
    }
}
