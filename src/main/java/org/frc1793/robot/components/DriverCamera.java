package org.frc1793.robot.components;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.vision.VisionThread;
import org.frc1793.robot.Config;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.AlignExposures;

/**
 * Created by melvin on 1/26/2017.
 * <p>
 * Controller for Camera
 */
public class DriverCamera {

    private static final int IMG_HEIGHT = 120, IMG_WIDTH = 160;

    public DriverCamera() {
        camera0();
    }

    public void camera0() {
        UsbCamera camera0 = CameraServer.getInstance().startAutomaticCapture();
        camera0.setResolution(IMG_WIDTH, IMG_HEIGHT);
        camera0.setFPS(30);
    }

}

