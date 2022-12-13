package controller;

import controller.commands.BrightnessCmd;
import controller.commands.DownscaleCmd;
import controller.commands.FilterCmd;
import controller.commands.FilterCmd.FilterType;
import controller.commands.HorizontalFlipCmd;
import controller.commands.LoadCmd;
import controller.commands.MenuCmd;
import controller.commands.MosaicCmd;
import controller.commands.SaveCmd;
import controller.commands.VerticalFlipCmd;
import controller.commands.VisualizeCmd;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import model.StoredImages;
import model.transformations.Visualize.Channel;
import view.ImageProcessorView;

/**
 * Implements the {@code ImageProcessorController} interface supporting the image processor. As the
 * user inputs commands, the controller validates the parameters and executes them. The controller
 * also handles the exceptions thrown by the model and view, and displays them as messages to the
 * user via the view.
 *
 * <p>
 * If an image name already exists in the image processor, the command will overwrite the existing
 * image with the new image. If a file already exists with the same file path as the new image, the
 * command will overwrite the existing file with the new image.
 * </p>
 */
public class ImageProcessorControllerImpl implements ImageProcessorController {

  private final ImageProcessorView view;
  private final StoredImages store;
  private final Scanner scan;
  private final Map<String, Function<Scanner, ImageProcessorCmd>> commands;

  /**
   * Constructs a new controller object using the given {@link Readable} that represents user input,
   * {@link StoredImages} that represents the image store, and {@link Appendable} that represents
   * the output.
   *
   * @param input the input to read from
   * @param view  the view to display to
   * @param store the store to store images in
   * @throws IllegalArgumentException if any of the parameters are null
   */
  public ImageProcessorControllerImpl(Readable input, ImageProcessorView view, StoredImages store)
      throws IllegalArgumentException {
    if (input == null || view == null || store == null) {
      throw new IllegalArgumentException("Input, view, and store cannot be null");
    }
    this.view = view;
    this.store = store;
    this.scan = new Scanner(input);
    this.commands = new HashMap<>();
  }

  @Override
  public void run() {
    this.view.renderMessage("Welcome to the Image Processor!" + System.lineSeparator());
    this.view.renderMessage("* Enter \"menu\" to see the list of supported commands or \"quit\""
        + " to exit the program" + System.lineSeparator()
        + "* After entering a command, hit return to process it" + System.lineSeparator()
        + "* An optional mask can be applied to some commands – "
        + "run the menu command to see the syntax" + System.lineSeparator() + "Command: ");
    while (scan.hasNext()) {
      String command = scan.next();
      if (command.equalsIgnoreCase("q") || command.equalsIgnoreCase("quit")) {
        this.view.renderMessage("Quitting...");
        return;
      } else {
        ImageProcessorCmd c;
        this.addCommands();
        Function<Scanner, ImageProcessorCmd> cmd = this.commands.getOrDefault(command, null);
        try {
          if (cmd == null) {
            throw new IllegalArgumentException("Invalid command, please try again");
          } else {
            c = cmd.apply(scan);
            c.execute();
          }
        } catch (IllegalArgumentException e) {
          this.view.renderMessage(
              "Error: " + e.getMessage() + System.lineSeparator() + "Command: ");
        }
      }
    }
  }

