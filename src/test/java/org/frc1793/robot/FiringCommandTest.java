package org.frc1793.robot;

import org.fest.assertions.Delta;
import org.frc1793.robot.commands.firing.ContinuousFireCommand;
import org.junit.Before;
import org.junit.Test;
import org.strongback.command.CommandTester;
import org.strongback.components.TalonSRX;
import org.strongback.mock.Mock;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 1/8/17
 */
@SuppressWarnings("ALL")
public class FiringCommandTest {

    private final Delta TOLERANCE = Delta.delta(0.001);
    private final long START_TIME_MS = 1000;

    private Shooter launcher;
    private TalonSRX motor;
    private CommandTester tester;

    @Before
    public void beforeEach() {
        motor = Mock.stoppedTalonSRX();
        launcher = new Shooter(motor);
    }
    @Test
    public void shouldFireAfterDuration() {
        double speed = 1;
        tester = new CommandTester(new ContinuousFireCommand(launcher,() -> speed));
        assertThat(motor.getSpeed()).isEqualTo(0.0, TOLERANCE);

        tester.step(1);
        assertThat(motor.getSpeed()).isEqualTo(speed, TOLERANCE);
    }

    @Test
    public void shouldStopWhenCancelled() {
        double speed = 1;
        tester = new CommandTester(new ContinuousFireCommand(launcher, () -> speed));
        assertThat(motor.getSpeed()).isEqualTo(0.0, TOLERANCE);

        tester.step(1);
        assertThat(motor.getSpeed()).isEqualTo(speed, TOLERANCE);

        tester.cancel();
        tester.step(1);
        assertThat(motor.getSpeed()).isEqualTo(0.0, TOLERANCE);
    }
}
