package controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import model.Image;
import model.ImageImpl;

/**
 * Implements the {@link ImageFileHandler} interface for converting any image file supported by the
 * {@link ImageIO} class image files into {@link Image} objects, and vice versa.
 */
public class ImageIOHandler implements ImageFileHandler {

  /**
   * Checks if the given extension is not supported by the {@link ImageIO} class.
   *
   * @param extension the extension to check
   * @return false if the extension is supported, true otherwise
   */
  private static boolean notSupported(String extension) {
    for (String s : ImageIO.getReaderFormatNames()) {
      if (s.equalsIgnoreCase(extension)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Gets the extension of the given file path.
   *
   * @param path a file path to get the extension of
   * @return the extension of the given file path as a string
   */
  private static String getExtension(String path) {
    int i = path.lastIndexOf('.');
    if (i > 0) {
      return path.substring(i + 1).toLowerCase();
    }
    return "";
  }

  @Override
  public Image process(String path) throws IllegalArgumentException {
    BufferedImage processedImage;

    try {
      FileInputStream file = new FileInputStream(path);
      if (notSupported(getExtension(path))) {
        throw new IllegalArgumentException(
            "\"" + getExtension(path) + "\" is not a supported image format");
      }
      processedImage = ImageIO.read(file);
      file.close();
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File \"" + path + "\" not found");
    } catch (IOException e) {
      throw new IllegalArgumentException(e.getMessage());
    }

    int width = processedImage.getWidth();
    int height = processedImage.getHeight();

    Color[][] pixels = new Color[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int rgb = processedImage.getRGB(j, i);
        pixels[i][j] = new Color(rgb);
      }
    }

    return new ImageImpl(pixels);
  }

  @Override
  public void export(Image image, String path) throws IllegalArgumentException {
    BufferedImage outputImage;

    if (image == null || path == null) {
      throw new IllegalArgumentException("Image and path cannot be null");
    }

    int width = image.getWidth();
    int height = image.getHeight();
    Color[][] pixels = image.getPixels();
    String extension = getExtension(path);

    if (notSupported(extension)) {
      throw new IllegalArgumentException("\"" + extension + "\" is not a supported image format");
    }

    outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        outputImage.setRGB(j, i, pixels[i][j].getRGB());
      }
    }

    try {
      FileOutputStream file = new FileOutputStream(path);
      ImageIO.write(outputImage, extension, file);
      file.close();
    } catch (IOException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

}
