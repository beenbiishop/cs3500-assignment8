package controller;

/**
 * Represents a controller for the image processor. As the user inputs commands, the controller
 * validates the parameters and executes them. The controller also handles the exceptions thrown by
 * the model and view, and displays them as messages to the user via the view.
 */
public interface ImageProcessorController {

  /**
   * Starts the controller. The controller will continue to run until the user inputs the "quit"
   * command.
   */
  void run();

}
