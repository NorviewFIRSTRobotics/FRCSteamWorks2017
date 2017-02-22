package org.frc1793.robot;

import org.fest.assertions.Assertions;
import org.fest.assertions.Delta;
import org.frc1793.robot.commands.agitator.AgitateStartCommand;
import org.frc1793.robot.components.HopperAgitators;
import org.junit.Before;
import org.junit.Test;
import org.strongback.command.CommandTester;
import org.strongback.components.Motor;
import org.strongback.mock.Mock;

/**
 * Created by melvin on 2/8/2017.
 */
public class AgitatorCommandTest {
    private static final Delta TOLERANCE = Delta.delta(0.1);
    private final long START_TIME_MS = 1000;
    private Motor left, right;
    private HopperAgitators agitators;
    private CommandTester tester;
    @Before
    public void beforeEach() {
        agitators = new HopperAgitators(left = Mock.stoppedMotor());
    }

    @Test
    public void shouldBeAgitating() {
        tester = new CommandTester(new AgitateStartCommand(agitators, () -> -0.25));

        //motors should not be moving
        Assertions.assertThat(left.getSpeed()).isEqualTo(0.0, TOLERANCE);

        tester.step(START_TIME_MS);
        //motors should be moving in opposite directions
        Assertions.assertThat(left.getSpeed()).isEqualTo(0.25, TOLERANCE);

    }

    @Test
    public void shouldStopWhenInterrupted() {
        tester = new CommandTester(new AgitateStartCommand(agitators, () -> 0.25));
        tester.step(START_TIME_MS);
        //motors should be moving in opposite directions
        Assertions.assertThat(left.getSpeed()).isEqualTo(-0.25, TOLERANCE);

        tester.cancel();
        tester.step(START_TIME_MS+1);
        //motors should be moving in opposite directions
        Assertions.assertThat(left.getSpeed()).isEqualTo(0.0, TOLERANCE);
    }
}
