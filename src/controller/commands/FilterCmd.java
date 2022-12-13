package controller.commands;

import controller.ImageProcessorCmd;
import model.Image;
import model.ImageTransformation;
import model.StoredImages;
import model.transformations.Blur;
import model.transformations.Greyscale;
import model.transformations.Mask;
import model.transformations.Sepia;
import model.transformations.Sharpen;
import view.ImageProcessorView;

/**
 * Class that represents a command, "Filter", that the processor can handle. Implements the
 * {@code ImageProcessorCmd} interface and execute the command. Filters an image using the given
 * filter (blur, sharpen, greyscale, sepia).
 */
public class FilterCmd implements ImageProcessorCmd {

  private final ImageProcessorView view;
  private final StoredImages store;
  private final FilterType type;
  private final String fileName;
  private final String newFileName;
  private String maskFileName = null;

  /**
   * Constructs a Filter command.
   *
   * @param view        the view to display the messages to.
   * @param store       the store to store images in.
   * @param type        the enum that represents which filter to apply.
   * @param fileName    the file name of the image to be transformed.
   * @param newFileName the file name of the new transformed image.
   * @throws IllegalArgumentException if any of the parameters are null.
   */
  public FilterCmd(ImageProcessorView view, StoredImages store, FilterType type, String fileName,
      String newFileName) throws IllegalArgumentException {
    if (view == null || store == null || type == null) {
      throw new IllegalArgumentException("View, store, and type cannot be null");
    }
    if (fileName == null || fileName.isEmpty() || newFileName == null || newFileName.isEmpty()) {
      throw new IllegalArgumentException("File name cannot be empty");
    }
    this.view = view;
    this.store = store;
    this.type = type;
    this.fileName = fileName.toLowerCase();
    this.newFileName = newFileName.toLowerCase();
  }

  /**
   * Constructs a Filter command that supports a mask.
   *
   * @param view        the view to display the messages to.
   * @param store       the store to store images in.
   * @param type        the enum that represents which filter to apply.
   * @param fileName    the file name of the image to be transformed.
   * @param newFileName the file name of the new transformed image.
   * @throws IllegalArgumentException if any of the parameters are null.
   */
  public FilterCmd(ImageProcessorView view, StoredImages store, FilterType type, String fileName,
      String maskFileName, String newFileName) {
    this(view, store, type, fileName, newFileName);
    if (maskFileName == null) {
      throw new IllegalArgumentException("Mask file name cannot be null");
    }
    this.maskFileName = maskFileName.toLowerCase();
  }


  @Override
  public void execute() throws IllegalArgumentException {
    Image retrieved = this.store.retrieve(this.fileName);
    ImageTransformation filter;
    switch (this.type) {
      case Blur:
        filter = new Blur();
        break;
      case Sharpen:
        filter = new Sharpen();
        break;
      case Greyscale:
        filter = new Greyscale();
        break;
      case Sepia:
        filter = new Sepia();
        break;
      default:
        // should never happen
        throw new IllegalArgumentException("Invalid filter type");
    }
    Image filtered = filter.transform(retrieved);
    if (this.maskFileName == null) {
      this.store.add(this.newFileName, filtered, true);
      this.view.renderMessage(
          "Applied the " + this.type.toString().toLowerCase() + " filter to \"" + this.fileName
              + "\"" + System.lineSeparator() + "Command: ");
    } else {
      Image maskImage = this.store.retrieve(this.maskFileName);
      ImageTransformation mask = new Mask(retrieved, maskImage);
      Image masked = mask.transform(filtered);
      this.store.add(this.newFileName, masked, true);
      this.view.renderMessage(
          "Applied the " + this.type.toString().toLowerCase() + " filter to \"" + this.fileName
              + "\" in the area masked by \"" + this.maskFileName + "\"" + System.lineSeparator()
              + "Command: ");
    }
  }

  /**
   * Enum that represents available filters.
   */
  public enum FilterType {
    Blur, Sharpen, Greyscale, Sepia
  }
}
