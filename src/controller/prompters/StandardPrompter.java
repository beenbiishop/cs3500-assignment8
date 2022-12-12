package controller.prompters;

import controller.ImageProcessorPrompter;
import java.util.ArrayList;
import java.util.List;
import view.ImageProcessorGui;

/**
 * Implements the {@link ImageProcessorPrompter} interface to collect input needed to perform a
 * {@code ImageProcessorCmd} with no inputs besides the new transformation name.
 *
 * <p>
 * This prompter will return an array with these elements:
 *   <ul>
 *     <li>[0] -> the name of the current image</li>
 *     <li>[1] -> the name for the transformed image</li>
 *   </ul>
 * </p>
 */
public class StandardPrompter implements ImageProcessorPrompter {

  private final ImageProcessorGui view;

  /**
   * Constructs a new prompter.
   *
   * @param view the view to display input prompts to
   */
  public StandardPrompter(ImageProcessorGui view) {
    if (view == null) {
      throw new IllegalArgumentException("View cannot be null");
    }
    this.view = view;
  }

  @Override
  public String[] prompt() throws IllegalArgumentException, IllegalStateException {
    // Store the name of the currently selected image
    String currentName = this.view.getCurrentImageName();
    if (currentName == null) {
      throw new IllegalArgumentException("No images loaded");
    }

    // Add a question prompting the user for the new image name
    List<String> questions = new ArrayList<>();
    questions.add("Enter the name of the new image:");

    // Render the input dialog
    String[] answers = this.view.renderInput(questions, null);

    // Validate the user's input
    if (answers == null || answers.length == 0) {
      throw new IllegalStateException("Transformation cancelled.");
    } else if (answers[0].length() == 0) {
      throw new IllegalArgumentException("Image name cannot be empty.");
    }

    // Add the name of the currently selected image to the returned array
    String[] ret = new String[2];
    ret[0] = currentName;
    ret[1] = answers[0];

    return ret;
  }
}
