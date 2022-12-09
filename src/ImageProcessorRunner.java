import controller.ImageProcessorController;
import controller.ImageProcessorControllerImpl;
import controller.ImageProcessorGuiController;
import controller.ImageProcessorGuiControllerImpl;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import model.StoredImages;
import model.StoredImagesImpl;
import view.ImageProcessorGui;
import view.ImageProcessorGuiImpl;
import view.ImageProcessorView;
import view.ImageProcessorViewImpl;

/**
 * Runs the image processor in the terminal for the user based on the given arguments.
 *
 * <p>
 * If no arguments are given, the image processor is run in the GUI. Otherwise, the image processor
 * is run in the terminal. More information is available in the README file.
 * </p>
 */
public final class ImageProcessorRunner {

  /**
   * Initiates a new image processor instance for the user.
   *
   * @param args the arguments taken in by the main method
   */
  public static void main(String[] args) {
    StoredImages store = new StoredImagesImpl();

    if (args.length == 0) { // no arguments -> GUI mode
      // Initialize the GUI mode view and controller
      ImageProcessorGui view = new ImageProcessorGuiImpl();
      ImageProcessorGuiController controller = new ImageProcessorGuiControllerImpl(store);
      // Set the view for the GUI controller
      controller.setView(view);
    } else if (args.length <= 2) { // 1 or 2 arguments -> terminal mode
      Readable input;
      if (args[0].equals("-text") && args.length == 1) { // -text flag -> read from typed input
        input = new InputStreamReader(System.in);
      } else if (args[0].equals("-script") && args.length == 2) { // -script flag -> read from file
        try {
          input = new BufferedReader(new FileReader(args[1]));
        } catch (FileNotFoundException e) {
          System.out.println("Log file not found.");
          return;
        }
      } else { // > 2 arguments -> invalid
        System.out.println("Invalid arguments provided.");
        return;
      }
      // Initialize the terminal mode view given the output stream appendable
      ImageProcessorView view = new ImageProcessorViewImpl(System.out);
      // Initialize the terminal mode controller with the input, view, and model
      ImageProcessorController controller = new ImageProcessorControllerImpl(input, view, store);
      // Run the terminal mode controller
      controller.run();
    } else {
      System.out.println("Invalid arguments provided.");
    }
  }
}
