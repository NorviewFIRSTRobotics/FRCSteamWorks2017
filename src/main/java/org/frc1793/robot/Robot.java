package org.frc1793.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc1793.robot.commands.AgitateStartCommand;
import org.frc1793.robot.commands.AgitateStopCommand;
import org.frc1793.robot.commands.TimedDriveCommand;
import org.frc1793.robot.commands.firing.ContinuousFireCommand;
import org.frc1793.robot.commands.firing.DisableFireCommand;
import org.frc1793.robot.commands.sweeper.SweeperStartCommand;
import org.frc1793.robot.commands.sweeper.SweeperStopCommand;
import org.frc1793.robot.components.HopperAgitators;
import org.frc1793.robot.components.Shooter;
import org.frc1793.robot.components.Sweeper;
import org.frc1793.robot.components.vision.DriverCamera;
import org.frc1793.robot.components.vision.Vision;
import org.strongback.Logger;
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
import org.strongback.util.Values;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

@SuppressWarnings("ALL")
public class Robot extends IterativeRobot {

    private BooleanSupplier controllerDrive;

    public static DoubleSupplier porportional, integral, differential;

    private boolean sweeperRunning = false;
    private boolean leftShooterRunning = false;
    private boolean rightShooterRunning = false;
    private boolean agitatorRunning = false;

    private TankDrive drive;
    private ContinuousRange driveSpeed;
    private ContinuousRange turnSpeed;

    private Shooter leftShooter, rightShooter;
    private SettableRange leftShooterSpeed = new SettableRange(0,1,1), rightShooterSpeed = new SettableRange(0,1,1);

    private Sweeper sweeper;
    private ContinuousRange sweeperSpeed;

    private HopperAgitators agitators;

    private DriverCamera driverCamera;
    private Vision vision;
    private CurrentSensor current;

    private ContinuousRange agitatorSpeed;
    //Drive PID: 0-3; Sweeper PID: 4; Shooters: CAN Bus
    @Override
    public void robotInit() {
        Strongback.configure().recordNoEvents().setLogLevel(Logger.Level.DEBUG);


        controllerDrive = config("controllerDrive",false);

        VoltageSensor battery = Hardware.powerPanel().getVoltageSensor();
        current = Hardware.powerPanel().getCurrentSensor(0);

        Motor left = Motor.compose(Hardware.Motors.talon(0), Hardware.Motors.talon(1));
        Motor right = Motor.compose(Hardware.Motors.talon(2), Hardware.Motors.talon(3)).invert();
        drive = new TankDrive(left, right);

        //hallo from bee! :3
        this.leftShooter = new Shooter(Hardware.Controllers.talonController(0, 1, 1));
        this.rightShooter = new Shooter(Hardware.Controllers.talonController(1, 1, 1));

        this.sweeper = new Sweeper(Hardware.Motors.spark(4));

        this.agitators = new HopperAgitators(Hardware.Motors.victor(5));
        this.driverCamera = new DriverCamera();

        this.vision = new Vision();

        this.initializeHumanInteraction();
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
        Strongback.submit(new TimedDriveCommand(drive, 0.5, 0, false, 2.0));
    }


    public BooleanSupplier config(String key, boolean defaultVal) {
        if(!Preferences.getInstance().containsKey(key))
            Preferences.getInstance().putBoolean(key,defaultVal);
        return () -> Preferences.getInstance().getBoolean(key, defaultVal);
    }

    public DoubleSupplier config(String key, double defaultVal) {
        if(!Preferences.getInstance().containsKey(key))
            Preferences.getInstance().putDouble(key,defaultVal);
        return () -> Preferences.getInstance().getDouble(key, defaultVal);
    }
    @Override
    public void teleopInit() {

        // Kill anything sweeperRunning if it is ...
        Strongback.disable();
        // Start Strongback functions ...
        Strongback.start();
    }

