package org.frc1793.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc1793.robot.commands.FireFuelCommand;
import org.frc1793.robot.commands.TimedDriveCommand;
import org.strongback.Strongback;
import org.strongback.SwitchReactor;
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

    private Motor ballLauncher;
    private ContinuousRange launcherSpeed;


    @Override
    public void robotInit() {

        Strongback.configure().recordNoEvents().recordNoData();

        VoltageSensor battery = Hardware.powerPanel().getVoltageSensor();
        CurrentSensor current = Hardware.powerPanel().getCurrentSensor(0);

        Motor left = Motor.compose(Hardware.Motors.talonSRX(1), Hardware.Motors.talonSRX(2));
        Motor right = Motor.compose(Hardware.Motors.talonSRX(3), Hardware.Motors.talonSRX(4));
        drive = new TankDrive(left, right);

        ballLauncher = Hardware.Motors.victor(0);

        FlightStick driveStick = Hardware.HumanInterfaceDevices.microsoftSideWinder(0);
        driveSpeed = driveStick.getPitch().scale(configScale("drive_speed"));
        turnSpeed = driveStick.getRoll().scale(configScale("turn_speed")).invert();

        //TODO this is a temporary joystick for testing purposes
        FlightStick launcherStick = Hardware.HumanInterfaceDevices.logitechAttack3D(1);
        launcherSpeed = launcherStick.getPitch().scale(configScale("launcher_speed"));

        SwitchReactor reactor = Strongback.switchReactor();
        reactor.onTriggered(launcherStick.getTrigger(),() -> Strongback.submit(new FireFuelCommand(ballLauncher,configScale("launcher_speed"),1)));

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
        return SmartDashboard.getNumber(key,1);
    }
}