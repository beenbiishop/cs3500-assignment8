package view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the ImageProcessorViewImpl class and its methods.
 */
// TODO: Document removal of renderWelcome() test methods (x2)
// TODO: Document removal of renderMenu() test methods (x2)
public class ImageProcessorViewImplTest {

  ImageProcessorView view;
  Appendable appendable;

  @Before
  public void setUp() {
    this.appendable = new StringBuilder();
    this.view = new ImageProcessorViewImpl(appendable);
  }

  @Test
  public void testNullConstructor() {
    try {
      new ImageProcessorViewImpl(null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Appendable cannot be null", e.getMessage());
    }
  }

  @Test
  public void testRenderMessage() {
    this.view.renderMessage("Message to Render.");
    assertEquals("Message to Render.", this.appendable.toString());
  }

  @Test
  public void testRenderMessageError() {
    this.view = new MockImageProcessorViewImpl(this.appendable);
    try {
      this.view.renderMessage("Message to Render.");
      fail("Expected an IllegalStateException to be thrown");
    } catch (IllegalStateException e) {
      assertEquals("Unable to render message", e.getMessage());
    }
  }
}