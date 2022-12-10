package controller.commands;

import controller.ImageProcessorCmd;
import model.Image;
import model.ImageTransformation;
import model.StoredImages;
import model.transformations.Mosaic;
import view.ImageProcessorView;

/**
 * Class that represents a command, "Mosaic", that the processor can handle. Implements the
 * {@code ImageProcessorCmd} interface and execute the command. Applies a mosaic filter with the
 * given number of seeds.
 */
public class MosaicCmd implements ImageProcessorCmd {

  private final ImageProcessorView view;
  private final StoredImages store;
  private final int numSeeds;
  private final String fileName;
  private final String newFileName;

  /**
   * Constructs a Mosaic command.
   *
   * @param view        the view to display the messages to.
   * @param store       the store to store images in.
   * @param numSeeds    the number of seeds to use.
   * @param fileName    the file name of the image to be transformed.
   * @param newFileName the file name of the new transformed image.
   * @throws IllegalArgumentException if any of the parameters are null.
   */
  public MosaicCmd(ImageProcessorView view, StoredImages store, int numSeeds, String fileName,
      String newFileName) throws IllegalArgumentException {
    if (view == null || store == null || fileName == null || newFileName == null) {
      throw new IllegalArgumentException("View, store, and file names cannot be null");
    }
    if (numSeeds < 1) {
      throw new IllegalArgumentException("Number of seeds must be greater than 0");
    }
    this.view = view;
    this.store = store;
    this.numSeeds = numSeeds;
    this.fileName = fileName.toLowerCase();
    this.newFileName = newFileName.toLowerCase();
  }


  @Override
  public void execute() {
    Image retrieved = this.store.retrieve(this.fileName);
    ImageTransformation mosaic = new Mosaic(this.numSeeds);
    Image processed = mosaic.transform(retrieved);
    this.store.add(this.newFileName, processed, true);
    this.view.renderMessage(
        "A mosaic filter with " + this.numSeeds + " seeds has been applied to \"" + this.fileName
            + "\"" + System.lineSeparator() + "Command: ");
  }
}
