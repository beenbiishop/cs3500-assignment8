package controller;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import controller.commands.BrightnessCmd;
import controller.commands.FilterCmd;
import controller.commands.FilterCmd.FilterType;
import controller.commands.HorizontalFlipCmd;
import controller.commands.LoadCmd;
import controller.commands.SaveCmd;
import controller.commands.VerticalFlipCmd;
import controller.commands.VisualizeCmd;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import model.Image;
import model.ImageImpl;
import model.ImageTransformation;
import model.StoredImages;
import model.StoredImagesImpl;
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
import view.ImageProcessorView;
import view.ImageProcessorViewImpl;

/**
 * Contains all the testers for the controller and related classes. Tests each of the commands and
 * whether the controller parses and runs the program correctly.
 */
public class ImageControllerTest {

  private Image beforeImage;
  private Readable in;
  private Appendable appendable;
  private ImageProcessorView view;
  private StoredImages store;
  private ImageProcessorController controller1;


  @Before
  public void setUp() {
    Color[][] pixels = new Color[3][3];
    pixels[0][0] = new Color(128, 16, 216);
    pixels[0][1] = new Color(114, 17, 219);
    pixels[0][2] = new Color(105, 18, 222);
    pixels[1][0] = new Color(114, 17, 219);
    pixels[1][1] = new Color(97, 18, 224);
    pixels[1][2] = new Color(84, 18, 227);
    pixels[2][0] = new Color(105, 18, 222);
    pixels[2][1] = new Color(84, 18, 227);
    pixels[2][2] = new Color(61, 18, 231);

    this.beforeImage = new ImageImpl(pixels);

    String userCommandEx1 = "load res/ExampleImage.ppm ExampleImage" + System.lineSeparator()
        + "brighten 10 ExampleImage BrightenedImage";
    InputStream targetStreamEx1 = new ByteArrayInputStream(userCommandEx1.getBytes());
    this.in = new InputStreamReader(targetStreamEx1);
    this.appendable = new StringBuilder();
    this.view = new ImageProcessorViewImpl(appendable);
    this.store = new StoredImagesImpl();
    this.store.add("example", this.beforeImage, true);

  }

  @Test
  public void testInvalidConstructors() {
    try {
      new ImageProcessorControllerImpl(null, this.view, this.store);
      fail("Should throw exception with null input");
    } catch (IllegalArgumentException e) {
      assertEquals("Input, view, and store cannot be null", e.getMessage());
    }

    try {
      new ImageProcessorControllerImpl(this.in, null, this.store);
      fail("Should throw exception with null view");
    } catch (IllegalArgumentException e) {
      assertEquals("Input, view, and store cannot be null", e.getMessage());
    }

    try {
      new ImageProcessorControllerImpl(this.in, this.view, null);
      fail("Should throw exception with null image store");
    } catch (IllegalArgumentException e) {
      assertEquals("Input, view, and store cannot be null", e.getMessage());
    }
  }

  //checks if the inputs are being parsed correctly
  @Test
  public void testControllerInput() {
    StringBuilder log = new StringBuilder();
    StoredImages mockStore = new MockStoredImages(log);
    this.controller1 = new ImageProcessorControllerImpl(this.in, this.view, mockStore);

    this.controller1.run();
    assertEquals("The parsed string for a new file's name: exampleimage" + System.lineSeparator()
        + "The parsed string for the name of the file to modify: exampleimage"
        + System.lineSeparator() + "The parsed string for a new file's name: brightenedimage"
        + System.lineSeparator(), log.toString());
  }

  @Test
  public void testScript() {
    String userCommandEx2 = "loadr res/ExampleImage.ppm ExampleImage" + System.lineSeparator();
    InputStream targetStreamEx2 = new ByteArrayInputStream(userCommandEx2.getBytes());
    Readable in2 = new InputStreamReader(targetStreamEx2);
    this.appendable = new StringBuilder();
    this.view = new ImageProcessorViewImpl(this.appendable);
    this.controller1 = new ImageProcessorControllerImpl(in2, this.view, this.store);
    this.controller1.run();

    assertTrue(this.appendable.toString().contains("Error: Invalid command, please try again"));
  }

