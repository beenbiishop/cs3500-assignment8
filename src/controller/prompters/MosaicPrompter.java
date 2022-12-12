package controller.prompters;

import controller.ImageProcessorPrompter;
import java.util.ArrayList;
import java.util.List;
import view.ImageProcessorGui;

/**
 * Implements the {@link ImageProcessorPrompter} interface to collect and validate input needed to
 * perform a {@code MosaicCmd}.
 * <p>
 * This prompter will return an array of strings with these elements:
 *   <ul>
 *     <li>[0] -> the name of the current image</li>
 *     <li>[1] -> the name for the transformed image</li>
 *     <li>[2] -> the number of seeds to use</li>
 *   </ul>
 * </p>
 */
public class MosaicPrompter implements ImageProcessorPrompter {

  private final ImageProcessorGui view;

  /**
   * Constructs a new mosaic prompter.
   *
   * @param view the view to display input prompts to
   */
  public MosaicPrompter(ImageProcessorGui view) {
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
    questions.add("Enter the number of seeds to mosaic this image with:");

    // Render the input dialog
    String[] answers = this.view.renderInput(questions, null);

    // Validate the user's input
    if (answers == null || answers.length != 2) {
      throw new IllegalStateException("Transformation cancelled.");
    } else if (answers[0] == null || answers[0].length() == 0) {
      throw new IllegalArgumentException("Image name cannot be empty.");
    } else if (answers[1] == null || answers[1].length() == 0) {
      throw new IllegalArgumentException("Seed amount cannot be empty.");
    } else {
      try {
        int parsed = Integer.parseInt(answers[1]);
        if (parsed < 0) {
          throw new IllegalArgumentException("Seed amount must be positive.");
        }
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Seed amount must be an integer.");
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
