package model.transformations;

import java.awt.Color;
import model.Image;
import model.ImageImpl;
import model.ImageTransformation;

/**
 * An implementation of the {@link ImageTransformation} interface representing a macro used to
 * combine two images together using a mask image.
 *
 * <p>
 * When constructing the transformation macro, both the background image and the mask image are
 * provided. The mask image must be the same size as the background image, and when the
 * transformation is applied, any black pixels in the mask image will be replaced with the
 * corresponding pixel in the provided foreground image, and any non-black pixels in the mask image
 * will be replaced the corresponding pixel in the background image.
 * </p>
 */
public class Mask implements ImageTransformation {

  private final Image backgroundImage;
  private final Image maskImage;

  /**
   * Constructs a new mask transformation macro object that combines the given images using the
   * given mask image.
   *
   * @param backgroundImage the image to be used as the background
   * @param maskImage       the mask image to determine which pixels to use from the foreground and
   *                        background images
   * @throws IllegalArgumentException if the background and mask are a different size
   */
  public Mask(Image backgroundImage, Image maskImage) {
    if (backgroundImage == null || maskImage == null) {
      throw new IllegalArgumentException("The images cannot be null");
    }
    if (!dimensionsMatch(backgroundImage, maskImage)) {
      throw new IllegalArgumentException("The background image and mask must be the same size");
    }
    this.backgroundImage = backgroundImage;
    this.maskImage = maskImage;
  }

  @Override
  public Image transform(Image image) {
    // check that the image is the same size as the mask
    dimensionsMatch(this.maskImage, image);

    // get the background, foreground, and mask pixels
    Color[][] backgroundPixels = this.backgroundImage.getPixels();
    Color[][] foregroundPixels = image.getPixels();
    Color[][] maskPixels = this.maskImage.getPixels();
    Color[][] newPixels = new Color[image.getHeight()][image.getWidth()];

    // apply the mask to the provided image
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        // if the mask pixel is black, use the foreground pixel
        if (maskPixels[i][j].equals(Color.BLACK)) {
          newPixels[i][j] = foregroundPixels[i][j];
        } else {
          // otherwise, use the background pixel
          newPixels[i][j] = backgroundPixels[i][j];
        }
      }
    }

    return new ImageImpl(newPixels);
  }

  /**
   * Checks if two given images are the same size.
   *
   * @param image1 the first image to check
   * @param image2 the second image to check
   * @return true if the images are the same size, false if not
   */
  private boolean dimensionsMatch(Image image1, Image image2) {
    return image1.getWidth() == image2.getWidth() && image1.getHeight() == image2.getHeight();
  }


}
