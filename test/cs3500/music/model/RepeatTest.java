package cs3500.music.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for the Repeat class.
 */
public class RepeatTest {
  Repeat one;
  Repeat two;
  Repeat three;

  private void init() {
    one = new Repeat(0, 5);
    two = new Repeat(1, 5, 10);
    three = new Repeat(5, 10, 15, 20);
  }

  @Test
  public void testChangeBeat() {
    init();
    one.setNumberOfRepetitions(1);
    assertEquals(one.changeBeat(3), 3);
    two.setNumberOfRepetitions(5);
    assertEquals(two.changeBeat(11), 11);
    three.setNumberOfRepetitions(5);
    assertEquals(three.changeBeat(11), 11);

    three.setNumberOfRepetitions(1);
    assertEquals(three.changeBeat(10), 15);
    assertEquals(three.changeBeat(20), 5);
    assertEquals(three.changeBeat(10), 20);
  }

  @Test
  public void testEarliestBeat() {
    init();
    assertEquals(one.getEarliestBeat(), 0);
    assertEquals(two.getEarliestBeat(), 1);
    assertEquals(three.getEarliestBeat(), 5);
  }

  @Test
  public void testLatestBeat() {
    init();
    assertEquals(one.getLatestBeat(), 5);
    assertEquals(two.getLatestBeat(), 10);
    assertEquals(three.getLatestBeat(), 20);
  }

  @Test
  public void testGetStartBeat() {
    init();
    assertEquals(one.getStartBeat(), 0);
    assertEquals(two.getStartBeat(), 1);
    assertEquals(three.getStartBeat(), 5);

    // after a repetition
    two.setNumberOfRepetitions(1);
    three.setNumberOfRepetitions(1);

    assertEquals(two.getStartBeat(), 10);
    assertEquals(three.getStartBeat(), 15);
  }

  @Test (expected = NullPointerException.class)
  public void testExceptionStart() {
    init();
    one.setNumberOfRepetitions(1);
    one.getStartBeat();
  }

  @Test
  public void testGetEndBeat() {
    init();
    assertEquals(one.getEndBeat(), 5);
    assertEquals(two.getEndBeat(), 10);
    assertEquals(three.getEndBeat(), 15);

    // after a repetition
    two.setNumberOfRepetitions(1);
    three.setNumberOfRepetitions(1);

    assertEquals(two.getEndBeat(), 5);
    assertEquals(three.getEndBeat(), 10);

    three.setNumberOfRepetitions(2);

    assertEquals(three.getEndBeat(), 20);
  }

  @Test (expected = NullPointerException.class)
  public void testExceptionEnd() {
    init();
    one.setNumberOfRepetitions(1);
    one.getEndBeat();
  }

}
