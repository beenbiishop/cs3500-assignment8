package model.transformations;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.Image;
import model.ImageImpl;
import model.ImageTransformation;

/**
 * An implementation of the {@link ImageTransformation} interface representing a macro used to apply
 * a mosaic filter to an image.
 */
public class Mosaic implements ImageTransformation {

  private final int numSeeds;

  /**
   * Constructs a new mosaic transformation macro object that transforms an image to be mosaic with
   * the given number of seeds.
   *
   * @param numSeeds the number of seeds to mosaic transformed images
   * @throws IllegalArgumentException if the given amount is not greater than 0
   */
  public Mosaic(int numSeeds) throws IllegalArgumentException {
    if (numSeeds < 1) {
      throw new IllegalArgumentException("The seed amount must be greater than 0");
    }
    this.numSeeds = numSeeds;
  }

  @Override
  public Image transform(Image image) {
    int width = image.getWidth();
    int height = image.getHeight();
    Color[][] oldPixels = image.getPixels();
    Color[][] newPixels = new Color[image.getHeight()][image.getWidth()];

    // generate seeds
    Random random = new Random();
    List<int[]> seeds = new ArrayList<>();
    for (int i = 0; i < this.numSeeds; i++) {
      int[] seed = new int[2];
      seed[0] = random.nextInt(height);
      seed[1] = random.nextInt(width);
      seeds.add(seed);
    }

    // set each pixel to the color of the closest seed
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int[] closestSeed = seeds.get(0);
        int closestSeedDist = Integer.MAX_VALUE;
        for (int[] seed : seeds) {
          int yDist = Math.abs(seed[0] - i);
          int xDist = Math.abs(seed[1] - j);
          int seedDist = yDist + xDist;
          if (seedDist < closestSeedDist) {
            closestSeed = seed;
            closestSeedDist = seedDist;
          }
        }
        newPixels[i][j] = oldPixels[closestSeed[0]][closestSeed[1]];
      }
    }

    return new ImageImpl(newPixels);
  }
}