    @Override
    public void teleopPeriodic() {
        porportional = config("p",0);
        integral = config("i",0);
        differential = config("d",0);

        SmartDashboard.putNumber("Current",current.getCurrent() );
        SmartDashboard.putNumber("Agitator Speed", agitatorSpeed.read());
        vision.run();

        drive.arcade(driveSpeed.read(), turnSpeed.read());
    }

    @Override
    public void disabledInit() {
        // Tell Strongback that the robot is disabled so it can flush and kill commands.
        Strongback.disable();
        sweeperRunning = false;
        rightShooterRunning = false;
        leftShooterRunning = false;
        agitatorRunning = false;
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

    public double fineseControl(double x) {
        double a = 0.5, b = 0.5;
        double y;
        if (x > 0) {
            if (x < a) {
                y = x * b;
            } else {
                y = x - 0.25;
            }
        } else {
            if (x > -a) {
                y = x * b;
            } else {
                y = x + 0.25;
            }
        }
        return y;
    }

    public double fineseSweeper(double x) {
        double a = 0.5, b = 0.5;
        double y;
        if (x > 0) {
            y = x * b + b;
        } else {
            y = x * b - b;
        }
        return y;
    }

    public void initializeHumanInteraction() {
        FlightStick driveStick = microsoftSideWinder(0);
        Gamepad controller = logitechDualAction(1);
        SwitchReactor reactor = Strongback.switchReactor();
        if (controllerDrive.getAsBoolean()) {
            driveSpeed = controller.getLeftY().map(this::fineseControl);
            turnSpeed = controller.getLeftX().map(n -> fineseControl(n)).invert();
        } else {
            driveSpeed = driveStick.getPitch().map(this::fineseControl);
            turnSpeed = driveStick.getYaw().invert().map(this::fineseControl);
        }


        sweeperSpeed = controller.getRightY(); //.map(this::fineseControl);

        agitatorSpeed = driveStick.getThrottle().map(n -> (n+1)/2);

        reactor.onTriggered(controller.getA(), new SwitchToggle(new SweeperStartCommand(sweeper, sweeperSpeed), new SweeperStopCommand(sweeper))::run);
        reactor.onTriggered(controller.getX(), new SwitchToggle(new AgitateStartCommand(agitators, agitatorSpeed), new AgitateStopCommand(agitators))::run);

        reactor.onTriggered(controller.getButton(10), new SwitchToggle(new ContinuousFireCommand(leftShooter, leftShooterSpeed), new DisableFireCommand(leftShooter))::run);
        reactor.onTriggered(controller.getButton(9), new SwitchToggle(new ContinuousFireCommand(rightShooter, rightShooterSpeed.invert()), new DisableFireCommand(rightShooter))::run);

        reactor.onTriggered(switchFromDpad(controller.getDPad(0), 270), () -> {
            rightShooterSpeed.increment(0.01);
            SmartDashboard.putNumber("right shooter speed",rightShooterSpeed.read()*100);
        });
        reactor.onTriggered(switchFromDpad(controller.getDPad(0), 90), () -> {
            rightShooterSpeed.decrement(00.01);
            SmartDashboard.putNumber("right shooter speed",rightShooterSpeed.read()*100);
        });


        reactor.onTriggered(switchFromDpad(controller.getDPad(0), 0), () -> {

            leftShooterSpeed.increment(0.01);
            SmartDashboard.putNumber("left shooter speed",leftShooterSpeed.read()*100);
        });
        reactor.onTriggered(switchFromDpad(controller.getDPad(0), 180), () -> {
            leftShooterSpeed.decrement(0.01);
            SmartDashboard.putNumber("left shooter speed",leftShooterSpeed.read()*100);
        });
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

    public static Switch switchFromRange(ContinuousRange range) {
        return () -> ((int) range.read()) == 1;
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

    public Switch switchFromDpad(DirectionalAxis axis, int angle) {
        return () -> axis.getDirection() == angle;
    }
}