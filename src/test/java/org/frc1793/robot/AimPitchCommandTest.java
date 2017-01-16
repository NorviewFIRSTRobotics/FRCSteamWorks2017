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
 *
 *
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 1/15/2017
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
        tester = new CommandTester(new AimPitchCommand(90,motor, SPEED, angleSensor));
        Assertions.assertThat(motor.getSpeed()).isEqualTo(0,TOLERANCE);

        // Start the command with the given artificial start time ...
        tester.step(START_TIME_MS);

        //Set current angle for launcher to 0
        angleSensor.setAngle(0);
        tester.step(START_TIME_MS+1);
        //Motor should be moving positively towards 90 degrees
        Assertions.assertThat(motor.getSpeed()).isEqualTo(1,TOLERANCE);

        //Set current angle for launcher to 120
        angleSensor.setAngle(120);
        tester.step(START_TIME_MS+1);
        //Motor should be moving negatively towards 90 degrees
        Assertions.assertThat(motor.getSpeed()).isEqualTo(-1,TOLERANCE);

        //Set current angle to 90
        angleSensor.setAngle(90);
        tester.step(START_TIME_MS+1);
        //Motor should not be moving, we are at our destination
        Assertions.assertThat(motor.getSpeed()).isEqualTo(0,TOLERANCE);
    }

    @Test
    public void shouldStopWhenCancelled() {
        tester = new CommandTester(new AimPitchCommand(90,motor, 1, angleSensor));
        angleSensor.setAngle(0);
        tester.step(START_TIME_MS);
        Assertions.assertThat(motor.getSpeed()).isEqualTo(1,TOLERANCE);

        tester.cancel();
        tester.step(START_TIME_MS);

        Assertions.assertThat(motor.getSpeed()).isEqualTo(0,TOLERANCE);
    }
}