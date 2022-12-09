package model.transformations;

import java.awt.Color;
import model.Image;
import model.ImageImpl;
import model.ImageTransformation;

/**
 * An implementation of the {@link ImageTransformation} interface representing a macro used to
 * sharpen an image.
 */
public class Sharpen implements ImageTransformation {

  private static final double[][] SHARPEN_KERNEL = {
      {-1.0 / 8.0, -1.0 / 8.0, -1.0 / 8.0, -1.0 / 8.0, -1.0 / 8.0},
      {-1.0 / 8.0, 1.0 / 4.0, 1.0 / 4.0, 1.0 / 4.0, -1.0 / 8.0},
      {-1.0 / 8.0, 1.0 / 4.0, 1.0, 1.0 / 4.0, -1.0 / 8.0},
      {-1.0 / 8.0, 1.0 / 4.0, 1.0 / 4.0, 1.0 / 4.0, -1.0 / 8.0},
      {-1.0 / 8.0, -1.0 / 8.0, -1.0 / 8.0, -1.0 / 8.0, -1.0 / 8.0}};

  @Override
  public Image transform(Image image) {
    int height = image.getHeight();
    int width = image.getWidth();
    Color[][] oldPixels = image.getPixels();
    Color[][] newPixels = new Color[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        newPixels[i][j] = sharpenPixel(oldPixels, i, j);
      }
    }
    return new ImageImpl(newPixels);
  }

  /**
   * Returns the color of the pixel at the given row and column in the given image after applying
   * the sharpen kernel to it.
   *
   * @param allPixels the pixels of the image
   * @param i         the row of the pixel
   * @param j         the column of the pixel
   * @return the new color of the pixel
   */
  private Color sharpenPixel(Color[][] allPixels, int i, int j) {
    int height = allPixels.length;
    int width = allPixels[0].length;
    Color[][] group = new Color[5][5];

    // Creates a 5x5 array of pixels that surround the given pixel
    for (int k = 0; k < 5; k++) {
      for (int l = 0; l < 5; l++) {
        group[k][l] = allPixels[Math.max(0, Math.min(height - 1, i + k - 5 / 2))][Math.max(0,
            Math.min(width - 1, j + l - 5 / 2))];
      }
    }

    double redVal = 0;
    double greenVal = 0;
    double blueVal = 0;

    // Loops through the 5x5 array of pixels and multiplies each pixel's RGB values by the
    // corresponding value in the sharpen kernel
    for (int k = 0; k < 5; k++) {
      for (int l = 0; l < 5; l++) {
        Color groupPixel = group[k][l];
        redVal += groupPixel.getRed() * (SHARPEN_KERNEL[k][l]);
        greenVal += groupPixel.getGreen() * (SHARPEN_KERNEL[k][l]);
        blueVal += groupPixel.getBlue() * (SHARPEN_KERNEL[k][l]);
      }
    }

    int red = 0;
    int green = 0;
    int blue = 0;

    // Rounds the RGB values to the nearest integer
    red = Math.toIntExact(Math.round(redVal));
    green = Math.toIntExact(Math.round(greenVal));
    blue = Math.toIntExact(Math.round(blueVal));

    // Clamps the RGB values to be between 0 and 255
    red = Math.max(0, Math.min(red, 255));
    green = Math.max(0, Math.min(green, 255));
    blue = Math.max(0, Math.min(blue, 255));

    // Returns the new sharpened pixel
    return new Color(red, green, blue);
  }
}
