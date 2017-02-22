package org.frc1793.robot;

import org.fest.assertions.Delta;
import org.frc1793.robot.commands.sweeper.SweeperStartCommand;
import org.frc1793.robot.commands.sweeper.SweeperStopCommand;
import org.frc1793.robot.components.Sweeper;
import org.frc1793.robot.mocks.MockCRange;
import org.junit.Before;
import org.junit.Test;
import org.strongback.command.CommandTester;
import org.strongback.components.Motor;
import org.strongback.components.ui.ContinuousRange;
import org.strongback.mock.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 1/8/17
 */
public class SweeperCommandTest {

    private final Delta TOLERANCE = Delta.delta(0.001);
    private final long START_TIME_MS = 1000;

    private Sweeper sweeper;
    private Motor motor;
    private CommandTester tester;

    private MockCRange speed;
    @Before
    public void beforeEach() {
        motor = Mock.stoppedMotor();
        sweeper = new Sweeper(motor);
        speed = new MockCRange();
    }
    @Test
    public void shouldContinueouslySweep() {
        tester = new CommandTester(new SweeperStartCommand(sweeper));
        assertThat(motor.getSpeed()).isEqualTo(0.0, TOLERANCE);

        // Start the command with the given artificial start time ...
        tester.step(START_TIME_MS);

        // Start the command and simulate time advancing almost 2 seconds ...
        tester.step(START_TIME_MS + 1999);
        assertThat(motor.getSpeed()).isEqualTo(1, TOLERANCE);

        // Advance time past the 2 seconds ...
        tester.step(START_TIME_MS + 2001);
        assertThat(motor.getSpeed()).isEqualTo(1, TOLERANCE);
    }
    @Test
    public void shouldStopWhenStopped() {
        tester = new CommandTester(new SweeperStopCommand(sweeper));

        //Motor is running at full speed;
        motor.setSpeed(1);
        assertThat(motor.getSpeed()).isEqualTo(1.0,TOLERANCE);

        //progress stop command, motor should stop
        tester.step(START_TIME_MS);
        assertThat(motor.getSpeed()).isEqualTo(0.0,TOLERANCE);

    }

    @Test
    public void shouldChangeSpeedWhenChanged() {
        tester = new CommandTester(new SweeperStartCommand(sweeper,speed));

        for(double i = 0; i < 1; i+=0.2) {
            speed.setValue(i);
            tester.step(START_TIME_MS+1);
            assertThat(motor.getSpeed()).isEqualTo(i,TOLERANCE);
        }
    }
    @Test
    public void shouldStopWhenCancelled() {
        tester = new CommandTester(new SweeperStartCommand(sweeper));
        assertThat(motor.getSpeed()).isEqualTo(0.0, TOLERANCE);
        // Start the command with the given artificial start time ...
        tester.step(START_TIME_MS);

        // Start the command and simulate time advancing almost 2 seconds ...
        tester.step(START_TIME_MS + 1999);
        assertThat(motor.getSpeed()).isEqualTo(1, TOLERANCE);

        // Cancel the command, which should interrupt the command and advance the time ...
        tester.cancel();
        tester.step(START_TIME_MS + 1);
        assertThat(motor.getSpeed()).isEqualTo(0.0, TOLERANCE);
    }
}
