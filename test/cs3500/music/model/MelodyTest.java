package cs3500.music.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the Melody class.
 */
public class MelodyTest {

  // tests the merge method
  @Test
  public void testMerge() throws Exception {
    Melody melody = new Melody();
    Melody melody2 = new Melody();
    Melody melody3 = new Melody();
    melody.addNote(new Note(Pitch.C, 5, 5), 0);
    melody.addNote(new Chord(3, new Note(Pitch.A, 3, 2), new Note(Pitch.G, 4, 1)), 1);
    melody2.addNote(new Note(Pitch.C, 5, 5), 0);

    melody3.addNote(new Note(Pitch.C, 5, 5), 0);
    melody3.addNote(new Note(Pitch.C, 5, 5), 1);
    melody3.addNote(new Chord(3, new Note(Pitch.A, 3, 2), new Note(Pitch.G, 4, 1)), 2);

    melody.merge(1, melody2);
    assertEquals(melody, melody3);
  }

  // tests the addNote method
  @Test
  public void testAddNote() throws Exception {
    Melody melody = new Melody();
    melody.addNote(new Note(Pitch.C, 5, 5), 0);
    assertEquals(melody.getNoteAtBeat(0), new Note(Pitch.C, 5, 5));
  }

  // tests the getNote method
  @Test
  public void testGetNote() throws Exception {
    Melody melody = new Melody();
    melody.addNote(new Note(Pitch.F, 5, 6), 0);
    melody.addNote(new Chord(3, new Note(Pitch.A, 3, 2), new Note(Pitch.G, 4, 1)), 1);
    assertEquals(melody.getNote(0), new Note(Pitch.F, 5, 6));
  }

  // teste the exception in getNote
  @Test (expected = IllegalArgumentException.class)
  public void testGetNoteFail() throws Exception {
    Melody melody = new Melody();
    melody.getNote(0);
  }

  // tests the getNoteAtBeat method
  @Test
  public void testGetNoteAtBeat() throws Exception {
    Melody melody = new Melody();
    melody.addNote(new Note(Pitch.F, 5, 5), 0);
    melody.addNote(new Note(Pitch.A, 5, 5), 1);
    assertEquals(melody.getNoteAtBeat(5), new Note(Pitch.A, 5, 5));
  }

  // tests the exception in getNoteAtBeat
  @Test (expected = IllegalArgumentException.class)
  public void testGetNoteAtBeatFail() throws Exception {
    Melody melody = new Melody();
    melody.getNoteAtBeat(0);
  }

  // tests the swapNote method
  @Test
  public void testSwapNote() throws Exception {
    Melody melody = new Melody();
    Melody melody2 = new Melody();
    melody.addNote(new Note(Pitch.C, 5, 5), 0);
    melody.addNote(new Note(Pitch.A, 6, 5), 1);

    melody2.addNote(new Note(Pitch.G, 2, 2), 0);
    melody2.addNote(new Note(Pitch.A, 6, 5), 1);

    melody.swapNote(new Note(Pitch.G, 2, 2), 0);
    assertEquals(melody, melody2);
  }

  // tests the noteStartsAtExactBeat method
  @Test
  public void testNoteStartsAtExactBeat() throws Exception {
    Melody melody = new Melody();
    melody.addNote(new Note(Pitch.F, 5, 6), 0);
    assertFalse(melody.noteStartsAtExactBeat(1));
    assertTrue(melody.noteStartsAtExactBeat(0));
  }

  // tests the changeInstrument method
  @Test
  public void testChangeInstrument() throws Exception {
    Melody melody = new Melody();
    melody.changeInstrument(5);
    assertEquals(melody.getInstrument(), 5);
  }

  // tests the hashCode method
  @Test
  public void testHashCode() throws Exception {
    Melody melody = new Melody();
    Melody melody2 = new Melody();
    melody.addNote(new Note(Pitch.C, 5, 5), 0);
    melody2.addNote(new Note(Pitch.C, 5, 5), 0);
    assertTrue(melody2.hashCode() == melody.hashCode());
  }

