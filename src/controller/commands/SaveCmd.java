package controller.commands;

import controller.ImageFileHandler;
import controller.ImageIOHandler;
import controller.ImagePPMHandler;
import controller.ImageProcessorCmd;
import model.Image;
import model.StoredImages;
import view.ImageProcessorView;

/**
 * Class that represents a command, "Save", that the processor can handle. Implements the
 * {@code ImageProcessorCmd} interface and execute the command. Save an image to the Stored Images.
 */
public class SaveCmd implements ImageProcessorCmd {

  private final ImageProcessorView view;
  private final StoredImages store;
  private final String path;
  private final String fileName;

  /**
   * Constructs a Save command.
   *
   * @param view     the view to display the messages to.
   * @param store    the store to store images in.
   * @param path     the file path of the image to be saved.
   * @param fileName the file name of the image to be retrieved.
   * @throws IllegalArgumentException if any of the parameters are null.
   */
  public SaveCmd(ImageProcessorView view, StoredImages store, String path, String fileName)
      throws IllegalArgumentException {
    if (view == null || store == null || path == null || fileName == null) {
      throw new IllegalArgumentException("View, store, path, and file name cannot be null");
    }
    this.view = view;
    this.store = store;
    this.path = path;
    this.fileName = fileName.toLowerCase();
  }

  @Override
  public void execute() throws IllegalArgumentException {
    ImageFileHandler handler;

    if (this.path.toLowerCase().endsWith(".ppm")) {
      handler = new ImagePPMHandler();
    } else {
      handler = new ImageIOHandler();
    }

    Image retrieved = this.store.retrieve(this.fileName);
    handler.export(retrieved, this.path);
    this.view.renderMessage("\"" + this.fileName + "\" saved successfully as \"" + this.path + "\""
        + System.lineSeparator() + "Command: ");

  }
}