  @Test
  public void testInvalidPath() {
    String userCommandEx3 = "load res.ppm ExampleImage" + System.lineSeparator();
    InputStream targetStreamEx3 = new ByteArrayInputStream(userCommandEx3.getBytes());
    Readable in3 = new InputStreamReader(targetStreamEx3);
    this.appendable = new StringBuilder();
    this.view = new ImageProcessorViewImpl(this.appendable);
    this.controller1 = new ImageProcessorControllerImpl(in3, this.view, this.store);

    this.controller1.run();
    assertTrue(this.appendable.toString().contains("File \"res.ppm\" not found"));
  }


  @Test
  public void testPPMHandlerProcess() {
    String filePath = "res/ExampleImage.ppm"; // relative to the project root
    ImageFileHandler ppmHandler = new ImagePPMHandler();

    assertArrayEquals(this.beforeImage.getPixels(), ppmHandler.process(filePath).getPixels());
  }

  @Test
  public void testPPMHandlerExport() {
    String filePath = "res/ExampleImage.ppm";
    String filePath2 = "res/exImage.ppm";

    ImageFileHandler ppmHandler1 = new ImagePPMHandler();
    Image processedImage = ppmHandler1.process(filePath);

    ImageFileHandler ppmHandler = new ImagePPMHandler();

    ppmHandler.export(this.beforeImage, filePath2);
    Image exportedImage = ppmHandler.process(filePath2);

    //Checks if the ppmHandler saved the image correctly under the new filePath.
    assertArrayEquals(processedImage.getPixels(), exportedImage.getPixels());
  }

  @Test
  public void testBrightnessCmd() {
    ImageTransformation macro = new Brightness(10);
    Image macroImage = macro.transform(this.beforeImage);

    ImageProcessorCmd command = new BrightnessCmd(this.view, this.store, 10, "example",
        "example-bright");
    command.execute();
    Image commandImage = this.store.retrieve("example-bright");

    assertArrayEquals(macroImage.getPixels(), commandImage.getPixels());
  }

  @Test
  public void testHorizontalFlip() {
    ImageTransformation macro = new HorizontalFlip();
    Image macroImage = macro.transform(this.beforeImage);

    ImageProcessorCmd command = new HorizontalFlipCmd(this.view, this.store, "example",
        "example-horizontal");
    command.execute();
    Image commandImage = this.store.retrieve("example-horizontal");

    assertArrayEquals(macroImage.getPixels(), commandImage.getPixels());
  }

  @Test
  public void testVerticalFlip() {
    ImageTransformation macro = new VerticalFlip();
    Image macroImage = macro.transform(this.beforeImage);

    ImageProcessorCmd command = new VerticalFlipCmd(this.view, this.store, "example",
        "example-vertical");
    command.execute();
    Image commandImage = this.store.retrieve("example-vertical");

    assertArrayEquals(macroImage.getPixels(), commandImage.getPixels());
  }

  @Test
  public void testVisualizeRed() {
    ImageTransformation macro = new Visualize(Channel.Red);
    Image macroImage = macro.transform(this.beforeImage);

    ImageProcessorCmd command = new VisualizeCmd(this.view, this.store, Channel.Red, "example",
        "example-red");
    command.execute();
    Image commandImage = this.store.retrieve("example-red");

    assertArrayEquals(macroImage.getPixels(), commandImage.getPixels());
  }

