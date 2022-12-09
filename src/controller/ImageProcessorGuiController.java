package controller;

import view.ImageProcessorGui;

/**
 * Represents a controller for the Image Processor program. It handles the interactions between the
 * user and the program for the graphical user interface.
 */
public interface ImageProcessorGuiController {

  /**
   * Sets the view for the controller.
   *
   * @param view the view for the controller
   */
  void setView(ImageProcessorGui view);

  /**
   * Action listener to handle the quitting the image processor program.
   */
  void quit();

  /**
   * Action listener to handle the loading of an image into the program.
   */
  void loadImage();

  /**
   * Action listener to handle the saving of an image from the program.
   */
  void saveImage();

  /**
   * Action listener to handle the transformation of an image in the program.
   */
  void transformImage(String transformation);
}
