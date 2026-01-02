package org.firstinspires.ftc.teamcode.eosvsim;

import org.openftc.easyopencv.OpenCvPipeline;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * ArtifactDetection — A clean, optimized EasyOpenCV pipeline
 * for detecting a colored "artifact" on the FTC field.
 */
public class ArtifactDetection extends OpenCvPipeline {

    // --- HSV thresholds (tune these for your artifact color) ---
    private final Scalar lowerGreenHSV = new Scalar(0, 255, 0);
    private final Scalar upperGreenHSV = new Scalar(45, 255, 255);

    // --- Morphology kernel (used to clean up noise) ---
    private final Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));

    // --- Internal mats (reused to reduce allocation) ---
    private final Mat hsv = new Mat();
    private final Mat mask = new Mat();
    private final Mat morphed = new Mat();
    private final Mat hierarchy = new Mat();

    // --- Detection results ---
    private volatile boolean artifactFound = false;
    private volatile double rightBorderX = -1.0; // -1 means "not found"
    private volatile double leftBorderX = -1.0;
    private volatile Rect artifactBox = null;

    @Override
    public Mat processFrame(Mat input) {
        if (input.empty()) return input;

        // Convert frame to HSV color space
        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_BGR2HSV);

        // Threshold for target color
        Core.inRange(hsv, lowerGreenHSV, upperGreenHSV, mask);

        // Clean up the mask
        Imgproc.morphologyEx(mask, morphed, Imgproc.MORPH_OPEN, kernel);
        Imgproc.morphologyEx(morphed, morphed, Imgproc.MORPH_CLOSE, kernel);

        // Find contours
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(morphed, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Find the largest contour
        double maxArea = 0;
        Rect bestBox = null;

        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour);
            if (area > maxArea) {
                maxArea = area;
                bestBox = Imgproc.boundingRect(contour);
            }
        }

        // Validate detection
        if (bestBox != null && maxArea > 250.0) { // threshold to tune
            artifactFound = true;
            artifactBox = bestBox;
            rightBorderX = bestBox.x + bestBox.width;
            leftBorderX = bestBox.x;

            // Draw detection rectangle
            Imgproc.rectangle(input, bestBox, new Scalar(0, 255, 0), 2);
        } else {
            artifactFound = false;
            artifactBox = null;
            rightBorderX = -1.0;
            leftBorderX = -1.0;
        }

        return input;
    }

    // --- Public getters (thread-safe) ---
    public boolean isArtifactFound() {
        return artifactFound;
    }

    public double getRightBorderX() {
        return rightBorderX;
    }
    public double getLeftBorderX() {return leftBorderX; }

    public Rect getArtifactBox() {
        return artifactBox != null ? artifactBox.clone() : null;
    }

    // --- Optional tuning helper ---
    public void setColorRange(Scalar lower, Scalar upper) {
        lowerGreenHSV.set(lower.val);
        upperGreenHSV.set(upper.val);
    }
}
