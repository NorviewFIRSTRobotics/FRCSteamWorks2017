package org.frc1793.robot;

import org.fest.assertions.Assertions;
import org.fest.assertions.Delta;
import org.frc1793.robot.commands.AimPitchCommand;
import org.junit.Before;
import org.junit.Test;
import org.strongback.command.CommandTester;
import org.strongback.components.Motor;
import org.strongback.mock.Mock;
import org.strongback.mock.MockAngleSensor;

/**
 * Created by melvin on 1/15/2017.
 */
public class AimPitchCommandTest {
    private final Delta TOLERANCE = Delta.delta(0.001);
    private final long START_TIME_MS = 1000;

    private Motor motor;
    private MockAngleSensor angleSensor;
    private CommandTester tester;

    @Before
    public void beforeEach() {
        motor = Mock.stoppedMotor();
        angleSensor = Mock.angleSensor();
    }

    @Test
    public void shouldStopAfterAngleReached() {
        final double SPEED = 1;
        tester = new CommandTester(new AimPitchCommand(AimPitchCommand.LOW_GOAL,motor, SPEED, angleSensor));
        Assertions.assertThat(motor.getSpeed()).isEqualTo(0,TOLERANCE);

        // Start the command with the given artificial start time ...
        tester.step(START_TIME_MS);

        angleSensor.setAngle(0);
        tester.step(START_TIME_MS+1);
        Assertions.assertThat(motor.getSpeed()).isEqualTo(1,TOLERANCE);

        angleSensor.setAngle(120);
        tester.step(START_TIME_MS+2);
        Assertions.assertThat(motor.getSpeed()).isEqualTo(-1,TOLERANCE);

        angleSensor.setAngle(90);
        tester.step(START_TIME_MS+3);
        Assertions.assertThat(motor.getSpeed()).isEqualTo(0,TOLERANCE);
    }

    @Test
    public void shouldStopWhenCancelled() {
        final double SPEED = 1;
        tester = new CommandTester(new AimPitchCommand(AimPitchCommand.LOW_GOAL,motor, SPEED, angleSensor));
        angleSensor.setAngle(0);
        tester.step(START_TIME_MS);
        Assertions.assertThat(motor.getSpeed()).isEqualTo(1,TOLERANCE);

        tester.cancel();
        tester.step(START_TIME_MS);

        Assertions.assertThat(motor.getSpeed()).isEqualTo(0,TOLERANCE);
    }
}
