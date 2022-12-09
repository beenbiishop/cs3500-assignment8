package controller.commands;

import controller.ImageFileHandler;
import controller.ImageIOHandler;
import controller.ImagePPMHandler;
import controller.ImageProcessorCmd;
import model.Image;
import model.StoredImages;
import view.ImageProcessorView;

/**
 * Class that represents a command, "Load", that the processor can handle. Implements the
 * {@code ImageProcessorCmd} interface and execute the command. Loads an image from the Stored
 * Images.
 */
public class LoadCmd implements ImageProcessorCmd {

  private final ImageProcessorView view;
  private final StoredImages store;
  private final String path;
  private final String fileName;

  /**
   * Constructs a Load command.
   *
   * @param view     the view to display the messages to.
   * @param store    the store to retrieve images from.
   * @param path     the file path of the image to be loaded.
   * @param fileName the file name of the image to be added.
   * @throws IllegalArgumentException if any of the parameters are null.
   */
  public LoadCmd(ImageProcessorView view, StoredImages store, String path, String fileName) {
    if (view == null || store == null) {
      throw new IllegalArgumentException("View and store cannot be null");
    }
    if (path == null || path.isEmpty() || fileName == null || fileName.isEmpty()) {
      throw new IllegalArgumentException("Path and file name cannot be empty");
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

    Image processed = handler.process(this.path);
    this.store.add(this.fileName, processed, true);
    this.view.renderMessage("\"" + this.path + "\" loaded successfully as \"" + this.fileName + "\""
        + System.lineSeparator() + "Command: ");
  }
}
