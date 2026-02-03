package org.firstinspires.ftc.teamcode.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;

@Config
public class HorizontalLineOverlay implements VisionProcessor {

    // 🔧 Adjustable from FTC Dashboard
    public static int CENTER_X = 320;   // square center X
    public static int CENTER_Y = 240;   // square center Y
    public static int SIZE = 150;       // square side length
    public static int THICKNESS = 6;

    private final Paint paint = new Paint();

    public HorizontalLineOverlay() {
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(THICKNESS);
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        // nothing needed
    }

    @Override
    public Object processFrame(org.opencv.core.Mat frame, long captureTimeNanos) {
        return null; // no image processing, overlay only
    }

    @Override
    public void onDrawFrame(
            Canvas canvas,
            int onscreenWidth,
            int onscreenHeight,
            float scaleBmpPxToCanvasPx,
            float scaleCanvasDensity,
            Object userContext) {

        paint.setStrokeWidth(THICKNESS);

        int half = SIZE / 2;

        float left   = CENTER_X - half;
        float top    = CENTER_Y - half;
        float right  = CENTER_X + half;
        float bottom = CENTER_Y + half;

        canvas.drawRect(left, top, right, bottom, paint);
    }
}
