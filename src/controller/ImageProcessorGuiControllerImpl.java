package controller;

import controller.commands.BrightnessCmd;
import controller.commands.DownscaleCmd;
import controller.commands.FilterCmd;
import controller.commands.FilterCmd.FilterType;
import controller.commands.HorizontalFlipCmd;
import controller.commands.LoadCmd;
import controller.commands.MosaicCmd;
import controller.commands.SaveCmd;
import controller.commands.VerticalFlipCmd;
import controller.commands.VisualizeCmd;
import controller.prompters.BrightnessPrompter;
import controller.prompters.DownscalePrompter;
import controller.prompters.MosaicPrompter;
import controller.prompters.StandardPrompter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.Image;
import model.ImageUtils;
import model.StoredImages;
import model.transformations.Visualize.Channel;
import view.ImageProcessorGui;
import view.ImageProcessorGui.DialogType;
import view.panels.MenubarPanel;
import view.panels.TransformationPanel;

/**
 * The class that implements the ImageProcessorGuiController. ImageProcessorGuiControllerImpl
 * implements all the features detailed out in the interface.
 */
public class ImageProcessorGuiControllerImpl implements ImageProcessorGuiController {

  private final StoredImages store;
  private final Map<String, Supplier<ImageProcessorPrompter>> prompts;
  private final Map<String, Function<String[], ImageProcessorCmd>> transformations;
  private ImageProcessorGui view;

  /**
   * Constructs a new controller for the GUI.
   *
   * @param store the store to store images in
   */
  public ImageProcessorGuiControllerImpl(StoredImages store) {
    if (store == null) {
      throw new IllegalArgumentException("Store cannot be null.");
    }
    this.store = store;
    this.prompts = new HashMap<>();
    this.transformations = new HashMap<>();
  }

  @Override
  public void setView(ImageProcessorGui view) {
    this.view = view;
    MenubarPanel menu = this.view.getMenubarPanel();
    menu.addFeatures(this);
    TransformationPanel transformations = this.view.getTransformationPanel();
    transformations.addFeatures(this);
    addPrompts();
    addTransformations();
  }

  @Override
  public void quit() {
    this.view.close();
    System.exit(0);
  }

  @Override
  public void loadImage() {
    String file = this.view.loadFile(this.supportedExtensions());
    if (file == null) {
      this.view.renderMessage("Load cancelled.");
      return;
    }

    List<String> questions = new ArrayList<>();
    questions.add("Choose a name to load this image as: ");

    String[] answers = this.view.renderInput(questions, null);
    if (answers == null) {
      this.view.renderMessage("Load cancelled.");
      return;
    }

    if (answers[0].equals("")) {
      this.view.renderDialog(DialogType.Danger, "Image name cannot be empty.");
      return;
    }
    String newFileName = answers[0];

    try {
      ImageProcessorCmd command = new LoadCmd(this.view, this.store, file, newFileName);
      command.execute();
    } catch (IllegalArgumentException e) {
      this.view.renderDialog(DialogType.Danger, e.getMessage());
      return;
    }

    Image image = this.store.retrieve(newFileName);
    BufferedImage buffered = ImageUtils.getBufferedImage(image);
    int[][] histogram = ImageUtils.getChannelFrequencies(image);
    this.view.displayImage(newFileName, buffered, histogram);
  }

  @Override
  public void saveImage() {
    String name = this.view.getCurrentImageName();
    if (name == null) {
      this.view.renderDialog(DialogType.Danger, "No images loaded");
      return;
    }

    String file = this.view.saveFile(this.supportedExtensions());
    if (file == null) {
      this.view.renderMessage("Save cancelled.");
      return;
    }

    try {
      ImageProcessorCmd command = new SaveCmd(this.view, this.store, file, name);
      command.execute();
    } catch (IllegalArgumentException e) {
      this.view.renderDialog(DialogType.Danger, e.getMessage());
    }
  }

  @Override
  public void transformImage(String command) {
    if (command == null) {
      throw new IllegalArgumentException("Command cannot be null.");
    }

    // Get the transformation's prompter and command
    Supplier<ImageProcessorPrompter> prcmd = this.prompts.getOrDefault(command, null);
    Function<String[], ImageProcessorCmd> cmd = this.transformations.getOrDefault(command, null);

    // Attempt to prompt the user for input
    String[] answers = null;
    if (prcmd == null) {
      throw new IllegalArgumentException("Prompter not defined for this transformation.");
    } else {
      ImageProcessorPrompter prompter = prcmd.get();
      try {
        answers = prompter.prompt();
      } catch (IllegalArgumentException e) {
        this.view.renderDialog(DialogType.Danger, e.getMessage());
        return;
      } catch (IllegalStateException e) {
        this.view.renderMessage(e.getMessage());
        return;
      }
    }

    // Check if the prompter returned a null value
    if (answers == null || answers.length == 0) {
      throw new IllegalArgumentException("Prompter returned a null value.");
    }

    // Attempt to execute the command
    try {
      cmd.apply(answers).execute();
      this.view.displayImage(answers[1],
          ImageUtils.getBufferedImage(this.store.retrieve(answers[1])),
          ImageUtils.getChannelFrequencies(this.store.retrieve(answers[1])));
    } catch (IllegalArgumentException e) {
      this.view.renderDialog(DialogType.Danger, e.getMessage());
    }

  }

