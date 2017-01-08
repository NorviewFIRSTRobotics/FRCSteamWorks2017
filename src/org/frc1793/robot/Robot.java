/* Created Sat Jan 07 17:41:26 EST 2017 */
package org.frc1793.robot;

import org.strongback.Strongback;
import org.strongback.command.Command;
import org.strongback.components.Motor;
import org.strongback.components.ui.ContinuousRange;
import org.strongback.components.ui.FlightStick;
import org.strongback.drive.TankDrive;
import org.strongback.hardware.Hardware;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {
	private TankDrive drive;
	private ContinuousRange driveSpeed;
	private ContinuousRange turnSpeed;
    @Override
    public void robotInit() {
    	Strongback.configure().recordNoEvents().recordNoData();
    	
    	Motor left = Motor.compose(Hardware.Motors.talon(1), Hardware.Motors.talon(2));
    	Motor right = Motor.compose(Hardware.Motors.talon(3), Hardware.Motors.talon(4));
    	
    	drive = new TankDrive(left, right);
    	
    	FlightStick joystick = Hardware.HumanInterfaceDevices.logitechAttack3D(0);
    	driveSpeed = joystick.getPitch();
    	turnSpeed = joystick.getRoll().invert();
    }
    @Override
    public void autonomousInit() {
    	Strongback.start();
    	Strongback.submit(new Command(5.0) {
            @Override
            public boolean execute() {
                drive.arcade(0.5,0);
                return false;
            }

            @Override
            public void end() {
                drive.stop();
            }
        });

    }
    @Override
    public void teleopInit() {
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

}
