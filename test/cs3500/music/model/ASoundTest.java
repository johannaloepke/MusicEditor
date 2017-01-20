package cs3500.music.model;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests ASound methods.
 */
public class ASoundTest {

  /**
   * Tests that lowerThan works as expected.
   */
  @Test
  public void testLowerThanNote() {
    Note lowerNote = new Note(Pitch.E, 5, 1);
    Note higherNote = new Note(Pitch.A, 6, 1);
    Note middleNote = new Note(Pitch.G, 5, 1);

    assertEquals(lowerNote.lowerThan(higherNote), true);
    assertEquals(lowerNote.lowerThan(middleNote), true);
    assertEquals(middleNote.lowerThan(higherNote), true);
  }

  /**
   * Tests that getLowestNote and getHighestNote works as expected with chords.
   */
  @Test
  public void testGetLowestNoteHighestNote() {
    Note lowerNote = new Note(Pitch.E, 5, 2);
    Note higherNote = new Note(Pitch.A, 6, 1);
    Note middleNote = new Note(Pitch.G, 5, 2);

    Chord chord = new Chord(1, higherNote, lowerNote, middleNote);

    assertEquals(chord.getLowestNote(), lowerNote);
    assertEquals(chord.getHighestNote(), higherNote);
  }

  /**
   * Tests that getLowestNote and getHighestNote works as expected with chords.
   */
  @Test
  public void testGetLowestNoteHighestNote2() {
    Note higherNote = new Note(Pitch.A, 5, 2);
    Note lowerNote = new Note(Pitch.D_SHARP, 5, 1);

    Chord chord = new Chord(1, higherNote, lowerNote);

    assertEquals(chord.getLowestNote(), lowerNote);
    assertEquals(chord.getHighestNote(), higherNote);
  }
}
