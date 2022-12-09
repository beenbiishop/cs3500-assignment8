package model;

import java.awt.Color;

/**
 * Represents an image and it's pixels.
 */
public interface Image {

  /**
   * Gets the height of the current state of the image.
   *
   * @return the height of the image as an integer
   */
  int getHeight();

  /**
   * Gets the width of the current state of the image.
   *
   * @return the width of the image as an integer
   */
  int getWidth();

  /**
   * Returns a 2D array of colors {@code Color[height][width]} that represents an image's pixels.
   *
   * @return a copy of the 2D array of pixels for the image
   */
  Color[][] getPixels();

  /**
   * Returns a copy of this image object.
   *
   * @return a copy of this image
   */
  Image copy();


}
