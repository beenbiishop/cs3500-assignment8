package model.transformations;

import java.awt.Color;
import model.Image;
import model.ImageImpl;
import model.ImageTransformation;

/**
 * An implementation of the {@link ImageTransformation} interface representing a macro used to blur
 * an image.
 */
public class Blur implements ImageTransformation {

  private static final double[][] BLUR_KERNEL = {{1.0 / 16.0, 1.0 / 8.0, 1.0 / 16.0},
      {1.0 / 8.0, 1.0 / 4.0, 1.0 / 8.0}, {1.0 / 16.0, 1.0 / 8.0, 1.0 / 16.0}};

  @Override
  public Image transform(Image image) {
    int height = image.getHeight();
    int width = image.getWidth();
    Color[][] oldPixels = image.getPixels();
    Color[][] newPixels = new Color[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        newPixels[i][j] = blurPixel(oldPixels, i, j);
      }
    }
    return new ImageImpl(newPixels);
  }

  /**
   * Returns the color of the pixel at the given row and column in the given image after applying
   * the blur kernel to it.
   *
   * @param allPixels the pixels of the image
   * @param i         the row of the pixel
   * @param j         the column of the pixel
   * @return the new color of the pixel
   */
  private Color blurPixel(Color[][] allPixels, int i, int j) {
    int height = allPixels.length;
    int width = allPixels[0].length;
    Color[][] group = new Color[3][3];

    // Creates a 3x3 array of pixels that surround the given pixel
    for (int k = 0; k < 3; k++) {
      for (int l = 0; l < 3; l++) {
        group[k][l] = allPixels[Math.max(0, Math.min(height - 1, i + k - 3 / 2))][Math.max(0,
            Math.min(width - 1, j + l - 3 / 2))];
      }
    }

    double redVal = 0;
    double greenVal = 0;
    double blueVal = 0;

    // Loops through the 3x3 array of pixels and multiplies each pixel's RGB values by the
    // corresponding value in the blur kernel
    for (int k = 0; k < 3; k++) {
      for (int l = 0; l < 3; l++) {
        Color groupPixel = group[k][l];
        redVal += groupPixel.getRed() * BLUR_KERNEL[k][l];
        greenVal += groupPixel.getGreen() * BLUR_KERNEL[k][l];
        blueVal += groupPixel.getBlue() * BLUR_KERNEL[k][l];
      }
    }

    // Rounds the RGB values to the nearest integer
    int red = Math.toIntExact(Math.round(redVal));
    int green = Math.toIntExact(Math.round(greenVal));
    int blue = Math.toIntExact(Math.round(blueVal));

    // Clamps the RGB values to be between 0 and 255
    red = Math.max(0, Math.min(red, 255));
    green = Math.max(0, Math.min(green, 255));
    blue = Math.max(0, Math.min(blue, 255));

    // Returns the new blurred pixel
    return new Color(red, green, blue);
  }
}
