import java.util.ArrayList;
import java.util.List;
import java.awt.Point;
import java.awt.*;

class DrawnLine {
    private List<Point> points;
    private Color color;
    private int strokeSize;

    public DrawnLine(Color color, int strokeSize) {
        this.points = new ArrayList<>();
        this.color = color;
        this.strokeSize = strokeSize;
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public List<Point> getPoints() {
        return points;
    }

    public Color getColor() {
        return color;
    }

    public int getStrokeSize() {
        return strokeSize;
    }
}