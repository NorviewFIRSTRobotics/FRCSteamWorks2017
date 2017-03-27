package org.frc1793.robot.utils;

import edu.wpi.first.wpilibj.Joystick;
import org.strongback.components.Switch;
import org.strongback.components.ui.DirectionalAxis;
import org.strongback.components.ui.FlightStick;
import org.strongback.components.ui.Gamepad;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 2/22/17
 */
public class Utils {

    public static double finesseControl(double x) {
        double a = 0.5, b = 0.5;
        double y;
        if (x > 0) {
            if (x <= a) {
                y = x * b;
            } else {
                y = x;
            }
        } else {
            if (x > -a) {
                y = x * b;
            } else {
                y = x;
            }
        }
        return y;
    }

    public static FlightStick microsoftSideWinder(int port) {
        Joystick joystick = new Joystick(port);
        joystick.getButtonCount();
        return FlightStick.create(
                joystick::getRawAxis,
                joystick::getRawButton,
                joystick::getPOV,
                () -> joystick.getY() * -1.0D,
                joystick::getTwist,
                joystick::getX,
                joystick::getThrottle,
                () -> joystick.getRawButton(1),
                () -> joystick.getRawButton(2));
    }

    public static Gamepad logitechDualAction(int port) {
        Joystick joystick = new Joystick(port);
        joystick.getButtonCount(); //verify
        return Gamepad.create(joystick::getRawAxis,
                joystick::getRawButton,
                joystick::getPOV,
                () -> joystick.getRawAxis(0),
                () -> joystick.getRawAxis(1) * -1,
                () -> joystick.getRawAxis(2),
                () -> joystick.getRawAxis(3) * -1,
                () -> joystick.getRawButton(7) ? 1.0 : 0.0,
                () -> joystick.getRawButton(8) ? 1.0 : 0.0,
                () -> joystick.getRawButton(5),
                () -> joystick.getRawButton(6),
                () -> joystick.getRawButton(2),
                () -> joystick.getRawButton(3),
                () -> joystick.getRawButton(1),
                () -> joystick.getRawButton(4),
                () -> joystick.getRawButton(10),
                () -> joystick.getRawButton(9),
                () -> joystick.getRawButton(11),
                () -> joystick.getRawButton(12));
    }

    public static Switch switchFromDpad(DirectionalAxis axis, int angle) {
        return () -> axis.getDirection() == angle;
    }
}
