package org.frc1793.robot.components;

import org.frc1793.robot.Config;
import org.strongback.command.Requirable;
import org.strongback.components.TalonSRX;
import org.strongback.components.ui.ContinuousRange;
import org.strongback.control.PIDController;
import org.strongback.control.TalonController;

/**
 * Created by melvin on 1/19/2017.
 * <p>
 * Controller for the fuel shooter
 */
public class Shooter extends BasicMotor implements Requirable {

    public Shooter(TalonSRX talon) {
        super(talon);
    }
    public Shooter(TalonController talon) {
        super(talon);
        ((TalonController)this.motor).setFeedbackDevice(TalonSRX.FeedbackDevice.QUADRATURE_ENCODER);
    }

    @Override
    public void start(ContinuousRange speed) {
        if(!(motor instanceof PIDController))
            super.start(speed);
        else
            fromConfig().setSpeed(speed.read());
    }

    public TalonController fromConfig() {
        return ((TalonController)this.motor).withGains(Config.proportional.getAsDouble(), Config.integral.getAsDouble(), Config.differential.getAsDouble());
    }

}
