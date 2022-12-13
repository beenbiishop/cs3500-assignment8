package controller.commands;

import controller.ImageProcessorCmd;

/**
 * Class that represents a command, "Preview", that the processor can handle. Implements the
 * {@code ImageProcessorCmd} interface and execute the command. Shows a preview of a transformation
 * on a section of the image.
 */

public class PreviewCmd implements ImageProcessorCmd {
  /**
   * Executes this object's command.
   *
   * @throws IllegalArgumentException if the command cannot be executed
   */
  @Override
  public void execute() throws IllegalArgumentException {

  }
}
