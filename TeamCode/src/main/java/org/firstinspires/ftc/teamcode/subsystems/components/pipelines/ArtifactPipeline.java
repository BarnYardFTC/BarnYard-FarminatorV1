package org.firstinspires.ftc.teamcode.subsystems.components.pipelines;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.components.pipelines.Artifact;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class ArtifactPipeline extends OpenCvPipeline {

    // --- COLOR BOUNDS (YCrCb) ---
    public Scalar purpleLower = new Scalar(0, 127, 134); //83.6, 104.8, 134
    public Scalar purpleUpper = new Scalar(255, 174, 162); // 150.2, 196.9, 167.2
    public Scalar greenLower = new Scalar(24, 52, 86); // 0.0, 73.7, 86.0
    public Scalar greenUpper = new Scalar(205, 114, 162); // 213.9, 119, 162.9

    // --- CONSTANTS ---
    private static final double MIN_AREA = 500;
    private static final double MERGE_WIDTH_MULTIPLIER = 1.5;

    // --- PIPELINE STATE ---
    private final ArrayList<Artifact> artifacts = new ArrayList<>();
    private final ArrayList<Double> artifactCoords = new ArrayList<Double>();
    private final Mat ycrcbMat = new Mat();
    private final Mat binaryMatPurple = new Mat();
    private final Mat binaryMatGreen = new Mat();
    private final Mat combinedMask = new Mat();
    private final Mat morphKernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3, 3));

    private final Telemetry telemetry;
    private double leftX1 = -1;
    private double rightX1 = -1;
    private double leftX2 = -1;
    private double rightX2 = -1;


    public ArtifactPipeline(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public Mat processFrame(Mat input) {
        artifacts.clear();
        if (input.empty()) return input;

        // Convert to YCrCb
        Imgproc.cvtColor(input, ycrcbMat, Imgproc.COLOR_RGB2YCrCb);

        // Apply color thresholds
        Core.inRange(ycrcbMat, purpleLower, purpleUpper, binaryMatPurple);
        Core.inRange(ycrcbMat, greenLower, greenUpper, binaryMatGreen);

        // Clean masks with morphology
        Imgproc.morphologyEx(binaryMatPurple, binaryMatPurple, Imgproc.MORPH_OPEN, morphKernel);
        Imgproc.morphologyEx(binaryMatGreen, binaryMatGreen, Imgproc.MORPH_OPEN, morphKernel);

        // Find and store artifacts
        findArtifacts(binaryMatPurple, input, "purple");
        findArtifacts(binaryMatGreen, input, "green");
        getAllArtifactsCoords();

        // Combine masks for visualization
        Core.bitwise_or(binaryMatPurple, binaryMatGreen, combinedMask);
        Mat output = new Mat();
        Core.bitwise_and(input, input, output, combinedMask);

        // Telemetry summary
        telemetry.addData("Artifacts found", artifacts.size());
        int purpleCount = (int) artifacts.stream().filter(a -> a.color.equals("purple")).count();
        int greenCount = (int) artifacts.stream().filter(a -> a.color.equals("green")).count();
        telemetry.addData("Purple", purpleCount);
        telemetry.addData("Green", greenCount);
        telemetry.addData("leftX1", leftX1);
        telemetry.addData("rightX1", rightX1);
        telemetry.addData("leftX2", leftX2);
        telemetry.addData("rightX2", rightX2);

        telemetry.update();

        return output;
    }

    private void findArtifacts(Mat binary, Mat output, String color) {
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(binary, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        hierarchy.release();

        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour);
            if (area < MIN_AREA) continue;

            Rect rect = Imgproc.boundingRect(contour);

            boolean merged = rect.width > 200 * MERGE_WIDTH_MULTIPLIER;
            if (!merged) {
                addArtifact(rect, contour, output, color);
            } else {
                // Split wide contour into two halves
                Mat roi = binary.submat(rect);
                int midX = roi.cols() / 2;

                Rect leftRect = new Rect(0, 0, midX, roi.rows());
                Rect rightRect = new Rect(midX, 0, roi.cols() - midX, roi.rows());

                ArrayList<MatOfPoint> leftContours = getContours(roi.submat(leftRect));
                ArrayList<MatOfPoint> rightContours = getContours(roi.submat(rightRect));

                for (MatOfPoint c : leftContours)
                    if (Imgproc.contourArea(c) > MIN_AREA)
                        addArtifact(relativeToGlobal(c, rect.x, rect.y), c, output, color);

                for (MatOfPoint c : rightContours)
                    if (Imgproc.contourArea(c) > MIN_AREA)
                        addArtifact(relativeToGlobal(c, rect.x + midX, rect.y), c, output, color);

                roi.release();
            }
        }
    }

    private ArrayList<MatOfPoint> getContours(Mat binary) {
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(binary, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        hierarchy.release();
        return contours;
    }

    private void addArtifact(Rect rect, MatOfPoint contour, Mat frame, String color) {
        Artifact artifact = new Artifact(rect);
        artifact.color = color;
        artifact.area = Imgproc.contourArea(contour);
        artifact.center = new Point(rect.x + rect.width / 2.0, rect.y + rect.height / 2.0);
        artifact.boundingBox = new Rect(rect.x, rect.y, rect.width, rect.height);
        artifacts.add(artifact);

        // Draw rectangle and center dot
        Imgproc.rectangle(frame, rect, color.equals("purple") ? new Scalar(255, 0, 255) : new Scalar(0, 255, 0), 2);
        Imgproc.circle(frame, artifact.center, 4, new Scalar(0, 0, 255), -1);
    }

    private Rect relativeToGlobal(MatOfPoint contour, int offsetX, int offsetY) {
        Rect localRect = Imgproc.boundingRect(contour);
        return new Rect(localRect.x + offsetX, localRect.y + offsetY, localRect.width, localRect.height);
    }

    public ArrayList<Artifact> getArtifacts() {
        return artifacts;
    }
    public void getAllArtifactsCoords() {
        if (!artifacts.isEmpty() && artifacts.size() <= 2) {

            artifactCoords.clear();
            if (artifacts.size() == 1) {
                for (Artifact artifact : artifacts) {
                    leftX1 = artifact.boundingBox.x;
                    rightX1 = artifact.boundingBox.x + artifact.boundingBox.width;
                }
            }
            if (artifacts.size() == 2) {
                for (Artifact artifact : artifacts) {
                    double leftX = artifact.boundingBox.x;
                    double rightX = artifact.boundingBox.x + artifact.boundingBox.width;
                    artifactCoords.add(leftX);
                    artifactCoords.add(rightX);
                }
                leftX1 = artifactCoords.get(0);
                rightX1 = artifactCoords.get(1);
                leftX2 = artifactCoords.get(2);
                rightX2 = artifactCoords.get(3);
            }
        } else {
            leftX1 = -1;
            rightX1 = -1;
            leftX2 = -1;
            rightX2 = -1;
        }
    }
}
