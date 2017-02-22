package org.frc1793.robot.components.vision;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.vision.VisionThread;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

/**
 * Created by melvin on 1/26/2017.
 * <p>
 * Controller for Camera
 */
public class DriverCamera {

    private static final int IMG_HEIGHT = 120, IMG_WIDTH = 160;

    public DriverCamera() {
        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
        camera.setFPS(30);
        if(!camera.isConnected())
            camera.free();
    }

}

