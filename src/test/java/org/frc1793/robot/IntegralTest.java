package org.frc1793.robot;

import org.fest.assertions.Assertions;
import org.fest.assertions.Delta;
import org.frc1793.robot.components.PositionCalculator;
import org.frc1793.robot.utils.DoubleIntegral;
import org.frc1793.robot.utils.Vector;
import org.frc1793.robot.utils.VectorIntegral;
import org.junit.Before;
import org.junit.Test;
import org.strongback.mock.Mock;
import org.strongback.mock.MockAccelerometer;
import org.strongback.mock.MockClock;
import org.strongback.mock.MockThreeAxisAccelerometer;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 3/16/17
 */
public class IntegralTest {
    MockThreeAxisAccelerometer accelerometer;
    MockClock clock;
    PositionCalculator positionCalculator;
    DoubleIntegral doubleIntegral;
    double value;
    VectorIntegral<Double> vectorIntegral, vectorIntegral2;
    Vector<Double> vectorValue;

    @Before
    public void beforeEach() {
        doubleIntegral = new DoubleIntegral();
        vectorIntegral = new VectorIntegral<Double>((a, b) -> a + b, (a, b) -> a * b);
        vectorIntegral2 = new VectorIntegral<Double>((a, b) -> a + b, (a, b) -> a * b);
        vectorValue = new Vector<Double>(0.0);
        value = 0;

        clock = Mock.clock();
        accelerometer = Mock.accelerometer3Axis();
        positionCalculator = new PositionCalculator(clock, accelerometer);
    }

    @Test
    public void testDoubleIntegral() {
        int iterations = 100;
        for (int i = 0; i <= iterations; i++) {
            doubleIntegral.integrate(value, 1.0);
//            System.out.printf("<%s,%s>\n", value, doubleIntegral.getValue());
            value++;
        }
        Assertions.assertThat(doubleIntegral.getValue()).isEqualTo(5000, Delta.delta(iterations));
    }

    @Test
    public void testVectorIntegral() {
        int iterations = 100;
        for (int i = 0; i <= iterations; i++) {
            vectorIntegral.integrate(vectorValue, new Vector<>(1.0));
//            System.out.printf("<%s,%s>\n", vectorValue, vectorIntegral.getValue());
            vectorValue.add(1.0, 1.0, 1.0, (a, b) -> a + b);
        }
        Assertions.assertThat(vectorIntegral.getValue().getX()).isEqualTo(5000, Delta.delta(iterations));
        Assertions.assertThat(vectorIntegral.getValue().getY()).isEqualTo(5000, Delta.delta(iterations));
        Assertions.assertThat(vectorIntegral.getValue().getZ()).isEqualTo(5000, Delta.delta(iterations));

    }

    @Test
    public void shouldGivePosition() {
        for(int i = 0;i < 10;i++) {
            clock.incrementBySeconds(1);
            ((MockAccelerometer) accelerometer.getDirection(0)).setAcceleration(0);
            positionCalculator.calculatePosition();
//            System.out.println(positionCalculator.getDistanceInFeet());
        }
    }
}
