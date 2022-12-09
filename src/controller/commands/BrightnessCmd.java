package controller.commands;

import controller.ImageProcessorCmd;
import model.Image;
import model.ImageTransformation;
import model.StoredImages;
import model.transformations.Brightness;
import view.ImageProcessorView;

/**
 * Class that represents a command, "Brightness", that the processor can handle. Implements the
 * {@code ImageProcessorCmd} interface and execute the command. Brightens the image by the given
 * amount.
 */
public class BrightnessCmd implements ImageProcessorCmd {

  private final ImageProcessorView view;
  private final StoredImages store;
  private final int amount;
  private final String fileName;
  private final String newFileName;

  /**
   * Constructs a Brightness command.
   *
   * @param view        the view to display the messages to.
   * @param store       the store to store images in.
   * @param amount      how much to brighten or darken the image by.
   * @param fileName    the file name of the image to be transformed.
   * @param newFileName the file name of the new transformed image.
   * @throws IllegalArgumentException if any of the parameters are null.
   */
  public BrightnessCmd(ImageProcessorView view, StoredImages store, int amount, String fileName,
      String newFileName) throws IllegalArgumentException {
    if (view == null || store == null || fileName == null || newFileName == null) {
      throw new IllegalArgumentException("View, store, and file names cannot be null");
    }
    if (amount == 0) {
      throw new IllegalArgumentException("Amount cannot be 0");
    }
    this.view = view;
    this.store = store;
    this.amount = amount;
    this.fileName = fileName.toLowerCase();
    this.newFileName = newFileName.toLowerCase();
  }


  @Override
  public void execute() {
    Image retrieved = this.store.retrieve(this.fileName);
    ImageTransformation brightness = new Brightness(this.amount);
    Image processed = brightness.transform(retrieved);
    this.store.add(this.newFileName, processed, true);
    this.view.renderMessage(
        "The brightness of \"" + this.fileName + "\" has been adjusted by " + this.amount
            + System.lineSeparator() + "Command: ");
  }
}
