package model;

/**
 * Represents a macro that can be applied to an {@link Image} to transform its pixels in some way.
 */
public interface ImageTransformation {

  /**
   * Apply this image transformation to the given image and return the transformed image.
   *
   * @return a new image with the transformation applied to the pixels of the given image
   */
  Image transform(Image image);
}
