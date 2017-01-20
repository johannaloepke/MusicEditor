package cs3500.music.model;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a Melody in music.
 * CHANGELOG: Removed notion of a Measure; No apparent benefit for this assignment with some
 *            drawbacks including set duration of a Measure.
 *            Added a field for instrument, representing the MIDI instrument to be played.
 *            Added a changeInstrument method that changes the instrument to the given one.
 *            Fixed longstanding bug in getNoteAtBeat().
 *            Removed nonMatchingMeasureException() as Measures no longer exist.
 */
public class Melody {
  /**
   * Represents the volume of this {@code Melody} compared to another; scale factor for volume.
   * Value is from 0-100.
   */
  private int dynamic;

  /**
   * Represents the {@code List} of {@code ASound} in this {@code Melody}.
   */
  private List<ASound> sounds;

  /**
   * Represents the instrument int code as interpreted by MIDI. Sets the instrument to this.
   */
  private int instrument;

  /**
   * Constructs a Melody.
   * @param dynamic the volume of this melody
   * @param instrument the instrument of this melody
   * @param sounds the sounds in this melody
   */
  public Melody(int dynamic, int instrument, List<ASound> sounds) {
    if (dynamic < 0 || dynamic > 100) {
      throw new IllegalArgumentException("The dynamic cannot be less than 0 or greater than 100.");
    }
    this.dynamic = dynamic;
    this.sounds = sounds;
    this.instrument = instrument;
    for (ASound s : this.sounds) {
      s.setInstrument(instrument);
    }
  }

  /**
   * Constructs a Melody that takes a various number of sounds.
   * @param dynamic volume of this melody
   * @param instrument instrument of this melody
   * @param sounds sounds in this methody
   */
  public Melody(int dynamic, int instrument, ASound... sounds) {
    if (dynamic < 0 || dynamic > 100) {
      throw new IllegalArgumentException("The dynamic cannot be less than 0 or greater than 100.");
    }

    this.dynamic = dynamic;
    this.instrument = instrument;
    this.sounds = new ArrayList<ASound>();
    for (ASound s : sounds) {
      this.sounds.add(s);
      s.setInstrument(instrument);
    }
  }

  /**
   * Creates a Melody.
   * @param dynamic represents the volume of the melody.
   * @param sounds represents the sounds to be added.
   */
  public Melody(int dynamic, List<ASound> sounds) {
    this(dynamic, 1, sounds);
  }

  /**
   * Creates a Melody.
   * @param dynamic represents the volume of the melody.
   * @param sounds represents the sounds to be added.
   */
  public Melody(int dynamic, ASound... sounds) {
    this(dynamic, 1, sounds);
  }

  /**
   * Creates a Melody.
   * @param dynamic represents the volume of the melody.
   */
  public Melody(int dynamic) {
    this(dynamic, new ArrayList<ASound>());
  }

  /**
   * Constructs a melody of volume 50.
   */
  public Melody() {
    this(50);
  }

  /**
   * Places the complete given melody into this melody at the given index.
   * @param index represents the index to start adding at.
   * @param other represents the other Melody.
   */
  public void merge(int index, Melody other) {
    List<ASound> toAdd = other.sounds;
    for (int i = 0; i < toAdd.size(); i++) {
      this.addNotePrivate(toAdd.get(i), index + i);
    }
  }

  /**
   * Adds a note at the given index.
   * @param note represents the {@code ASound} being added to the given position.
   * @param noteIndex represents the index to add note to.
   * @throws IllegalArgumentException if the index is invalid.
   */
  public void addNote(ASound note, int noteIndex) {
    this.addNotePrivate(note, noteIndex);
  }

