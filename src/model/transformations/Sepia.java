package model.transformations;

import java.awt.Color;
import model.Image;
import model.ImageImpl;
import model.ImageTransformation;

/**
 * An implementation of the {@link ImageTransformation} interface representing a macro used to
 * filter an image to sepia.
 */
public class Sepia implements ImageTransformation {

  private static final double[][] SEPIA_FILTER = {{0.393, 0.769, 0.189}, {0.349, 0.686, 0.168},
      {0.272, 0.534, 0.131}};

  @Override
  public Image transform(Image image) {
    int height = image.getHeight();
    int width = image.getWidth();
    Color[][] oldPixels = image.getPixels();
    Color[][] newPixels = new Color[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Color oldPixel = oldPixels[i][j];
        newPixels[i][j] = sepiaPixel(oldPixel);
      }
    }
    return new ImageImpl(newPixels);
  }

  /**
   * Returns the color of the pixel at the given row and column in the given image after applying
   * the sepia filter to it.
   *
   * @param pixel the color of the original pixel
   * @return the new color of the pixel
   */
  private Color sepiaPixel(Color pixel) {
    int red = pixel.getRed();
    int green = pixel.getGreen();
    int blue = pixel.getBlue();

    // Initializes variables to store the new RGB values
    double redVal = 0;
    double greenVal = 0;
    double blueVal = 0;

    // Multiplies each RGB value by the corresponding value in the filter to calculate the filtered
    // RGB values
    redVal = red * SEPIA_FILTER[0][0] + green * SEPIA_FILTER[0][1] + blue * SEPIA_FILTER[0][2];
    greenVal = red * SEPIA_FILTER[1][0] + green * SEPIA_FILTER[1][1] + blue * SEPIA_FILTER[1][2];
    blueVal = red * SEPIA_FILTER[2][0] + green * SEPIA_FILTER[2][1] + blue * SEPIA_FILTER[2][2];

    // Rounds the RGB values to the nearest integer
    red = Math.toIntExact(Math.round(redVal));
    green = Math.toIntExact(Math.round(greenVal));
    blue = Math.toIntExact(Math.round(blueVal));

    // Clamps the RGB values to be between 0 and 255
    red = Math.max(0, Math.min(red, 255));
    green = Math.max(0, Math.min(green, 255));
    blue = Math.max(0, Math.min(blue, 255));

    // Returns the new sepia filtered pixel
    return new Color(red, green, blue);
  }
}
