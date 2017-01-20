package cs3500.music.model;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import cs3500.music.util.MusicReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests methods in the MusicModel class.
 * CHANGELOG: Added tests for the CompositionBuilder methods.
 */
public class MusicModelTest {
  /**
   * Tests that the getAllSounds method works correctly.
   */
  @Test
  public void testGetAllNotes() {
    IMusicModel musicModel =
            new MusicModel(4 ,50,
                    new Melody(50,
                            new Chord(1,
                                    new Note(Pitch.D_SHARP, 5, 1),
                                    new Note(Pitch.A, 5, 2)),
                            new Note(Pitch.G, 5, 2),
                            new Note(Pitch.C_SHARP, 5, 1),
                            new Note(Pitch.G_SHARP, 6, 3)));

    assertTrue(musicModel.getAllSounds().get(1).contains(new Note(Pitch.G, 5, 2)));
    assertTrue(musicModel.getAllSounds().get(0).contains(new Note(Pitch.D_SHARP, 5, 1)));
  }

  /**
   * Tests that the getGameState method is just an empty line when there are no melodies.
   */
  @Test
  public void testGetGameStateNoMelodies() {
    IMusicModel model = new MusicModel(4, 50);
    assertEquals(model.getGameState(), "\n");
  }

  /**
   * Tests that the getGameState method is just an empty line when the melodies have no measures.
   */
  @Test
  public void testGetGameStateNoMeasures() {
    IMusicModel model = new MusicModel(4, 50,
            new Melody(4));

    assertEquals(model.getGameState(), "\n");
  }

  /**
   * Tests that the getGameState method is just an empty line when the measures have no notes.
   */
  @Test
  public void testGetGameStateNoNotes() {
    IMusicModel model = new MusicModel(4, 50,
            new Melody(50));

    assertEquals(model.getGameState(), "\n");
  }

  /**
   * Tests that the addTracks works as expected.
   */
  @Test
  public void testAddTracks() {
    IMusicModel musicModel =
            new MusicModel(4, 50,
                    new Melody(50,
                            new Chord(1,
                                    new Note(Pitch.D_SHARP, 5, 1),
                                    new Note(Pitch.A, 5, 2)),
                            new Note(Pitch.G, 5, 2),
                            new Rest(1),
                            new Note(Pitch.C_SHARP, 5, 1),
                            new Note(Pitch.A, 7, 2),
                            new Rest(1)));

    Melody mergeMelody = new Melody(50,
                    new Note(Pitch.E, 7, 1),
                    new Note(Pitch.G, 5, 2),
                    new Rest(1),
                    new Note(Pitch.C_SHARP, 5, 1),
                    new Note(Pitch.G_SHARP, 6, 3));

    IMusicModel merged =
            new MusicModel(4, 50,
                    new Melody(50,
                            new Chord(1,
                                    new Note(Pitch.D_SHARP, 5, 1),
                                    new Note(Pitch.A, 5, 2)),
                            new Note(Pitch.G, 5, 2),
                            new Rest(1),
                            new Note(Pitch.C_SHARP, 5, 1),
                            new Note(Pitch.A, 7, 2),
                            new Rest(1)),
                    new Melody(50,
                            new Note(Pitch.E, 7, 1),
                            new Note(Pitch.G, 5, 2),
                            new Rest(1),
                            new Note(Pitch.C_SHARP, 5, 1),
                            new Note(Pitch.G_SHARP, 6, 3)));
    musicModel.addTracks(mergeMelody);
    assertEquals(musicModel, merged);
  }

