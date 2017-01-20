package cs3500.music.provider.model;

import java.util.Objects;

/**
 * Class to represent a Sound, which is a {@code Note} played on an {@code Instrument}.
 */
public class Sound extends ASound {

  public static Sound MUTE = new Sound();

  private final int instrument;
  private final int volume;
  private int duration;
  private final ANote note;
  private final BeatState state;

  /**
   * Creates a Sound with given note and instrument.
   * @param note        The {@code Note} that corresponds to this sound.
   * @param volume      The {@code Volume} of this sound.
   * @param instrument  The instrument playing this sound, a number interpreted by MIDI.
   * @param state       The {@code BeatState} of this sound.
   * @param duration    The duration in beats for which this sound plays.
   * @throws IllegalArgumentException if any argument is null or invalid.
   */
  public Sound(ANote note, int volume, int instrument, BeatState state, int duration)
          throws IllegalArgumentException {

    try {
      this.duration = duration;
      this.note = Objects.requireNonNull(note);
      this.volume = volume;
      this.instrument = instrument;
      this.state = Objects.requireNonNull(state);
    } catch (NullPointerException e) {

      throw new IllegalArgumentException("Note, Instrument and State must be non-null.");
    }
  }

  /**
   * Creates a Sound with given note and instrument.
   * @param note        The {@code Note} that corresponds to this sound.
   * @param instrument  The instrument playing this sound, a number interpreted by MIDI.
   * @param dur    The duration in beats for which this sound plays.
   * @throws IllegalArgumentException if any argument is null or invalid.
   */
  public Sound(ANote note, int instrument, int dur)
          throws IllegalArgumentException {
    this(note, 64, instrument, BeatState.START, dur);
  }

  /**
   * Constructor for making a MUTE Sound.
   */
  private Sound() {
    this.note = Note.MUTE;
    this.instrument = -1;
    this.volume = 0;
    this.state = null;
  }

  @Override
  public int getOctave() {
    return this.note.getOctave();
  }

  @Override
  public Tone getTone() {
    return this.note.getTone();
  }

  @Override
  public String toString() {
    return this.note.toString();
  }

  @Override
  public int getInstrument() {
    return this.instrument;
  }

  @Override
  public BeatState getState() {
    return this.state;
  }

  @Override
  public int getVolume() {
    return this.volume;
  }

  @Override
  public int getDuration() {
    return this.duration;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (!(other instanceof Sound)) {
      return false;
    }
    else {

      Sound that = (Sound) other;

      return this.note.equals(that.note) && this.instrument == that.instrument
              && this.volume == that.volume;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.note, this.instrument, this.volume);
  }
}
