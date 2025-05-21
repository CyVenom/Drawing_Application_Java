import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

class CustomImage extends Creation {
    private BufferedImage image;
    private boolean transposed = false;

    public CustomImage(String file, Point position) {
        super(file, position);
        try {
            image = ImageIO.read(new File(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rotate(int degrees) {
        rotation += degrees;
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (image != null) {
            AffineTransform transform = new AffineTransform();
            transform.rotate(Math.toRadians(rotation), position.x + image.getWidth() / 2, position.y + image.getHeight() / 2);
            transform.translate(position.x, position.y);
            if (transposed) {
                transform.shear(1.0, 0.0);
            }
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
    public void transpose() {
        transposed = !transposed;
    }
}