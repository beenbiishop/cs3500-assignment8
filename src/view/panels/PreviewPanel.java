package view.panels;

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import view.ImageProcessorGui;

/**
 * Represents the image preview panel that is a part of the {@link ImageProcessorGui} view.
 */
public class PreviewPanel extends JTabbedPane {

  /**
   * Constructs a new image preview panel.
   */
  public PreviewPanel() {
    super();
  }

  /**
   * Displays the given image in the preview panel.
   *
   * @param title the title to display on the tab
   * @param image the image to be displayed
   */
  public void addImageTab(String title, BufferedImage image) {
    if (title == null || title.length() == 0) {
      throw new IllegalArgumentException("Title cannot be null or empty");
    }
    if (image == null) {
      throw new IllegalArgumentException("Image cannot be null");
    }
    JLabel label = new JLabel();
    label.setVerticalAlignment(JLabel.TOP);
    label.setHorizontalAlignment(JLabel.LEFT);
    label.setIcon(new ImageIcon(image));
    JScrollPane scrollPane = new JScrollPane(label);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
    if (this.indexOfTab(title) == -1) {
      this.addTab(title, scrollPane);
    } else {
      this.setComponentAt(this.indexOfTab(title), scrollPane);
    }
    this.repaint();
  }

  /**
   * Selects the image tab with the given title.
   *
   * @param title the title of the tab to select
   */
  public void displayImageTab(String title) {
    if (title == null || title.length() == 0) {
      throw new IllegalArgumentException("Title cannot be null or empty");
    }
    this.setSelectedIndex(this.indexOfTab(title));
  }

  /**
   * Returns the title of the currently selected image tab.
   *
   * @return the name of the currently selected image tab
   */
  public String getSelectedImageTab() {
    try {
      return this.getTitleAt(this.getSelectedIndex());
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }
}
