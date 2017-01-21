package org.frc1793.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc1793.robot.commands.FireFuelCommand;
import org.frc1793.robot.commands.TimedDriveCommand;
import org.strongback.Strongback;
import org.strongback.SwitchReactor;
import org.strongback.components.CurrentSensor;
import org.strongback.components.Motor;
import org.strongback.components.Switch;
import org.strongback.components.VoltageSensor;
import org.strongback.components.ui.ContinuousRange;
import org.strongback.components.ui.DirectionalAxis;
import org.strongback.components.ui.FlightStick;
import org.strongback.components.ui.Gamepad;
import org.strongback.drive.TankDrive;
import org.strongback.hardware.Hardware;

public class Robot extends IterativeRobot {
    private TankDrive drive;
    private ContinuousRange driveSpeed;
    private ContinuousRange turnSpeed;

    private ShooterDrive shooterDrive;
    private ContinuousRange shootingSpeed;
    private UsbCamera camera;
    @Override
    public void robotInit() {
        Strongback.configure().recordNoEvents().recordNoData();

        VoltageSensor battery = Hardware.powerPanel().getVoltageSensor();
        CurrentSensor current = Hardware.powerPanel().getCurrentSensor(0);

        Motor left = Motor.compose(Hardware.Motors.talon(0), Hardware.Motors.talon(1));
        Motor right = Motor.compose(Hardware.Motors.talon(2), Hardware.Motors.talon(3));
        drive = new TankDrive(left, right);
        //hallo from bee! :3
        shooterDrive = new ShooterDrive(Hardware.Motors.victor(4), Hardware.Motors.victor(5));

        initializeHumanInteraction();
        // Set up the data recorder to capture the left & right motor speeds (since both motors on the same side should
        // be at the same speed, we can just use the composed motors for each) and the sensitivity. We have to do this
        // before we start Strongback...
        Strongback.dataRecorder()
                .register("Battery Volts", 1000, battery::getVoltage)
                .register("Current load", 1000, current::getCurrent)
                .register("Drive Speed", driveSpeed::read)
                .register("Turn Speed", turnSpeed::read);
    }

    @Override
    public void autonomousInit() {
        Strongback.start();
        Strongback.submit(new TimedDriveCommand(drive, 0.5, 0.5, false, 5.0));
    }


    @Override
    public void teleopInit() {
        // Kill anything running if it is ...
        Strongback.disable();
        // Start Strongback functions ...
        Strongback.start();
    }
    @Override
    public void teleopPeriodic() {
        SmartDashboard.putNumber("shooterSpeed",shootingSpeed.read());
        drive.arcade(driveSpeed.read(), turnSpeed.read());
    }

    @Override
    public void disabledInit() {
        // Tell Strongback that the robot is disabled so it can flush and kill commands.
        Strongback.disable();
    }


    @Override
    public void testInit() {
        LiveWindow.run();
    }

    /**
     * Useful for configurable scaling of joystick inputs on the fly.
     * @param key String key used to recieve the value from the {@link SmartDashboard}
     * @return the value of the number from the {@link SmartDashboard} corresponding to key, defaults to 1 if no key is available.
     */
    public static double configScale(String key) {
        if(!SmartDashboard.containsKey(key))
            SmartDashboard.putNumber(key,1);
        return SmartDashboard.getNumber(key,1);
    }

    public void initializeHumanInteraction() {
        String joystick = DriverStation.getInstance().getJoystickName(0);
        //TODO replace contains with proper equals value
        if(joystick.toLowerCase().contains("sidewinder")) {
            FlightStick driveStick = microsoftSideWinder(0);
            driveSpeed = driveStick.getPitch();
            turnSpeed = driveStick.getYaw();

            shootingSpeed = driveStick.getThrottle().map( n -> 1-((n+1)/2));
            shootingSpeed = shootingSpeed != null ? shootingSpeed : () -> 1;
            SwitchReactor reactor = Strongback.switchReactor();
            reactor.onTriggered(driveStick.getTrigger(),() -> {
                SmartDashboard.putNumber("shooterSpeed",shootingSpeed.read());
                info("Firing!");
                Strongback.submit(new FireFuelCommand(shooterDrive,shootingSpeed,shootingSpeed,3));
            });

        } else {
            info("Init with Controller %s", joystick);
            //If no FlightSticks are available use a gamepad
            Gamepad gamepad = Hardware.HumanInterfaceDevices.logitechDualAction(0);
            //TODO to be determined
            driveSpeed = gamepad.getRightY();
            turnSpeed = gamepad.getRightX();
            SwitchReactor reactor = Strongback.switchReactor();
            reactor.onTriggeredSubmit(gamepad.getA(), () -> new FireFuelCommand(shooterDrive,shootingSpeed,shootingSpeed,1));
        }
    }

    public void info(String format, Object... o) {
        Strongback.logger().info(String.format(format,o));
    }

    public void debug(String format, Object... o) {
        Strongback.logger().debug(String.format(format,o));
    }

    public void error(String format, Object... o) {
        Strongback.logger().error(String.format(format,o));
    }

    public static FlightStick microsoftSideWinder(int port) {
        Joystick joystick = new Joystick(port);
        joystick.getButtonCount();
        return FlightStick.create(joystick::getRawAxis, joystick::getRawButton, joystick::getPOV, () -> {
            return joystick.getY() * -1.0D;
        }, joystick::getTwist, joystick::getX, joystick::getThrottle, () -> {
            return joystick.getRawButton(1);
        }, () -> {
            return joystick.getRawButton(2);
        });
    }

}