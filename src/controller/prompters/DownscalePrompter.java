package controller.prompters;

import controller.ImageProcessorPrompter;
import java.util.ArrayList;
import java.util.List;
import view.ImageProcessorGui;

/**
 * Implements the {@link ImageProcessorPrompter} interface to collect and validate input needed to
 * perform a {@code DownscaleCmd}.
 * <p>
 * This prompter will return an array of strings with these elements:
 *   <ul>
 *     <li>[0] -> the name of the current image</li>
 *     <li>[1] -> the name for the transformed image</li>
 *     <li>[2] -> the width of the downscaled image</li>
 *     <li>[3] -> the height of the downscaled image</li>
 *   </ul>
 * </p>
 */
public class DownscalePrompter implements ImageProcessorPrompter {

  private final ImageProcessorGui view;

  /**
   * Constructs a new downscale prompter.
   *
   * @param view the view to display input prompts to
   */
  public DownscalePrompter(ImageProcessorGui view) {
    if (view == null) {
      throw new IllegalArgumentException("View cannot be null");
    }
    this.view = view;
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
    questions.add("Enter the width of the downscaled image:");
    questions.add("Enter the height of the downscaled image:");

    // Render the input dialog
    String[] answers = this.view.renderInput(questions, null);

    // Validate the user's input
    if (answers == null || answers.length != 3) {
      throw new IllegalStateException("Transformation cancelled.");
    } else if (answers[0] == null || answers[0].length() == 0) {
      throw new IllegalArgumentException("Image name cannot be empty.");
    } else if (answers[1] == null || answers[1].length() == 0) {
      throw new IllegalArgumentException("New width cannot be empty.");
    } else if (answers[2] == null || answers[2].length() == 0) {
      throw new IllegalArgumentException("New height cannot be empty.");
    } else {
      try {
        Integer.parseInt(answers[1]);
        Integer.parseInt(answers[2]);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("New width and height must be integers.");
      }
    }

    // Add the name of the currently selected image to the returned array
    String[] ret = new String[4];
    ret[0] = currentName;
    ret[1] = answers[0];
    ret[2] = answers[1];
    ret[3] = answers[2];

    return ret;
  }

}
