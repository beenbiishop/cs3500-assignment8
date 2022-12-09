package model;

import java.awt.Color;


/**
 * Implements the {@link Image} interface.
 *
 * <p>Each image is represented by a 2D array of colors {@code Color[height][width]} that
 * represents its pixels. A pixel's x value is it's column (width) index in the 2D array, and a
 * pixel's y value it it's row (height) index in the 2D array.</p>
 */
public class ImageImpl implements Image {

  private final Color[][] pixels;

  /**
   * Constructs a new {@link ImageImpl} with the given pixels.
   *
   * @param pixels the pixels of this image state as a 2D array {@code Color[height][width]}
   * @throws IllegalArgumentException if there is not at least one color in the given array, or if
   *                                  any pixels are null
   */
  public ImageImpl(Color[][] pixels) throws IllegalArgumentException {
    this.pixels = this.copyPixels(pixels);
  }

  /**
   * Gets the height of this image.
   *
   * @return the height of the image as an integer
   */
  public int getHeight() {
    return this.pixels.length;
  }

  /**
   * Gets the width of this image.
   *
   * @return the width of the image as an integer
   */
  public int getWidth() {
    return this.pixels[0].length;
  }

  /**
   * Gets a copy of this image state's pixels.
   *
   * @return the pixels of the image state as a 2D array of {@link Color}s in the format
   * {@code Color[height][width]}
   */
  public Color[][] getPixels() {
    return this.copyPixels(this.pixels);
  }

  @Override
  public ImageImpl copy() {
    return new ImageImpl(this.copyPixels(this.pixels));
  }

  /**
   * Creates a deep copy of a given pixel 2D array of colors to ensure an image's pixels are not
   * modified by other classes.
   *
   * @param original the original pixel 2D array of colors to copy
   * @return a deep copy of the given pixel 2D array of colors
   * @throws IllegalArgumentException if the original array is null or empty, or if any pixels are
   *                                  null
   */
  private Color[][] copyPixels(Color[][] original) throws IllegalArgumentException {
    if (original == null || original.length == 0 || original[0].length == 0) {
      throw new IllegalArgumentException("The pixels array must contain at least one pixel");
    }
    Color[][] ret = new Color[original.length][original[0].length];
    for (int i = 0; i < original.length; i++) {
      for (int j = 0; j < original[0].length; j++) {
        if (original[i][j] == null) {
          throw new IllegalArgumentException("The pixels array must not contain null pixels");
        } else {
          Color newColor = new Color(original[i][j].getRGB());
          ret[i][j] = newColor;
        }
      }
    }
    return ret;
  }
}