  @Test
  public void testVisualizeGreen() {
    ImageTransformation macro = new Visualize(Channel.Green);
    Image macroImage = macro.transform(this.beforeImage);

    ImageProcessorCmd command = new VisualizeCmd(this.view, this.store, Channel.Green, "example",
        "example-green");
    command.execute();
    Image commandImage = this.store.retrieve("example-green");

    assertArrayEquals(macroImage.getPixels(), commandImage.getPixels());
  }

  @Test
  public void testVisualizeBlue() {
    ImageTransformation macro = new Visualize(Channel.Blue);
    Image macroImage = macro.transform(this.beforeImage);

    ImageProcessorCmd command = new VisualizeCmd(this.view, this.store, Channel.Blue, "example",
        "example-blue");
    command.execute();
    Image commandImage = this.store.retrieve("example-blue");

    assertArrayEquals(macroImage.getPixels(), commandImage.getPixels());
  }

  @Test
  public void testVisualizeLuma() {
    ImageTransformation macro = new Visualize(Channel.Luma);
    Image macroImage = macro.transform(this.beforeImage);

    ImageProcessorCmd command = new VisualizeCmd(this.view, this.store, Channel.Luma, "example",
        "example-luma");
    command.execute();
    Image commandImage = this.store.retrieve("example-luma");

    assertArrayEquals(macroImage.getPixels(), commandImage.getPixels());
  }

  @Test
  public void testVisualizeValue() {
    ImageTransformation macro = new Visualize(Channel.Value);
    Image macroImage = macro.transform(this.beforeImage);

    ImageProcessorCmd command = new VisualizeCmd(this.view, this.store, Channel.Value, "example",
        "example-value");
    command.execute();
    Image commandImage = this.store.retrieve("example-value");

    assertArrayEquals(macroImage.getPixels(), commandImage.getPixels());
  }

  @Test
  public void testVisualizeIntensity() {
    ImageTransformation macro = new Visualize(Channel.Intensity);
    Image macroImage = macro.transform(this.beforeImage);

    ImageProcessorCmd command = new VisualizeCmd(this.view, this.store, Channel.Intensity,
        "example", "example-intensity");
    command.execute();
    Image commandImage = this.store.retrieve("example-intensity");

    assertArrayEquals(macroImage.getPixels(), commandImage.getPixels());
  }

  @Test
  public void testBlur() {
    ImageTransformation macro = new Blur();
    Image macroImage = macro.transform(this.beforeImage);

    ImageProcessorCmd command = new FilterCmd(this.view, this.store, FilterType.Blur, "example",
        "example-blur");
    command.execute();
    Image commandImage = this.store.retrieve("example-blur");

    assertArrayEquals(macroImage.getPixels(), commandImage.getPixels());
  }

  @Test
  public void testSharpen() {
    ImageTransformation macro = new Sharpen();
    Image macroImage = macro.transform(this.beforeImage);

    ImageProcessorCmd command = new FilterCmd(this.view, this.store, FilterType.Sharpen, "example",
        "example-sharp");
    command.execute();
    Image commandImage = this.store.retrieve("example-sharp");

    assertArrayEquals(macroImage.getPixels(), commandImage.getPixels());
  }

  @Test
  public void testGreyscale() {
    ImageTransformation macro = new Greyscale();
    Image macroImage = macro.transform(this.beforeImage);

    ImageProcessorCmd command = new FilterCmd(this.view, this.store, FilterType.Greyscale,
        "example", "example-greyscale");
    command.execute();
    Image commandImage = this.store.retrieve("example-greyscale");

    assertArrayEquals(macroImage.getPixels(), commandImage.getPixels());
  }

  @Test
  public void testSepia() {
    ImageTransformation macro = new Sepia();
    Image macroImage = macro.transform(this.beforeImage);

    ImageProcessorCmd command = new FilterCmd(this.view, this.store, FilterType.Sepia, "example",
        "example-sepia");
    command.execute();
    Image commandImage = this.store.retrieve("example-sepia");

    assertArrayEquals(macroImage.getPixels(), commandImage.getPixels());
  }