  /**
   * Tests that addTracks works as expected when given an index.
   */
  @Test
  public void testAddTracksAtIndex() {
    IMusicModel model =
            new MusicModel(4, 50,
                    new Melody(50,
                            new Chord(1,
                                    new Note(Pitch.D_SHARP, 5, 1),
                                    new Note(Pitch.A, 5, 2)),
                            new Note(Pitch.G, 5, 2),
                            new Rest(1),
                            new Note(Pitch.C_SHARP, 5, 1),
                            new Note(Pitch.A, 7, 2),
                            new Rest(1)),
                    new Melody(50,
                            new Note(Pitch.E, 7, 1),
                            new Note(Pitch.G, 5, 2),
                            new Rest(1),
                            new Note(Pitch.C_SHARP, 5, 1),
                            new Note(Pitch.G_SHARP, 6, 3)));

    model.addTracks(new Melody(50,
                    new Chord(1,
                            new Note(Pitch.D_SHARP, 5, 1),
                            new Note(Pitch.A, 5, 2)),
                    new Note(Pitch.G, 5, 2),
                    new Rest(1),
                    new Note(Pitch.C_SHARP, 5, 1),
                    new Note(Pitch.A, 7, 2),
                    new Rest(1)),
            1);


    IMusicModel merged = new MusicModel(4, 50,
            new Melody(50,
                    new Chord(1,
                            new Note(Pitch.D_SHARP, 5, 1),
                            new Note(Pitch.A, 5, 2)),
                    new Note(Pitch.G, 5, 2),
                    new Rest(1),
                    new Note(Pitch.C_SHARP, 5, 1),
                    new Note(Pitch.A, 7, 2),
                    new Rest(1)),
            new Melody(50,
                    new Note(Pitch.E, 7, 1),
                    new Note(Pitch.G, 5, 2),
                    new Rest(1),
                    new Note(Pitch.C_SHARP, 5, 1),
                    new Note(Pitch.G_SHARP, 6, 3)),
            new Melody(50,
                    new Rest(1),
                    new Chord(1,
                            new Note(Pitch.D_SHARP, 5, 1),
                            new Note(Pitch.A, 5, 2)),
                    new Note(Pitch.G, 5, 2),
                    new Rest(1),
                    new Note(Pitch.C_SHARP, 5, 1),
                    new Note(Pitch.A, 7, 2),
                    new Rest(1)));

    assertEquals(model, merged);
  }

  /**
   * Tests that the mergeTracks method works as expected.
   */
  @Test
  public void mergeMusicTest() {
    IMusicModel musicModel =
            new MusicModel(4, 50,
                    new Melody(50,
                            new Chord(1,
                                  new Note(Pitch.D_SHARP, 5, 1),
                                  new Note(Pitch.A, 5, 2)),
                            new Note(Pitch.G, 5, 2),
                            new Rest(1),
                            new Note(Pitch.C_SHARP, 5, 1),
                            new Note(Pitch.A, 7, 2),
                            new Rest(1)));

    IMusicModel mergingModel =
            new MusicModel(4, 50,
                    new Melody(50,
                            new Note(Pitch.E, 7, 1),
                            new Note(Pitch.G, 5, 2),
                            new Rest(1),
                            new Note(Pitch.C_SHARP, 5, 1),
                            new Note(Pitch.G_SHARP, 6, 3)));
    IMusicModel merged =
            new MusicModel(4, 50,
                    new Melody(50,
                            new Chord(1,
                                    new Note(Pitch.D_SHARP, 5, 1),
                                    new Note(Pitch.A, 5, 2)),
                            new Note(Pitch.G, 5, 2),
                            new Rest(1),
                            new Note(Pitch.C_SHARP, 5, 1),
                            new Note(Pitch.A, 7, 2),
                            new Rest(1)),
                    new Melody(50,
                            new Note(Pitch.E, 7, 1),
                            new Note(Pitch.G, 5, 2),
                            new Rest(1),
                            new Note(Pitch.C_SHARP, 5, 1),
                            new Note(Pitch.G_SHARP, 6, 3)));
    musicModel.mergeMusic(mergingModel, 0);
    assertEquals(musicModel, merged);
  }

