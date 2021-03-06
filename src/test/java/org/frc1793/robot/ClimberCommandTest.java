package org.frc1793.robot;

import org.fest.assertions.Delta;
import org.frc1793.robot.core.commands.climber.ClimberStartCommand;
import org.frc1793.robot.core.commands.climber.ClimberStopCommand;
import org.frc1793.robot.core.components.Climber;
import org.frc1793.robot.mocks.MockCRange;
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
public class ClimberCommandTest {

    private final Delta TOLERANCE = Delta.delta(0.001);
    private final long START_TIME_MS = 1000;

    private Climber sweeper;
    private Motor left, right;
    private CommandTester tester;

    private MockCRange speed;
    @Before
    public void beforeEach() {
        left = Mock.stoppedMotor();
        right = Mock.stoppedMotor();
        sweeper = new Climber(left,right);
        speed = new MockCRange();
    }
    @Test
    public void shouldContinuouslySweep() {
        tester = new CommandTester(new ClimberStartCommand(sweeper));
        assertThat(left.getSpeed()).isEqualTo(0.0, TOLERANCE);
        assertThat(right.getSpeed()).isEqualTo(0.0, TOLERANCE);

        // Start the command with the given artificial start duration ...
        tester.step(START_TIME_MS);

        // Start the command and simulate duration advancing almost 2 seconds ...
        tester.step(START_TIME_MS + 1999);
        assertThat(left.getSpeed()).isEqualTo(1.0, TOLERANCE);
        assertThat(right.getSpeed()).isEqualTo(-1.0, TOLERANCE);
    }
    @Test
    public void shouldStopWhenStopped() {
        tester = new CommandTester(new ClimberStopCommand(sweeper));

        //Motor is running at full speed;
        left.setSpeed(1);
        right.setSpeed(-1);
        assertThat(left.getSpeed()).isEqualTo(1.0,TOLERANCE);
        assertThat(right.getSpeed()).isEqualTo(-1.0,TOLERANCE);

        //progress stop command, motor should stop
        tester.step(START_TIME_MS);
        assertThat(left.getSpeed()).isEqualTo(0.0,TOLERANCE);
        assertThat(right.getSpeed()).isEqualTo(0.0,TOLERANCE);

    }

    @Test
    public void shouldChangeSpeedWhenChanged() {
        tester = new CommandTester(new ClimberStartCommand(sweeper,speed));

        for(double i = 0; i < 1; i+=0.2) {
            speed.setValue(i);
            tester.step(START_TIME_MS+1);
            assertThat(left.getSpeed()).isEqualTo(i,TOLERANCE);
            assertThat(right.getSpeed()).isEqualTo(-i,TOLERANCE);
        }
    }
    @Test
    public void shouldStopWhenCancelled() {
        tester = new CommandTester(new ClimberStartCommand(sweeper));
        assertThat(left.getSpeed()).isEqualTo(0.0, TOLERANCE);
        assertThat(right.getSpeed()).isEqualTo(0.0, TOLERANCE);
        // Start the command with the given artificial start duration ...
        tester.step(START_TIME_MS);

        // Start the command and simulate duration advancing almost 2 seconds ...
        tester.step(START_TIME_MS + 1999);
        assertThat(left.getSpeed()).isEqualTo(1, TOLERANCE);
        assertThat(right.getSpeed()).isEqualTo(-1, TOLERANCE);

        // Cancel the command, which should interrupt the command and advance the duration ...
        tester.cancel();
        tester.step(START_TIME_MS + 1);
        assertThat(left.getSpeed()).isEqualTo(0.0, TOLERANCE);
        assertThat(right.getSpeed()).isEqualTo(0.0, TOLERANCE);
    }
}
