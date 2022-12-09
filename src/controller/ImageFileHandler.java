package controller;

import model.Image;

/**
 * Represents a model used to convert image files into {@code Image} objects.
 */
public interface ImageFileHandler {

  /**
   * Processes the given file and returns an {@code Image} object.
   *
   * @param path the path to the file to be processed
   * @return the {@code Image} object representing the file
   * @throws IllegalArgumentException if the file cannot be processed
   */
  Image process(String path) throws IllegalArgumentException;

  /**
   * Saves the given {@code Image} object to the given file.
   *
   * @param image the {@code Image} object to be saved
   * @param path  the path to the file to be saved to
   * @throws IllegalArgumentException if the file cannot be saved
   */
  void export(Image image, String path) throws IllegalArgumentException;

}
