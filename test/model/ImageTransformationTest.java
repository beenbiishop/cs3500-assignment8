package model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.Color;
import model.transformations.Blur;
import model.transformations.Brightness;
import model.transformations.Greyscale;
import model.transformations.HorizontalFlip;
import model.transformations.Sepia;
import model.transformations.Sharpen;
import model.transformations.VerticalFlip;
import model.transformations.Visualize;
import model.transformations.Visualize.Channel;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the transform method for the {@link Brightness}, {@link HorizontalFlip},
 * {@link VerticalFlip}, and {@link Visualize} implementations of the {@link ImageTransformation}
 * interface.
 */
public class ImageTransformationTest {

  Color[][] startPixels;
  Color[][] transformedPixels;
  Image startImage;
  Image transformedImage;
  ImageTransformation transformation;

  @Before
  public void setUp() {
    this.startPixels = new Color[3][3];
    this.transformedPixels = new Color[3][3];

    this.startPixels[0][0] = new Color(128, 16, 216);
    this.startPixels[0][1] = new Color(114, 17, 219);
    this.startPixels[0][2] = new Color(105, 18, 222);

    this.startPixels[1][0] = new Color(114, 17, 219);
    this.startPixels[1][1] = new Color(97, 18, 224);
    this.startPixels[1][2] = new Color(84, 18, 227);

    this.startPixels[2][0] = new Color(105, 18, 222);
    this.startPixels[2][1] = new Color(84, 18, 227);
    this.startPixels[2][2] = new Color(61, 18, 231);

    this.startImage = new ImageImpl(this.startPixels);
    this.transformedImage = null;
    this.transformation = null;
  }

  @Test
  public void testHorizontalFlip() {
    this.transformation = new HorizontalFlip();
    this.transformedImage = this.transformation.transform(this.startImage);

    this.transformedPixels[0][0] = new Color(105, 18, 222);
    this.transformedPixels[0][1] = new Color(114, 17, 219);
    this.transformedPixels[0][2] = new Color(128, 16, 216);

    this.transformedPixels[1][0] = new Color(84, 18, 227);
    this.transformedPixels[1][1] = new Color(97, 18, 224);
    this.transformedPixels[1][2] = new Color(114, 17, 219);

    this.transformedPixels[2][0] = new Color(61, 18, 231);
    this.transformedPixels[2][1] = new Color(84, 18, 227);
    this.transformedPixels[2][2] = new Color(105, 18, 222);

    assertArrayEquals(this.transformedPixels, this.transformedImage.getPixels());
  }

  @Test
  public void testVerticalFlip() {
    this.transformation = new VerticalFlip();
    this.transformedImage = this.transformation.transform(this.startImage);

    this.transformedPixels[0][0] = new Color(105, 18, 222);
    this.transformedPixels[0][1] = new Color(84, 18, 227);
    this.transformedPixels[0][2] = new Color(61, 18, 231);

    this.transformedPixels[1][0] = new Color(114, 17, 219);
    this.transformedPixels[1][1] = new Color(97, 18, 224);
    this.transformedPixels[1][2] = new Color(84, 18, 227);

    this.transformedPixels[2][0] = new Color(128, 16, 216);
    this.transformedPixels[2][1] = new Color(114, 17, 219);
    this.transformedPixels[2][2] = new Color(105, 18, 222);

    assertArrayEquals(this.transformedPixels, this.transformedImage.getPixels());
  }

  @Test
  public void testBrightnessPositive() {
    this.transformation = new Brightness(10);
    this.transformedImage = this.transformation.transform(this.startImage);

    this.transformedPixels[0][0] = new Color(138, 26, 226);
    this.transformedPixels[0][1] = new Color(124, 27, 229);
    this.transformedPixels[0][2] = new Color(115, 28, 232);

    this.transformedPixels[1][0] = new Color(124, 27, 229);
    this.transformedPixels[1][1] = new Color(107, 28, 234);
    this.transformedPixels[1][2] = new Color(94, 28, 237);

    this.transformedPixels[2][0] = new Color(115, 28, 232);
    this.transformedPixels[2][1] = new Color(94, 28, 237);
    this.transformedPixels[2][2] = new Color(71, 28, 241);

    assertArrayEquals(this.transformedPixels, this.transformedImage.getPixels());
  }

  @Test
  public void testBrightnessNegative() {
    this.transformation = new Brightness(-10);
    this.transformedImage = this.transformation.transform(this.startImage);

    this.transformedPixels[0][0] = new Color(118, 6, 206);
    this.transformedPixels[0][1] = new Color(104, 7, 209);
    this.transformedPixels[0][2] = new Color(95, 8, 212);

    this.transformedPixels[1][0] = new Color(104, 7, 209);
    this.transformedPixels[1][1] = new Color(87, 8, 214);
    this.transformedPixels[1][2] = new Color(74, 8, 217);

    this.transformedPixels[2][0] = new Color(95, 8, 212);
    this.transformedPixels[2][1] = new Color(74, 8, 217);
    this.transformedPixels[2][2] = new Color(51, 8, 221);

    assertArrayEquals(this.transformedPixels, this.transformedImage.getPixels());
  }

