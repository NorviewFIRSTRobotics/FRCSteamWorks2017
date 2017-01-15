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
    private Motor victor;

    @Override
    public void robotInit() {
        Strongback.configure().recordNoEvents().recordNoData();

        VoltageSensor battery = Hardware.powerPanel().getVoltageSensor();
        CurrentSensor current = Hardware.powerPanel().getCurrentSensor(0);
        victor = Hardware.Motors.victor(0);
//        Motor left = Motor.compose(Hardware.Motors.talonSRX(0), Hardware.Motors.talonSRX(1));
//        Motor right = Motor.compose(Hardware.Motors.talonSRX(2), Hardware.Motors.talonSRX(3));
//        drive = new TankDrive(left, right);
        FlightStick stick = Hardware.HumanInterfaceDevices.microsoftSideWinder(0);
        driveSpeed = stick.getPitch();
        turnSpeed = stick.getRoll().invert();


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
//        drive.arcade(driveSpeed.read(), turnSpeed.read());
        victor.setSpeed(driveSpeed.read());
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