package org.frc1793.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.frc1793.robot.commands.sweeper.SweeperStartCommand;
import org.frc1793.robot.commands.sweeper.SweeperStopCommand;
import org.frc1793.robot.components.HopperAgitators;
import org.frc1793.robot.components.PositionCalculator;
import org.frc1793.robot.components.Shooter;
import org.frc1793.robot.components.Sweeper;
import org.frc1793.robot.components.vision.DriverCamera;
import org.frc1793.robot.components.vision.Vision;
import org.frc1793.robot.utils.SettableRange;
import org.frc1793.robot.utils.SwitchToggle;
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
import static org.frc1793.robot.utils.Utils.logitechDualAction;
import static org.frc1793.robot.utils.Utils.microsoftSideWinder;

@SuppressWarnings("ALL")
public class Robot extends IterativeRobot {


    private TankDrive drive;
    private Shooter leftShooter, rightShooter;
    private HopperAgitators agitators;
    private Sweeper sweeper;
    private DriverCamera driverCamera;
    private Vision vision;

    private PositionCalculator position;

    private SettableRange leftShooterSpeed;
    private SettableRange rightShooterSpeed;

    private ContinuousRange driveSpeed;
    private ContinuousRange turnSpeed;
    private ContinuousRange sweeperSpeed;
    private ContinuousRange agitatorSpeed;

    private CurrentSensor current;
    private VoltageSensor battery;
    private Autonomous autonomous;

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
//        this.leftShooter = new Shooter(Hardware.Controllers.talonController(0, 1, 1));
//        this.rightShooter = new Shooter(Hardware.Controllers.talonController(1, 1, 1));
        this.sweeper = new Sweeper(Hardware.Motors.talonSRX(0), Hardware.Motors.talonSRX(1));

        this.agitators = new HopperAgitators(Hardware.Motors.victor(5));
        this.driverCamera = new DriverCamera();
//        this.vision = new Vision();

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
//        Strongback.submit(new TimedDriveCommand(this.drive, 0.9,-0.75, 0));
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
//        vision.run();
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
        if (isControllerDrive.getAsBoolean()) {
            driveSpeed = controller.getLeftY().map(Utils::finesseControl);
            turnSpeed = controller.getLeftX().map(n -> Utils.finesseControl(n)).invert();
        } else {
            driveSpeed = driveStick.getPitch().map(Utils::finesseControl);
            turnSpeed = driveStick.getYaw().invert().map(Utils::finesseControl);
        }

        sweeperSpeed = controller.getRightY();
        reactor.onTriggered(controller.getA(), new SwitchToggle(new SweeperStartCommand(sweeper, sweeperSpeed), new SweeperStopCommand(sweeper))::run);



//        reactor.onTriggeredSubmit(driveStick.getTrigger(), () -> new TimedDriveCommand(drive, Config.turnTime.getAsDouble(), 0, Config.turnSpeed.getAsDouble()));
        //        agitatorSpeed = () -> 0.5;

        rightShooterSpeed = new SettableRange(0, 1, Config.rightShooterInitialSpeed.getAsDouble());
        leftShooterSpeed = new SettableRange(0, 1, Config.leftShooterInitialSpeed.getAsDouble());

//        reactor.onTriggered(controller.getA(), new SwitchToggle(new SweeperStartCommand(sweeper, sweeperSpeed), new SweeperStopCommand(sweeper))::run);
//        reactor.onTriggered(controller.getX(), new SwitchToggle(new AgitateStartCommand(agitators, agitatorSpeed), new AgitateStopCommand(agitators))::run);

//        reactor.onTriggered(controller.getButton(9), new SwitchToggle(new ContinuousFireCommand(leftShooter, leftShooterSpeed), new DisableFireCommand(leftShooter))::run);
//        reactor.onTriggered(controller.getButton(10), new SwitchToggle(new ContinuousFireCommand(rightShooter, rightShooterSpeed.invert()), new DisableFireCommand(rightShooter))::run);
//
//        reactor.onTriggered(switchFromDpad(controller.getDPad(0), 270), () -> {
//            rightShooterSpeed.increment(0.01);
//            SmartDashboard.putNumber("right shooter speed", rightShooterSpeed.read() * 100);
//        });
//        reactor.onTriggered(switchFromDpad(controller.getDPad(0), 90), () -> {
//            rightShooterSpeed.decrement(00.01);
//            SmartDashboard.putNumber("right shooter speed", rightShooterSpeed.read() * 100);
//        });
//
//        reactor.onTriggered(switchFromDpad(controller.getDPad(0), 0), () -> {
//            leftShooterSpeed.increment(0.01);
//            SmartDashboard.putNumber("left shooter speed", leftShooterSpeed.read() * 100);
//        });
//        reactor.onTriggered(switchFromDpad(controller.getDPad(0), 180), () -> {
//            leftShooterSpeed.decrement(0.01);
//            SmartDashboard.putNumber("left shooter speed", leftShooterSpeed.read() * 100);
//        });

    }

}