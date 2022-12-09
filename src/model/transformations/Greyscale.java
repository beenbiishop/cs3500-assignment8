package model.transformations;

import java.awt.Color;
import model.Image;
import model.ImageImpl;
import model.ImageTransformation;

/**
 * An implementation of the {@link ImageTransformation} interface representing a macro used to
 * filter an image to greyscale.
 */
public class Greyscale implements ImageTransformation {

  private static final double[][] GREYSCALE_FILTER = {{0.2126, 0.7152, 0.0722},
      {0.2126, 0.7152, 0.0722}, {0.2126, 0.7152, 0.0722}};

  @Override
  public Image transform(Image image) {
    int height = image.getHeight();
    int width = image.getWidth();
    Color[][] oldPixels = image.getPixels();
    Color[][] newPixels = new Color[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Color oldPixel = oldPixels[i][j];
        newPixels[i][j] = greyscalePixel(oldPixel);
      }
    }
    return new ImageImpl(newPixels);
  }

  /**
   * Returns the color of the pixel at the given row and column in the given image after applying
   * the greyscale filter to it.
   *
   * @param pixel the color of the original pixel
   * @return the new color of the pixel
   */
  private Color greyscalePixel(Color pixel) {
    int red = pixel.getRed();
    int green = pixel.getGreen();
    int blue = pixel.getBlue();

    // Initializes variables to store the new RGB values
    double redVal = 0;
    double greenVal = 0;
    double blueVal = 0;

    // Multiplies each RGB value by the corresponding value in the filter to calculate the filtered
    // RGB values
    redVal = red * GREYSCALE_FILTER[0][0] + green * GREYSCALE_FILTER[0][1]
        + blue * GREYSCALE_FILTER[0][2];
    greenVal = red * GREYSCALE_FILTER[1][0] + green * GREYSCALE_FILTER[1][1]
        + blue * GREYSCALE_FILTER[1][2];
    blueVal = red * GREYSCALE_FILTER[2][0] + green * GREYSCALE_FILTER[2][1]
        + blue * GREYSCALE_FILTER[2][2];

    // Rounds the RGB values to the nearest integer
    red = Math.toIntExact(Math.round(redVal));
    green = Math.toIntExact(Math.round(greenVal));
    blue = Math.toIntExact(Math.round(blueVal));

    // Clamps the RGB values to be between 0 and 255
    red = Math.max(0, Math.min(red, 255));
    green = Math.max(0, Math.min(green, 255));
    blue = Math.max(0, Math.min(blue, 255));

    // Returns the new greyscale filtered pixel
    return new Color(red, green, blue);
  }
}