  @Test
  public void testLoadPNG() {
    String filePath = "res/ExampleImage2.png"; // relative to the project root

    ImageProcessorCmd command = new LoadCmd(this.view, this.store, filePath, "ExampleImage2");
    command.execute();
    Image commandImage = this.store.retrieve("ExampleImage2");

    ImageFileHandler pngHandler = new ImageIOHandler();
    assertArrayEquals(pngHandler.process(filePath).getPixels(), commandImage.getPixels());
  }

  @Test
  public void testSavePNG() {
    String filePath = "res/ExampleImage2.png"; // relative to the project root
    String filePath2 = "res/exImage2.png"; // relative to the project root

    ImageProcessorCmd command1 = new LoadCmd(this.view, this.store, filePath, "exImage2");
    command1.execute();
    ImageProcessorCmd command2 = new SaveCmd(this.view, this.store, filePath2, "exImage2");
    command2.execute();

    assertTrue(
        this.appendable.toString().contains("\" saved successfully as \"" + filePath2 + "\""));
  }

  @Test
  public void testSaveDiffFile() {
    String filePath1 = "res/ExampleImage2.png"; // relative to the project root
    String filePath2 = "res/ExampleImage2.ppm"; // relative to the project root

    ImageProcessorCmd command1 = new LoadCmd(this.view, this.store, filePath1, "exImage2");
    command1.execute();
    ImageProcessorCmd command2 = new SaveCmd(this.view, this.store, filePath2, "exImage2");
    command2.execute();

    assertTrue(this.appendable.toString().contains("ExampleImage2.ppm"));

  }


  @Test
  public void testIOHandlerProcess() {

    String filePath = "res/ExampleImage2.png"; // relative to the project root

    ImageProcessorCmd command1 = new LoadCmd(this.view, this.store, filePath, "exImage2");
    command1.execute();
    ImageProcessorCmd command2 = new SaveCmd(this.view, this.store, filePath, "exImage2");
    command2.execute();

    ImageFileHandler ioHandler = new ImageIOHandler();

    Image retrievedImage = this.store.retrieve("exImage2");

    assertArrayEquals(retrievedImage.getPixels(), ioHandler.process(filePath).getPixels());
  }

  @Test
  public void testIOHandlerExport() {
    String filePath = "res/ExampleImage2.png";
    String filePath2 = "res/exImage2.png";

    ImageFileHandler ioHandler = new ImageIOHandler();
    Image processedImage = ioHandler.process(filePath);

    ImageFileHandler ioHandler2 = new ImageIOHandler();

    ioHandler2.export(processedImage, filePath2);
    Image exportedImage = ioHandler2.process(filePath2);

    //Checks if the ppmHandler saved the image correctly under the new filePath.
    assertArrayEquals(processedImage.getPixels(), exportedImage.getPixels());
  }

  @Test
  public void testScriptFile() {
    try {
      String filePath = "res/test-script.txt";
      this.appendable = new StringBuilder();
      this.view = new ImageProcessorViewImpl(appendable);
      InputStream targetStreamEx1 = new FileInputStream(filePath);
      this.in = new InputStreamReader(targetStreamEx1);
      StringBuilder log = new StringBuilder();
      StoredImages mockStore = new MockStoredImages(log);
      this.controller1 = new ImageProcessorControllerImpl(this.in, this.view, mockStore);
      this.controller1.run();
      assertEquals(
          "The parsed string for the name of the file to modify: example2" + System.lineSeparator()
              + "The parsed string for the name of the file to modify: example2"
              + System.lineSeparator()
              + "The parsed string for the name of the file to modify: example2-blur"
              + System.lineSeparator()
              + "The parsed string for the name of the file to modify: example2-sepia"
              + System.lineSeparator(), log.toString());

    } catch (FileNotFoundException ex) {
      try {
        this.appendable.append(ex.getMessage());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

  }


}
