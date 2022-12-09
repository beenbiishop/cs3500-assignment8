package view;

import java.io.IOException;

/**
 * Implements the {@link ImageProcessorView} interface and handles the appending of messages sent to
 * the user by the controller.
 */
public class ImageProcessorViewImpl implements ImageProcessorView {

  private final Appendable appendable;

  /**
   * Constructs a new ImageProcessorViewImpl object with the given appendable.
   *
   * @param appendable the appendable to append the text to the user
   * @throws IllegalArgumentException if the appendable is null
   */
  public ImageProcessorViewImpl(Appendable appendable) throws IllegalArgumentException {
    if (appendable == null) {
      throw new IllegalArgumentException("Appendable cannot be null");
    }
    this.appendable = appendable;
  }

  @Override
  public void renderMessage(String message) throws IllegalStateException {
    try {
      this.appendable.append(message);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to render message");
    }
  }

}
