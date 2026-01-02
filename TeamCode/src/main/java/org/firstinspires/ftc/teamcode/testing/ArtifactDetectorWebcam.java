package org.firstinspires.ftc.teamcode.testing;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.util.ArrayList;
import java.util.List;

public class ArtifactDetectorWebcam {

    static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main(String[] args) {

        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            System.out.println("Cannot open camera");
            return;
        }

        Mat frame = new Mat();
        ArtDetector detector = new ArtDetector();

        System.out.println("Press Ctrl+C to stop...");
        while (true) {
            camera.read(frame);
            if (frame.empty()) break;

            detector.processFrame(frame);

            if (detector.isArtifactFound()) {
                System.out.println("Artifact found! Right border X: " + detector.getRightBorderX());
                System.out.println("Left border X:  " + detector.getLeftBorderX());
            } else {
                System.out.println("Artifact not found");
            }

            // Optional: add small delay
            try { Thread.sleep(50); } catch (InterruptedException e) { break; }
        }

        camera.release();
    }
}

class ArtDetector {

    private final Scalar lowerHSV = new Scalar(15, 100, 100);
    private final Scalar upperHSV = new Scalar(45, 255, 255);
    private final Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
    private final Mat hsv = new Mat(), mask = new Mat(), morphed = new Mat(), hierarchy = new Mat();

    private boolean artifactFound = false;
    private double rightBorderX = -1;
    private double leftBorderX = -1;

    public Mat processFrame(Mat input) {

        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_BGR2HSV);
        Core.inRange(hsv, lowerHSV, upperHSV, mask);
        Imgproc.morphologyEx(mask, morphed, Imgproc.MORPH_OPEN, kernel);
        Imgproc.morphologyEx(morphed, morphed, Imgproc.MORPH_CLOSE, kernel);

        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(morphed, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        double maxArea = 0;
        Rect bestBox = null;
        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour);
            if (area > maxArea) {
                maxArea = area;
                bestBox = Imgproc.boundingRect(contour);
            }
        }

        if (bestBox != null && maxArea > 200) {
            artifactFound = true;
            rightBorderX = bestBox.x + bestBox.width;
            leftBorderX = bestBox.x;
        } else {
            artifactFound = false;
            rightBorderX = -1;
            leftBorderX = -1;
        }

        return input;
    }

    public boolean isArtifactFound() { return artifactFound; }
    public double getRightBorderX() { return rightBorderX; }
    public double getLeftBorderX() { return leftBorderX; }
}
