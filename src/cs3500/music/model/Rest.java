package cs3500.music.model;

import java.util.ArrayList;
import java.util.List;

import cs3500.music.provider.model.Tone;

/**
 * Represents the musical concept of a rest; the space between notes.
 * CHANGELOG: Implemented makeChord as per ASound's documentation.
 *            Made class final; while there could be reasons to vary different types of Notes or
 *              Chords, there seems little reason to make different types of Rests.
 */
public final class Rest extends ASound {
  /**
   * Creates a {@code Rest} with a duration as a {@code Fraction}.
   * @param duration represents the length of this {@code Rest}.
   */
  public Rest(int duration) {
    if (duration <= 0) {
      throw new IllegalArgumentException("Attempted to give duration: " + duration);
    }
    this.duration = duration;
    this.volume = 0;
  }

  /**
   * Does nothing for this type of Sound.
   * @param octaveDifference represents how much the Octave would be changed.
   */
  public void changeOctave(int octaveDifference) {
    //This method does nothing for a Rest.
  }

  /**
   * Does nothing for this type of Sound.
   * @param volume represents how much the volume would be changed.
   */
  public void changeVolume(int volume) {
    //This method does nothing in this class.
  }

  /**
   * Does nothing, as a Rest's volume is always 0.
   * @param volume represents the volume this sound will get.
   */
  public void setVolume(int volume) {
    //This does nothing in this class.
  }

  public boolean isRest() {
    return true;
  }

  @Override
  public List<String> getName() {
    List<String> list = new ArrayList<>();
    list.add("Rest.");
    return list;
  }

  @Override
  public boolean higherThan(ASound other) {
    return false;
  }

  @Override
  public boolean lowerThan(ASound other) {
    return false;
  }

  @Override
  public Note getHighestNote() {
    throw new IllegalArgumentException("Rests do not have a highest note");
  }

  @Override
  public Note getLowestNote() {
    throw new IllegalArgumentException("Rests do not have a highest note");
  }

  @Override
  public Chord makeChord(Note note) {
    throw new IllegalArgumentException("Chords cannot be made from Rests");
  }

  @Override
  public int hashCode() {
    return this.getDuration() * 5;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof  Rest)) {
      return false;
    }
    else {
      return ((Rest) other).duration == this.duration;
    }
  }

  @Override
  public int getOctave() {
    throw new UnsupportedOperationException("Rests have no octaves.");
  }

  @Override
  public Tone getTone() {
    throw new UnsupportedOperationException("Rests have no tones.");
  }

  @Override
  public String toString() {
    return "Rest of length " + this.duration;
  }
}
