package cs3500.music.provider.model;

/**
 * Class to represent a sound.
 */
public abstract class ASound extends ANote {

  /**
   * Get the instrument this ASound is playing on.
   * @return the Instrument this ASound is playing on.
   */
  public abstract int getInstrument();

  /**
   * Returns the BeatState of the sound.
   * @return the BeatState of this sound.
   */
  public abstract BeatState getState();

  /**
   * Get the volume of this sound.
   * @return the volume of this sound.
   */
  public abstract int getVolume();

  /**
   * Returns the duration of this ASound in beats.
   * @return the duration of this ASound in beats.
   */
  public abstract int getDuration();
}
