package org.firstinspires.ftc.teamcode.testing;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * FTC Java8-safe intrinsics estimator for VisionPortal AprilTags.
 *
 * Outputs approximate:
 *   fx, fy, cx, cy
 *
 * HOW TO USE:
 * 1) Print/hold ONE AprilTag and measure the BLACK square edge (not white border).
 * 2) Set TAG_SIZE_METERS below correctly.
 * 3) Run this OpMode.
 * 4) Keep tag fully visible, good lighting, 0.3m–1.5m away.
 * 5) Move slowly and vary distance a bit.
 * 6) Copy fx/fy/cx/cy from telemetry into your robot code:
 *      .setLensIntrinsics(fx, fy, cx, cy)
 *
 * NOTES:
 * - This does NOT model lens distortion. It still usually improves pose a lot.
 * - cx/cy are assumed to be image center (common for webcams).
 * - If your SDK exposes corner points, this uses them. If not, it uses a bounding-box
 *   approximation from values that are typically present.
 */
@TeleOp(name = "Calibrate: AprilTag Intrinsics (MJPEG 640x480)", group = "Calibration")
public class AprilTagIntrinsicsCalibrator extends LinearOpMode {

    // ====== CAMERA SETTINGS (match your real code) ======
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private static final VisionPortal.StreamFormat STREAM_FORMAT = VisionPortal.StreamFormat.MJPEG;

    // ====== YOU MUST SET THIS ======
    // Physical size of the AprilTag BLACK square edge, in METERS.
    // Example: 6 inches -> 0.1524m. 10cm -> 0.10m.
    private static final double TAG_SIZE_METERS = 0.1651; // <-- CHANGE if needed

    // ====== FILTERS / STABILITY ======
    private static final int WINDOW = 60;          // rolling average samples
    private static final double MIN_RANGE_M = 0.25;
    private static final double MAX_RANGE_M = 3.00;

    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTag;

    private final Deque<Double> fxSamples = new ArrayDeque<Double>();
    private final Deque<Double> fySamples = new ArrayDeque<Double>();

    @Override
    public void runOpMode() {

        aprilTag = AprilTagProcessor.easyCreateWithDefaults();

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setCameraResolution(new Size(WIDTH, HEIGHT))
                .setStreamFormat(STREAM_FORMAT)
                .addProcessor(aprilTag)
                .build();

        final double cx = WIDTH / 2.0;
        final double cy = HEIGHT / 2.0;

        telemetry.addLine("AprilTag Intrinsics Calibrator (Java 8)");
        telemetry.addData("Resolution", "%dx%d", WIDTH, HEIGHT);
        telemetry.addData("StreamFormat", STREAM_FORMAT);
        telemetry.addData("TAG_SIZE_METERS", TAG_SIZE_METERS);
        telemetry.addLine("Put ONE tag in view. Good lighting. 0.3m–1.5m away.");
        telemetry.addLine("Move slowly, vary distance.");
        telemetry.addLine("Press START.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            List<AprilTagDetection> dets = aprilTag.getDetections();
            AprilTagDetection best = pickClosestValid(dets);

            if (best == null) {
                telemetry.addData("#detections", dets.size());
                telemetry.addLine("No usable detection yet.");
                telemetry.addLine("Make tag bigger in frame + improve lighting.");
                telemetry.update();
                sleep(50);
                continue;
            }

            // ftcPose.range is reported in INCHES in the official sample telemetry.
            // Convert inches -> meters:
            double rangeM = best.ftcPose.range * 0.0254;

            double pixelW = estimateTagPixelWidth(best);
            double pixelH = estimateTagPixelHeight(best);

            telemetry.addData("Tag", "id=%d  name=%s",
                    best.id, (best.metadata != null ? best.metadata.name : "unknown"));
            telemetry.addData("Range (m)", "%.3f", rangeM);
            telemetry.addData("Tag px (w,h)", "%.1f , %.1f", pixelW, pixelH);

            if (pixelW <= 5.0) {
                telemetry.addLine("Couldn't estimate tag pixel width.");
                telemetry.addLine("If your SDK has no corners/bounds, tell me what fields your detection has.");
                telemetry.update();
                sleep(50);
                continue;
            }

            // Pinhole approximation:
            // pixelWidth ≈ fx * (tagSize / range)  => fx ≈ pixelWidth * range / tagSize
            double fx = (pixelW * rangeM) / TAG_SIZE_METERS;

            // If we got a reasonable pixel height, compute fy similarly. Otherwise assume square pixels.
            double fy = fx;
            if (pixelH > 5.0) {
                fy = (pixelH * rangeM) / TAG_SIZE_METERS;
            }

            pushSample(fxSamples, fx);
            pushSample(fySamples, fy);

            telemetry.addLine("----- Copy these into your code -----");
            telemetry.addData("cx", "%.1f", cx);
            telemetry.addData("cy", "%.1f", cy);
            telemetry.addData("fx_avg", "%.1f", avg(fxSamples));
            telemetry.addData("fy_avg", "%.1f", avg(fySamples));
            telemetry.addData("samples", "%d", fxSamples.size());

            telemetry.addLine("Use:");
            telemetry.addLine(String.format("setLensIntrinsics(%.1f, %.1f, %.1f, %.1f)",
                    avg(fxSamples), avg(fySamples), cx, cy));

            telemetry.update();
            sleep(50);
        }

        if (visionPortal != null) visionPortal.close();
    }

