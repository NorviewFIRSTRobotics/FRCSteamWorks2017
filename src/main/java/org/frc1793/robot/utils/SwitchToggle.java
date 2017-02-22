package org.frc1793.robot.utils;


import org.strongback.Strongback;
import org.strongback.command.Command;

/**
 * Created by melvin on 2/15/2017.
 */
public class SwitchToggle {
    private boolean running;
    private Command start, stop;

    public SwitchToggle(Command start, Command stop) {
        this.start = start;
        this.stop = stop;
    }

    public void run() {
        if (running) {
            Strongback.submit(stop);
        } else {
            Strongback.submit(start);
        }
        running = !running;
    }
}
