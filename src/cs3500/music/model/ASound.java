package cs3500.music.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cs3500.music.provider.model.BeatState;

/**
 * Represents the different sounds possible.
 * CHANGELOG: Beats are now integer values and not represented as fractions.
 *            New field added: instrument. Represents the integer value of the instrument this sound
 *              is represented as. Held off previously because of uncertainty about how such notion
 *              might be represented. Is set to 0 (Grand Piano) inherently.
 *            Added new method setInstrument to allow for changing the instrument.
 *            Added new abstract method makeChord to allow for making new chords.
 */
public abstract class ASound extends cs3500.music.provider.model.ASound {

  public static HashMap<Integer, Pitch> PITCHES = new HashMap<>();


  // A {@code duration} is used for representing musical notes' lengths.
  protected int duration;

  // A {@code volume} represents how loud the sound should be played.
  protected int volume;

  // A {@code instrument} represents the MIDI representation of an instrument.
  protected int instrument = 1;

  /**
   * Returns the duration of this Sound.
   */
  public int getDuration() {
    return this.duration;
  }

  /**
   * Changes the octave of this Sound.
   */
  public abstract void changeOctave(int octaveDifference);

  /**
   * Changes the volume of this Sound.
   * @param volume represents the amount this volume will be changed.
   */
  public void changeVolume(int volume) {
    if (this.volume + volume > 100) {
      throw new IllegalArgumentException("Volume exceeds limit");
    }
    this.volume += volume;
  }

  /**
   * Sets the volume of this sound.
   * @param volume represents the volume this sound will get.
   */
  public void setVolume(int volume) {
    if (this.volume > 100) {
      throw new IllegalArgumentException("Volume exceeds limit");
    }
    this.volume = volume;
  }

  /**
   * Determines whether this {@code ASound} is a rest.
   * @return {@code true} if this {@code ASound} is a rest, otherwise false.
   */
  public abstract boolean isRest();

  /**
   * Gets a List of all Sounds that are in this Sound.
   * @return a List of all Sounds that are in this Sound.
   */
  public List<ASound> getSoundsIn() {
    List<ASound> sounds = new ArrayList<ASound>();
    sounds.add(this);
    return sounds;
  }

  /**
   * Gets all notes that are part of this.
   * @return all notes that are part of this.
   */
  public List<Note> getNotesIn() {
    return ASound.getNotesIn(this.getSoundsIn());
  }

  /**
   * Gets all the notes that are part of {@param list}.
   * @param list represents the {@code List<ASound>} that's being looked through.
   * @return {@code List<Note>} representing the notes in the List of ASound.
   */
  public static List<Note> getNotesIn(List<ASound> list) {
    List<Note> sounds = new ArrayList<>();
    for (ASound s: list) {
      if (s.isNote()) {
        sounds.add((Note)s);
      }
    }
    return sounds;
  }

  /**
   * Sets the instrument code to the given code.
   * @param instrument represents the instrument to be set.
   */
  public void setInstrument(int instrument) {
    this.instrument = instrument;
  }

  /**
   * Returns this sound's instrument value.
   * @return this instrument value
   */
  public int getInstrument() {
    int instr = this.instrument;
    return instr;
  }

  @Override
  public BeatState getState() {
    return BeatState.START;
  }

  @Override
  public int getVolume() {
    return this.volume;
  }

  /**
   * Determines whether this is a Note.
   * @return whether this is a Note.
   */
  public boolean isNote() {
    return false;
  }

  /**
   * Gives the list of names of any Sound in this Sound.
   * @return {@code List} of {@code String}'s representing the name of {@code this ASound}.
   */
  public abstract List<String> getName();

  /**
   * Determines if this {@code ASound} is higher pitched than the other.
   * @param other represents the other ASound.
   * @return {@code boolean} representing if this is higher pitched than other.
   */
  public abstract boolean higherThan(ASound other);

  /**
   * Determines if this {@code ASound} is lower pitched than the other.
   * @param other represents the other ASound.
   * @return {@code boolean} representing if this is lower pitched than other.
   */
  public abstract boolean lowerThan(ASound other);

  /**
   * Gives the highest note of this ASound.
   * @return int representing the highest octave of this ASound.
   * @throws IllegalArgumentException if used on invalid classes.
   */
  public abstract Note getHighestNote();

  /**
   * Gives the lowest note of this ASound.
   * @return int representing the lowest octave of this ASound.
   * @throws IllegalArgumentException if used on invalid classes.
   */
  public abstract Note getLowestNote();

  /**
   * Creates a new Chord from the given note and the chord.
   * @param note represents the note being added to the Chord.
   * @return {@code Chord} from given notes.
   */
  public abstract Chord makeChord(Note note);
}
