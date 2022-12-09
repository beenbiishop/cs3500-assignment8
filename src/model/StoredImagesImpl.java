package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Implements the {@link StoredImages} interface.
 */

public class StoredImagesImpl implements StoredImages {

  private final Map<String, Image> storedImages;

  /**
   * Constructs a new empty image storage object.
   */
  public StoredImagesImpl() {
    this.storedImages = new HashMap<>();
  }

  @Override
  public void add(String fileName, Image image, boolean force) {
    if ((this.exists(fileName) && force) || !this.exists(fileName)) {
      this.storedImages.put(fileName.toLowerCase(), image);
    } else if (this.exists(fileName) && !force) {
      throw new IllegalArgumentException("An image with that file name already exists");
    }
  }

  @Override
  public void remove(String fileName) {
    this.storedImages.remove(fileName);
  }

  @Override
  public boolean exists(String fileName) {
    boolean exists = false;

    for (Map.Entry<String, Image> entry : this.storedImages.entrySet()) {
      if (fileName.equalsIgnoreCase(entry.getKey())) {
        exists = true;
        break;
      }
    }
    return exists;
  }

  @Override
  public Image retrieve(String fileName) throws IllegalArgumentException {
    Image retrieved = this.storedImages.get(fileName.toLowerCase());
    if (retrieved == null) {
      throw new IllegalArgumentException(
          "No image with the file name \"" + fileName + "\" has been loaded");
    } else {
      return retrieved.copy();
    }
  }

}
