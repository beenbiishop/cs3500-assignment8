package controller.commands;

import controller.ImageProcessorCmd;
import view.ImageProcessorView;

/**
 * Class that represents a command, "Menu", that the processor can handle. Implements the
 * {@code ImageProcessorCmd} interface and executes the command. Displays the menu.
 */
public class MenuCmd implements ImageProcessorCmd {

  private final ImageProcessorView view;

  /**
   * Constructs a Menu command.
   *
   * @param view the view to display the menu to.
   * @throws IllegalArgumentException if the view is null.
   */
  public MenuCmd(ImageProcessorView view) throws IllegalArgumentException {
    if (view == null) {
      throw new IllegalArgumentException("View cannot be null");
    }
    this.view = view;
  }

  @Override
  public void execute() {
    this.view.renderMessage("Supported commands:" + System.lineSeparator());
    this.view.renderMessage("* \"quit\" - quits the program" + System.lineSeparator());
    this.view.renderMessage("* \"menu\" - displays the menu of commands" + System.lineSeparator());
    this.view.renderMessage(
        "* \"load\" <path> <filename> - loads an image (identified by given name) into the"
            + " processor" + System.lineSeparator());
    this.view.renderMessage(
        "* \"save\" <path> <filename> - saves an image to an output file" + System.lineSeparator());
    this.view.renderMessage(
        "* \"visualize-<component>\" <filename> <new filename> - transforms an image to a new"
            + " greyscale image using a chosen component" + System.lineSeparator());
    this.view.renderMessage(
        "    * component can be \"red\", \"green\", \"blue\", \"value\", \"intensity\", or \"luma\""
            + System.lineSeparator());
    this.view.renderMessage(
        "* \"brighten\" <amount> <filename> <new filename> - transforms an image"
            + " to a new image brightened by an amount" + System.lineSeparator());
    this.view.renderMessage(
        "* \"darken\" <amount> <filename> <new filename> - transforms an image to a "
            + "new image darkened by an amount" + System.lineSeparator());
    this.view.renderMessage(
        "* \"horizontal-flip\" <filename> <new filename> - horizontally flips an image"
            + " to a new image" + System.lineSeparator());
    this.view.renderMessage(
        "* \"vertical-flip\" <filename> <new filename> - vertically flips an image"
            + " to a new image" + System.lineSeparator());
    this.view.renderMessage(
        "* \"greyscale\" <filename> <new filename> - transforms an image to a new greyscale"
            + " filtered image" + System.lineSeparator());
    this.view.renderMessage(
        "* \"sepia\" <filename> <new filename> - transforms an image to a new sepia"
            + " filtered image" + System.lineSeparator());
    this.view.renderMessage(
        "* \"blur\" <filename> <new filename> - transforms an image to a new blurred image"
            + System.lineSeparator());
    this.view.renderMessage(
        "* \"sharpen\" <filename> <new filename> - transforms an image to a new sharpened image"
            + System.lineSeparator());
    this.view.renderMessage("Command: ");
  }

}
