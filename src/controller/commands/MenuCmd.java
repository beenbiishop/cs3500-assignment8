package controller.commands;

import controller.ImageProcessorCmd;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import view.ImageProcessorView;

/**
 * Class that represents a command, "Menu", that the processor can handle. Implements the
 * {@code ImageProcessorCmd} interface and executes the command. Displays the menu.
 */
public class MenuCmd implements ImageProcessorCmd {

  private final ImageProcessorView view;
  private final List<String> transformations;

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
    this.transformations = new ArrayList<>();
    addTransformations();
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
    for (String transformation : this.transformations) {
      this.view.renderMessage(transformation + System.lineSeparator());
    }
    this.view.renderMessage("Command: ");
  }

  /**
   * Adds supported transformation commands and their descriptions to the map.
   */
  private final void addTransformations() {
    List<String[]> items = new ArrayList<>();
    items.add(new String[]{"visualize-<component>", "<filename> <new filename>",
        "applies a greyscale filter to an image using the given color channel "
            + "(\"red\", \"green\", \"blue\", \"value\", \"intensity\", or \"luma\")"});
    items.add(new String[]{"brighten", "<amount> <filename> <new filename>",
        "brightens image by an amount"});
    items.add(new String[]{"darken", "<amount> <filename> <new filename>",
        "darkens an image by an amount"});
    items.add(new String[]{"horizontal-flip", "horizontally flips an image", ""});
    items.add(new String[]{"vertical-flip", "vertically flips an image", ""});
    items.add(new String[]{"greyscale", "<filename> <new filename>",
        "applies a greyscale filter to an image"});
    items.add(
        new String[]{"sepia", "<filename> <new filename>", "applies a sepia filter to an image"});
    items.add(
        new String[]{"blur", "<filename> <new filename>", "applies a blur filter to an image"});
    items.add(new String[]{"sharpen", "<filename> <new filename>",
        "transforms an image to a new sharpened image"});
    items.add(new String[]{"mosaic", "<number of seeds> <filename> <new filename>",
        "applies a mosaic filter to an image with the given number of seeds"});
    items.add(new String[]{"downscale", "<new width> <new height> <filename> <new filename>",
        "downscales an image to the given dimensions"});

    // sort the transformations alphabetically
    items.sort(Comparator.comparing(item -> item[0]));

    // add the transformations to the list
    List<String> sorted = new ArrayList<>();
    for (String[] item : items) {
      this.transformations.add("* \"" + item[0] + "\" " + item[1] + " - " + item[2]);
    }
  }

}
