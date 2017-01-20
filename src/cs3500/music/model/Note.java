package cs3500.music.model;

import java.util.ArrayList;
import java.util.List;

import cs3500.music.provider.model.Tone;

/**
 * Creates a {@code Note} extension of the {@code ASound} class.
 * CHANGELOG: Changed the percentOfBeatPlayed() method to lengthOfBeatRemoved(), and now subtracts
 *              a constant double value from the Note's duration.
 *            Added implementation of makeChord as per the ASound's changelog.
 *            Added the field of instrument to constructing a note.
 */
public class Note extends ASound {

  /**
   * Represents the NoteName assigned to this Note.
   */
  private Pitch name;

  /**
   * Represents the octave this Note takes.
   */
  private int octave;

  /**
   * Creates a Note.
   * @param name     represents the type of note this is.
   * @param volume   represents how loud this note is.
   * @param octave   represents the octave this note has.
   * @param duration represents the duration of this note.
   * @throws IllegalArgumentException if given an invalid volume.
   */
  public Note(Pitch name, int  octave, int volume, int duration, int instrument) {
    if (volume < 0 || volume > 127) {
      throw new IllegalArgumentException("This volume is impossible."
              + "Volume must be between 0 and 100");
    }
    if (duration <= 0) {
      throw new IllegalArgumentException("Attempted to give non-positive duration: " + duration);
    }

    if (instrument < 1 || instrument > 128) {
      throw new IllegalArgumentException(
              "Attempted to give non-existent instrument with code " + instrument);
    }
    this.name = name;
    this.octave = octave;
    this.volume = volume;
    this.duration = duration;
    this.instrument = instrument;
  }

  /**
   * Creates a Note with a volume set to 50.
   * @param name     represents this {@code Note}'s name.
   * @param octave   represents what octave this note has.
   * @param duration represents how long this note lasts.
   */
  public Note(Pitch name, int octave, int duration) {
    this(name, octave, 50, duration, 1);
  }

  /**
   * Creates a Note with a volume set to 50.
   * @param name     represents this {@code Note}'s name.
   * @param octave   represents what octave this note has.
   * @param duration represents how long this note lasts.
   */
  public Note(Pitch name, int octave, int volume, int duration) {
    this(name, octave, volume, duration, 1);
  }

  /**
   * Changes the duration.
   * @param newDuration represents the new duration, as a {@code Fraction}.
   */
  protected void setDuration(int newDuration) {
    this.duration = newDuration;
  }

  /**
   * Changes the octave.
   * @param octaveDifference represents what's being added to the octave from current.
   */
  public void changeOctave(int octaveDifference) {
    this.octave += octaveDifference;
  }

  /**
   * Creates a {@code String} representation of this Note's name.
   * @return a {@code String} representing this Note's name.
   */
  public String pitchName() {
    return this.name.toString();
  }

  /**
   * Creates a {@code String} representation of this Note's name in flat notation.
   * @return a {@code String} representing this Note's flat name.
   */
  public String flatName() {
    return this.name.flatString();
  }

  /**
   * Creates a {@code String} representation of this Note's name in sharp notation.
   * @return a {@code String} representing this Note's sharp name.
   */
  public String sharpName() {
    return this.name.sharpString();
  }

  public boolean isRest() {
    return false;
  }

  @Override
  public List<String> getName() {
    ArrayList<String> list = new ArrayList<>();
    list.add(this.name.toString());
    return list;
  }

  @Override
  public boolean higherThan(ASound other) {
    if (other.isRest()) {
      return true;
    }
    else {
      return this.noteValue() > other.getHighestNote().noteValue();
    }
  }

  @Override
  public boolean lowerThan(ASound other) {
    if (other.isRest()) {
      return true;
    }
    else {
      return this.noteValue() < other.getLowestNote().noteValue();
    }
  }

  @Override
  public Note getHighestNote() {
    return this;
  }

  @Override
  public Note getLowestNote() {
    return this;
  }

  @Override
  public Chord makeChord(Note note) {
    return new Chord(this.duration, this, note);
  }

  /**
   * Gives the Pitch this is at.
   * @return {@code Pitch} of this.
   */
  public Pitch getPitch() {
    return this.name;
  }

  /**
   * Gives the int representing the octave this is at.
   * @return {@code int} of this' octave.
   */
  public int getOctave() {
    return this.octave;
  }

  @Override
  public Tone getTone() {
    return Tone.getAllTones().get(this.getPitch().val);
  }


  /**
   * Represents percentage of time a note actually lasts over a beat out of 1.
   * @return double representing percentage of time note should last over beat.
   */
  public double finalNoteLength() {
    return (double)this.duration - .1;
  }

  /**
   * Returns the value associated with this note.
   * @return int representing the value of this note.
   */
  public int noteValue() {
    return this.octave * 12 + this.getPitch().val;
  }

  @Override
  public String toString() {
    return "Note " + this.name.toString() + this.octave + " of length " + this.duration;
  }

  @Override
  public boolean isNote() {
    return true;
  }

  @Override
  public int hashCode() {
    return this.octave * this.name.hashCode() * this.duration;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Note)) {
      return false;
    }
    else {
      return ((Note) other).octave == this.octave
              && ((Note) other).name == this.name
              && ((Note) other).duration == this.duration;
    }
  }
}
