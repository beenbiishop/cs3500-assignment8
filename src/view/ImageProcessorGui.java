package view;

import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.filechooser.FileNameExtensionFilter;
import view.panels.MenubarPanel;
import view.panels.TransformationPanel;

/**
 * This interface represents a view of the Image Processor that implements the graphical user
 * interface.
 */
public interface ImageProcessorGui extends ImageProcessorView {

  /**
   * Displays a {@link BufferedImage} in the view identified by the given file name.
   *
   * <p>
   * If an image's file name has already been added, the previous buffered image will be overridden
   * with the new image, otherwise a new tab will be added (like a map).
   * </p>
   *
   * @param name      the name to identify this image by
   * @param image     the image to render in the preview panel
   * @param histogram the data to render in the histogram panel
   */
  void displayImage(String name, BufferedImage image, int[][] histogram);

  /**
   * Renders a given dialog message to the user in the form of a popup.
   *
   * @param type    the type of popup to display
   * @param message the content of the message to display
   */
  void renderDialog(DialogType type, String message);

  /**
   * Renders a form with multiple inputs in the form of a popup.
   *
   * <p>An input box will be generated for each question title in the given list of strings, and an
   * equally sized list will be returned with the response to each question. If a user did not
   * answer any of the questions, an empty string will be returned in its' place.</p>
   *
   * @param questions the list of questions to ask
   * @param error     an optional error message to display (will not display if null)
   * @return an array of responses to the questions
   */
  String[] renderInput(List<String> questions, String error);

  /**
   * Renders a popup prompting a user to choose a file.
   *
   * @param filter the file types allowed to be loaded
   * @return a string with the file path of the selected file or null if dismissed
   */
  String loadFile(FileNameExtensionFilter filter);

  /**
   * Renders a popup prompting a user to choose a file.
   *
   * @param filter the file types allowed to be loaded
   * @return the file path of the selected file save location or null if dismissed
   */
  String saveFile(FileNameExtensionFilter filter);

  /**
   * Sets the available transformations the controller supports.
   *
   * @param transformations the list of transformations to display
   */
  void setTransformations(List<String> transformations);

  /**
   * Gets the name of the image tab that is currently selected.
   *
   * @return the name of the image tab that is currently selected
   */
  String getCurrentImageName();

  /**
   * Gets the menu bar panel of this GUI view.
   *
   * @return the menu bar panel of this GUI view
   */
  MenubarPanel getMenubarPanel();

  /**
   * Gets the transformation panel of this GUI view.
   *
   * @return the transformation panel of this GUI view
   */
  TransformationPanel getTransformationPanel();

  /**
   * Closes the GUI and all panels.
   */
  void close();

  /**
   * Represents a type of dialog's styling that can be displayed to a user.
   */
  enum DialogType {
    Warning, Danger, Note
  }

}