  /**
   * Adds note to a given beat.
   * @param note      represents the note being added.
   * @param beatIndex represents the beat to add the note to.
   */
  public void addNoteToBeat(ASound note, int beatIndex) {
    int oldIndex = getIndexOfBeat(beatIndex);
    if (oldIndex == -1 || this.sounds.size() < oldIndex) {
      if (beatIndex - this.numOfBeats() > 0) {
        this.sounds.add(new Rest(beatIndex - this.numOfBeats()));
      }
      this.sounds.add(note);
      return;
    }

    if (oldIndex == 0 && this.sounds.size() == 0) {
      if (beatIndex > 0) {
        this.addNotePrivate(new Rest(beatIndex), 0);
      }
      this.addNotePrivate(note, this.size());
      return;
    }

    if (!this.noteStartsAtExactBeat(beatIndex)) {
      oldIndex -= 1;
    }

    ASound oldSound = this.sounds.get(oldIndex);

    if (this.numOfBeatsElapsedAt(oldIndex) + oldSound.getDuration() <= beatIndex) {
      int restLength = this.numOfBeats() - beatIndex;
      if (restLength > 0) {
        this.addNotePrivate(new Rest(restLength), this.size());
      }
      this.addNotePrivate(note, this.size());
      return;
    }

    boolean notePlaysDuringThisNote = false;
    for (int i = beatIndex; i < beatIndex + note.getDuration(); i++) {
      if (this.hasNotePlayingAt(i)) {
        notePlaysDuringThisNote = true;
      }
    }

    if (!oldSound.isRest() || notePlaysDuringThisNote) {
      //this.sounds.add(oldIndex, oldSound);
      throw new IllegalArgumentException("Attempted to add note " + note +
              " to a beat that was already "
              + "occupied with note " + oldSound + ".");
    }

    if (oldSound.getDuration() < note.getDuration()
            && this.numOfBeats() >= note.getDuration() + beatIndex) {
      throw new IllegalArgumentException("Attempted to add note " + note + " to a rest that was "
      + "overextended by " + (note.getDuration() - oldSound.getDuration()) + ".");
    }

    int startDuration = beatIndex - this.numOfBeatsElapsedAt(oldIndex);
    int endPoint = beatIndex + note.getDuration();
    int lastRestPosition = this.numOfBeatsElapsedAt(oldIndex) + oldSound.getDuration() - endPoint;
    if (lastRestPosition > 0) {
      this.sounds.add(oldIndex,
              new Rest(lastRestPosition));
    }
    this.sounds.add(oldIndex, note);
    if (endPoint - beatIndex > 0 && startDuration > 0) {
      this.sounds.add(oldIndex, new Rest(startDuration));
    }
    this.sounds.remove(oldSound);
  }

  /**
   * Adds a note at the given index.
   * @param note represents the {@code ASound} being added to the given position.
   * @param noteIndex represents the index to add note to.
   * @throws IllegalArgumentException if the index is invalid.
   */
  private void addNotePrivate(ASound note, int noteIndex) {
    if (this.sounds.size() >= noteIndex) {
      this.sounds.add(noteIndex, note);
      note.setInstrument(this.instrument);
    }
    else {
      throw new IllegalArgumentException("This index doesn't exist: " + noteIndex
              + " in list of size "
              + this.sounds.size());
    }
  }

  /**
   * Determines whether a note is playing during a given beat (starting or continuing).
   * @param beat represents the beat at which a note could be playing.
   * @return {@code boolean} determining whether there is a note playing at the given beat.
   */
  public boolean hasNotePlayingAt(int beat) {
    for (ASound s : this.sounds) {
      if (beat == 0 && !s.isRest()) {
        return true;
      }
      else if (beat == 0) {
        return false;
      }
      beat -= s.getDuration();
      if (beat < 0) {
        return !s.isRest();
      }
    }
    return this.size() != 0 && !this.sounds.get(this.size() - 1).isRest();
  }

  /**
   * Gets the note at the given index.
   * @param noteIndex represents the index of the note to be returned.
   * @return {@code ASound} that's at the given index.
   */
  public ASound getNote(int noteIndex) {
    if (noteIndex >= this.sounds.size() || noteIndex < 0) {
      throw new IllegalArgumentException("This index doesn't exist " + noteIndex + ".");
    }
    return this.sounds.get(noteIndex);
  }

