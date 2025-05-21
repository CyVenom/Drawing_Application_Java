import java.util.ArrayList;
import java.util.List;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class Canvas extends JPanel {
    //private String side;
    private List<Creation> images;
    private int rotation;
    private Creation selectedImage;
    private List<DrawnLine> drawnLines;
    private DrawnLine currentLine;
    Color penColor;
    int penStrokeSize;

    public Canvas(String side) {
        //this.side = side;
        this.images = new ArrayList<>();
        this.rotation = 0;
        this.selectedImage = null;
        this.drawnLines = new ArrayList<>();
        this.currentLine = null;
        this.penColor = Color.BLACK;
        this.penStrokeSize = 4;

        if (side.equals("Right")) {
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    currentLine = new DrawnLine(penColor, penStrokeSize);
                    currentLine.addPoint(e.getPoint());
                    drawnLines.add(currentLine);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (currentLine != null) {
                        currentLine.addPoint(e.getPoint());
                        currentLine = null;
                    }
                }
            });

            this.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (currentLine != null) {
                        currentLine.addPoint(e.getPoint());
                        repaint();
                    }
                }
            });
        } else if (side.equals("Left")) {
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    selectImageAt(e.getPoint());
                }
            });
            this.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (selectedImage instanceof AnimalImage) {
                        ((AnimalImage) selectedImage).flip();
                    } else if (selectedImage instanceof FlowerImage) {
                        ((FlowerImage) selectedImage).scale(1.1);
                    } else if (selectedImage instanceof CustomImage) {
                        ((CustomImage) selectedImage).transpose();
                    }
                    repaint();
                }
            });
        }
    }

    public void addImage(Creation image) {
        // images.clear();
        images.add(image);
        repaint();
    }

    public void rotate(int degrees) {
        rotation += degrees;
        repaint();
    }

    public void rotateSelectedImage(int degrees) {
        if (selectedImage != null) {
            selectedImage.rotate(degrees);
            repaint();
        }
    }

    public void compose(Canvas source) {
        images.addAll(source.getImages());
        repaint();
    }

    public void clear() {
        images.clear();
        drawnLines.clear();
        repaint();
    }

    public List<Creation> getImages() {
        return images;
    }

    public void setPenColor(Color color) {
        this.penColor = color;
    }

    public void setPenStrokeSize(int size) {
        this.penStrokeSize = size;
    }

    public void saveToFile(File file) {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        paint(g2d);
        g2d.dispose();
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selectImageAt(Point point) {
        for (Creation image : images) {
            if (image.contains(point)) {
                selectedImage = image;
                return;
            }
        }
        selectedImage = null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(Math.toRadians(rotation), getWidth() / 2, getHeight() / 2);
        for (Creation image : images) {
            image.draw(g2d);
        }
        for (DrawnLine line : drawnLines) {
            g2d.setColor(line.getColor());
            g2d.setStroke(new BasicStroke(line.getStrokeSize()));
            List<Point> points = line.getPoints();
            for (int i = 0; i < points.size() - 1; i++) {
                Point p1 = points.get(i);
                Point p2 = points.get(i + 1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }
}