  /**
   * Defines the prompters needed to collect user input for the transformations supported by this
   * controller.
   */
  private void addPrompts() {
    // Add prompters to the map
    this.prompts.put("Blur", () -> new StandardPrompter(this.view));
    this.prompts.put("Brighten", () -> new BrightnessPrompter(this.view, true));
    this.prompts.put("Darken", () -> new BrightnessPrompter(this.view, false));
    this.prompts.put("Greyscale", () -> new StandardPrompter(this.view));
    this.prompts.put("Horizontal Flip", () -> new StandardPrompter(this.view));
    this.prompts.put("Vertical Flip", () -> new StandardPrompter(this.view));
    this.prompts.put("Sepia", () -> new StandardPrompter(this.view));
    this.prompts.put("Sharpen", () -> new StandardPrompter(this.view));
    this.prompts.put("Visualize Red", () -> new StandardPrompter(this.view));
    this.prompts.put("Visualize Green", () -> new StandardPrompter(this.view));
    this.prompts.put("Visualize Blue", () -> new StandardPrompter(this.view));
    this.prompts.put("Visualize Value", () -> new StandardPrompter(this.view));
    this.prompts.put("Visualize Intensity", () -> new StandardPrompter(this.view));
    this.prompts.put("Visualize Luma", () -> new StandardPrompter(this.view));
    this.prompts.put("Mosaic", () -> new MosaicPrompter(this.view));
    this.prompts.put("Downscale", () -> new DownscalePrompter(this.view));
  }

  /**
   * Defines the transformations that can be applied to an image supported by this controller.
   */
  private void addTransformations() {
    // Add transformations to the map
    this.transformations.put("Blur",
        (String[] s) -> new FilterCmd(this.view, this.store, FilterType.Blur, s[0], s[1]));
    this.transformations.put("Brighten",
        (String[] s) -> new BrightnessCmd(this.view, this.store, Integer.parseInt(s[2]), s[0],
            s[1]));
    this.transformations.put("Darken",
        (String[] s) -> new BrightnessCmd(this.view, this.store, Integer.parseInt(s[2]) * -1, s[0],
            s[1]));
    this.transformations.put("Greyscale",
        (String[] s) -> new FilterCmd(this.view, this.store, FilterType.Greyscale, s[0], s[1]));
    this.transformations.put("Horizontal Flip",
        (String[] s) -> new HorizontalFlipCmd(this.view, this.store, s[0], s[1]));
    this.transformations.put("Vertical Flip",
        (String[] s) -> new VerticalFlipCmd(this.view, this.store, s[0], s[1]));
    this.transformations.put("Sepia",
        (String[] s) -> new FilterCmd(this.view, this.store, FilterType.Sepia, s[0], s[1]));
    this.transformations.put("Sharpen",
        (String[] s) -> new FilterCmd(this.view, this.store, FilterType.Sharpen, s[0], s[1]));
    this.transformations.put("Visualize Red",
        (String[] s) -> new VisualizeCmd(this.view, this.store, Channel.Red, s[0], s[1]));
    this.transformations.put("Visualize Green",
        (String[] s) -> new VisualizeCmd(this.view, this.store, Channel.Green, s[0], s[1]));
    this.transformations.put("Visualize Blue",
        (String[] s) -> new VisualizeCmd(this.view, this.store, Channel.Blue, s[0], s[1]));
    this.transformations.put("Visualize Value",
        (String[] s) -> new VisualizeCmd(this.view, this.store, Channel.Value, s[0], s[1]));
    this.transformations.put("Visualize Intensity",
        (String[] s) -> new VisualizeCmd(this.view, this.store, Channel.Intensity, s[0], s[1]));
    this.transformations.put("Visualize Luma",
        (String[] s) -> new VisualizeCmd(this.view, this.store, Channel.Luma, s[0], s[1]));
    this.transformations.put("Mosaic",
        (String[] s) -> new MosaicCmd(this.view, this.store, Integer.parseInt(s[2]), s[0], s[1]));
    this.transformations.put("Downscale",
        (String[] s) -> new DownscaleCmd(this.view, this.store, Integer.parseInt(s[2]),
            Integer.parseInt(s[3]), s[0], s[1]));

    // Confirms a prompter is defined for each transformation
    for (String transformation : this.transformations.keySet()) {
      if (!this.prompts.containsKey(transformation)) {
        throw new IllegalStateException(
            "No prompter defined for the " + transformation + " transformation: ");
      }
    }

    // Set the transformations in the view
    List<String> list = new ArrayList<>(this.transformations.keySet());
    this.view.setTransformations(list);
  }

  /**
   * Returns a file name extension filter for image files this controller supports.
   *
   * @return the file name extension filter
   */
  private final FileNameExtensionFilter supportedExtensions() {
    List<String> extensions = new ArrayList<>();
    extensions.addAll(Arrays.asList(new ImageIOHandler().getSupportedExtensions()));
    extensions.addAll(Arrays.asList(new ImagePPMHandler().getSupportedExtensions()));
    String[] exts = extensions.toArray(new String[extensions.size()]);
    return new FileNameExtensionFilter("Supported Image Files", exts);
  }
}

