package controller;

import controller.commands.BrightnessCmd;
import controller.commands.FilterCmd;
import controller.commands.FilterCmd.FilterType;
import controller.commands.HorizontalFlipCmd;
import controller.commands.LoadCmd;
import controller.commands.SaveCmd;
import controller.commands.VerticalFlipCmd;
import controller.commands.VisualizeCmd;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
    this.transformations = new HashMap<>();
  }

  @Override
  public void setView(ImageProcessorGui view) {
    this.view = view;
    MenubarPanel menu = this.view.getMenubarPanel();
    menu.addFeatures(this);
    TransformationPanel transformations = this.view.getTransformationPanel();
    transformations.addFeatures(this);
    addTransformations();
  }

  @Override
  public void quit() {
    this.view.close();
    System.exit(0);
  }

  @Override
  public void loadImage() {
    String file = this.view.loadFile(null);
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
    String file = this.view.saveFile(null);
    if (file == null) {
      this.view.renderMessage("Save cancelled.");
      return;
    }

    String name = this.view.getCurrentImageName();
    if (name == null) {
      this.view.renderDialog(DialogType.Danger, "No image selected.");
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
    String fileName = this.view.getCurrentImageName();
    if (fileName == null) {
      this.view.renderDialog(DialogType.Danger, "No image selected.");
      return;
    }

    String[] args = new String[3];
    args[0] = fileName;
    List<String> questions = new ArrayList<>();
    questions.add("Enter the name of the new image:");
    String[] answers = null;

    if (command.equals("Brighten") || command.equals("Darken")) {
      questions.add("Enter the amount to brighten/darken by:");
    }

    answers = this.view.renderInput(questions, null);
    if (answers == null || answers.length == 0) {
      this.view.renderMessage(command + " cancelled.");
      return;
    } else if (answers.length == 1) {
      if (answers[0].length() == 0) {
        this.view.renderDialog(DialogType.Danger, "Image name cannot be empty.");
        return;
      } else {
        args[1] = answers[0];
      }
    } else if (answers.length == 2) {
      if (answers[0].length() == 0 || answers[1].length() == 0) {
        this.view.renderDialog(DialogType.Danger, "Image name and amount cannot be empty.");
        return;
      } else {
        try {
          Integer.parseInt(answers[1]);
          if (Integer.parseInt(answers[1]) < 0) {
            this.view.renderDialog(DialogType.Danger, "Amount must be a positive number.");
            return;
          }
        } catch (NumberFormatException e) {
          this.view.renderDialog(DialogType.Danger, "Amount must be a positive number.");
          return;
        }
        args[1] = answers[0];
        args[2] = answers[1];
      }
    }

    Function<String[], ImageProcessorCmd> cmd = this.transformations.getOrDefault(command, null);
    try {
      if (cmd == null) {
        throw new IllegalArgumentException("Unsupported transformation.");
      } else {
        cmd.apply(args).execute();
        this.view.displayImage(args[1], ImageUtils.getBufferedImage(this.store.retrieve(args[1])),
            ImageUtils.getChannelFrequencies(this.store.retrieve(args[1])));
      }
    } catch (IllegalArgumentException e) {
      this.view.renderDialog(DialogType.Danger, e.getMessage());
    }

  }

  /**
   * Defines the transformations that can be applied to an image supported by this controller.
   */
  private void addTransformations() {
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
    List<String> list = new ArrayList<>(this.transformations.keySet());
    this.view.setTransformations(list);
  }
}

