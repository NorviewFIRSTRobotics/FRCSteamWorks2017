package org.frc1793.robot.components;

import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.frc1793.robot.Robot;
import org.strongback.command.Requirable;
import org.strongback.components.Motor;
import org.strongback.components.TalonSRX;
import org.strongback.control.PIDController;
import org.strongback.control.TalonController;

import java.util.HashMap;

/**
 * Created by melvin on 1/19/2017.
 * <p>
 * Controller for the fuel shooter
 */
public class Shooter implements Requirable {
    private static final HashMap<Double, Double> LOOKUP_TABLE = new HashMap<>();
    {
        LOOKUP_TABLE.put(1.0,1.0);
    }
    private TalonController talon;

    public Shooter(TalonController talon) {
        this.talon = talon.setFeedbackDevice(TalonSRX.FeedbackDevice.QUADRATURE_ENCODER);
    }

    public void shooter(double distance) {
        fromConfig().setSpeed(calculateSpeed(distance));
        TalonController.Gains g = talon.getGainsForCurrentProfile();
        System.out.printf("%s,%s,%s",g.getP(),g.getI(),g.getD());
    }

    public TalonController fromConfig() {
        return talon.withGains(Robot.porportional.getAsDouble(), Robot.integral.getAsDouble(), Robot.differential.getAsDouble());
    }
    public void stop() {
        talon.stop();
    }

    public double calculateSpeed(double d) {
        return d;
    }

}
