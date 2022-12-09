package controller;

import controller.commands.BrightnessCmd;
import controller.commands.FilterCmd;
import controller.commands.FilterCmd.FilterType;
import controller.commands.HorizontalFlipCmd;
import controller.commands.LoadCmd;
import controller.commands.MenuCmd;
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
        + "Command: ");
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
    this.commands.put("visualize-red",
        (Scanner s) -> new VisualizeCmd(this.view, this.store, Channel.Red, s.next(), s.next()));
    this.commands.put("visualize-green",
        (Scanner s) -> new VisualizeCmd(this.view, this.store, Channel.Green, s.next(), s.next()));
    this.commands.put("visualize-blue",
        (Scanner s) -> new VisualizeCmd(this.view, this.store, Channel.Blue, s.next(), s.next()));
    this.commands.put("visualize-value",
        (Scanner s) -> new VisualizeCmd(this.view, this.store, Channel.Value, s.next(), s.next()));
    this.commands.put("visualize-intensity",
        (Scanner s) -> new VisualizeCmd(this.view, this.store, Channel.Intensity, s.next(),
            s.next()));
    this.commands.put("visualize-luma",
        (Scanner s) -> new VisualizeCmd(this.view, this.store, Channel.Luma, s.next(), s.next()));
    this.commands.put("brighten",
        (Scanner s) -> new BrightnessCmd(this.view, this.store, s.nextInt(), s.next(), s.next()));
    this.commands.put("darken",
        (Scanner s) -> new BrightnessCmd(this.view, this.store, (s.nextInt() * -1), s.next(),
            s.next()));
    this.commands.put("horizontal-flip",
        (Scanner s) -> new HorizontalFlipCmd(this.view, this.store, s.next(), s.next()));
    this.commands.put("vertical-flip",
        (Scanner s) -> new VerticalFlipCmd(this.view, this.store, s.next(), s.next()));
    this.commands.put("blur",
        (Scanner s) -> new FilterCmd(this.view, this.store, FilterType.Blur, s.next(), s.next()));
    this.commands.put("sharpen",
        (Scanner s) -> new FilterCmd(this.view, this.store, FilterType.Sharpen, s.next(),
            s.next()));
    this.commands.put("greyscale",
        (Scanner s) -> new FilterCmd(this.view, this.store, FilterType.Greyscale, s.next(),
            s.next()));
    this.commands.put("sepia",
        (Scanner s) -> new FilterCmd(this.view, this.store, FilterType.Sepia, s.next(), s.next()));
  }

}
