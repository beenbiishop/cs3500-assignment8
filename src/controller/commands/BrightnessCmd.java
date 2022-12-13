package controller.commands;

import controller.ImageProcessorCmd;
import model.Image;
import model.ImageTransformation;
import model.StoredImages;
import model.transformations.Brightness;
import model.transformations.Mask;
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
  private String maskFileName = null;

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

  /**
   * Constructs a Brightness command that supports a mask.
   *
   * @param view         the view to display the messages to.
   * @param store        the store to store images in.
   * @param amount       how much to brighten or darken the image by.
   * @param fileName     the file name of the image to be transformed.
   * @param maskFileName the file name of the mask image.
   * @param newFileName  the file name of the new transformed image.
   */
  public BrightnessCmd(ImageProcessorView view, StoredImages store, int amount, String fileName,
      String maskFileName, String newFileName) {
    this(view, store, amount, fileName, newFileName);
    if (maskFileName == null) {
      throw new IllegalArgumentException("Mask file name cannot be null");
    }
    this.maskFileName = maskFileName.toLowerCase();
  }


  @Override
  public void execute() {
    Image retrieved = this.store.retrieve(this.fileName);
    ImageTransformation brightness = new Brightness(this.amount);
    Image brightened = brightness.transform(retrieved);
    if (this.maskFileName == null) {
      this.store.add(this.newFileName, brightened, true);
      this.view.renderMessage(
          "The brightness of \"" + this.fileName + "\" has been adjusted by " + this.amount
              + System.lineSeparator() + "Command: ");
    } else {
      Image maskImage = this.store.retrieve(this.maskFileName);
      ImageTransformation mask = new Mask(retrieved, maskImage);
      Image masked = mask.transform(brightened);
      this.store.add(this.newFileName, masked, true);
      this.view.renderMessage(
          "The brightness of \"" + this.fileName + "\" has been adjusted by " + this.amount
              + " in the area masked by \"" + this.maskFileName + "\"" + System.lineSeparator()
              + "Command: ");
    }
  }
}
