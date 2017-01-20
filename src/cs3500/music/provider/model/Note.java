package cs3500.music.provider.model;

import java.util.Objects;

/**
 * Class to represent a pitch as a Tone in one of the 10 Octaves audible to humans.
 */
public class Note extends ANote {

  // Some constants to define Lowest and Highest octave that the model supports.
  public static int HIGHEST_OCTAVE = 10;
  public static int LOWEST_OCTAVE = 1;
  public static Note MUTE = new Note();

  private final Tone tone;
  private final int octave;

  /**
   * Creates a pitch of a given tone in a given octave.
   * @param tone    the {@code Tone} of this tone.
   * @param octave  the octave this tone is in.
   * @throws IllegalArgumentException if tone or octave is invalid.
   */
  public Note(Tone tone, int octave) throws IllegalArgumentException {

    if (octave > HIGHEST_OCTAVE || octave < LOWEST_OCTAVE) {
      throw new IllegalArgumentException("That octave is not supported.");
    }
    this.octave = octave;

    try {
      this.tone = Objects.requireNonNull(tone);
    }
    catch (NullPointerException e) {
      throw new IllegalArgumentException("Tone can not be null.");
    }
    if (tone.equals(Tone.MUTE)) {
      throw new IllegalArgumentException("Not a valid Tone for a Note, use Note.MUTE instead.");
    }
  }

  /**
   * Makes the "MUTE" Note.
   */
  private Note() {

    this.tone = Tone.MUTE;
    this.octave = 0;
  }

  @Override
  public int getOctave() {
    return this.octave;
  }

  @Override
  public Tone getTone() {
    return this.tone;
  }

  @Override
  public String toString() {

    if (this.equals(Note.MUTE)) {
      return "";
    }

    return this.tone.toString() + this.octave;
  }
}