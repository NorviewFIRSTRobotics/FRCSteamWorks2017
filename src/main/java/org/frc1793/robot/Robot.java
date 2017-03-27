package org.frc1793.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.frc1793.robot.core.auto.Autonomous;
import org.frc1793.robot.core.commands.climber.ClimberStartCommand;
import org.frc1793.robot.core.commands.climber.ClimberStopCommand;
import org.frc1793.robot.core.components.Climber;
import org.frc1793.robot.core.components.DriverCamera;
import org.frc1793.robot.core.config.Config;
import org.frc1793.robot.core.utils.Utils;
import org.frc1793.robot.core.utils.values.SwitchToggle;
import org.strongback.Logger;
import org.strongback.Strongback;
import org.strongback.SwitchReactor;
import org.strongback.components.CurrentSensor;
import org.strongback.components.Motor;
import org.strongback.components.VoltageSensor;
import org.strongback.components.ui.ContinuousRange;
import org.strongback.components.ui.FlightStick;
import org.strongback.components.ui.Gamepad;
import org.strongback.drive.TankDrive;
import org.strongback.hardware.Hardware;

import static org.frc1793.robot.core.config.Config.isControllerDrive;
import static org.frc1793.robot.core.utils.Utils.logitechDualAction;
import static org.strongback.hardware.Hardware.HumanInterfaceDevices.microsoftSideWinder;

@SuppressWarnings("ALL")
public class Robot extends IterativeRobot {

    private TankDrive drive;
    private Climber climber;
    private DriverCamera camera;

    private CurrentSensor current;
    private VoltageSensor battery;

    private Autonomous autonomous;

    private ContinuousRange climberSpeed;
    private ContinuousRange driveSpeed;
    private ContinuousRange turnSpeed;

    public static final double turnTime = 0.42;
    public static final double driveTime0 = 0.88;
    public static final double driveTime1 = 0.65;

    @Override
    public void robotInit() {
        Strongback.configure().recordNoEvents().setLogLevel(Logger.Level.DEBUG);
        Config.init();

        this.battery = Hardware.powerPanel().getVoltageSensor();
        this.current = Hardware.powerPanel().getTotalCurrentSensor();
        //hallo from bee! :3

        Motor left = Motor.compose(Hardware.Motors.talon(0), Hardware.Motors.talon(1));
        Motor right = Motor.compose(Hardware.Motors.talon(2), Hardware.Motors.talon(3)).invert();
        this.drive = new TankDrive(left, right);
        this.climber = new Climber(Hardware.Motors.talonSRX(0), Hardware.Motors.talonSRX(1));
        this.camera = new DriverCamera();

        this.autonomous = new Autonomous(drive);

        this.initializeHumanInteraction();
    }

    @Override
    public void autonomousInit() {
        Strongback.disable();
        Strongback.start();
        Config.update();
        autonomous.init();
    }

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopInit() {
        Config.update();
        // Kill anything if it is ...
        Strongback.disable();
        // Start Strongback functions ...
        Strongback.start();
    }

    @Override
    public void teleopPeriodic() {
        Config.update();
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

    public void initializeHumanInteraction() {
        FlightStick driveStick = microsoftSideWinder(0);
        Gamepad controller = logitechDualAction(1);
        SwitchReactor reactor = Strongback.switchReactor();
        if (isControllerDrive.get()) {
            driveSpeed = controller.getLeftY().map(Utils::finesseControl);
            turnSpeed = controller.getLeftX().map(n -> Utils.finesseControl(n)).invert();
        } else {
            driveSpeed = driveStick.getPitch().map(Utils::finesseControl);
            turnSpeed = driveStick.getYaw().invert().map(Utils::finesseControl);
        }
        climberSpeed = controller.getRightY();
        reactor.onTriggered(controller.getA(), new SwitchToggle(new ClimberStartCommand(climber, climberSpeed), new ClimberStopCommand(climber))::run);
    }

}