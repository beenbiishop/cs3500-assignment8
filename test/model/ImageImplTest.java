package model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.awt.Color;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link ImageImpl} class and its methods.
 */
public class ImageImplTest {

  private Color[][] pixels;
  private Image image;
  private Image image2;

  @Before
  public void setUp() {
    this.pixels = new Color[3][2];
    Color[][] pixels2 = new Color[12][27];

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 2; j++) {
        Color pixel;
        if (i == 0) {
          pixel = Color.RED;
        } else if (i == 1) {
          pixel = Color.GREEN;
        } else {
          pixel = Color.BLUE;
        }
        this.pixels[i][j] = pixel;
      }
    }

    for (int i = 0; i < 12; i++) {
      for (int j = 0; j < 27; j++) {
        pixels2[i][j] = Color.BLACK;
      }
    }

    this.image = new ImageImpl(this.pixels);
    this.image2 = new ImageImpl(pixels2);
  }

  @Test
  public void testValidConstructor() {
    Image testImage = new ImageImpl(this.pixels);
    Color[][] testPixels = testImage.getPixels();
    assertEquals(Color.RED, testPixels[0][0]);
    assertEquals(Color.RED, testPixels[0][1]);
    assertEquals(Color.GREEN, testPixels[1][0]);
    assertEquals(Color.GREEN, testPixels[1][1]);
    assertEquals(Color.BLUE, testPixels[2][0]);
    assertEquals(Color.BLUE, testPixels[2][1]);
  }

  @Test
  public void testConstructorMutable() {
    Image testImage = new ImageImpl(this.pixels);
    this.pixels[2][1] = Color.YELLOW;
    assertEquals(Color.BLUE, testImage.getPixels()[2][1]);
  }

  @Test
  public void testInvalidConstructors() {
    try {
      new ImageImpl(new Color[0][0]);
      fail("Should through error for empty array");
    } catch (IllegalArgumentException e) {
      assertEquals("The pixels array must contain at least one pixel", e.getMessage());
    }

    try {
      new ImageImpl(new Color[0][1]);
      fail("Should through error for empty array");
    } catch (IllegalArgumentException e) {
      assertEquals("The pixels array must contain at least one pixel", e.getMessage());
    }

    try {
      this.pixels[0][1] = null;
      new ImageImpl(this.pixels);
      fail("Should through error for null pixel");
    } catch (IllegalArgumentException e) {
      assertEquals("The pixels array must not contain null pixels", e.getMessage());
    }
  }

  @Test
  public void testGetHeight() {
    assertEquals(3, this.image.getHeight());
    assertEquals(12, this.image2.getHeight());
  }

  @Test
  public void testGetWidth() {
    assertEquals(2, this.image.getWidth());
    assertEquals(27, this.image2.getWidth());
  }

  @Test
  public void testGetPixels() {
    Color[][] gottenPixels = this.image.getPixels();

    Color[][] manualPixels = new Color[3][2];
    manualPixels[0][0] = Color.RED;
    manualPixels[0][1] = Color.RED;
    manualPixels[1][0] = Color.GREEN;
    manualPixels[1][1] = Color.GREEN;
    manualPixels[2][0] = Color.BLUE;
    manualPixels[2][1] = Color.BLUE;

    assertArrayEquals(manualPixels, gottenPixels);
  }

  @Test
  public void testGetPixelsMutable() {
    Image testImage = new ImageImpl(this.pixels);
    Color[][] gotten = testImage.getPixels();
    gotten[0][0] = Color.ORANGE;
    assertEquals(Color.RED, testImage.getPixels()[0][0]);
  }

  @Test
  public void testGetPixelsUnique() {
    Image testImage = new ImageImpl(this.pixels);
    Color[][] gotten = testImage.getPixels();
    assertNotSame(this.pixels, gotten);
  }

  @Test
  public void testCopy() {
    Image copy = this.image.copy();
    Color[][] copyPixels = copy.getPixels();
    assertEquals(Color.RED, copyPixels[0][0]);
    assertEquals(Color.RED, copyPixels[0][1]);
    assertEquals(Color.GREEN, copyPixels[1][0]);
    assertEquals(Color.GREEN, copyPixels[1][1]);
    assertEquals(Color.BLUE, copyPixels[2][0]);
    assertEquals(Color.BLUE, copyPixels[2][1]);
  }

  @Test
  public void testCopyUnique() {
    Image copy = this.image.copy();
    assertNotSame(this.image, copy);

    Image copy2 = this.image2.copy();
    assertNotSame(this.image2, copy2);
  }

  @Test
  public void testCopyMutable() {
    Image copy = this.image.copy();
    Color[][] copyPixels = copy.getPixels();
    copyPixels[0][0] = Color.BLACK;
    assertEquals(Color.BLACK, copyPixels[0][0]);
    assertEquals(Color.RED, this.image.getPixels()[0][0]);
  }
}