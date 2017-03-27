package org.frc1793.robot;

import org.fest.assertions.Delta;
import org.frc1793.robot.core.utils.Utils;
import org.junit.Test;

import java.util.function.DoubleFunction;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 2/22/17
 */
public class FinesseTest {
    private final Delta TOLERANCE = Delta.delta(0.001);

    @Test
    public void shouldBeCorrect() {
        DoubleFunction<Double> finesse = Utils::finesseControl;

        assertThat(finesse.apply(0)).isEqualTo(0,TOLERANCE);
        assertThat(finesse.apply(0.5)).isEqualTo(0.25,TOLERANCE);
        assertThat(finesse.apply(0.51)).isEqualTo(0.51,TOLERANCE);
        assertThat(finesse.apply(1)).isEqualTo(1,TOLERANCE);

    }

}
