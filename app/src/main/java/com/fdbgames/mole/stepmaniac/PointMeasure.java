package com.fdbgames.mole.stepmaniac;

import com.fdbgames.mole.sa.io.files.Measure;

import org.opencv.core.Point;

public class PointMeasure {
    Point point;
    Measure measure;

    public PointMeasure(Point point, Measure measure) {
        this.point = point;
        this.measure = measure;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Measure getMeasure() {
        return measure;
    }

    public void setMeasure(Measure measure) {
        this.measure = measure;
    }
}
