package cs3500.music.controller;

import org.junit.Test;

import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests the KeyHandler methods.
 */
public class KeyHandlerTest {
  /**
   * Tests that the keyPressed event works as expected.
   */
  @Test
  public void testAddToKeyPressedMap() {
    final int[] I = {0};
    KeyHandler handler = new KeyHandler();
    handler.addToKeyPressedMap(0, new Runnable() {
      @Override
      public void run() {
        I[0]++;
      }
    });

    handler.keyPressed(new KeyEvent(new JPanel(), 0, 0, 0, 0));
    assertEquals(I[0], 1);
  }

  /**
   * Tests that the keyHeld event works as expected.
   */
  @Test
  public void testAddToKeyTypedMap() {
    final int[] I = {0};
    KeyHandler handler = new KeyHandler();
    handler.addToKeyTypedMap(0, new Runnable() {
      @Override
      public void run() {
        I[0]++;
      }
    });

    handler.keyTyped(new KeyEvent(new JPanel(), 0, 0, 0, 0));
    assertEquals(I[0], 1);
  }

  /**
   * Tests that the keyReleased event works as expected.
   */
  @Test
  public void testAddToKeyReleasedMap() {
    final int[] I = {0};
    KeyHandler handler = new KeyHandler();
    handler.addToKeyReleasedMap(0, new Runnable() {
      @Override
      public void run() {
        I[0]++;
      }
    });

    handler.keyReleased(new KeyEvent(new JPanel(), 0, 0, 0, 0));
    assertEquals(I[0], 1);
  }
}
