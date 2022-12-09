package model;

/**
 * Represents a collection of {@link Image}s that have been loaded into the program by the user,
 * identified by the image's file name selected by the user.
 */
public interface StoredImages {

  /**
   * Add a new image to the collection identified by a given file name.
   *
   * @param fileName the file name of the image to add
   * @param image    the image object to add
   * @param force    if false, check if the file name is unique, if true do not check and overwrite
   *                 the previous image
   * @throws IllegalArgumentException if the given file name is already in the collection and force
   *                                  is set to false
   */
  void add(String fileName, Image image, boolean force) throws IllegalArgumentException;

  /**
   * Removes an image from the collection with the given file name. If the file does not exist, the
   * collection is not modified.
   *
   * @param fileName the file name of the image to remove
   */
  void remove(String fileName);

  /**
   * Returns the image with the given file name.
   *
   * @param fileName the file name of the image to retrieve
   * @return the image with the given file name
   * @throws IllegalArgumentException if the file name does not exist in the collection
   */
  Image retrieve(String fileName) throws IllegalArgumentException;

  /**
   * Returns true if an image with the given file name exists in the collection, false otherwise.
   *
   * @param fileName the file name of the image to check
   * @return true if the image exists in the collection, false otherwise
   */
  boolean exists(String fileName);
}