  /**
   * Adds all supported commands and the lambda functions to create the command objects to this
   * controller object's map of valid commands.
   */
  private void addCommands() {
    this.commands.put("menu", (Scanner s) -> new MenuCmd(this.view));
    this.commands.put("load",
        (Scanner s) -> new LoadCmd(this.view, this.store, s.next(), s.next()));
    this.commands.put("save",
        (Scanner s) -> new SaveCmd(this.view, this.store, s.next(), s.next()));
    this.commands.put("visualize-red", (Scanner s) -> parseVisualize(s, Channel.Red));
    this.commands.put("visualize-green", (Scanner s) -> parseVisualize(s, Channel.Green));
    this.commands.put("visualize-blue", (Scanner s) -> parseVisualize(s, Channel.Blue));
    this.commands.put("visualize-value", (Scanner s) -> parseVisualize(s, Channel.Value));
    this.commands.put("visualize-intensity", (Scanner s) -> parseVisualize(s, Channel.Intensity));
    this.commands.put("visualize-luma", (Scanner s) -> parseVisualize(s, Channel.Luma));
    this.commands.put("blur", (Scanner s) -> parseFilter(s, FilterType.Blur));
    this.commands.put("sharpen", (Scanner s) -> parseFilter(s, FilterType.Sharpen));
    this.commands.put("greyscale", (Scanner s) -> parseFilter(s, FilterType.Greyscale));
    this.commands.put("sepia", (Scanner s) -> parseFilter(s, FilterType.Sepia));
    this.commands.put("brighten", (Scanner s) -> parseBrightness(s, true));
    this.commands.put("darken", (Scanner s) -> parseBrightness(s, false));
    this.commands.put("horizontal-flip",
        (Scanner s) -> new HorizontalFlipCmd(this.view, this.store, s.next(), s.next()));
    this.commands.put("vertical-flip",
        (Scanner s) -> new VerticalFlipCmd(this.view, this.store, s.next(), s.next()));
    this.commands.put("mosaic",
        (Scanner s) -> new MosaicCmd(this.view, this.store, s.nextInt(), s.next(), s.next()));
    this.commands.put("downscale",
        (Scanner s) -> new DownscaleCmd(this.view, this.store, s.nextInt(), s.nextInt(), s.next(),
            s.next()));
  }

  /**
   * Parses the given scanner to create a new {@link VisualizeCmd} object.
   *
   * @param s       the scanner to parse
   * @param channel the channel to visualize
   * @return the new visualize command object
   */
  private VisualizeCmd parseVisualize(Scanner s, Channel channel) {
    String nextLine = s.nextLine().trim();
    String[] params = nextLine.split("\\s+");
    switch (params.length) {
      case 2:
        return new VisualizeCmd(this.view, this.store, channel, params[0], params[1]);
      case 3:
        return new VisualizeCmd(this.view, this.store, channel, params[0], params[1], params[2]);
      default:
        throw new IllegalArgumentException("Invalid command, please try again");
    }
  }

  /**
   * Parses the given scanner to create a new {@link FilterCmd} object.
   *
   * @param s    the scanner to parse
   * @param type the type of filter to return
   * @return the new filter command object
   */
  private FilterCmd parseFilter(Scanner s, FilterType type) {
    String nextLine = s.nextLine().trim();
    String[] params = nextLine.split("\\s+");
    switch (params.length) {
      case 2:
        return new FilterCmd(this.view, this.store, type, params[0], params[1]);
      case 3:
        return new FilterCmd(this.view, this.store, type, params[0], params[1], params[2]);
      default:
        throw new IllegalArgumentException("Invalid command, please try again");
    }
  }

  /**
   * Parses the given scanner to create a new {@link BrightnessCmd} object.
   *
   * @param s          the scanner to parse
   * @param isBrighten true if a brighten command, false if a darken command
   * @return the new brightness command object
   */
  private BrightnessCmd parseBrightness(Scanner s, boolean isBrighten) {
    String nextLine = s.nextLine().trim();
    String[] params = nextLine.split("\\s+");
    int multiplier = (isBrighten) ? 1 : -1;

    // process the amount
    int amount;
    try {
      amount = multiplier * Integer.parseInt(params[0]);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid amount, please try again");
    }

    // return the command
    switch (params.length) {
      case 3:
        return new BrightnessCmd(this.view, this.store, amount, params[1], params[2]);
      case 4:
        return new BrightnessCmd(this.view, this.store, amount, params[1], params[2], params[3]);
      default:
        throw new IllegalArgumentException("Invalid command, please try again");
    }
  }

}
