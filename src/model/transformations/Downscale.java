package model.transformations;

import java.awt.Color;
import model.Image;
import model.ImageImpl;
import model.ImageTransformation;

/**
 * An implementation of the {@link ImageTransformation} interface representing a macro used to
 * downscale an image.
 */
public class Downscale implements ImageTransformation {

  private final int nWidth;
  private final int nHeight;

  /**
   * Constructs a new downscale transformation macro object that transforms an image to be
   * downscaled to the given width and height.
   *
   * @param nWidth  the new width of the image
   * @param nHeight the new height of the image
   */
  public Downscale(int nWidth, int nHeight) {
    if (nWidth <= 0 || nHeight <= 0) {
      throw new IllegalArgumentException("The new width and height must be positive");
    }
    this.nWidth = nWidth;
    this.nHeight = nHeight;
  }

  @Override
  public Image transform(Image image) {
    Color[][] oldPixels = image.getPixels();
    Color[][] newPixels = new Color[this.nHeight][this.nWidth];

    // calculate the ratio of the new width and height to the old width and height
    float yRatio = (image.getHeight() * 1.0f) / this.nHeight;
    float xRatio = (image.getWidth() * 1.0f) / this.nWidth;

    // check if user is attempting to upscale
    if (yRatio < 1) {
      throw new IllegalArgumentException(
          "The image's new width (" + this.nWidth + "px) cannot be larger"
              + " than the original width (" + image.getWidth() + "px).");
    } else if (xRatio < 1) {
      throw new IllegalArgumentException(
          "The image's new height (" + this.nHeight + "px) cannot be larger"
              + " than the original height (" + image.getHeight() + "px).");
    }

    // set the new pixels to the average of the old pixels
    for (int i = 0; i < this.nHeight; i++) {
      for (int j = 0; j < this.nWidth; j++) {
        int x = Math.round(xRatio * j);
        int y = Math.round(yRatio * i);
        newPixels[i][j] = oldPixels[y][x];
      }
    }

    return new ImageImpl(newPixels);
  }
}
