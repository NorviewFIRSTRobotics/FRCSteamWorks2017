package org.frc1793.robot;

import org.fest.assertions.Assertions;
import org.fest.assertions.Delta;
import org.frc1793.robot.commands.firing.AimYawCommand;
import org.junit.Before;
import org.junit.Test;
import org.strongback.command.CommandTester;
import org.strongback.components.Motor;
import org.strongback.drive.TankDrive;
import org.strongback.mock.Mock;
import org.strongback.mock.MockAngleSensor;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 1/15/17
 */
public class AimYawCommandTest {

    private final Delta TOLERANCE = Delta.delta(0.001);
    private final long START_TIME_MS = 1000;

    private Motor leftMotor;
    private Motor rightMotor;
    private TankDrive drive;
    private MockAngleSensor angleSensor;

    private CommandTester tester;

    @Before
    public void beforeEach() {
        leftMotor = Mock.stoppedMotor();
        rightMotor = Mock.stoppedMotor();
        drive = new TankDrive(leftMotor, rightMotor);
        angleSensor = Mock.angleSensor();
    }

    @Test
    public void shouldStopAfterAngleReached() {
        tester = new CommandTester(new AimYawCommand(drive,angleSensor,1,false,90));
        Assertions.assertThat(leftMotor.getSpeed()).isEqualTo(0,TOLERANCE);
        Assertions.assertThat(rightMotor.getSpeed()).isEqualTo(0,TOLERANCE);

        // Start the command with the given artificial start time ...
        tester.step(START_TIME_MS);

        //Set angle to 0, less than target
        angleSensor.setAngle(0);
        tester.step(START_TIME_MS+1);
        //Left motor should be reverse and right forward, we are turning positively towards 90
        Assertions.assertThat(leftMotor.getSpeed()).isEqualTo(-1,TOLERANCE);
        Assertions.assertThat(rightMotor.getSpeed()).isEqualTo(1,TOLERANCE);

        //Set angle to 120, greater than target
        angleSensor.setAngle(120);
        tester.step(START_TIME_MS+1);
        //Left motor should be forward and right reverse, we are turning negatively towards 90
        Assertions.assertThat(leftMotor.getSpeed()).isEqualTo(1,TOLERANCE);
        Assertions.assertThat(rightMotor.getSpeed()).isEqualTo(-1,TOLERANCE);

        //Set angle to our target
        angleSensor.setAngle(90);
        tester.step(START_TIME_MS+1);
        //Should no longer be moving!
        Assertions.assertThat(leftMotor.getSpeed()).isEqualTo(0,TOLERANCE);
        Assertions.assertThat(rightMotor.getSpeed()).isEqualTo(0,TOLERANCE);
    }

    @Test
    public void shouldStopWhenCancelled() {
        tester = new CommandTester(new AimYawCommand(drive,angleSensor,1,false,90));

        //Should not be moving
        Assertions.assertThat(leftMotor.getSpeed()).isEqualTo(0,TOLERANCE);
        Assertions.assertThat(rightMotor.getSpeed()).isEqualTo(0,TOLERANCE);

        angleSensor.setAngle(0);
        tester.step(START_TIME_MS+1);
        //Should be moving
        Assertions.assertThat(leftMotor.getSpeed()).isEqualTo(-1,TOLERANCE);
        Assertions.assertThat(rightMotor.getSpeed()).isEqualTo(1,TOLERANCE);
        angleSensor.setAngle(0);
//happiness flowers springtime smiles laughter puppies death
        tester.cancel();
        //Cancel the command
        tester.step(START_TIME_MS+1);
        //Should no longer be moving
        Assertions.assertThat(leftMotor.getSpeed()).isEqualTo(0,TOLERANCE);
        Assertions.assertThat(rightMotor.getSpeed()).isEqualTo(0,TOLERANCE);
    }
}
