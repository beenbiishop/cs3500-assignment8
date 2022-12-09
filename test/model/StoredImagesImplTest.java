package model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link StoredImagesImpl} class and its methods.
 */
public class StoredImagesImplTest {

  private Color[][] pixels1;
  private Color[][] pixels2;
  private Image image1;
  private Image image2;
  private StoredImages store1;
  private StoredImages store2;

  @Before
  public void setUp() {
    this.pixels1 = new Color[3][2];
    this.pixels2 = new Color[12][27];

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
        this.pixels1[i][j] = pixel;
      }
    }

    for (int i = 0; i < 12; i++) {
      for (int j = 0; j < 27; j++) {
        this.pixels2[i][j] = Color.BLACK;
      }
    }

    this.image1 = new ImageImpl(this.pixels1);
    this.image2 = new ImageImpl(this.pixels2);

    this.store1 = new StoredImagesImpl();
    this.store1.add("image1", this.image1, true);
    this.store1.add("image2", this.image2, true);

    this.store2 = new StoredImagesImpl();
  }

  @Test
  public void testAddForced() {
    this.store2.add("image1", this.image1, true);
    this.store2.add("image1", this.image2, true);
    Image retrieved = this.store2.retrieve("image1");
    assertArrayEquals(this.image2.getPixels(), retrieved.getPixels());
  }

  @Test
  public void testAddNotForced() {
    this.store2.add("image2", this.image2, false);
    try {
      this.store2.add("image2", this.image1, false);
      fail("Exception not thrown when adding an image with a duplicate file name without force");
    } catch (IllegalArgumentException e) {
      assertEquals("An image with that file name already exists", e.getMessage());
    }
    assertArrayEquals(this.image2.getPixels(), this.store2.retrieve("image2").getPixels());
  }

  @Test
  public void testAddMutable() {
    this.store2.add("image1", this.image1, true);
    this.image1 = new ImageImpl(this.pixels2);
    Image retrieved = this.store2.retrieve("image1");
    assertArrayEquals(this.pixels1, retrieved.getPixels());
  }

  @Test
  public void testRemove() {
    this.store1.remove("image1");
    this.store1.remove("image2");

    try {
      this.store1.retrieve("image1");
      fail("Exception not thrown after attempting to retrieve an image that has been removed");
    } catch (IllegalArgumentException e) {
      assertEquals("No image with the file name \"image1\" has been loaded", e.getMessage());
    }

    try {
      this.store1.retrieve("image2");
      fail("Exception not thrown after attempting to retrieve an image that has been removed");
    } catch (IllegalArgumentException e) {
      assertEquals("No image with the file name \"image2\" has been loaded", e.getMessage());
    }
  }

  @Test
  public void testExists() {
    assertTrue(this.store1.exists("image1"));
    assertTrue(this.store1.exists("image2"));
    assertFalse(this.store1.exists("image3"));
    assertFalse(this.store2.exists("image1"));
  }

  @Test
  public void testRetrieve() {
    this.store2.add("image1", this.image1, true);
    this.store2.add("image2", this.image2, true);
    Image retrieved1 = this.store2.retrieve("image1");
    Image retrieved2 = this.store2.retrieve("image2");
    assertEquals(this.image1.getHeight(), retrieved1.getHeight());
    assertEquals(this.image1.getWidth(), retrieved1.getWidth());
    assertArrayEquals(this.image1.getPixels(), retrieved1.getPixels());
    assertArrayEquals(this.image2.getPixels(), retrieved2.getPixels());
  }

  @Test
  public void testRetrieveNoFile() {
    try {
      this.store1.retrieve("fakeimage");
      fail("Exception not thrown when retrieving an image that does not exist");
    } catch (IllegalArgumentException e) {
      assertEquals("No image with the file name \"fakeimage\"" + " has been loaded",
          e.getMessage());
    }
  }
}