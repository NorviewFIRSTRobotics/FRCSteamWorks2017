package org.frc1793.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc1793.robot.commands.TimedDriveCommand;
import org.frc1793.robot.vision.Vision;
import org.strongback.Strongback;
import org.strongback.SwitchReactor;
import org.strongback.components.CurrentSensor;
import org.strongback.components.Motor;
import org.strongback.components.Switch;
import org.strongback.components.VoltageSensor;
import org.strongback.components.ui.ContinuousRange;
import org.strongback.components.ui.FlightStick;
import org.strongback.components.ui.Gamepad;
import org.strongback.drive.TankDrive;
import org.strongback.hardware.Hardware;

public class Robot extends IterativeRobot {

    private TankDrive drive;
    private ContinuousRange driveSpeed;
    private ContinuousRange turnSpeed;

    private ShooterDrive leftShooter, rightShooter;
    private ContinuousRange leftShooterSpeed, rightShooterSpeed;

    private Vision vision;

    @Override
    public void robotInit() {
        Strongback.configure().recordNoEvents().recordNoData();

        VoltageSensor battery = Hardware.powerPanel().getVoltageSensor();
        CurrentSensor current = Hardware.powerPanel().getCurrentSensor(0);

        Motor left = Motor.compose(Hardware.Motors.talon(0).invert(), Hardware.Motors.talon(1));
        Motor right = Motor.compose(Hardware.Motors.talon(2).invert(), Hardware.Motors.talon(3));
        drive = new TankDrive(left, right);
        //hallo from bee! :3
        this.leftShooter = new ShooterDrive(Hardware.Controllers.talonController(0, 1, 1));
        this.rightShooter = new ShooterDrive(Hardware.Controllers.talonController(1, 1, 1));

//        vision = new Vision();
//        vision.startCamera();

        initializeHumanInteraction();
        // Set up the data recorder to capture the leftShooter & rightShooter motor speeds (since both motors on the same side should
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
        SmartDashboard.putNumber("leftShooter shooter speed", leftShooterSpeed.read());
        SmartDashboard.putNumber("rightShooter shooter speed", rightShooterSpeed.read());

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
     *
     * @param key String key used to recieve the value from the {@link SmartDashboard}
     * @return the value of the number from the {@link SmartDashboard} corresponding to key, defaults to 1 if no key is available.
     */
    public static double configScale(String key) {
        if (!SmartDashboard.containsKey(key))
            SmartDashboard.putNumber(key, 1);
        return SmartDashboard.getNumber(key, 1);
    }

    public static ContinuousRange getShooterSpeed(ContinuousRange input) {
        ContinuousRange output = input.map(n -> 1 - ((n + 1) / 2));
        return output != null ? output : () -> 1;
    }

    public void initializeHumanInteraction() {
        SmartDashboard.putString("Input 0",DriverStation.getInstance().getJoystickName(0));
        SmartDashboard.putString("Input 1",DriverStation.getInstance().getJoystickName(1));
        FlightStick driveStick = microsoftSideWinder(0);
        driveSpeed = driveStick.getPitch();
        turnSpeed = driveStick.getYaw().invert();

        Gamepad controller = logitechDualAction(1);
        leftShooterSpeed =  () -> 1;
        rightShooterSpeed = () -> 1;

        SwitchReactor reactor = Strongback.switchReactor();

        reactor.whileTriggered(switchFromRange(controller.getLeftTrigger()), () -> leftShooter.drive(leftShooterSpeed.read()));
        reactor.whileUntriggered(switchFromRange(controller.getLeftTrigger()), () -> leftShooter.stop());

        reactor.whileTriggered(switchFromRange(controller.getRightTrigger()), () -> rightShooter.drive(rightShooterSpeed.read()));
        reactor.whileUntriggered(switchFromRange(controller.getRightTrigger()), () -> rightShooter.stop());
    }

    public void info(String format, Object... o) {
        Strongback.logger().info(String.format(format, o));
    }

    public void debug(String format, Object... o) {
        Strongback.logger().debug(String.format(format, o));
    }

    public void error(String format, Object... o) {
        Strongback.logger().error(String.format(format, o));
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

    public static Switch switchFromRange(ContinuousRange range) {
        return () -> range.read() == 1;
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

}