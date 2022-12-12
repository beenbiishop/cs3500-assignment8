package controller;

/**
 * Represents a prompter command that can be executed by the GUI controller. Handles the user flow
 * to prompt a user for input in the GUI view and validate the input based on the subclass
 * implementation of the {@code prompt} method.
 */

public interface ImageProcessorPrompter {

  /**
   * Prompts the user for information and returns the user's input. The user's input is validated
   * based on this method's implementation.
   *
   * @return the user's validated input as an array of strings
   * @throws IllegalStateException    if a user terminates the input prompt
   * @throws IllegalArgumentException if the command cannot be executed due to an error with user
   *                                  provided input
   */
  String[] prompt() throws IllegalStateException, IllegalArgumentException;

}
