package view.panels;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import view.ImageProcessorGui;

/**
 * Represents the message panel that is a part of the {@link ImageProcessorGui} view.
 */
public class MessagePanel extends JPanel {

  private final JLabel messageLabel;

  /**
   * Constructs a new message panel.
   */
  public MessagePanel() {
    super(new BorderLayout());
    this.messageLabel = new JLabel("");
    this.messageLabel.setHorizontalAlignment(JLabel.LEFT);
    this.messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.add(this.messageLabel, BorderLayout.CENTER);
    this.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10),
        BorderFactory.createTitledBorder("Messages")));
  }

  /**
   * Updates the message displayed in the message panel.
   *
   * @param message the message to be displayed
   */
  public void updateMessage(String message) {
    if (message == null || message.length() == 0) {
      throw new IllegalArgumentException("Message cannot be null or empty");
    }
    this.messageLabel.setText(message);
  }

}
