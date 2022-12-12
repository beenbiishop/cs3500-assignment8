package controller.commands;

import controller.ImageProcessorCmd;
import model.Image;
import model.ImageTransformation;
import model.StoredImages;
import model.transformations.Downscale;
import view.ImageProcessorView;

/**
 * Class that represents a command, "Downscale", that the processor can handle. Implements the
 * {@code ImageProcessorCmd} interface and execute the command. Downscale the image to the given
 * width and height.
 */
public class DownscaleCmd implements ImageProcessorCmd {

  private final ImageProcessorView view;
  private final StoredImages store;
  private final int nWidth;
  private final int nHeight;
  private final String fileName;
  private final String newFileName;

  /**
   * Constructs a Downscale command.
   *
   * @param view        the view to display the messages to.
   * @param store       the store to store images in.
   * @param nWidth      the new width of the image.
   * @param nHeight     the new height of the image.
   * @param fileName    the file name of the image to be transformed.
   * @param newFileName the file name of the new transformed image.
   * @throws IllegalArgumentException if any of the parameters are null.
   */
  public DownscaleCmd(ImageProcessorView view, StoredImages store, int nWidth, int nHeight,
      String fileName, String newFileName) throws IllegalArgumentException {
    if (view == null || store == null || fileName == null || newFileName == null) {
      throw new IllegalArgumentException("View, store, and file names cannot be null");
    }
    if (nWidth <= 0 || nHeight <= 0) {
      throw new IllegalArgumentException("The new width and height must be positive");
    }
    this.view = view;
    this.store = store;
    this.nWidth = nWidth;
    this.nHeight = nHeight;
    this.fileName = fileName.toLowerCase();
    this.newFileName = newFileName.toLowerCase();
  }

  @Override
  public void execute() throws IllegalArgumentException {
    Image retrieved = this.store.retrieve(this.fileName);
    ImageTransformation downscale = new Downscale(this.nWidth, this.nHeight);
    Image processed = downscale.transform(retrieved);
    this.store.add(this.newFileName, processed, true);
    this.view.renderMessage(
        "The image \"" + this.fileName + "\" has been downscaled to " + this.nWidth + "x"
            + this.nHeight + "px" + System.lineSeparator() + "Command: ");
  }
}