  /**
   * Tests that addToEnd works as expected.
   */
  @Test
  public void testAddToEnd() {
    IMusicModel musicModel =
            new MusicModel(4, 50,
                    new Melody(50,
                            new Chord(1,
                                  new Note(Pitch.D_SHARP, 5, 1),
                                  new Note(Pitch.A, 5, 2)),
                            new Note(Pitch.G, 5, 2),
                            new Rest(1),
                            new Note(Pitch.C_SHARP, 5, 1),
                            new Note(Pitch.A, 7, 2),
                            new Rest(1)));

    Melody mergeMelody = new Melody(50,
            new Note(Pitch.E, 7, 1),
            new Note(Pitch.G, 5, 2),
            new Rest(1),
            new Note(Pitch.C_SHARP, 5, 1),
            new Note(Pitch.G_SHARP, 6, 3));

    IMusicModel merged =
            new MusicModel(4, 50,
                    new Melody(50,
                            new Chord(1,
                                new Note(Pitch.D_SHARP, 5, 1),
                                new Note(Pitch.A, 5, 2)),
                            new Note(Pitch.G, 5, 2),
                            new Rest(1),
                            new Note(Pitch.C_SHARP, 5, 1),
                            new Note(Pitch.A, 7, 2),
                            new Rest(1)),
                    new Melody(50,
                            new Rest(8),
                            new Note(Pitch.E, 7, 1),
                            new Note(Pitch.G, 5, 2),
                            new Rest(1),
                            new Note(Pitch.C_SHARP, 5, 1),
                            new Note(Pitch.G_SHARP, 6, 3)));
    musicModel.addToEnd(mergeMelody);
    assertEquals(musicModel, merged);
  }

  /**
   * Tests that the spliceTrack method works as expected.
   */
  @Test
  public void testSpliceTracks() {

    IMusicModel musicModel =
            new MusicModel(4, 50,
                    new Melody(50,
                            new Chord(1,
                                    new Note(Pitch.D_SHARP, 5, 1),
                                    new Note(Pitch.A, 5, 2)),
                            new Note(Pitch.G, 5, 2),
                            new Rest(1),
                            new Note(Pitch.C_SHARP, 5, 1),
                            new Note(Pitch.A, 7, 2),
                            new Rest(1)),
                    new Melody(50,
                            new Note(Pitch.C, 3, 1),
                            new Note(Pitch.E, 2, 2),
                            new Rest(2)));

    int numberOfBeats = musicModel.getMelodies().get(0).numOfBeats();

    Melody spliceMelody = new Melody(50,
            new Note(Pitch.E, 7, 1),
            new Note(Pitch.G, 5, 2),
            new Rest(1),
            new Note(Pitch.C_SHARP, 5, 1),
            new Note(Pitch.G_SHARP, 6, 3));

    musicModel.spliceTracks(1, spliceMelody);

    assertNotEquals(numberOfBeats, musicModel.getMelodies().get(0).numOfBeats());

    assertEquals(musicModel.getMelodies().get(0).getNote(1),
            new Rest(8));

    assertEquals(musicModel.getMelodies().get(2).getNote(0), new Rest(1));

    assertEquals(musicModel.getMelodies().get(2), spliceMelody);
  }

  /**
   * Tests that the addNote method works as expected.
   */
  @Test
  public void testAddNote() {
    IMusicModel model = new MusicModel(4, 20,
            new Melody(50,
                    new Rest(4)));

    model.addNote(new Note(Pitch.D, 3, 1), 0, 0);

    ArrayList<ASound> value = new ArrayList<>();

    value.add(new Note(Pitch.D, 3, 1));

    assertEquals(model.getAllSounds().containsValue(value),
            true);
  }

