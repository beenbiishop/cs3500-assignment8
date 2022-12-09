package view.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import view.ImageProcessorGui;

/**
 * Represents the histogram panel that is a part of the {@link ImageProcessorGui} view.
 */
public class HistogramPanel extends JPanel {

  private int[][] histogram;

  /**
   * Constructs a new histogram panel.
   */
  public HistogramPanel() {
    super();
    Dimension size = new Dimension(276, 150);
    this.setPreferredSize(size);
    this.setMinimumSize(size);
    this.setMaximumSize(size);
  }

  /**
   * Updates the histogram displayed in the histogram panel.
   *
   * @param histogram the histogram to be displayed
   */
  public void updateHistogram(int[][] histogram) {
    if (histogram == null) {
      throw new IllegalArgumentException("Histogram cannot be null");
    }
    this.histogram = histogram;
    this.repaint();
  }

  @Override
  protected void paintComponent(java.awt.Graphics g) {
    super.paintComponent(g);
    drawGraph(g);
  }

  /**
   * Draws the histogram graph.
   *
   * @param g the graphics object to draw on
   */
  private void drawGraph(final Graphics g) {
    if (this.histogram == null) {
      return;
    }
    int height = this.getHeight() - 12;
    int max = 0;
    for (int i = 0; i < this.histogram.length; i++) {
      for (int j = 0; j < this.histogram[i].length; j++) {
        if (this.histogram[i][j] > max) {
          max = this.histogram[i][j];
        }
      }
    }
    for (int i = 0; i < 256; i++) {
      g.setColor(new Color(255, 0, 0, 40));
      g.drawLine(i + 10, height, i + 10,
          Math.max(12, height - (int) ((double) this.histogram[0][i] / max * height)));
      g.setColor(new Color(0, 255, 0, 40));
      g.drawLine(i + 10, height, i + 10,
          Math.max(12, height - (int) ((double) this.histogram[1][i] / max * height)));
      g.setColor(new Color(0, 0, 255, 40));
      g.drawLine(i + 10, height, i + 10,
          Math.max(12, height - (int) ((double) this.histogram[2][i] / max * height)));
      g.setColor(new Color(0, 0, 0, 40));
      g.drawLine(i + 10, height, i + 10,
          Math.max(12, height - (int) ((double) this.histogram[3][i] / max * height)));
    }
  }
}
