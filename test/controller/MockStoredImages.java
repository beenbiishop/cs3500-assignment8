package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import model.Image;
import model.StoredImages;

/**
 * Creates a mock class for stored images, to return all the values being parsed to it.
 */
public class MockStoredImages implements StoredImages {

  final StringBuilder log;
  private final Map<String, Image> storedImages;

  public MockStoredImages(StringBuilder log) {
    this.storedImages = new HashMap<>();
    this.log = Objects.requireNonNull(log);
  }

  /**
   * Add a new image to the collection identified by a given file name. And adds the message to the
   * log.
   *
   * @param fileName the file name of the image to add
   * @param image    the image object to add
   * @param force    if false, check if the file name is unique, if true do not check and overwrite
   *                 the previous image
   * @throws IllegalArgumentException if the given file name is already in the collection and force
   *                                  is set to false
   */
  @Override
  public void add(String fileName, Image image, boolean force) throws IllegalArgumentException {
    if ((this.exists(fileName) && force) || !this.exists(fileName)) {
      this.storedImages.put(fileName, image);
    } else if (this.exists(fileName) && !force) {
      throw new IllegalArgumentException("An image with that file name already exists");
    }
    this.log.append(String.format(
        "The parsed string for a new file's name: " + fileName + System.lineSeparator()));
  }

  /**
   * Removes an image from the collection with the given file name. If the file does not exist, the
   * collection is not modified.
   *
   * @param fileName the file name of the image to remove
   */
  @Override
  public void remove(String fileName) {
    this.storedImages.remove(fileName);
  }

  /**
   * Returns the image with the given file name.
   *
   * @param fileName the file name of the image to retrieve
   * @return the image with the given file name
   * @throws IllegalArgumentException if the file name does not exist in the collection
   */
  @Override
  public Image retrieve(String fileName) throws IllegalArgumentException {
    Image retrieved = this.storedImages.get(fileName);
    this.log.append(String.format(
        "The parsed string for the name of the file to modify: " + fileName
            + System.lineSeparator()));
    if (retrieved == null) {
      throw new IllegalArgumentException(
          "No image with the file name \"" + fileName + "\" has been loaded");
    } else {
      return retrieved.copy();
    }
  }

  /**
   * Returns true if an image with the given file name exists in the collection, false otherwise.
   *
   * @param fileName the file name of the image to check
   * @return true if the image exists in the collection, false otherwise
   */
  @Override
  public boolean exists(String fileName) {
    boolean exists = false;

    for (Map.Entry<String, Image> entry : this.storedImages.entrySet()) {
      if (fileName.equals(entry.getKey())) {
        exists = true;
        break;
      }
    }
    return exists;
  }
}
