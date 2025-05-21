import java.awt.*;
import javax.swing.*;

public class DrawingProgram extends JFrame {

    public DrawingProgram() {
        super("Painter");

        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(480);
        this.add(splitPane, BorderLayout.CENTER);

        Canvas leftCanvas = new Canvas("Left");
        Canvas rightCanvas = new Canvas("Right");

        splitPane.setLeftComponent(leftCanvas);
        splitPane.setRightComponent(rightCanvas);

        Toolbar toolbar = new Toolbar(leftCanvas, rightCanvas);
        this.add(toolbar, BorderLayout.SOUTH);

        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new DrawingProgram();
    }
}
