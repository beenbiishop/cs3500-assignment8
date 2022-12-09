package view;

/**
 * Represents a mock view for the image processor that always throws an exception.
 */
// TODO: Document removal of renderWelcome() mock method
// TODO: Document removal of renderMenu() mock method
public class MockImageProcessorViewImpl implements ImageProcessorView {

  /**
   * Constructs a mock view for the image processor that always throws an exception.
   *
   * @param appendable the appendable to append the messages to
   * @throws IllegalArgumentException if the appendable is null
   */
  public MockImageProcessorViewImpl(Appendable appendable) throws IllegalArgumentException {
    if (appendable == null) {
      throw new IllegalArgumentException("Appendable cannot be null");
    }
  }

  /**
   * Implements renderMessage from the ImageProcessorView interface. Always throws an exception.
   *
   * @param message the message to be transmitted
   * @throws IllegalStateException every time for testing
   */
  @Override
  public void renderMessage(String message) throws IllegalStateException {
    throw new IllegalStateException("Unable to render message");
  }
}
