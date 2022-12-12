package controller.prompters;

import controller.ImageProcessorPrompter;
import java.util.ArrayList;
import java.util.List;
import view.ImageProcessorGui;

/**
 * Implements the {@link ImageProcessorPrompter} interface to collect and validate input needed to
 * perform a {@code BrightnessCmd}.
 * <p>
 * This prompter will return an array of strings with these elements:
 *   <ul>
 *     <li>[0] -> the name of the current image</li>
 *     <li>[1] -> the name for the transformed image</li>
 *     <li>[2] -> the brightness adjustment amount</li>
 *   </ul>
 * </p>
 */
public class BrightnessPrompter implements ImageProcessorPrompter {

  private final ImageProcessorGui view;
  private final String type;

  /**
   * Constructs a new brightness prompter.
   *
   * @param view          the view to display input prompts to
   * @param isBrightening whether the prompter is for brightening or darkening
   */
  public BrightnessPrompter(ImageProcessorGui view, boolean isBrightening) {
    if (view == null) {
      throw new IllegalArgumentException("View cannot be null");
    }
    this.view = view;
    if (isBrightening) {
      this.type = "brighten";
    } else {
      this.type = "darken";
    }
  }

  @Override
  public String[] prompt() throws IllegalStateException, IllegalArgumentException {
    // Store the name of the currently selected image
    String currentName = this.view.getCurrentImageName();
    if (currentName == null) {
      throw new IllegalArgumentException("No images loaded");
    }

    // Add the questions to the list
    List<String> questions = new ArrayList<>();
    questions.add("Enter the name of the new image:");
    questions.add("Enter the amount to " + this.type + " the image by:");

    // Render the input dialog
    String[] answers = this.view.renderInput(questions, null);

    // Validate the user's input
    if (answers == null || answers.length != 2) {
      throw new IllegalStateException("Transformation cancelled.");
    } else if (answers[0] == null || answers[0].length() == 0) {
      throw new IllegalArgumentException("Image name cannot be empty.");
    } else if (answers[1] == null || answers[1].length() == 0) {
      throw new IllegalArgumentException("Amount cannot be empty.");
    } else {
      try {
        Integer.parseInt(answers[1]);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Amount must be an integer.");
      }
    }

    // Add the name of the currently selected image to the returned array
    String[] ret = new String[3];
    ret[0] = currentName;
    ret[1] = answers[0];
    ret[2] = answers[1];

    return ret;
  }
}
