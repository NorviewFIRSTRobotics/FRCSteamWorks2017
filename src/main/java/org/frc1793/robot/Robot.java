package org.frc1793.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.frc1793.robot.commands.TimedDriveCommand;
import org.strongback.Strongback;
import org.strongback.components.CurrentSensor;
import org.strongback.components.Motor;
import org.strongback.components.VoltageSensor;
import org.strongback.components.ui.ContinuousRange;
import org.strongback.components.ui.FlightStick;
import org.strongback.drive.TankDrive;
import org.strongback.hardware.Hardware;

public class Robot extends IterativeRobot {
    private TankDrive drive;
    private ContinuousRange driveSpeed;
    private ContinuousRange turnSpeed;
    @Override
    public void robotInit() {
        Strongback.configure().recordNoEvents().recordNoData();

        Motor left = Motor.compose(Hardware.Motors.talonSRX(1), Hardware.Motors.talonSRX(2));
        Motor right = Motor.compose(Hardware.Motors.talonSRX(3), Hardware.Motors.talonSRX(4));

        drive = new TankDrive(left, right);

        // Set up the human input controls for teleoperated mode. We want to use the Logitech Attack 3D's throttle as a
        // "sensitivity" input to scale the drive speed and throttle, so we'll map it from it's native [-1,1] to a simple scale
        // factor of [0,1] ...
        FlightStick joystick = Hardware.HumanInterfaceDevices.logitechAttack3D(0);
        ContinuousRange sensitivity = joystick.getThrottle().map(t -> (t + 1.0) / 2.0);
        driveSpeed = joystick.getPitch().scale(sensitivity::read);
        turnSpeed = joystick.getRoll().scale(sensitivity::read).invert();

        VoltageSensor battery = Hardware.powerPanel().getVoltageSensor();
        CurrentSensor current = Hardware.powerPanel().getCurrentSensor(0);

        // Set up the data recorder to capture the left & right motor speeds (since both motors on the same side should
        // be at the same speed, we can just use the composed motors for each) and the sensitivity. We have to do this
        // before we start Strongback...
        Strongback.dataRecorder()
                .register("Battery Volts",1000, battery::getVoltage)
                .register("Current load", 1000, current::getCurrent)
                .register("Left motors", left)
                .register("Right motors", right)
                .register("Sensitivity", sensitivity.scaleAsInt(1000))
                .register("Drive Speed",driveSpeed::read)
                .register("Turn Speed",turnSpeed::read);
    }
    @Override
    public void autonomousInit() {
        Strongback.start();
        Strongback.submit(new TimedDriveCommand(drive,0.5,0.5,false,5.0));
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
}