import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class AnimalImage extends Creation {
    private BufferedImage image;
    private boolean flipped = false;
    private Point initialClick;

    public AnimalImage(String file, Point position) {
        super(file, position);
        try {
            image = ImageIO.read(new File(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.flipped = false;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                initialClick = null;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (initialClick != null) {
                    Point current = e.getPoint();
                    int deltaX = current.x - initialClick.x;
                    if (Math.abs(deltaX) > image.getWidth() / 2) {
                        flip();
                        repaint();
                        initialClick = null; // Reset after flipping
                    }
                }
            }
        });
    }

    @Override
    public void flip() {
        flipped = !flipped;
    }

    @Override
    public void rotate(int degrees) {
        rotation += degrees;
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (image != null) {
            AffineTransform transform = new AffineTransform();
            if (flipped) {
                transform.scale(-1, 1);
                transform.translate(-position.x * 2 - image.getWidth(), 0);
            }
            transform.rotate(Math.toRadians(rotation), position.x + image.getWidth() / 2, position.y + image.getHeight() / 2);
            transform.translate(position.x, position.y);
            g2d.drawImage(image, transform, null);
        }
    }

    @Override
    public boolean contains(Point p) {
        if (image != null) {
            Rectangle rect = new Rectangle(position.x, position.y, image.getWidth(), image.getHeight());
            return rect.contains(p);
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw((Graphics2D) g);
    }
}