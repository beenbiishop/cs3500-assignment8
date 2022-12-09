package controller;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import model.Image;
import model.ImageImpl;

/**
 * Implements the {@code ImageFileHandler} interface for converting PPM image files into
 * {@code Image} objects, and vice versa.
 */
public class ImagePPMHandler implements ImageFileHandler {

  @Override
  public Image process(String path) throws IllegalArgumentException {
    Scanner scan;

    try {
      scan = new Scanner(new FileInputStream(path));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File \"" + path + "\" not found");
    }

    StringBuilder sb = new StringBuilder();

    while (scan.hasNextLine()) {
      String s = scan.nextLine();
      if (s.charAt(0) != '#') {
        sb.append(s);
        sb.append(System.lineSeparator());
      }
    }

    scan.close();
    scan = new Scanner(sb.toString());

    String token;

    if (scan.hasNext()) {
      token = scan.next();
    } else {
      throw new IllegalArgumentException("Invalid PPM file: file is empty");
    }

    if (!token.equals("P3")) {
      throw new IllegalArgumentException("Invalid PPM file: plain RAW file should begin with P3");
    }
    int width = scan.nextInt();
    int height = scan.nextInt();
    int maxValue = scan.nextInt();

    if (maxValue != 255) {
      throw new IllegalArgumentException(
          "Invalid PPM file: maximum value of a color in this file should be 255");
    }

    Color[][] pixels = new Color[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = scan.nextInt();
        int g = scan.nextInt();
        int b = scan.nextInt();
        pixels[i][j] = new Color(r, g, b);
      }
    }

    scan.close();
    return new ImageImpl(pixels);
  }

  @Override
  public void export(Image image, String path) throws IllegalArgumentException {
    BufferedWriter writer;

    if (image == null || path == null) {
      throw new IllegalArgumentException("Image and path cannot be null");
    }

    try {
      writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Export file location \"" + path + "\" is invalid");
    }

    try {
      writer.write("P3" + System.lineSeparator());
      writer.write("# Created by Ben and Smita's Image Processor" + System.lineSeparator());
      writer.write(image.getWidth() + " " + image.getHeight() + System.lineSeparator());
      writer.write("255" + System.lineSeparator());
      Color[][] pixels = image.getPixels();
      for (int i = 0; i < image.getHeight(); i++) {
        for (int j = 0; j < image.getWidth(); j++) {
          Color pixel = pixels[i][j];
          writer.write(pixel.getRed() + System.lineSeparator());
          writer.write(pixel.getGreen() + System.lineSeparator());
          writer.write(pixel.getBlue() + System.lineSeparator());
        }
      }
      writer.flush();
      writer.close();
    } catch (Exception e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }
}