    /**
     * Pick the closest detection with a usable ftcPose + sane range.
     */
    private AprilTagDetection pickClosestValid(List<AprilTagDetection> dets) {
        AprilTagDetection best = null;
        double bestRange = 1e9;

        for (int i = 0; i < dets.size(); i++) {
            AprilTagDetection d = dets.get(i);
            if (d == null || d.ftcPose == null) continue;

            double rangeM = d.ftcPose.range * 0.0254;
            if (rangeM < MIN_RANGE_M || rangeM > MAX_RANGE_M) continue;

            if (rangeM < bestRange) {
                bestRange = rangeM;
                best = d;
            }
        }
        return best;
    }

    private void pushSample(Deque<Double> q, double v) {
        q.addLast(v);
        while (q.size() > WINDOW) q.removeFirst();
    }

    private double avg(Deque<Double> q) {
        if (q.isEmpty()) return 0.0;
        double s = 0.0;
        for (Double v : q) s += v;
        return s / q.size();
    }

    // ==========================================================
    // Pixel size estimation:
    //
    // FTC SDK versions differ. Some expose corner points; some don’t.
    // We try multiple strategies safely.
    // ==========================================================

    /**
     * Strategy order:
     * 1) Use corners if available (most accurate).
     * 2) Use bounding box if available (approx).
     * 3) Return 0 if we can’t.
     */
    private double estimateTagPixelWidth(AprilTagDetection d) {

        // 1) Try corners (reflection so compile works even if field missing)
        Double fromCorners = tryWidthFromCorners(d);
        if (fromCorners != null && fromCorners > 0) return fromCorners;

        // 2) Try bounding box (reflection)
        Double fromBox = tryWidthFromBoundingBox(d);
        if (fromBox != null && fromBox > 0) return fromBox;

        return 0.0;
    }

    private double estimateTagPixelHeight(AprilTagDetection d) {

        Double fromCorners = tryHeightFromCorners(d);
        if (fromCorners != null && fromCorners > 0) return fromCorners;

        Double fromBox = tryHeightFromBoundingBox(d);
        if (fromBox != null && fromBox > 0) return fromBox;

        return 0.0;
    }

    /**
     * Use reflection to read d.corners if it exists. Expected: List with 4 points,
     * each point has public double fields x,y.
     */
    private Double tryWidthFromCorners(AprilTagDetection d) {
        try {
            Object cornersObj = d.getClass().getField("corners").get(d);
            if (!(cornersObj instanceof List)) return null;

            @SuppressWarnings("rawtypes")
            List corners = (List) cornersObj;
            if (corners.size() < 4) return null;

            // corners[0..3] are points with x,y
            Object p0 = corners.get(0);
            Object p1 = corners.get(1);
            Object p2 = corners.get(2);
            Object p3 = corners.get(3);

            double x0 = getDoubleField(p0, "x");
            double y0 = getDoubleField(p0, "y");
            double x1 = getDoubleField(p1, "x");
            double y1 = getDoubleField(p1, "y");
            double x2 = getDoubleField(p2, "x");
            double y2 = getDoubleField(p2, "y");
            double x3 = getDoubleField(p3, "x");
            double y3 = getDoubleField(p3, "y");

            double top = hypot(x1 - x0, y1 - y0);
            double bottom = hypot(x2 - x3, y2 - y3);

            return (top + bottom) / 2.0;
        } catch (Exception ignored) {
            return null;
        }
    }

    private Double tryHeightFromCorners(AprilTagDetection d) {
        try {
            Object cornersObj = d.getClass().getField("corners").get(d);
            if (!(cornersObj instanceof List)) return null;

            @SuppressWarnings("rawtypes")
            List corners = (List) cornersObj;
            if (corners.size() < 4) return null;

            Object p0 = corners.get(0);
            Object p1 = corners.get(1);
            Object p2 = corners.get(2);
            Object p3 = corners.get(3);

            double x0 = getDoubleField(p0, "x");
            double y0 = getDoubleField(p0, "y");
            double x1 = getDoubleField(p1, "x");
            double y1 = getDoubleField(p1, "y");
            double x2 = getDoubleField(p2, "x");
            double y2 = getDoubleField(p2, "y");
            double x3 = getDoubleField(p3, "x");
            double y3 = getDoubleField(p3, "y");

            double left = hypot(x3 - x0, y3 - y0);
            double right = hypot(x2 - x1, y2 - y1);

            return (left + right) / 2.0;
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Try detection.boundingBox if present.
     * Some versions expose something like RectF or similar with left/top/right/bottom.
     */
    private Double tryWidthFromBoundingBox(AprilTagDetection d) {
        try {
            Object bb = d.getClass().getField("boundingBox").get(d);
            if (bb == null) return null;

            double left = getDoubleField(bb, "left");
            double right = getDoubleField(bb, "right");
            return Math.abs(right - left);
        } catch (Exception ignored) {
            return null;
        }
    }

    private Double tryHeightFromBoundingBox(AprilTagDetection d) {
        try {
            Object bb = d.getClass().getField("boundingBox").get(d);
            if (bb == null) return null;

            double top = getDoubleField(bb, "top");
            double bottom = getDoubleField(bb, "bottom");
            return Math.abs(bottom - top);
        } catch (Exception ignored) {
            return null;
        }
    }

    private double getDoubleField(Object obj, String fieldName) throws Exception {
        Object v = obj.getClass().getField(fieldName).get(obj);
        if (v instanceof Double) return (Double) v;
        if (v instanceof Float) return ((Float) v).doubleValue();
        if (v instanceof Integer) return ((Integer) v).doubleValue();
        return Double.parseDouble(String.valueOf(v));
    }

    private double hypot(double a, double b) {
        return Math.hypot(a, b);
    }
}