  /**
   * Tests that the addNote method fails as expected given invalid melody index.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testAddNoteInvalidMelodyIndex() {
    IMusicModel model = new MusicModel(4, 20,
            new Melody(50,
                    new Rest(4)));

    model.addNote(new Note(Pitch.D, 3, 1), 2, 0);
  }

  /**
   * Tests that the swapNote method works as expected.
   */
  @Test
  public void testSwapNoteBeat() {
    IMusicModel model = new MusicModel(4, 20,
            new Melody(50,
                    new Note(Pitch.G, 11, 1),
                    new Note(Pitch.E, 8, 1),
                    new Note(Pitch.D_SHARP, 1, 2)));

    model.swapNote(new Note(Pitch.D, 3, 1), 0, 1);

    assertEquals(model.getAllSounds().get(1).contains(new Note(Pitch.D, 3, 1)),
            true);

    assertEquals(model.getAllSounds().get(1).contains(new Note(Pitch.E, 8, 1)),
            false);
  }

  /**
   * Tests that the swapNote(int) method works as expected.
   */
  @Test
  public void testSwapNoteInt() {
    IMusicModel model = new MusicModel(4, 20,
            new Melody(50,
                    new Note(Pitch.G, 11, 1),
                    new Note(Pitch.E, 8, 1),
                    new Note(Pitch.D_SHARP, 1, 2)));

    model.swapNote(new Note(Pitch.D, 3, 1), 0, 1);

    assertEquals(model.getAllSounds().get(1).contains(new Note(Pitch.D, 3, 1)),
            true);

    assertEquals(model.getAllSounds().get(1).contains(new Note(Pitch.E, 8, 1)),
            false);
  }

  /**
   * Tests that the mergeTracks method works as expected.
   */
  @Test
  public void testMergeTracks() {
    IMusicModel musicModel =
            new MusicModel(4, 50,
                    new Melody(50,
                            new Chord(1,
                                    new Note(Pitch.D_SHARP, 5, 1),
                                    new Note(Pitch.A, 5, 2)),
                            new Note(Pitch.G, 5, 2),
                            new Rest(1),
                            new Note(Pitch.C_SHARP, 5, 1),
                            new Note(Pitch.A, 7, 2),
                            new Rest(1)),
                    new Melody(50,
                            new Note(Pitch.C, 3, 1),
                            new Note(Pitch.E, 2, 2),
                            new Rest(2)));

    Melody mergeMelody = new Melody(50,
            new Note(Pitch.E, 7, 1),
            new Note(Pitch.G, 5, 2),
            new Rest(1),
            new Note(Pitch.C_SHARP, 5, 1),
            new Note(Pitch.G_SHARP, 6, 3));

    musicModel.mergeTracks(0, 1, mergeMelody);

    assertTrue(musicModel.getMelodies().get(0).getNote(2).equals(mergeMelody.getNote(1)));
  }

  /**
   * Tests that the mergeTracks method fails as expected given invalid indeces.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testMergeTracksIllegalIndex() {
    IMusicModel musicModel =
            new MusicModel(4, 50,
                    new Melody(50,
                            new Chord(1,
                                  new Note(Pitch.D_SHARP, 5, 1),
                                  new Note(Pitch.A, 5, 2)),
                            new Note(Pitch.G, 5, 2),
                            new Rest(1),
                            new Note(Pitch.C_SHARP, 5, 1),
                            new Note(Pitch.A, 7, 2),
                            new Rest(1)),
                    new Melody(50,
                            new Note(Pitch.C, 3, 1),
                            new Note(Pitch.E, 2, 2),
                            new Rest(2)));

    Melody mergeMelody = new Melody(50,
            new Note(Pitch.E, 7, 1),
            new Note(Pitch.G, 5, 2),
            new Rest(1),
            new Note(Pitch.C_SHARP, 5, 1),
            new Note(Pitch.G_SHARP, 6, 3));

    musicModel.mergeTracks(5, 0, mergeMelody);
  }

  /**
   * Tests that setNotes works as expected.
   */
  @Test
  public void testGetNote() {
    IMusicModel model = new MusicModel(4, 20,
            new Melody(50, new Rest(4)));

    model.addNote(new Note(Pitch.D, 3, 1), 0, 0);

    ASound value = new Note(Pitch.D, 3, 1);

    assertEquals(model.getNote(0, 0), value);
  }