  @Test
  public void testSuperLowBrightness() {
    this.startPixels = new Color[1][1];
    this.startPixels[0][0] = new Color(255, 100, 0);
    this.startImage = new ImageImpl(this.startPixels);

    this.transformation = new Brightness(-500);
    this.transformedImage = this.transformation.transform(this.startImage);

    assertEquals(new Color(0, 0, 0), this.transformedImage.getPixels()[0][0]);
  }

  @Test
  public void testSuperHighBrightness() {
    this.startPixels = new Color[1][1];
    this.startPixels[0][0] = new Color(255, 100, 0);
    this.startImage = new ImageImpl(this.startPixels);

    this.transformation = new Brightness(500);
    this.transformedImage = this.transformation.transform(this.startImage);

    assertEquals(new Color(255, 255, 255), this.transformedImage.getPixels()[0][0]);
  }

  @Test
  public void testZeroBrightness() {
    try {
      this.transformation = new Brightness(0);
      fail("Brightness of 0 should throw an exception");
    } catch (IllegalArgumentException e) {
      assertEquals("The adjustment amount must be non-zero", e.getMessage());
    }
  }

  @Test
  public void testVisualizeRed() {
    this.transformation = new Visualize(Channel.Red);
    this.transformedImage = this.transformation.transform(this.startImage);

    this.transformedPixels[0][0] = new Color(128, 128, 128);
    this.transformedPixels[0][1] = new Color(114, 114, 114);
    this.transformedPixels[0][2] = new Color(105, 105, 105);

    this.transformedPixels[1][0] = new Color(114, 114, 114);
    this.transformedPixels[1][1] = new Color(97, 97, 97);
    this.transformedPixels[1][2] = new Color(84, 84, 84);

    this.transformedPixels[2][0] = new Color(105, 105, 105);
    this.transformedPixels[2][1] = new Color(84, 84, 84);
    this.transformedPixels[2][2] = new Color(61, 61, 61);

    assertArrayEquals(this.transformedPixels, this.transformedImage.getPixels());
  }

  @Test
  public void testVisualizeGreen() {
    this.transformation = new Visualize(Channel.Green);
    this.transformedImage = this.transformation.transform(this.startImage);

    this.transformedPixels[0][0] = new Color(16, 16, 16);
    this.transformedPixels[0][1] = new Color(17, 17, 17);
    this.transformedPixels[0][2] = new Color(18, 18, 18);

    this.transformedPixels[1][0] = new Color(17, 17, 17);
    this.transformedPixels[1][1] = new Color(18, 18, 18);
    this.transformedPixels[1][2] = new Color(18, 18, 18);

    this.transformedPixels[2][0] = new Color(18, 18, 18);
    this.transformedPixels[2][1] = new Color(18, 18, 18);
    this.transformedPixels[2][2] = new Color(18, 18, 18);

    assertArrayEquals(this.transformedPixels, this.transformedImage.getPixels());
  }

  @Test
  public void testVisualizeBlue() {
    this.transformation = new Visualize(Channel.Blue);
    this.transformedImage = this.transformation.transform(this.startImage);

    this.transformedPixels[0][0] = new Color(216, 216, 216);
    this.transformedPixels[0][1] = new Color(219, 219, 219);
    this.transformedPixels[0][2] = new Color(222, 222, 222);

    this.transformedPixels[1][0] = new Color(219, 219, 219);
    this.transformedPixels[1][1] = new Color(224, 224, 224);
    this.transformedPixels[1][2] = new Color(227, 227, 227);

    this.transformedPixels[2][0] = new Color(222, 222, 222);
    this.transformedPixels[2][1] = new Color(227, 227, 227);
    this.transformedPixels[2][2] = new Color(231, 231, 231);

    assertArrayEquals(this.transformedPixels, this.transformedImage.getPixels());
  }

  @Test
  public void testVisualizeLuma() {
    this.transformation = new Visualize(Channel.Luma);
    this.transformedImage = this.transformation.transform(this.startImage);

    this.transformedPixels[0][0] = new Color(54, 54, 54);
    this.transformedPixels[0][1] = new Color(52, 52, 52);
    this.transformedPixels[0][2] = new Color(51, 51, 51);

    this.transformedPixels[1][0] = new Color(52, 52, 52);
    this.transformedPixels[1][1] = new Color(50, 50, 50);
    this.transformedPixels[1][2] = new Color(47, 47, 47);

    this.transformedPixels[2][0] = new Color(51, 51, 51);
    this.transformedPixels[2][1] = new Color(47, 47, 47);
    this.transformedPixels[2][2] = new Color(43, 43, 43);

    assertArrayEquals(this.transformedPixels, this.transformedImage.getPixels());
  }

