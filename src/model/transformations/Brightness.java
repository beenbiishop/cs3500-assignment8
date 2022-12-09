package model.transformations;

import java.awt.Color;
import model.Image;
import model.ImageImpl;
import model.ImageTransformation;

/**
 * An implementation of the {@link ImageTransformation} interface representing a macro used to
 * adjust the brightness of an image, whether positively or negatively.
 */
public class Brightness implements ImageTransformation {

  private final int amount;

  /**
   * Constructs a new brightness transformation macro object that transforms an image to be brighter
   * by the given amount if positive, or darker by the given amount if negative.
   *
   * @param amount the amount to brighten or darken an image
   * @throws IllegalArgumentException if the given amount is not positive or negative
   */
  public Brightness(int amount) throws IllegalArgumentException {
    if (amount == 0) {
      throw new IllegalArgumentException("The adjustment amount must be non-zero");
    }
    this.amount = amount;
  }

  @Override
  public Image transform(Image image) {
    Color[][] newPixels = new Color[image.getHeight()][image.getWidth()];
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        Color pixel = image.getPixels()[i][j];

        int red = pixel.getRed() + this.amount;
        int blue = pixel.getBlue() + this.amount;
        int green = pixel.getGreen() + this.amount;

        red = Math.max(0, Math.min(red, 255));
        green = Math.max(0, Math.min(green, 255));
        blue = Math.max(0, Math.min(blue, 255));
        newPixels[i][j] = new Color(red, green, blue);
      }
    }
    return new ImageImpl(newPixels);
  }
}
