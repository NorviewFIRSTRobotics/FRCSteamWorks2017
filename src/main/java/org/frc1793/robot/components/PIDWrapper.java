package org.frc1793.robot.components;

import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import org.strongback.control.TalonController;

/**
 * Created by melvin on 2/18/2017.
 */
public class PIDWrapper implements LiveWindowSendable {
    private TalonController controller;
    private ITable table;

    public PIDWrapper(TalonController controller) {
        this.controller = controller;
    }

    private double getP() {
        return controller.getGainsForCurrentProfile().getP();
    }

    private double getI() {
        return controller.getGainsForCurrentProfile().getI();
    }

    private double getD() {
        return controller.getGainsForCurrentProfile().getD();
    }

    private double getF() {
        return controller.getGainsForCurrentProfile().getFeedForward();
    }
    private void setPID(double p, double i , double d, double f) {
        this.controller.withGains(p, i, d, f);
    }

    private double getTarget() {
        return this.controller.getTarget();
    }
    private void setTarget(double target) {
        this.controller.withTarget(target);
    }

    private boolean isEnable() {
        return controller.isEnabled();
    }

    private final ITableListener listener = (table, key, value, isNew) -> {
        if (key.equals("p") || key.equals("i") || key.equals("d") || key.equals("f")) {
            if (getP() != table.getNumber("p", 0.0) || getI() != table.getNumber("i", 0.0)
                    || getD() != table.getNumber("d", 0.0) || getF() != table.getNumber("f", 0.0)) {
                setPID(table.getNumber("p", 0.0), table.getNumber("i", 0.0), table.getNumber("d", 0.0),
                        table.getNumber("f", 0.0));
            }
        } else if (key.equals("target")) {
            if (getTarget() != (Double) value) {
                setTarget((Double) value);
            }
        } else if (key.equals("enabled")) {
            if (isEnable() != (Boolean) value) {
                if ((Boolean) value) {
                    controller.enable();
                } else {
                    controller.disable();
                }
            }
        }
    };

    @Override
    public void updateTable() {

    }

    @Override
    public void startLiveWindowMode() {
        controller.disable();
    }

    @Override
    public void stopLiveWindowMode() {

    }

    @Override
    public void initTable(ITable table) {
        if (this.table != null) {
            table.removeTableListener(listener);
        }
        this.table = table;
        if (table != null) {
            table.putNumber("p", getP());
            table.putNumber("i", getI());
            table.putNumber("d", getD());
            table.putNumber("f", getF());
            table.putNumber("target", getTarget());
            table.putBoolean("enabled", isEnable());
            table.addTableListener(listener, false);
        }
    }

    @Override
    public ITable getTable() {
        return null;
    }

    @Override
    public String getSmartDashboardType() {
        return null;
    }
}
