package cs3500.music.view;

import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * A dummy view that simply draws a string .
 */
public class ConcreteGuiViewPanel extends JPanel {

  @Override
  public void paintComponent(Graphics g) {
    // Handle the default painting
    super.paintComponent(g);
    // Look for more documentation about the Graphics class,
    // and methods on it that may be useful
    g.drawString("Hello World", 25, 25);
  }
}

