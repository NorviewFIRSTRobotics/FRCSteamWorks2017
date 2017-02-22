package org.frc1793.robot.components.vision;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Created by melvin on 2/20/2017.
 */
public class Vision {
    NetworkTable table;

    public Vision() {
        table = NetworkTable.getTable("/GRIP/preprocessed");
    }

    public void run() {
        SmartDashboard.putBoolean("Connected:", table.isConnected());
        SmartDashboard.putString("Keys", table.getKeys().toString());
        double x = (table.getNumber("x-pos",-1));
        double y = (table.getNumber("y-pos",-1));
//        System.out.println(x + "," + y);
        SmartDashboard.putNumber("x-pos", x);
        SmartDashboard.putNumber("y-pos", y);
        SmartDashboard.putNumber("edvewgv",4567890);
    }
}

