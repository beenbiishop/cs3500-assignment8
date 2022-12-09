package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import view.panels.HistogramPanel;
import view.panels.MenubarPanel;
import view.panels.MessagePanel;
import view.panels.PreviewPanel;
import view.panels.TransformationPanel;

/**
 * Represents an implementation of the {@link ImageProcessorGui} interface.
 */
public class ImageProcessorGuiImpl implements ImageProcessorGui {

  private final Map<String, int[][]> histograms = new HashMap<>();
  private final Map<String, BufferedImage> images = new HashMap<>();
  private final JFrame frame;
  private final MenubarPanel menubarPanel = new MenubarPanel();
  private final PreviewPanel previewPanel;
  private final TransformationPanel transformationPanel;
  private final HistogramPanel histogramPanel = new HistogramPanel();
  private final MessagePanel messagePanel = new MessagePanel();

  /**
   * Constructs a new image processor gui.
   */
  public ImageProcessorGuiImpl() {
    // Initialize the frame
    this.frame = new JFrame("Image Processor");
    this.frame.setLayout(new BorderLayout(5, 5));
    this.frame.setPreferredSize(new Dimension(800, 600));
    this.frame.setMinimumSize(new Dimension(800, 600));
    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Add panels to the frame
    this.frame.setJMenuBar(this.menubarPanel);
    this.previewPanel = new PreviewPanel();
    this.previewPanel.addChangeListener(evt -> this.changeHistogram());
    this.frame.add(this.previewPanel, BorderLayout.CENTER);
    this.frame.add(this.messagePanel, BorderLayout.SOUTH);
    this.transformationPanel = new TransformationPanel();
    initSidebar();

    // Display the frame
    this.frame.pack();
    this.frame.setVisible(true);
  }


  @Override
  public void displayImage(String name, BufferedImage image, int[][] histogram) {
    if (name == null || image == null || histogram == null) {
      throw new IllegalArgumentException("Arguments cannot be null");
    }
    this.images.put(name, image);
    this.histograms.put(name, histogram);
    this.previewPanel.addImageTab(name, image);
    this.previewPanel.displayImageTab(name);
    this.histogramPanel.updateHistogram(histogram);
    this.frame.repaint();
  }

  @Override
  public void renderDialog(DialogType type, String message) {
    if (type == null || message == null) {
      throw new IllegalArgumentException("Arguments cannot be null");
    }
    switch (type) {
      case Danger:
        JOptionPane.showMessageDialog(this.frame, message, "Error", JOptionPane.ERROR_MESSAGE);
        break;
      case Warning:
        JOptionPane.showMessageDialog(this.frame, message, "Info", JOptionPane.WARNING_MESSAGE);
        break;
      case Note:
        JOptionPane.showMessageDialog(this.frame, message, "Note", JOptionPane.INFORMATION_MESSAGE);
        break;
      default:
        // should never happen
        throw new IllegalArgumentException("Invalid dialog type");
    }
  }

  @Override
  public String[] renderInput(List<String> questions, String error) {
    if (questions == null) {
      throw new IllegalArgumentException("Questions cannot be null");
    }
    if (error == null) {
      error = "";
    }
    Map<String, JTextField> fields = new HashMap<>();
    for (String question : questions) {
      fields.put(question, new JTextField());
    }
    JPanel panel = new JPanel(new GridLayout(questions.size() * 2, 0));
    for (String question : questions) {
      panel.add(new JLabel(question));
      panel.add(fields.get(question));
    }
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    int result = JOptionPane.showConfirmDialog(null, panel, error, JOptionPane.OK_CANCEL_OPTION);
    if (result == JOptionPane.OK_OPTION) {
      String[] answers = new String[questions.size()];
      int answerIndex = 0;
      for (String question : questions) {
        if (fields.get(question).getText().isEmpty()) {
          answers[answerIndex] = "";
        } else {
          answers[answerIndex] = fields.get(question).getText();
        }
        answerIndex++;
      }
      return answers;
    } else {
      return null;
    }
  }

  @Override
  public String loadFile(FileNameExtensionFilter filter) {
    JFileChooser chooser = new JFileChooser();
    if (filter != null) {
      chooser.setFileFilter(filter);
    }
    int result = chooser.showOpenDialog(this.frame);
    if (result == JFileChooser.APPROVE_OPTION) {
      return chooser.getSelectedFile().getAbsolutePath();
    } else {
      return null;
    }
  }

  @Override
  public String saveFile(FileNameExtensionFilter filter) {
    JFileChooser chooser = new JFileChooser();
    if (filter != null) {
      chooser.setFileFilter(filter);
    }
    int result = chooser.showSaveDialog(this.frame);
    if (result == JFileChooser.APPROVE_OPTION) {
      return chooser.getSelectedFile().getAbsolutePath();
    } else {
      return null;
    }
  }

  @Override
  public void renderMessage(String message) throws IllegalStateException {
    message = message.replace("Command:", "");
    this.messagePanel.updateMessage(message);
  }

  /**
   * Initializes the sidebar with the transformation panel and the histogram panel.
   */
  private void initSidebar() {
    JPanel sidebarPanel = new JPanel(new GridLayout(2, 0));
    this.transformationPanel.setBorder(BorderFactory.createTitledBorder("Transformations"));
    this.histogramPanel.setBorder(BorderFactory.createTitledBorder("Histogram"));
    sidebarPanel.add(this.transformationPanel);
    sidebarPanel.add(this.histogramPanel);
    sidebarPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
    this.frame.add(sidebarPanel, BorderLayout.EAST);
  }

  /**
   * Changes the histogram panel to be the current one.
   */
  private void changeHistogram() {
    this.histogramPanel.updateHistogram(
        this.histograms.get(this.previewPanel.getSelectedImageTab()));
  }

  @Override
  public void setTransformations(List<String> transformations) {
    if (transformations == null) {
      throw new IllegalArgumentException("Transformations cannot be null");
    }
    this.transformationPanel.setTransformations(transformations);
    this.frame.repaint();
  }

  @Override
  public String getCurrentImageName() {
    return this.previewPanel.getSelectedImageTab();
  }

  @Override
  public MenubarPanel getMenubarPanel() {
    return this.menubarPanel;
  }

  @Override
  public TransformationPanel getTransformationPanel() {
    return this.transformationPanel;
  }

  @Override
  public void close() {
    this.frame.dispose();
  }
}
