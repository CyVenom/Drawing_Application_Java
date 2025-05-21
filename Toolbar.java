import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.filechooser.FileNameExtensionFilter;

class Toolbar extends JPanel {
    private JLabel textLabel;
    private JButton rotateCanvasBtn, colorButton, strokeSizeButton, addAnimalButton, addFlowerButton, addCustomButton, composeButton, saveButton, rotateImageButton, clearRightButton, clearLeftButton;
    private List<ImageIcon> animalImages;
    private List<ImageIcon> flowerImages;
    private int currentAnimalIndex;
    private int currentFlowerIndex;

    public Toolbar(Canvas leftCanvas, Canvas rightCanvas) {
        textLabel = new JLabel("Toolbar: ");
        setPreferredSize(new Dimension(500,100));
        add(textLabel);

        rotateCanvasBtn = new JButton("Rotate Canvas");
        rotateImageButton = new JButton("Rotate Image");
        colorButton = new JButton("Change Pen Color");
        strokeSizeButton = new JButton("Change Pen Stroke Size");
        addAnimalButton = new JButton("Add Animal Image");
        addFlowerButton = new JButton("Add Flower Image");
        addCustomButton = new JButton("Add Custom Image");
        composeButton = new JButton("Compose Canvases");
        saveButton = new JButton("Save Drawn Images");
        clearRightButton = new JButton("Clear Right Canvas");
        clearLeftButton = new JButton("Clear Left Canvas");

        add(rotateCanvasBtn);
        add(rotateImageButton);
        add(colorButton);
        add(strokeSizeButton);
        add(addAnimalButton);
        add(addFlowerButton);
        add(addCustomButton);
        add(composeButton);
        add(saveButton);
        add(clearRightButton);
        add(clearLeftButton);

        rotateCanvasBtn.addActionListener(e -> leftCanvas.rotate(90));
        rotateImageButton.addActionListener(e -> leftCanvas.rotateSelectedImage(90));

        colorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose Pen Color", rightCanvas.penColor);
            if (newColor != null) {
                rightCanvas.setPenColor(newColor);
            }
        });

        // Updated ActionListener to show a slider dialog without numbers
        strokeSizeButton.addActionListener(e -> {
            // Create the JSlider without numbers
            JSlider strokeSizeSlider = new JSlider(JSlider.HORIZONTAL, 1, 50, rightCanvas.penStrokeSize);
            strokeSizeSlider.setMajorTickSpacing(0); // Remove major tick spacing
            strokeSizeSlider.setMinorTickSpacing(0); // Remove minor tick spacing
            strokeSizeSlider.setPaintTicks(true);    // Keep the ticks visible
            strokeSizeSlider.setPaintLabels(false);  // Remove the labels (numbers)

            // Create a panel to hold the slider
            JPanel sliderPanel = new JPanel();
            sliderPanel.add(new JLabel("Pen Stroke Size: "));
            sliderPanel.add(strokeSizeSlider);

            // Show the slider panel in a dialog
            int result = JOptionPane.showConfirmDialog(null, sliderPanel, "Select Pen Stroke Size", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                rightCanvas.setPenStrokeSize(strokeSizeSlider.getValue());
            }
        });

        animalImages = new ArrayList<>();
        loadImages("Images/AnimalImages");
        currentAnimalIndex = 0;

        addAnimalButton.addActionListener(e -> {
            if (!animalImages.isEmpty()) {
                leftCanvas.addImage(new AnimalImage(animalImages.get(currentAnimalIndex).getDescription(), new Point(50, 50)));
                currentAnimalIndex = (currentAnimalIndex + 1) % animalImages.size();
            }
        });

        flowerImages = new ArrayList<>();
        loadImages("Images/FlowerImages");
        currentFlowerIndex = 0;

        addFlowerButton.addActionListener(e -> {
            if (!flowerImages.isEmpty()) {
                leftCanvas.addImage(new FlowerImage(flowerImages.get(currentFlowerIndex).getDescription(), new Point(50, 50)));
                currentFlowerIndex = (currentFlowerIndex + 1) % flowerImages.size();
            }
        });

        addCustomButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                leftCanvas.addImage(new CustomImage(selectedFile.getAbsolutePath(), new Point(250, 250)));
            }
        });

        composeButton.addActionListener(e -> rightCanvas.compose(leftCanvas)); // Compose left canvas into right canvas

        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Image", "png"));
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".png")) {
                    file = new File(file.getAbsolutePath() + ".png");
                }
                rightCanvas.saveToFile(file);
            }
        });

        clearRightButton.addActionListener(e -> rightCanvas.clear()); // Clear the right canvas
        clearLeftButton.addActionListener(e -> leftCanvas.clear()); // Clear the left canvas
    }


    private void loadImages(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png"));

        if (files != null) {
            if(folderPath == "Images/AnimalImages"){
                for (File file : files) {
                    animalImages.add(new ImageIcon(file.getPath()));
                }
            }
            else if(folderPath == "Images/FlowerImages"){ 
                for (File file : files) {
                    flowerImages.add(new ImageIcon(file.getPath()));
                }   
            }
        }
    }
}