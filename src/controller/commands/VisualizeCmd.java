package controller.commands;

import controller.ImageProcessorCmd;
import model.Image;
import model.ImageTransformation;
import model.StoredImages;
import model.transformations.Mask;
import model.transformations.Visualize;
import model.transformations.Visualize.Channel;
import view.ImageProcessorView;


/**
 * Class that represents a command, "Visualize", that the processor can handle. Implements the
 * {@code ImageProcessorCmd} interface and execute the command. Visualizes the image according to
 * the given channel (R, G, B, Luma, Value, Intensity).
 */
public class VisualizeCmd implements ImageProcessorCmd {

  private final ImageProcessorView view;
  private final StoredImages store;
  private final Channel channel;
  private final String fileName;
  private final String newFileName;
  private String maskFileName = null;

  /**
   * Constructs a Visualize command.
   *
   * @param view        the view to display the messages to.
   * @param store       the store to store images in.
   * @param channel     the enum that represents which visualize transformation to perform.
   * @param fileName    the file name of the image to be transformed.
   * @param newFileName the file name of the new transformed image.
   * @throws IllegalArgumentException if any of the parameters are null.
   */
  public VisualizeCmd(ImageProcessorView view, StoredImages store, Channel channel, String fileName,
      String newFileName) throws IllegalArgumentException {
    if (view == null || store == null || channel == null) {
      throw new IllegalArgumentException("View, store, and channel cannot be null");
    }
    if (fileName == null || fileName.isEmpty() || newFileName == null || newFileName.isEmpty()) {
      throw new IllegalArgumentException("File name cannot be empty");
    }
    this.view = view;
    this.store = store;
    this.channel = channel;
    this.fileName = fileName.toLowerCase();
    this.newFileName = newFileName.toLowerCase();
  }

  public VisualizeCmd(ImageProcessorView view, StoredImages store, Channel channel, String fileName,
      String maskFileName, String newFileName) {
    this(view, store, channel, fileName, newFileName);
    if (maskFileName == null) {
      throw new IllegalArgumentException("Mask file name cannot be null");
    }
    this.maskFileName = maskFileName.toLowerCase();
  }


  @Override
  public void execute() throws IllegalArgumentException {
    Image retrieved = this.store.retrieve(this.fileName);
    ImageTransformation visualize = new Visualize(this.channel);
    Image visualized = visualize.transform(retrieved);
    if (this.maskFileName == null) {
      this.store.add(this.newFileName, visualized, true);
      this.view.renderMessage(
          "Transformed \"" + this.fileName + "\" to visualize the " + this.channel.toString()
              .toLowerCase() + " channel" + System.lineSeparator() + "Command: ");
    } else {
      Image maskImage = this.store.retrieve(this.maskFileName);
      ImageTransformation mask = new Mask(retrieved, maskImage);
      Image masked = mask.transform(visualized);
      this.store.add(this.newFileName, masked, true);
      this.view.renderMessage(
          "Transformed \"" + this.fileName + "\" to visualize the " + this.channel.toString()
              .toLowerCase() + " channel in the area masked by \"" + this.maskFileName + "\""
              + System.lineSeparator() + "Command: ");
    }
  }
}