  // tests the equals method
  @Test
  public void testEquals() throws Exception {
    Melody melody = new Melody();
    Melody melody2 = new Melody();
    melody.addNote(new Note(Pitch.C, 5, 5), 0);
    melody2.addNote(new Note(Pitch.C, 5, 5), 0);
    assertTrue(melody2.equals(melody));
  }

  // tests the numOfBeats method
  @Test
  public void testNumOfBeats() throws Exception {
    Melody melody = new Melody();
    assertEquals(melody.numOfBeats(), 0);
  }

  // tests the numOfBeatsElapsedAt method
  @Test
  public void testNumOfBeatsElapsedAt() throws Exception {
    Melody melody = new Melody();
    melody.addNote(new Note(Pitch.C, 5, 5), 0);
    melody.addNote(new Note(Pitch.A, 6, 5), 1);
    assertEquals(melody.numOfBeatsElapsedAt(1), 5);
  }

  @Test
  public void testAddNoteToBeat() {
    Melody melody = new Melody(20, new Rest(500));
    melody.addNoteToBeat(new Note(Pitch.C, 4, 10), 10);
    melody.addNoteToBeat(new Chord(5, new Note(Pitch.C, 4, 10), new Note(Pitch.A, 4, 10)), 5);
    melody.addNoteToBeat(new Note(Pitch.G, 4, 1), 499);

    Melody endMelody = new Melody(20, new Rest(5), new Chord(5, new Note(Pitch.C, 4, 10),
            new Note(Pitch.A, 4, 10)), new Note(Pitch.C, 4, 10), new Rest(479),
            new Note(Pitch.G, 4, 1));

    assertEquals(melody, endMelody);
  }

  @Test
  public void testAddNoteToBeat2() {
    Melody melody = new Melody(20, new Rest(500));
    melody.addNoteToBeat(new Note(Pitch.C, 4, 1), 499);
    melody.addNoteToBeat(new Note(Pitch.G, 4, 1), 10);
    melody.addNoteToBeat(new Chord(5, new Note(Pitch.C, 4, 10), new Note(Pitch.A, 4, 10)), 5);


    Melody endMelody = new Melody(20, new Rest(5), new Chord(5, new Note(Pitch.C, 4, 10),
            new Note(Pitch.A, 4, 10)), new Note(Pitch.G, 4, 1), new Rest(488),
            new Note(Pitch.C, 4, 1));

    assertEquals(melody, endMelody);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testAddNoteToBeatFail() {
    Melody melody = new Melody(20, new Rest(30));
    melody.addNoteToBeat(new Note(Pitch.C, 4, 10), 10);
    melody.addNoteToBeat(new Chord(6, new Note(Pitch.C, 4, 10), new Note(Pitch.A, 4, 10)), 5);
  }

  @Test
  public void testAddNoteToBeatLastBeat() {
    Melody melody = new Melody(20, new Note(Pitch.A, 4, 10), new Note(Pitch.B, 3, 10));
    melody.addNoteToBeat(new Note(Pitch.C, 4, 10), 20);

    Melody endMelody = new Melody(20,  new Note(Pitch.A, 4, 10), new Note(Pitch.B, 3, 10),
            new Note(Pitch.C, 4, 10));

    assertEquals(melody, endMelody);
  }

  // tests the getIndexOfBeat method
  @Test
  public void testGetIndexOfBeat() throws Exception {
    Melody melody = new Melody();
    melody.addNote(new Note(Pitch.C, 5, 5), 0);
    assertEquals(melody.getIndexOfBeat(0), 0);
  }

  // tests the getInstrument method
  @Test
  public void testGetInstrument() throws Exception {
    Melody melody = new Melody();
    assertEquals(melody.getInstrument(), 1);
  }

  // tests the size method
  @Test
  public void testSize() throws Exception {
    Melody melody = new Melody();
    assertEquals(melody.size(), 0);
  }

  // tests the toString method
  @Test
  public void testToString() throws Exception {
    Melody melody = new Melody();
    assertEquals(melody.toString(), "Instrument 1: []");
  }

}