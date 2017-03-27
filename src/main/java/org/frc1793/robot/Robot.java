package org.frc1793.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc1793.robot.commands.agitator.AgitateStartCommand;
import org.frc1793.robot.commands.agitator.AgitateStopCommand;
import org.frc1793.robot.commands.firing.ContinuousFireCommand;
import org.frc1793.robot.commands.firing.DisableFireCommand;
import org.frc1793.robot.commands.climber.ClimberStartCommand;
import org.frc1793.robot.commands.climber.ClimberStopCommand;
import org.frc1793.robot.components.PositionCalculator;
import org.frc1793.robot.components.Climber;
import org.frc1793.robot.components.DriverCamera;
import org.frc1793.robot.utils.values.ConfigRange;
import org.frc1793.robot.utils.values.SwitchToggle;
import org.frc1793.robot.utils.Utils;
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

import static org.frc1793.robot.Config.isControllerDrive;
import static org.frc1793.robot.utils.Utils.*;

@SuppressWarnings("ALL")
public class Robot extends IterativeRobot {

    private TankDrive drive;
    private Shooter rightShooter;
    private HopperAgitators agitators;
    private Climber sweeper;
    private DriverCamera driverCamera;
    private PositionCalculator position;
    private CurrentSensor current;
    private VoltageSensor battery;
    private Autonomous autonomous;

    private ConfigRange shooterSpeed;
    private ConfigRange agitatorSpeed;

    private ContinuousRange climberSpeed;
    private ContinuousRange driveSpeed;
    private ContinuousRange turnSpeed;
    @Override
    public void robotInit() {
        Strongback.configure().recordNoEvents().setLogLevel(Logger.Level.DEBUG);
        Config.init();

        this.battery = Hardware.powerPanel().getVoltageSensor();
        this.current = Hardware.powerPanel().getTotalCurrentSensor();

        Motor left = Motor.compose(Hardware.Motors.talon(0), Hardware.Motors.talon(1));
        Motor right = Motor.compose(Hardware.Motors.talon(2), Hardware.Motors.talon(3)).invert();
        this.drive = new TankDrive(left, right);
        //hallo from bee! :3
        this.rightShooter = new Shooter(Hardware.Motors.spark(4));

        this.sweeper = new Climber(Hardware.Motors.talonSRX(0), Hardware.Motors.talonSRX(1));

        this.agitators = new HopperAgitators(Hardware.Motors.victor(5));

        this.driverCamera = new DriverCamera();

        this.autonomous = new Autonomous(drive);

        this.position = new PositionCalculator(Hardware.Accelerometers.builtIn());

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
    public void autonomousPeriodic() {

    }

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
//        System.out.println(Config.autoSender);
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
        reactor.onTriggered(controller.getA(), new SwitchToggle(new ClimberStartCommand(sweeper, climberSpeed), new ClimberStopCommand(sweeper))::run);
//
        shooterSpeed = new ConfigRange(Config.shooterInitialSpeed,0, 1, Config.shooterInitialSpeed.get());

        agitatorSpeed = new ConfigRange(Config.agitatorSpeed,0,1, Config.agitatorSpeed.get());

        reactor.onTriggered(controller.getX(), new SwitchToggle(new AgitateStartCommand(agitators, agitatorSpeed), new AgitateStopCommand(agitators))::run);

        reactor.onTriggered(controller.getButton(10), new SwitchToggle(new ContinuousFireCommand(rightShooter, shooterSpeed), new DisableFireCommand(rightShooter))::run);

        reactor.onTriggered(switchFromDpad(controller.getDPad(0), 270), () -> {
            shooterSpeed.increment(0.01);
            SmartDashboard.putNumber("right shooter speed", shooterSpeed.read() * 100);
        });
        reactor.onTriggered(switchFromDpad(controller.getDPad(0), 90), () -> {
            shooterSpeed.decrement(0.01);
            SmartDashboard.putNumber("right shooter speed", shooterSpeed.read() * 100);
        });

    }

}