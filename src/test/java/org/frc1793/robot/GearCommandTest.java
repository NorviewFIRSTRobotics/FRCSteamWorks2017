package org.frc1793.robot;

import org.frc1793.robot.core.commands.gear.CloseGearCommand;
import org.frc1793.robot.core.commands.gear.OpenGearCommand;
import org.frc1793.robot.core.components.Servo;
import org.frc1793.robot.mocks.Mocks;
import org.junit.Before;
import org.junit.Test;
import org.strongback.command.CommandTester;
import org.strongback.components.Solenoid;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 4/1/17
 */
public class GearCommandTest {
    private Servo left, right;
    private CommandTester tester;

    @Before
    public void before() {
        left = Mocks.servo();
        right = Mocks.servo();
    }

    @Test
    public void shouldRetract() {
        tester = new CommandTester(new CloseGearCommand(left, right));

        assertThat(left.getDirection()).isEqualTo(Solenoid.Direction.STOPPED);
        assertThat(right.getDirection()).isEqualTo(Solenoid.Direction.STOPPED);
        tester.step(1000);
        assertThat(left.getDirection()).isEqualTo(Solenoid.Direction.RETRACTING);
        assertThat(left.getDirection()).isEqualTo(Solenoid.Direction.RETRACTING);

        tester.cancel();
        tester.step(1001);
        assertThat(left.getDirection()).isEqualTo(Solenoid.Direction.STOPPED);
        assertThat(right.getDirection()).isEqualTo(Solenoid.Direction.STOPPED);
    }

    @Test
    public void shouldExtend() {
        tester = new CommandTester(new OpenGearCommand(left, right));

        assertThat(left.getDirection()).isEqualTo(Solenoid.Direction.STOPPED);
        assertThat(right.getDirection()).isEqualTo(Solenoid.Direction.STOPPED);
        tester.step(1000);
        assertThat(left.getDirection()).isEqualTo(Solenoid.Direction.EXTENDING);
        assertThat(left.getDirection()).isEqualTo(Solenoid.Direction.EXTENDING);

        tester.cancel();
        tester.step(1001);
        assertThat(left.getDirection()).isEqualTo(Solenoid.Direction.STOPPED);
        assertThat(right.getDirection()).isEqualTo(Solenoid.Direction.STOPPED);
    }
}

