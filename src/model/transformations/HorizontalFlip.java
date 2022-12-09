package model.transformations;

import java.awt.Color;
import model.Image;
import model.ImageImpl;
import model.ImageTransformation;

/**
 * An implementation of the {@link ImageTransformation} interface representing a macro used to flip
 * the pixels of an image horizontally.
 */
public class HorizontalFlip implements ImageTransformation {

  @Override
  public Image transform(Image image) {
    int height = image.getHeight();
    int width = image.getWidth();
    Color[][] newPixels = new Color[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Color pixel = image.getPixels()[i][width - j - 1];
        newPixels[i][j] = pixel;
      }
    }
    return new ImageImpl(newPixels);
  }
}
