package org.firstinspires.ftc.teamcode.subsystems.components;

import org.opencv.core.Point;
import org.opencv.core.Rect;

public class Artifact {
    public Rect boundingBox;
    public double area;
    public Point center;
    public String color;

    public Artifact(Rect rect) {
        this.boundingBox = rect;
        this.area = rect.area();
    }
}
