package org.frc1793.robot.vision;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.vision.VisionThread;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

/**
 * Created by melvin on 1/26/2017.
 */
public class Vision {

    private static final int IMG_HEIGHT = 120, IMG_WIDTH = 160;
    private static volatile boolean running = false;

    private final VisionThread visionThread;
    private static final Object imgLock = new Object();
    public static double centerX = 0.0;

    public Vision() {
        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
        camera.setFPS(30);
        visionThread = new VisionThread(camera, new GripPipeline(), pipeline -> {
            if (!pipeline.filterContoursOutput().isEmpty()) {
                Rect r = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
                synchronized (imgLock) {
                    centerX = r.x + (r.width / 2);
                }
            }
        }) {
            @Override
            public void run() {
                if (running) {
                    super.run();
                }
            }
        };
        visionThread.start();
    }

    public void startCamera() {
        running = true;
    }

    public void stopCamera() {
        running = false;
    }

    public double getCenterX() {
        return centerX;
    }
}