  /**
   * Gets the note at the given beat.
   * @param beat represents the beat of the note to be returned.
   * @return {@code ASound} that's at the given beat.
   */
  public ASound getNoteAtBeat(int beat) {
    for (ASound s : this.sounds) {
      if (beat == 0) {
        return s;
      }
      beat -= s.duration;
    }
    throw new IllegalArgumentException("There is no note at the given beat.");
  }

  /**
   * Swaps a note in with another.
   * @param sound        represents the sound to be swapped in.
   * @param beat         what beat the sounds starts on.
   */
  public void swapNote(ASound sound, int beat) {
    int noteIndex = this.getIndexOfBeat(beat);
    if (noteIndex >= this.sounds.size()) {
      throw new IllegalArgumentException("This index doesn't exist.");
    }
    this.sounds.remove(noteIndex);
    this.sounds.add(noteIndex, sound);
    sound.setInstrument(this.instrument);
  }

  /**
   * Determines whether a note exists at an exact index.
   * @param  beat represents the beat being checked.
   * @return whether a note starts at the given beat.
   */
  public boolean noteStartsAtExactBeat(int beat) {
    for (ASound s : this.sounds) {
      if (beat == 0) {
        return true;
      }
      beat -= s.duration;
      if (beat < 0) {
        return false;
      }
    }
    return false;
  }

  /**
   * Abstraction for throwing an {@code IllegalArgumentException}.
   * @throws IllegalArgumentException .
   */
  private IllegalArgumentException indexOutOfBoundsException() {
    throw new IllegalArgumentException("Given index is out of bounds.");
  }

  /**
   * Changes instrument to the given instrument.
   * @param instrument represents the int MIDI representation of the instrument to be changed to.
   */
  public void changeInstrument(int instrument) {
    this.instrument = instrument;
    for (ASound s : this.sounds) {
      s.setInstrument(instrument);
    }
  }

  @Override
  public int hashCode() {
    return (int)(this.sounds.hashCode() * this.dynamic);
  }

  @Override
  public boolean equals(Object other) {
    if (! (other instanceof  Melody)) {
      return false;
    }
    else {
      return ((Melody) other).sounds.equals(this.sounds)
              && ((Melody) other).dynamic == this.dynamic;
    }
  }

  /**
   * Returns the total number of beats in this melody.
   * @return number of beats
   */
  public int numOfBeats() {
    int numOfBeats = 0;
    for (ASound s : this.sounds) {
      numOfBeats += s.getDuration();
    }
    return numOfBeats;
  }

  /**
   * Returns the number of beats elapsed so far at the given index.
   * @param index index of a given note
   * @return the number of beats elapsed until the beginning of the given index
   */
  public int numOfBeatsElapsedAt(int index) {
    int numOfBeats = 0;
    for (int i = 0; i < index; i++) {
      numOfBeats += sounds.get(i).getDuration();
    }
    return numOfBeats;
  }

  /**
   * Returns the index at a given beat, or -1 if no such index exists.
   * @param beat represents the beat of the given index.
   * @return the index at a given beat, or -1 if no such index exists.
   */
  public int getIndexOfBeat(int beat) {
    int index = 0;
    int curBeat = 0;
    if (this.sounds.size() == 0) {
      return 0;
    }
    for (ASound s : this.sounds) {

      if (curBeat >= beat) {
        return index;
      }
      curBeat += s.getDuration();
      index++;
    }
    if (curBeat >= beat) {
      return index;
    }
    return -1;
  }

  /**
   * Gives the instrument represented by this Melody.
   * @return the instrument represented by this melody.
   */
  public int getInstrument() {
    return this.instrument;
  }

  /**
   * Returns the sounds in this Melody.
   * @return list of sounds in this melody
   */
  public List<ASound> getMelodySounds() {
    List<ASound> list = this.sounds;
    return list;
  }

  /**
   * Gives the size of this, in terms of the number of ASounds it contains.
   * @return the size of this Melody's Notes field.
   */
  public int size() {
    return this.sounds.size();
  }

  @Override
  public String toString() {
    return "Instrument " + this.instrument + ": " +  this.sounds.toString();
  }
}