  /**
   * Tests that setNotes fails as expected given invalid indeces.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testGetNoteInvalidIndex() {
    IMusicModel model = new MusicModel(4, 20,
            new Melody(50, new Rest(4)));

    model.getNote(0, 5);
  }

  /**
   * Tests that setNotes works as expected.
   */
  @Test
  public void testGetNoteBeat() {
    IMusicModel model = new MusicModel(4, 20,
            new Melody(50, new Rest(4)));

    model.addNote(new Note(Pitch.D, 3, 1), 0, 0);
    model.addNote(new Note(Pitch.F, 6, 2), 0, 1);

    ASound value = new Note(Pitch.F, 6, 2);

    assertEquals(model.getNote(0, 1), value);
  }

  /**
   * Tests that setNotes fails as expected given invalid indeces.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testGetNoteInvalidBeat() {
    IMusicModel model = new MusicModel(4, 20,
            new Melody(50, new Rest(4)));

    model.getNote(0, 4);
  }

  /**
   * Tests that the setVolume method works as expected.
   */
  @Test
  public void testSetVolume() {
    IMusicModel model = new MusicModel(4, 20,
            new Melody(50, new Rest(4)));

    model.setVolume(75);

    IMusicModel model2 = new MusicModel(4, 75,
            new Melody(50, new Rest(4)));

    assertEquals(model, model2);
  }

  /**
   * Tests that the setVolume method fails as expected given invalid int.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testSetVolumeInvalidVolume() {
    IMusicModel model = new MusicModel(4, 20,
            new Melody(50, new Rest(4)));

    model.setVolume(120);
  }

  /**
   * Tests that the changeVolume method works as expected.
   */
  @Test
  public void testChangeVolume() {
    IMusicModel model = new MusicModel(4, 20,
            new Melody(50, new Rest(4)));

    model.changeVolume(5);

    IMusicModel model2 = new MusicModel(4, 25,
            new Melody(50, new Rest(4)));

    assertEquals(model, model2);
  }

  /**
   * Tests that the changeVolume method works as expected when asked to change volume past 100.
   */
  @Test
  public void testSetVolumeOver100() {
    IMusicModel model = new MusicModel(4, 98,
            new Melody(50, new Rest(4)));

    model.changeVolume(5);

    IMusicModel model2 = new MusicModel(4, 100,
            new Melody(50, new Rest(4)));

    assertEquals(model, model2);
  }

  @Test
  public void testBuildPiece() {
    IMusicModel piece = new MusicModel(4, 50, new Melody(50, new Note(Pitch.G, 3, 40, 7)));

    piece.addNote(7, 9, 1, 64, 76);

    assertEquals(piece, new MusicModel(4, 50, new Melody(50, new Note(Pitch.G, 3, 40, 7),
            new Note(Pitch.E, 5, 76, 2))));
  }

  /**
   * Tests that the build method correctly returns Mary Had A Little Lamb.
   */
  @Test
  public void testCompositionBuilder() {
    MusicModel piece = new MusicModel(4, 50);
    try {
      MusicReader.parseFile(new BufferedReader(new FileReader("resources/mary-little-lamb.txt")),
              piece);
      assertEquals(piece.getMelodies().get(0).getNote(0), new Note(Pitch.E, 5, 2));
      assertEquals(piece.getMelodies().get(1).getNote(0), new Note(Pitch.G, 4, 7));
      assertEquals(piece.getMelodies().get(0).getNote(1), new Note(Pitch.D, 5, 2));
      assertEquals(piece.getMelodies().get(0).getNote(2), new Note(Pitch.C, 5, 2));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File not found: " + e.getMessage());
    }
  }

  @Test
  public void testIndexOfMelody() {
    Melody melody = new Melody(4, new Note(Pitch.A, 3, 2), new Note(Pitch.C, 2, 1),
            new Note(Pitch.G, 3, 1));

    assertEquals(melody.getIndexOfBeat(2), 1);
  }
}
