package view.panels;

import controller.ImageProcessorGuiController;
import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import view.ImageProcessorGui;

/**
 * Represents the transformation panel that is a part of the {@link ImageProcessorGui} view.
 */
public class TransformationPanel extends JPanel {

  private final JList<String> list = new JList<>();
  private final JButton applyButton = new JButton("Apply Transformation");
  private ImageProcessorGuiController controller;

  /**
   * Constructs a new transformation panel.
   */
  public TransformationPanel() {
    super(new BorderLayout());
    this.add(new JScrollPane(this.list), BorderLayout.CENTER);
    this.add(this.applyButton, BorderLayout.SOUTH);
  }

  /**
   * Sets the controller for this panel.
   *
   * @param controller the controller for this panel
   */
  public void addFeatures(ImageProcessorGuiController controller) {
    if (controller == null) {
      throw new IllegalArgumentException("Controller cannot be null");
    }
    this.controller = controller;
    this.applyButton.addActionListener(
        evt -> this.controller.transformImage(this.getSelectedTransformation()));
  }

  /**
   * Sets the transformations that can be applied to the image.
   *
   * @param transformations the transformations that can be applied to the image
   */
  public void setTransformations(List<String> transformations) {
    String[] validated = validateTransformations(transformations);
    Arrays.sort(validated);
    this.list.setListData(validated);
    this.list.repaint();
    this.repaint();
  }

  /**
   * Returns the selected transformation.
   *
   * @return the selected transformation
   */
  private String getSelectedTransformation() {
    return this.list.getSelectedValue();
  }

  /**
   * Takes a list of transformations and validates them by removing duplicate and null values.
   *
   * @param transformations the transformations to be validated
   * @return the validated transformations as an array
   */
  private String[] validateTransformations(List<String> transformations) {
    Set<String> set = new HashSet<>();
    if (transformations == null) {
      throw new IllegalArgumentException("Transformations cannot be null");
    }
    for (String transformation : transformations) {
      if (transformation != null && transformation.length() > 0) {
        set.add(transformation);
      }
    }
    return set.toArray(new String[0]);
  }

}
