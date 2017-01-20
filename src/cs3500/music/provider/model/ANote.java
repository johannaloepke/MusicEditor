package cs3500.music.provider.model;

import java.util.Objects;

/**
 * Interface to represent data that can produce a Note.
 */
public abstract class ANote implements Comparable<ANote> {

  /**
   * Returns the Octave of this ANote.
   * @return the number representing the Octave of this ANote.
   */
  public abstract int getOctave();

  /**
   * Returns the Tone of this ANote.
   * @return the Tone of this ANote.
   */
  public abstract Tone getTone();

  /**
   * Compares 2 INotes based on octave and tone.
   * @param that The other ANote to compare this ANote with.
   * @return A negative integer if this ANote has a lower frequency, a positive integer if this
   *         ANote has a higher frequency, 0 if both ANotes have the same frequency.
   */
  public final int compareTo(ANote that) {

    if (this.getOctave() != that.getOctave()) {
      return this.getOctave() - that.getOctave();
    }
    else {
      return this.getTone().compareTo(that.getTone());
    }
  }

  /**
   * Checks whether this ANote is Mute or not.
   * @return true if this ANote is mute, false otherwise.
   */
  public final boolean isMute() {
    return this.getTone().equals(Tone.MUTE);
  }

  @Override
  public abstract String toString();

  @Override
  public boolean equals(Object other) {

    if (other instanceof ANote) {
      return this.compareTo((ANote)other) == 0;
    }
    else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getTone(), this.getOctave());
  }
}
