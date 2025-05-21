import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JPanel;

abstract class Creation extends JPanel implements ImageManipulation {
    protected String file;
    protected Point position;
    protected int rotation;

    public Creation(String file, Point position) {
        this.file = file;
        this.position = position;
        this.rotation = 0;
    }

    public abstract void rotate(int degrees);
    public abstract void draw(Graphics2D g2d);
    public abstract boolean contains(Point point);
    
    @Override
    public void flip() {}
    @Override
    public void scale(double factor) {}
    @Override
    public void transpose() {}
}