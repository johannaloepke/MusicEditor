package cs3500.music.controller;

import org.junit.Test;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;


import static junit.framework.TestCase.assertEquals;

/**
 * Tests methods in the MouseEvent class;
 */
public class MusicControllerTest {

  /**
   * Tests that the mouseEvent methods work as expected.
   */
  @Test
  public void testAddToKeyTypedMap() {
    final int[] I = {0};
    final int[] J = {1};
    final int[] K = {2};
    MouseHandler handler = new MouseHandler(new Runnable() {
      @Override
      public void run() {
        I[0]++;
      }
    }, new Runnable() {
      @Override
      public void run() {
        J[0]++;
      }
    }, new Runnable() {
      @Override
      public void run() {
        K[0] ++;
      }
    });

    handler.mouseClicked(new MouseEvent(new JPanel(), 0, 0, 0, 0, 0, 0, false));
    assertEquals(I[0], 1);

    handler.mouseReleased(new MouseEvent(new JPanel(), 0, 0, 0, 0, 0, 0, false));
    assertEquals(J[0], 2);

    handler.mousePressed(new MouseEvent(new JPanel(), 0, 0, 0, 0, 0, 0, false));
    assertEquals(K[0], 3);

    assertEquals(handler.getPoint(), new Point(0, 0));
  }
}
