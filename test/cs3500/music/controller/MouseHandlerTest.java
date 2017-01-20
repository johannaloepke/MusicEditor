package cs3500.music.controller;

import org.junit.Test;

import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests methods in the MouseHandler class.
 */
public class MouseHandlerTest {

  /**
   * Tests that the mousePressed event works as expected.
   */
  @Test
  public void testMousePressed() {
    Runnable pressedMethod;
    final int[] I = {0};
    pressedMethod = new Runnable() {
      @Override
      public void run() {
        I[0]++;
      }
    };
    MouseHandler handler = new MouseHandler(pressedMethod, pressedMethod, pressedMethod);
    handler.mousePressed((new MouseEvent(new JPanel(), 0, 0, 0, 0, 0, 0, false)));
    assertEquals(I[0], 1);
  }

  /**
   * Tests that the mouseClicked event works as expected.
   */
  @Test
  public void testMouseClicked() {
    Runnable clickedMethod;
    final int[] I = {0};
    clickedMethod = new Runnable() {
      @Override
      public void run() {
        I[0]++;
      }
    };
    MouseHandler handler = new MouseHandler(clickedMethod, clickedMethod, clickedMethod);
    handler.mousePressed((new MouseEvent(new JPanel(), 0, 0, 0, 0, 0, 0, false)));
    assertEquals(I[0], 1);
  }

  /**
   * Tests that the mouseReleased event works as expected.
   */
  @Test
  public void testMouseReleased() {
    Runnable releasedMethod;
    final int[] I = {0};
    releasedMethod = new Runnable() {
      @Override
      public void run() {
        I[0]++;
      }
    };
    MouseHandler handler = new MouseHandler(releasedMethod, releasedMethod, releasedMethod);
    handler.mousePressed((new MouseEvent(new JPanel(), 0, 0, 0, 0, 0, 0, false)));
    assertEquals(I[0], 1);
  }
}