  @Test
  public void testVisualizeValue() {
    this.transformation = new Visualize(Channel.Value);
    this.transformedImage = this.transformation.transform(this.startImage);

    this.transformedPixels[0][0] = new Color(216, 216, 216);
    this.transformedPixels[0][1] = new Color(219, 219, 219);
    this.transformedPixels[0][2] = new Color(222, 222, 222);
    this.transformedPixels[1][0] = new Color(219, 219, 219);
    this.transformedPixels[1][1] = new Color(224, 224, 224);
    this.transformedPixels[1][2] = new Color(227, 227, 227);
    this.transformedPixels[2][0] = new Color(222, 222, 222);
    this.transformedPixels[2][1] = new Color(227, 227, 227);
    this.transformedPixels[2][2] = new Color(231, 231, 231);

    assertArrayEquals(this.transformedPixels, this.transformedImage.getPixels());
  }

  @Test
  public void testVisualizeIntensity() {
    this.transformation = new Visualize(Channel.Intensity);
    this.transformedImage = this.transformation.transform(this.startImage);

    this.transformedPixels[0][0] = new Color(120, 120, 120);
    this.transformedPixels[0][1] = new Color(117, 117, 117);
    this.transformedPixels[0][2] = new Color(115, 115, 115);
    this.transformedPixels[1][0] = new Color(117, 117, 117);
    this.transformedPixels[1][1] = new Color(113, 113, 113);
    this.transformedPixels[1][2] = new Color(110, 110, 110);
    this.transformedPixels[2][0] = new Color(115, 115, 115);
    this.transformedPixels[2][1] = new Color(110, 110, 110);
    this.transformedPixels[2][2] = new Color(103, 103, 103);

    assertArrayEquals(this.transformedPixels, this.transformedImage.getPixels());
  }

  @Test
  public void testBlur() {
    this.transformation = new Blur();
    this.transformedImage = this.transformation.transform(this.startImage);

    this.transformedPixels[0][0] = new Color(121, 17, 218);
    this.transformedPixels[0][1] = new Color(111, 17, 220);
    this.transformedPixels[0][2] = new Color(102, 18, 223);
    this.transformedPixels[1][0] = new Color(111, 17, 220);
    this.transformedPixels[1][1] = new Color(99, 18, 223);
    this.transformedPixels[1][2] = new Color(87, 18, 226);
    this.transformedPixels[2][0] = new Color(102, 18, 223);
    this.transformedPixels[2][1] = new Color(87, 18, 226);
    this.transformedPixels[2][2] = new Color(72, 18, 229);

    assertArrayEquals(this.transformedPixels, this.transformedImage.getPixels());
  }

  @Test
  public void testSharpen() {
    this.transformation = new Sharpen();
    this.transformedImage = this.transformation.transform(this.startImage);

    this.transformedPixels[0][0] = new Color(146, 15, 211);
    this.transformedPixels[0][1] = new Color(125, 17, 217);
    this.transformedPixels[0][2] = new Color(108, 19, 222);
    this.transformedPixels[1][0] = new Color(125, 17, 217);
    this.transformedPixels[1][1] = new Color(97, 18, 224);
    this.transformedPixels[1][2] = new Color(71, 19, 230);
    this.transformedPixels[2][0] = new Color(108, 19, 222);
    this.transformedPixels[2][1] = new Color(71, 19, 230);
    this.transformedPixels[2][2] = new Color(33, 19, 238);

    assertArrayEquals(this.transformedPixels, this.transformedImage.getPixels());
  }

  @Test
  public void testGreyscale() {
    this.transformation = new Greyscale();
    this.transformedImage = this.transformation.transform(this.startImage);

    this.transformedPixels[0][0] = new Color(54, 54, 54);
    this.transformedPixels[0][1] = new Color(52, 52, 52);
    this.transformedPixels[0][2] = new Color(51, 51, 51);

    this.transformedPixels[1][0] = new Color(52, 52, 52);
    this.transformedPixels[1][1] = new Color(50, 50, 50);
    this.transformedPixels[1][2] = new Color(47, 47, 47);

    this.transformedPixels[2][0] = new Color(51, 51, 51);
    this.transformedPixels[2][1] = new Color(47, 47, 47);
    this.transformedPixels[2][2] = new Color(43, 43, 43);

    assertArrayEquals(this.transformedPixels, this.transformedImage.getPixels());
  }

  @Test
  public void testSepia() {
    this.transformation = new Sepia();
    this.transformedImage = this.transformation.transform(this.startImage);

    this.transformedPixels[0][0] = new Color(103, 92, 72);
    this.transformedPixels[0][1] = new Color(99, 88, 69);
    this.transformedPixels[0][2] = new Color(97, 86, 67);

    this.transformedPixels[1][0] = new Color(99, 88, 69);
    this.transformedPixels[1][1] = new Color(94, 84, 65);
    this.transformedPixels[1][2] = new Color(90, 80, 62);

    this.transformedPixels[2][0] = new Color(97, 86, 67);
    this.transformedPixels[2][1] = new Color(90, 80, 62);
    this.transformedPixels[2][2] = new Color(81, 72, 56);

    assertArrayEquals(this.transformedPixels, this.transformedImage.getPixels());
  }

}